package com.rekha.ecommerce.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

public class ObjectMapperUtil {

	//Converts Entity to DTO and returns dto object
	public static <S, D> D convertEntityToDTO(S entityObject, Class<D> dtoClass) {
        if (entityObject == null || dtoClass == null) {
            throw new IllegalArgumentException("Source object and destination class must not be null.");
        }

        try {
            D dto = dtoClass.getDeclaredConstructor().newInstance(); 
            BeanUtils.copyProperties(entityObject, dto);
            return dto;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException("Error occurred while converting entity to DTO", e);
        }
    }
	
	//Converts DTO to Entity and returns entity object
	public static <S, D> S convertDTOToEntity(D dtoObject, Class<S> entityClass) {
        if (dtoObject == null || entityClass == null) {
            throw new IllegalArgumentException("Source object and destination class must not be null.");
        }

        try {
            S entity = entityClass.getDeclaredConstructor().newInstance(); 
            BeanUtils.copyProperties(dtoObject, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while converting DTO to entity", e);
        }
    }
	
	//Converts DTOList to EntityList and returns entity list
	public static <S, D> List<S> convertDTOListToEntityList(List<D> dtoList, Class<S> entityClass) {
        if (dtoList == null || entityClass == null) {
            throw new IllegalArgumentException("DTO list and entity class must not be null.");
        }

        try {
            return dtoList.stream()
                    .map(dto -> convertDTOToEntity(dto, entityClass))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while converting DTO list to entity list", e);
        }
    }
	
	//Converts EntityList to DTOList and returns dto List
	public static <S, D> List<D> convertEntityListToDTOList(List<S> entityList, Class<D> dtoClass) {
        if (entityList == null || dtoClass == null) {
            throw new IllegalArgumentException("Entity list and DTO class must not be null.");
        }

        try {
            return entityList.stream()
                    .map(entity -> convertEntityToDTO(entity, dtoClass))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while converting entity list to DTO list", e);
        }
    }
	
	//This is Generic method for converting entityTodto and vice versa
	public static <S, D> D convertObject(S sourceObject, Class<S> sourceClass, Class<D> destinationClass) {
        if (sourceObject == null || sourceClass == null || destinationClass == null) {
            throw new IllegalArgumentException("Source object, source class, and destination class must not be null.");
        }

        try {
            D destination = destinationClass.getDeclaredConstructor().newInstance();
            // Copy properties from source object to destination object
            BeanUtils.copyProperties(sourceObject, destination);
            return destination;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while converting object from " + sourceClass.getSimpleName() + " to " + destinationClass.getSimpleName(), e);
        }
    }
	
	
//
//	public static <S, D> List<D> convertEntityListToDtoList(List<S> sourceList, List<D> destinationList, Class<S> sourceType) {
//		if (sourceList == null || destinationList == null) {
//			throw new IllegalArgumentException("Source list and destination list must not be null.");
//		}
//
//		try {
//			for (S source : sourceList) {
//				S destination = sourceType.getDeclaredConstructor().newInstance(); 
//																						
//				BeanUtils.copyProperties(source, destination);
//				destinationList.add(destination);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException("Error occurred while copying properties from source list to destination list",
//					e);
//		}
//		return destinationList;
//
//	}
	
//	public static <S, D> D convertDtoListToEntityList(List<S> sourceList, List<D> destinationList, Class<D> destinationType) {
//		if (sourceList == null || destinationList == null) {
//			throw new IllegalArgumentException("Source list and destination list must not be null.");
//		}
//
//		try {
//			for (S source : sourceList) {
//				D destination = destinationType.getDeclaredConstructor().newInstance(); 
//																						
//				BeanUtils.copyProperties(source, destination);
//				destinationList.add(destination);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException("Error occurred while copying properties from source list to destination list",
//					e);
//		}
//
//	}
}
