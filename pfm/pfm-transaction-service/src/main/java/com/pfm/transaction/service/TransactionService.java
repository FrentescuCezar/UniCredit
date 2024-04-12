package com.pfm.transaction.service;

import com.pfm.transaction.exception.TransactionNotFoundException;
import com.pfm.transaction.repository.model.TransactionEntity;
import com.pfm.transaction.repository.TransactionRepository;
import com.pfm.transaction.service.dto.CategoryUpdateDTO;
import com.pfm.transaction.service.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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

    public Page<TransactionDTO> getAllTransactionsPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<TransactionEntity> transactionEntities = transactionRepository.findAllByOrderByDateDesc(pageable);
        return transactionEntities.map(TRANSACTION_MAPPER::toTransactionDTO);
    }

    @Transactional
    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = TRANSACTION_MAPPER.toTransactionEntity(transactionDTO);
        TransactionEntity savedEntity = transactionRepository.save(transactionEntity);
        return TRANSACTION_MAPPER.toTransactionDTO(savedEntity);
    }

    @Transactional
    public List<TransactionDTO> saveTransactions(List<TransactionDTO> transactionDTOs) {
        List<TransactionEntity> entities = TRANSACTION_MAPPER.toTransactionEntityList(transactionDTOs);
        List<TransactionEntity> savedEntities = transactionRepository.saveAll(entities);
        return TRANSACTION_MAPPER.toTransactionDTOList(savedEntities);
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

        if (ObjectUtils.isNotEmpty(transactionDTO.getDate())) {
            transactionEntity.setDate(transactionDTO.getDate());
        }
        if (ObjectUtils.isNotEmpty(transactionDTO.getAmount())) {
            transactionEntity.setAmount(transactionDTO.getAmount());
        }
        if (ObjectUtils.isNotEmpty(transactionDTO.getCategoryId())) {
            transactionEntity.setCategoryId(transactionDTO.getCategoryId());
        }
        if (ObjectUtils.isNotEmpty(transactionDTO.getKeywordId())) {
            transactionEntity.setKeywordId(transactionDTO.getKeywordId());
        }
        if (ObjectUtils.isNotEmpty(transactionDTO.getParentId())) {
            transactionEntity.setParentId(transactionDTO.getParentId());
        }
        if (ObjectUtils.isNotEmpty(transactionDTO.getDescription())) {
            transactionEntity.setDescription(transactionDTO.getDescription());
        }

        TransactionEntity updatedEntity = transactionRepository.save(transactionEntity);

        return TRANSACTION_MAPPER.toTransactionDTO(updatedEntity);
    }

    @Transactional
    public TransactionDTO replaceTransaction(Long id, TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id " + id + " not found"));

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
    public TransactionDTO updateTransactionCategory(Long id, CategoryUpdateDTO categoryUpdateDTO) {
        TransactionEntity transactionEntity = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with ID " + id + " not found"));

        transactionEntity.setCategoryId(categoryUpdateDTO.getCategoryId());

        TransactionEntity updatedTransaction = transactionRepository.save(transactionEntity);
        return TRANSACTION_MAPPER.toTransactionDTO(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        boolean exists = transactionRepository.existsById(id);
        if (!exists) {
            throw new TransactionNotFoundException("Transaction with ID " + id + " not found");
        }
        transactionRepository.deleteById(id);
    }

    public List<TransactionDTO> findTransactionsBetweenDates(LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<TransactionEntity> transactions = transactionRepository.findByDateBetween(start, end);

        return transactions.stream()
                .map(TRANSACTION_MAPPER::toTransactionDTO)
                .collect(Collectors.toList());
    }
}