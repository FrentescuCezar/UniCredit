package com.pfm.category.controller;

import com.pfm.category.service.dto.TransactionDTO;
import com.pfm.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/findCategoryForTransaction")
    public ResponseEntity<Long> findCategoryForTransaction(@RequestBody TransactionDTO transactionDTO) {
        Optional<Long> categoryId = categoryService.findCategoryForTransaction(transactionDTO);
        return categoryId.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
