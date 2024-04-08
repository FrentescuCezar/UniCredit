package com.pfm.transaction.service;

import com.pfm.transaction.exception.TransactionNotFoundException;
import com.pfm.transaction.repository.model.TransactionEntity;
import com.pfm.transaction.repository.TransactionRepository;
import com.pfm.transaction.service.dto.TransactionDTO;
import com.pfm.transaction.service.mapper.TransactionMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.pfm.transaction.service.mapper.TransactionMapper.*;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;


    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(TRANSACTION_MAPPER::toTransactionDTO)
                .toList();
    }

    @Transactional
    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = TRANSACTION_MAPPER.toTransactionEntity(transactionDTO);
        TransactionEntity savedEntity = transactionRepository.save(transactionEntity);
        return TRANSACTION_MAPPER.toTransactionDTO(savedEntity);
    }

    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(TRANSACTION_MAPPER::toTransactionDTO)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id " + id + " not found"));
    }

    @Transactional
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO) {

        TransactionEntity transactionEntity = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id " + id + " not found"));

        if (transactionDTO.getDate() != null) {
            transactionEntity.setDate(transactionDTO.getDate());
        }
        if (transactionDTO.getAmount() != null) {
            transactionEntity.setAmount(transactionDTO.getAmount());
        }
        if (transactionDTO.getCategoryId() != null) {
            transactionEntity.setCategoryId(transactionDTO.getCategoryId());
        }
        if (transactionDTO.getKeywordId() != null) {
            transactionEntity.setKeywordId(transactionDTO.getKeywordId());
        }
        if (transactionDTO.getParentId() != null) {
            transactionEntity.setParentId(transactionDTO.getParentId());
        }
        if (transactionDTO.getDescription() != null) {
            transactionEntity.setDescription(transactionDTO.getDescription());
        }

        TransactionEntity updatedEntity = transactionRepository.save(transactionEntity);

        return TRANSACTION_MAPPER.toTransactionDTO(updatedEntity);
    }

    @Transactional
    public TransactionDTO replaceTransaction(Long id, TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id " + id + " not found"));

        // Directly set all fields without checking for null, assuming validation passed.
        transactionEntity.setDate(transactionDTO.getDate());
        transactionEntity.setAmount(transactionDTO.getAmount());
        transactionEntity.setCategoryId(transactionDTO.getCategoryId());
        transactionEntity.setKeywordId(transactionDTO.getKeywordId());
        transactionEntity.setParentId(transactionDTO.getParentId());
        transactionEntity.setDescription(transactionDTO.getDescription());

        TransactionEntity updatedEntity = transactionRepository.save(transactionEntity);
        return TRANSACTION_MAPPER.toTransactionDTO(updatedEntity);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Transactional
    public List<Map<String, Object>> getTransactionBetweenDates(Date startDate, Date endDate) {
        return transactionRepository.getTransactionBetweenDates(startDate, endDate).stream()
                .map(te -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", te.getDate());
                    map.put("amount", te.getAmount());
                    return map;
                })
                .toList();
    }
}