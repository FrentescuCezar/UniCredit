package com.pfm.transaction.service.dto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class TransactionDTO{

    private Date date;

    private BigDecimal amount;

    private Long categoryId;

    private Long keywordId;

    private Long parentId;

    private String description;

}