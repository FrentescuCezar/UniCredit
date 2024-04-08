package com.pfm.category.controller;

import com.pfm.category.service.dto.TransactionDTO;
import com.pfm.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/findCategoryForTransaction")
    public ResponseEntity<Long> findCategoryForTransaction(@RequestBody TransactionDTO transactionDTO) {
        return categoryService.findCategoryForTransaction(transactionDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
