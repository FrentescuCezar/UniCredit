package com.pfm.transaction.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "pfm_trx")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Date date;

    @Column(nullable = true)
    private BigDecimal amount;

    @Column(name = "category_id", nullable = true)
    private Long categoryId;

    @Column(name = "keyword_id", nullable = true)
    private Long keywordId;

    @Column(name = "parent_id", nullable = true)
    private Long parentId;

    @Column(nullable = true, length = 255)
    private String description;

}