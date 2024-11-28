package com.rekha.ecommerce.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.ProductImagesDTO;
import com.rekha.ecommerce.dto.ProductQuantityDTO;
import com.rekha.ecommerce.dto.ProductsDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.ProductImages;
import com.rekha.ecommerce.entity.ProductQuantity;
import com.rekha.ecommerce.entity.Products;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.ProductImagesRepository;
import com.rekha.ecommerce.repository.ProductQuantityRepository;
import com.rekha.ecommerce.repository.ProductsRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductsService {

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private ProductImagesRepository imagesRepository;

	@Autowired
	private ProductQuantityRepository productQuantityRepository;

	@Transactional
	public ResponseObject<List<ProductsDTO>> saveProducts(List<ProductsDTO> productsDTOs, String userName) {
		try {

			List<ProductsDTO> responseList = new ArrayList<>();
//			ProductsDTO response = new ProductsDTO();

//			List<Products> productEntitys = new ArrayList<>();
			productsDTOs.forEach(dto -> {
				ProductsDTO productDTO = new ProductsDTO();
				Products productsEntity = new Products();
				dto.setCreatedBy(userName);

				BeanUtils.copyProperties(dto, productsEntity);
				// product save
				Long id = productsRepository.save(productsEntity).getProductId();

				List<ProductImages> imageList = new ArrayList<>();

				dto.getProductImages().forEach(image -> {
					Blob productImageBlob = null;
					try {
						productImageBlob = new SerialBlob(image);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					ProductImages imageEntity = new ProductImages();
					imageEntity.setProductId(id);
					imageEntity.setProductImage(productImageBlob);
					imageEntity.setCreatedBy(userName);
					imageList.add(imageEntity);
				});
				// product images List save
				imagesRepository.saveAll(imageList);

				List<ProductQuantity> prodQuantityList = new ArrayList<>();
				dto.getProductQuantityDTOList().forEach(prodQunty -> {
					ProductQuantity quantity = new ProductQuantity();
					BeanUtils.copyProperties(prodQunty, quantity);
					quantity.setCreatedBy(userName);
					quantity.setProductId(id);
					prodQuantityList.add(quantity);
				});
				// product quantity save
				productQuantityRepository.saveAll(prodQuantityList);

				// convert quantities into dtos

				List<ProductQuantityDTO> prodQuantDTOs = new ArrayList<>();
				prodQuantityList.forEach(qunt -> {
					ProductQuantityDTO qunatityDTO = new ProductQuantityDTO();

					BeanUtils.copyProperties(qunt, qunatityDTO);
					prodQuantDTOs.add(qunatityDTO);

				});

				// converting imageDTOs into list
				List<ProductImagesDTO> imageDTOList = new ArrayList<>();
				imageList.forEach(image -> {
					ProductImagesDTO imageDTO = new ProductImagesDTO();

					Blob productImageBlob = image.getProductImage(); // Assuming getBrandImage() returns a Blob
					if (productImageBlob != null) {
						try {
							byte[] productImageBytes = productImageBlob.getBytes(1, (int) productImageBlob.length()); // Convert
																														// Blob
							// to byte[]
							imageDTO.setProductImage(productImageBytes); // Set the byte[] in DTO
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

					BeanUtils.copyProperties(image, imageDTO);
					imageDTOList.add(imageDTO);
				});
				BeanUtils.copyProperties(productsEntity, productDTO);
				productDTO.setImagesDTOs(imageDTOList);
				productDTO.setProductQuantityDTOList(prodQuantDTOs);

				responseList.add(productDTO);
			});

			return ResponseObject.success(responseList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	@Transactional
	public ResponseObject<ProductsDTO> update(ProductsDTO productsDTO, String userName) {
		try {
			productsRepository.findById(productsDTO.getProductId())
					.orElseThrow(() -> new CloudBaseException(ResponseCode.PRODUCT_NOT_FOUND));
			Products productEntity = new Products();
			productsDTO.setLastModifiedBy(userName);
			BeanUtils.copyProperties(productsDTO, productEntity);
			productsRepository.save(productEntity);
			ProductsDTO response = new ProductsDTO();
			BeanUtils.copyProperties(productEntity, response);
			return ResponseObject.success(response);
		} catch (CloudBaseException exp) {
			exp.printStackTrace();
			throw exp;
		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}
	}

	public ResponseObject<List<ProductsDTO>> getProductsByBrand(String brand) {

		try {

			List<Products> products = productsRepository.findByBrandName(brand);

			List<Long> productIds = products.stream().map(Products::getProductId).collect(Collectors.toList());

			List<ProductImages> imagesList = imagesRepository.getImagesByProductIds(productIds);

			List<ProductQuantity> prodQtyList = productQuantityRepository.getProductQuantitesByProductIds(productIds);

			// Map product ID to images and quantities for easy lookup
			Map<Long, List<ProductImages>> imagesMap = imagesList.stream()
					.collect(Collectors.groupingBy(ProductImages::getProductId));

			Map<Long, List<ProductQuantity>> quantitiesMap = prodQtyList.stream()
					.collect(Collectors.groupingBy(ProductQuantity::getProductId));

			// Map each product to a ProductDTO containing its images and quantities
			List<ProductsDTO> responseList = products.stream().map(product -> {

				ProductsDTO productDTO = new ProductsDTO();
				BeanUtils.copyProperties(product, productDTO); // Copy properties from entity to DTO
				System.out.println("Entity " + product);
//			System.out.println(productDTO);

				// Get the images and quantities for the current product
				List<ProductImages> productImages = imagesMap.getOrDefault(product.getProductId(), List.of());
//			List<ProductImages> productImages = imagesMap.getOrDefault(product.getProductId(), new ArrayList<>());
				List<ProductQuantity> productQuantities = quantitiesMap.getOrDefault(product.getProductId(), List.of());
//			List<ProductQuantity> productQuantities = quantitiesMap.getOrDefault(product.getProductId(), new ArrayList<>());

//				System.out.println("product images  :: " + productImages);
//				System.out.println("product quantities ::  " + productQuantities);

				// Convert ProductImages and ProductQuantity to DTOs using BeanUtils
//				List<ProductImagesDTO> productImagesDTOList = productImages.stream().map(image -> {
//					ProductImagesDTO imageDTO = new ProductImagesDTO();
//					BeanUtils.copyProperties(image, imageDTO); 
//					
////					Blob productImage = ; // Assuming getBrandImage() returns a Blob
//					if (image.get != null) {
//						try {
//							byte[] productImageBytes = productImage.getBytes(1, (int) brandImageBlob.length()); // Convert Blob
//																												// to byte[]
//							imageDTO.setProductImage(productImageBytes); // Set the byte[] in DTO
//						} catch (SQLException e) {
//							// Handle the error if Blob conversion fails
//							e.printStackTrace();
//						}
//
//					return imageDTO;
//				}).collect(Collectors.toList());
				
				List<ProductImagesDTO> productImagesDTOList = new ArrayList<>();
				
				 productImages.forEach(image -> {
					ProductImagesDTO imageDTO = new ProductImagesDTO();
					BeanUtils.copyProperties(image, imageDTO); 
					
					if(image.getProductImage() != null) {
						try {
							byte[] productImageBytes = image.getProductImage().getBytes(1, (int) image.getProductImage().length()); // Convert Blob
																												
							imageDTO.setProductImage(productImageBytes); // Set the byte[] in DTO
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
					productImagesDTOList.add(imageDTO);
				});

				List<ProductQuantityDTO> productQuantityDTOList = productQuantities.stream().map(quantity -> {
					ProductQuantityDTO quantityDTO = new ProductQuantityDTO();
					BeanUtils.copyProperties(quantity, quantityDTO); // Copy properties from entity to DTO
					return quantityDTO;
				}).collect(Collectors.toList());

				// Create ProductDTO that includes the product, images, and quantities
//			ProductsDTO productDTO = new ProductsDTO();
				productDTO.setImagesDTOs(productImagesDTOList);
				productDTO.setProductQuantityDTOList(productQuantityDTOList);

//				System.out.println(productQuantityDTOList);
//				System.out.println(productImagesDTOList);

				return productDTO;
			}).collect(Collectors.toList());

			return ResponseObject.success(responseList);
		} catch (Exception e) {
			e.printStackTrace(); // Handle exceptions appropriately
			throw new CloudBaseException(ResponseCode.BAD_REQUEST);
		}
	}

}