package com.pfm.category.controller;

import com.pfm.category.service.TransactionUpdateService;
import com.pfm.category.service.dto.TransactionDTO;
import com.pfm.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final TransactionUpdateService transactionUpdateService;

    @PostMapping("/findCategoryForTransaction")
    public ResponseEntity<Long> findCategoryForTransaction(@RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(categoryService.findCategoryForTransaction(transactionDTO));
    }

    @PostMapping("/updateTransactionCategory")
    public ResponseEntity<TransactionDTO> updateTransactionCategory(@RequestParam Long transactionId, @RequestParam Long categoryId) {

        return ResponseEntity.ok(transactionUpdateService.updateTransactionCategory(transactionId, categoryId));
    }
}
