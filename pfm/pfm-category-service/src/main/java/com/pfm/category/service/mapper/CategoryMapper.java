package com.pfm.category.service.mapper;

import com.pfm.category.repository.model.CategoryEntity;
import com.pfm.category.service.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper CATEGORY_MAPPER = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "parent.id", target = "parentId")
    CategoryDTO entityToDto(CategoryEntity category);

    @Mapping(source = "parentId", target = "parent.id")
    CategoryEntity dtoToEntity(CategoryDTO categoryDTO);
}