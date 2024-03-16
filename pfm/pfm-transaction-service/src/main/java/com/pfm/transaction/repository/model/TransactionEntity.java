package com.pfm.transaction.repository.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "pfm_trx")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    private BigDecimal amount;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "keyword_id")
    private Long keywordId;

    @Column(name = "parent_id")
    private Long parentId;

    private String description;

}