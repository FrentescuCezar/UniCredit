package com.pfm.category.service.mapper;

import com.pfm.category.repository.model.CategoryEntity;
import com.pfm.category.service.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper CATEGORY_MAPPER = Mappers.getMapper(CategoryMapper.class);
    CategoryDTO toCategoryDTO(CategoryEntity categoryEntity);
    CategoryEntity toCategoryEntity(CategoryDTO categoryDTO);
}
