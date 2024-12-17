package com.rekha.ecommerce.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rekha.ecommerce.dto.BlogDTO;
import com.rekha.ecommerce.dto.BlogImagesDTO;
import com.rekha.ecommerce.dto.ResponseObject;
import com.rekha.ecommerce.entity.Blog;
import com.rekha.ecommerce.entity.BlogImages;
import com.rekha.ecommerce.entity.SecUser;
import com.rekha.ecommerce.enumeration.ResponseCode;
import com.rekha.ecommerce.exception.CloudBaseException;
import com.rekha.ecommerce.repository.BlogImagesRepository;
import com.rekha.ecommerce.repository.BolgRepository;
import com.rekha.ecommerce.repository.SecUserRepository;
import com.rekha.ecommerce.utils.ImageConversion;
import com.rekha.ecommerce.utils.ObjectMapperUtil;

import jakarta.transaction.Transactional;

@Service
public class BlogService {

	@Autowired
	BolgRepository bolgRepository;

	@Autowired
	BlogImagesRepository blogImagesRepository;

	@Autowired
	private SecUserRepository secUserRepository;

	public ResponseObject<?> getAllBlogs() {

		try {
			List<Blog> blogEntityList = bolgRepository.findAll();

			List<Long> blogIds = blogEntityList.stream().map(Blog::getId).toList();

			List<BlogImages> imagesList = blogImagesRepository.getImagesByBlogId(blogIds);

			Map<Long, List<BlogImages>> imagesMap = imagesList.stream()
					.collect(Collectors.groupingBy(BlogImages::getBlogId));

			List<BlogDTO> responseList = blogEntityList.stream().map(entity -> {

				BlogDTO blogDto = ObjectMapperUtil.convertEntityToDTO(entity, BlogDTO.class);
				byte[] blogImageBlob = null;
				try {
					if (entity.getAuthorAvatar() != null) {
						blogImageBlob = ImageConversion.blobToByteConversion(entity.getAuthorAvatar());
						blogDto.setAuthorAvatar(blogImageBlob);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				List<BlogImages> blogImages = imagesMap.getOrDefault(entity.getId(), List.of());

				List<BlogImagesDTO> blogImagesDTOList = new ArrayList<>();

				blogImages.forEach(image -> {
					BlogImagesDTO imageDTO = new BlogImagesDTO();
					BeanUtils.copyProperties(image, imageDTO);

					/*if (image.getBlogImage() != null) {
						try {
					
							byte[] blogImageBytes = ImageConversion.blobToByteConversion(image.getBlogImage());
					
							imageDTO.setBlogImage(blogImageBytes);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}*/

					blogImagesDTOList.add(imageDTO);
				});
				blogDto.setImages(blogImagesDTOList);

				return blogDto;
			}).collect(Collectors.toList());

			return ResponseObject.success(responseList);

		} catch (Exception e2) {
			e2.printStackTrace();
			throw new CloudBaseException(ResponseCode.UNKNOWN_ERROR_OCCURRED);
		}

	}

	@Transactional
	public ResponseObject<?> save(BlogDTO dto, String phoneNumber, String userName) {

		try {
			Blog blogEntity = ObjectMapperUtil.convertDTOToEntity(dto, Blog.class);
			SecUser secUser = secUserRepository.findByPhoneNumberAndIsActive(phoneNumber, true);

			if (secUser != null && secUser.getProfile() != null) {
				blogEntity.setAuthorAvatar(secUser.getProfile());
			}
			blogEntity.setAuthorName(userName);
			blogEntity.setCreatedBy(userName);

			Long id = bolgRepository.save(blogEntity).getId();

			List<BlogImages> imagesList = new ArrayList<>();

			dto.getImages().forEach(image -> {
				/*Blob blogImageBlob = null;
				try {
					if (image.getBlogImage() != null) {
						blogImageBlob = ImageConversion.byteToBlobConversion(image.getBlogImage());
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}*/
				BlogImages imageEntity = ObjectMapperUtil.convertDTOToEntity(image, BlogImages.class);
				
				imageEntity.setBlogId(id);
				imageEntity.setBlogImageId(image.getBlogImageId());
				imageEntity.setCreatedBy(userName);
				imagesList.add(imageEntity);
			});

			blogImagesRepository.saveAll(imagesList);

			List<BlogImagesDTO> imageDTOList = new ArrayList<>();

			imagesList.forEach(image -> {
				BlogImagesDTO imageDTO = ObjectMapperUtil.convertEntityToDTO(image, BlogImagesDTO.class);
//				BeanUtils.copyProperties(image, imageDTO);

				imageDTOList.add(imageDTO);
			});

//			BlogDTO response = ObjectMapperUtil.convertEntityToDTO(blogEntity, BlogDTO.class);
			BlogDTO response = new BlogDTO();

			Blob avatarBlob = blogEntity.getAuthorAvatar();
			if (avatarBlob != null) {
				try {
					byte[] byteImage = ImageConversion.blobToByteConversion(avatarBlob);
					response.setAuthorAvatar(byteImage);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			BeanUtils.copyProperties(blogEntity, response);
			response.setImages(imageDTOList);

			return ResponseObject.success(response);
		} catch (Exception e2) {
			e2.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

	@Transactional
	public ResponseObject<?> update(BlogDTO dto, String phoneNumber, String userName) {

		try {
			Blog blogEntity = ObjectMapperUtil.convertDTOToEntity(dto, Blog.class);
//			Blog blogEntity = new Blog();
			try {
				if (dto.getAuthorAvatar() != null) {
					Blob blogAvatar = ImageConversion.byteToBlobConversion(dto.getAuthorAvatar());
					blogEntity.setAuthorAvatar(blogAvatar);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
//			BeanUtils.copyProperties(dto, blogEntity);

			blogEntity.setLastModifiedBy(userName);

			Long id = bolgRepository.save(blogEntity).getId();

			List<BlogImages> imagesList = new ArrayList<>();

			dto.getImages().forEach(image -> {
				/*Blob blogImageBlob = null;
				try {
					if (image.getBlogImage() != null) {
						blogImageBlob = ImageConversion.byteToBlobConversion(image.getBlogImage());
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}*/
				BlogImages imageEntity = new BlogImages();
				if (imageEntity.getId() == null) {
					imageEntity.setBlogId(id);
//					imageEntity.setBlogImage(blogImageBlob);
					imageEntity.setCreatedBy(userName);
				} else {
					imageEntity.setLastModifiedBy(userName);
				}
				image.setBlogImageId(image.getBlogImageId());
				imagesList.add(imageEntity);
			});

			blogImagesRepository.saveAll(imagesList);

			List<BlogImagesDTO> imageDTOList = new ArrayList<>();
			imagesList.forEach(image -> {
				BlogImagesDTO imageDTO = new BlogImagesDTO();

				/*Blob blogImageBlob = image.getBlogImage();
				if (blogImageBlob != null) {
					try {
						ImageConversion.blobToByteConversion(blogImageBlob);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}*/

				BeanUtils.copyProperties(image, imageDTO);
				imageDTOList.add(imageDTO);
			});
			BlogDTO response = ObjectMapperUtil.convertEntityToDTO(blogEntity, BlogDTO.class);

			Blob blogAvatar = blogEntity.getAuthorAvatar();
			if (blogAvatar != null) {
				try {
					byte[] byteImage = ImageConversion.blobToByteConversion(blogAvatar);
					response.setAuthorAvatar(byteImage);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

//			BeanUtils.copyProperties(blogEntity, response);
			response.setImages(imageDTOList);

			return ResponseObject.success(response);
		} catch (Exception e2) {
			e2.printStackTrace();
			throw new CloudBaseException(ResponseCode.ERROR_STORING_DATA);
		}

	}

}
