package com.pfm.transaction.controller;

import com.pfm.transaction.service.TransactionService;
import com.pfm.transaction.service.dto.CategoryUpdateDTO;
import com.pfm.transaction.service.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactionDTOs = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactionDTOs);
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransactionDTO = transactionService.saveTransaction(transactionDTO);
        return new ResponseEntity<>(createdTransactionDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id,
                                                            @RequestBody TransactionDTO transactionDTO)
    {
        if (transactionService.getTransactionById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TransactionDTO updatedTransactionDTO = transactionService.updateTransaction(id, transactionDTO);
        return ResponseEntity.ok(updatedTransactionDTO);
    }

    @PutMapping("/{id}/category")
    public ResponseEntity<TransactionDTO> updateTransactionCategory(@PathVariable Long id, @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        Optional<TransactionDTO> transactionDTOOptional = transactionService.getTransactionById(id);
        if (transactionDTOOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TransactionDTO transactionDTO = transactionDTOOptional.get();
        transactionDTO.setCategoryId(categoryUpdateDTO.getCategoryId());
        TransactionDTO updatedTransactionDTO = transactionService.updateTransaction(id, transactionDTO);

        return ResponseEntity.ok(updatedTransactionDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        if (transactionService.getTransactionById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        transactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}