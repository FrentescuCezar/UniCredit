package com.pfm.category.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionDTO {

    private Long id;
    private Date date;
    private BigDecimal amount;
    private String description;

    // Optionally include other fields if they are needed for categorization
}