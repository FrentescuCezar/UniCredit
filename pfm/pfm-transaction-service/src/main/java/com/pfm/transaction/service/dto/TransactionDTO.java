package com.pfm.transaction.service.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class TransactionDTO {
    @NotNull(groups = OnUpdate.class) // Required for PUT
    private Date date;

    @NotNull(groups = OnUpdate.class)
    private BigDecimal amount;

    @NotNull(groups = OnUpdate.class)
    private Long categoryId;

    @NotNull(groups = OnUpdate.class)
    private Long keywordId;

    @NotNull(groups = OnUpdate.class)
    private Long parentId;

    @NotNull(groups = OnUpdate.class)
    private String description;
}