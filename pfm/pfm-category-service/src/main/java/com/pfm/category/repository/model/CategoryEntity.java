package com.pfm.category.repository.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pfm_category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;

    @Column(nullable = false)
    private String value;
}
