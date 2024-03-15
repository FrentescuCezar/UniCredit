package com.pfm.category.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pfm_keyword")
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Standard getters and setters
}