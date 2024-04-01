package com.pfm.transaction.repository.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "pfm_trx")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "keyword_id")
    private Long keywordId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "description")
    private String description;

}