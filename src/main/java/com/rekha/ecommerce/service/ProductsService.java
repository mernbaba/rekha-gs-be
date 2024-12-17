package com.rekha.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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

import ch.qos.logback.core.util.StringUtil;
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

			productsDTOs.forEach(dto -> {
				ProductsDTO productDTO = new ProductsDTO();
				Products productsEntity = new Products();
				dto.setCreatedBy(userName);

				BeanUtils.copyProperties(dto, productsEntity);
				// product save
				Long id = productsRepository.save(productsEntity).getProductId();

				List<ProductImages> imageList = new ArrayList<>();

				dto.getProductImageIds().forEach(imageId -> {
					/*Blob productImageBlob = null;
					try {
						productImageBlob = new SerialBlob(image);
					} catch (SQLException e) {
						e.printStackTrace();
					}*/
					ProductImages imageEntity = new ProductImages();
					imageEntity.setProductId(id);
//					imageEntity.setProductImage(productImageBlob);
					imageEntity.setProductImageId(imageId);
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

					/*Blob productImageBlob = image.getProductImage(); // Assuming getBrandImage() returns a Blob
					if (productImageBlob != null) {
						try {
							byte[] productImageBytes = productImageBlob.getBytes(1, (int) productImageBlob.length()); // Convert
																														// Blob
							// to byte[]
							imageDTO.setProductImage(productImageBytes); // Set the byte[] in DTO
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}*/

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

			Long id = productsRepository.save(productEntity).getProductId();

			List<ProductImages> imageList = new ArrayList<>();

			productsDTO.getImagesDTOs().forEach(imageDTO -> {

				ProductImages imageEntity = new ProductImages();
				BeanUtils.copyProperties(imageDTO, imageEntity);

				/*try {
					if (imageDTO.getProductImage() != null) {
						Blob productImageBlob = new SerialBlob(imageDTO.getProductImage());
						imageEntity.setProductImage(productImageBlob);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}*/
				if (imageEntity.getId() == null) {
					imageEntity.setProductId(id);
//					imageEntity.setProductImage(productImageBlob);
					imageEntity.setCreatedBy(userName);
				} else {
					imageEntity.setLastModifiedBy(userName);
				}

				imageList.add(imageEntity);
			});
			// product images List save
			imagesRepository.saveAll(imageList);

			List<ProductQuantity> prodQuantityList = new ArrayList<>();
			productsDTO.getProductQuantityDTOList().forEach(prodQunty -> {
				ProductQuantity quantity = new ProductQuantity();
				BeanUtils.copyProperties(prodQunty, quantity);
				if (quantity.getId() == null) {
					quantity.setCreatedBy(userName);
					quantity.setProductId(id);
				} else {
					quantity.setLastModifiedBy(userName);
				}
				prodQuantityList.add(quantity);
			});
			// product quantity save
			productQuantityRepository.saveAll(prodQuantityList);

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
		List<Products> products = null;
		if (StringUtils.isNotBlank(brand) && brand.equalsIgnoreCase("ALL")) {
			products = productsRepository.findAll();
		} else {
			products = productsRepository.findByBrandName(brand);

		}
		return getProducts(products);
	}

	public ResponseObject<List<ProductsDTO>> getProductsByQuantity(List<ProductQuantityDTO> dtoList) {

		try {
			List<Long> prodIds = dtoList.stream().map(ProductQuantityDTO::getProductId).collect(Collectors.toList());

			List<Long> quntIds = dtoList.stream().map(ProductQuantityDTO::getId).collect(Collectors.toList());

			List<Products> productEntities = productsRepository.getProductsByIds(prodIds);

			List<ProductImages> imagesList = imagesRepository.getImagesByProductIds(prodIds);

			List<ProductQuantity> prodQtyList = productQuantityRepository.getProductQuantitesByProductIds(prodIds);

			Map<Long, List<ProductImages>> imagesMap = imagesList.stream()
					.collect(Collectors.groupingBy(ProductImages::getProductId));

			Map<Long, List<ProductQuantity>> quantitiesMap = prodQtyList.stream()
					.collect(Collectors.groupingBy(ProductQuantity::getProductId));

			// Map each product to a ProductDTO containing its images and quantities
			List<ProductsDTO> responseList = productEntities.stream().map(product -> {

				ProductsDTO productDTO = new ProductsDTO();
				BeanUtils.copyProperties(product, productDTO); // Copy properties from entity to DTO
				System.out.println("Entity " + product);

				// Get the images and quantities for the current product
				List<ProductImages> productImages = imagesMap.getOrDefault(product.getProductId(), List.of());

				List<ProductQuantity> productQuantities = quantitiesMap.getOrDefault(product.getProductId(), List.of());

				List<ProductImagesDTO> productImagesDTOList = new ArrayList<>();

				productImages.forEach(image -> {
					ProductImagesDTO imageDTO = new ProductImagesDTO();
					BeanUtils.copyProperties(image, imageDTO);

					/*if (image.getProductImage() != null) {
						try {
							byte[] productImageBytes = image.getProductImage().getBytes(1,
									(int) image.getProductImage().length()); // Convert Blob
					
							imageDTO.setProductImage(productImageBytes); // Set the byte[] in DTO
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}*/

					productImagesDTOList.add(imageDTO);
				});

				List<ProductQuantityDTO> productQuantityDTOList = productQuantities.stream()
						.filter(quantity -> quntIds.contains(quantity.getId())).map(quantity -> {
							ProductQuantityDTO quantityDTO = new ProductQuantityDTO();
							BeanUtils.copyProperties(quantity, quantityDTO); // Copy properties from entity to DTO
							return quantityDTO;
						}).collect(Collectors.toList());

				// Create ProductDTO that includes the product, images, and quantities
				productDTO.setImagesDTOs(productImagesDTOList);
				productDTO.setProductQuantityDTOList(productQuantityDTOList);

				return productDTO;
			}).collect(Collectors.toList());
			return ResponseObject.success(responseList);
		} catch (Exception e) {
			throw new CloudBaseException(ResponseCode.UNKNOWN_ERROR_OCCURRED);
		}

	}

	public ResponseObject<List<ProductsDTO>> getAllProducts() {
		List<Products> productsList = productsRepository.findAll();

		return getProducts(productsList);

	}

	public ResponseObject<List<ProductsDTO>> getProducts(List<Products> products) {

		try {

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

				List<ProductQuantity> productQuantities = quantitiesMap.getOrDefault(product.getProductId(), List.of());

				List<ProductImagesDTO> productImagesDTOList = new ArrayList<>();

				productImages.forEach(image -> {
					ProductImagesDTO imageDTO = new ProductImagesDTO();
					BeanUtils.copyProperties(image, imageDTO);

					/*if (image.getProductImage() != null) {
						try {
							byte[] productImageBytes = image.getProductImage().getBytes(1,
									(int) image.getProductImage().length()); // Convert Blob
					
							imageDTO.setProductImage(productImageBytes); // Set the byte[] in DTO
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}*/

					productImagesDTOList.add(imageDTO);
				});

				List<ProductQuantityDTO> productQuantityDTOList = productQuantities.stream().map(quantity -> {
					ProductQuantityDTO quantityDTO = new ProductQuantityDTO();
					BeanUtils.copyProperties(quantity, quantityDTO); // Copy properties from entity to DTO
					return quantityDTO;
				}).collect(Collectors.toList());

				// Create ProductDTO that includes the product, images, and quantities
				productDTO.setImagesDTOs(productImagesDTOList);
				productDTO.setProductQuantityDTOList(productQuantityDTOList);

				return productDTO;
			}).collect(Collectors.toList());

			return ResponseObject.success(responseList);
		} catch (Exception e) {
			e.printStackTrace(); // Handle exceptions appropriately
			throw new CloudBaseException(ResponseCode.BAD_REQUEST);
		}
	}

}
