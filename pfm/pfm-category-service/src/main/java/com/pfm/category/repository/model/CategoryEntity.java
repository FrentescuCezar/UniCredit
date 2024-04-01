package com.pfm.category.repository.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "pfm_category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Column(name = "parent_id")
    private Long parentId;

    @Column(nullable = false)
    private String value;

    @OneToMany(mappedBy = "parent")
    private Set<CategoryEntity> child;

    @OneToOne(mappedBy = "category")
    private KeywordEntity keywords;

}