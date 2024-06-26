package com.pfm.category.controller;

import com.pfm.category.service.TransactionUpdateService;
import com.pfm.category.service.dto.CategoryDTO;
import com.pfm.category.service.dto.TransactionDTO;
import com.pfm.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("autoUpdateCategory/{transactionId}")
    public ResponseEntity<TransactionDTO> autoUpdateTransactionCategory(@PathVariable Long transactionId, @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO updatedTransaction = transactionUpdateService.autoUpdateTransactionCategory(transactionId, transactionDTO);
        return ResponseEntity.ok(updatedTransaction);
    }

    @PostMapping
    public ResponseEntity<List<TransactionDTO>> splitTransaction(@RequestParam Long parentId, @RequestBody List<TransactionDTO> transactionDTOs) {
        List<TransactionDTO> createdTransactions = transactionUpdateService.splitTransaction(parentId, transactionDTOs);
        return new ResponseEntity<>(createdTransactions, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("/categories/top-level")
    public List<CategoryDTO> getTopLevelCategories() {
        return categoryService.getTopLevelCategories();
    }

    @GetMapping("/categories/by-parent")
    public List<CategoryDTO> getCategoriesByParentId(@RequestParam Long parentId) {
        return categoryService.getCategoriesByParentId(parentId);
    }
}
