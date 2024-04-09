package com.pfm.transaction.controller;

import com.pfm.transaction.service.TransactionService;
import com.pfm.transaction.service.dto.CategoryUpdateDTO;
import com.pfm.transaction.service.dto.OnUpdate;
import com.pfm.transaction.service.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        return new ResponseEntity<>(transactionService.saveTransaction(transactionDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id) {
        TransactionDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO updatedTransaction = transactionService.replaceTransaction(id, transactionDTO);
        return ResponseEntity.ok(updatedTransaction);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TransactionDTO> patchTransaction(@PathVariable Long id,
                                                           @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO updatedTransactionDTO = transactionService.updateTransaction(id, transactionDTO);
        return ResponseEntity.ok(updatedTransactionDTO);
    }

    @PutMapping("/{id}/category")
    public ResponseEntity<TransactionDTO> updateTransactionCategory(@PathVariable Long id,
                                                                    @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        return ResponseEntity.ok(transactionService.updateTransactionCategory(id, categoryUpdateDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/summary")
    public ResponseEntity<List<Map<String, Object>>> getTransactionBetweenDates(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                                @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        List<Map<String, Object>> transactionDTOs = transactionService.getTransactionBetweenDates(startDate, endDate);
        return ResponseEntity.ok(transactionDTOs);
    }
}