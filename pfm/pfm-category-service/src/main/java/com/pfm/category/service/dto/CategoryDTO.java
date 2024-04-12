package com.pfm.category.service.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private Long parentId;
    private String value;


}
