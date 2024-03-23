package com.pfm.category.service.dto;

import com.pfm.category.repository.model.CategoryEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeywordDTO {
    private String value;
    private CategoryEntity category;
}
