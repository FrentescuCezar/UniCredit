package com.pfm.category.service.mapper;

import com.pfm.category.repository.model.CategoryEntity;
import com.pfm.category.repository.model.KeywordEntity;
import com.pfm.category.service.dto.CategoryDTO;
import com.pfm.category.service.dto.KeywordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface  KeywordMapper {

    KeywordMapper KEYWORD_MAPPER = Mappers.getMapper(KeywordMapper.class);
    KeywordDTO toKeywordDTO(KeywordEntity keywordEntity);
    KeywordEntity toKeywordEntity(KeywordDTO keywordDTO);

}
