package com.pfm.category.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pfm_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(nullable = false)
    private String value;

    // Standard getters and setters
}