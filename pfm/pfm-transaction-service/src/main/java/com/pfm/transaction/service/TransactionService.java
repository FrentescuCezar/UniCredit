package com.pfm.transaction.service;

import com.pfm.transaction.exception.TransactionNotFoundException;
import com.pfm.transaction.redis.TransactionCacheService;
import com.pfm.transaction.repository.TransactionRepository;
import com.pfm.transaction.repository.model.TransactionEntity;

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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.pfm.transaction.service.mapper.TransactionMapper.TRANSACTION_MAPPER;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionCacheService cacheService;
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(TRANSACTION_MAPPER::toTransactionDTO)
                .toList();
    }

    public Page<TransactionEntity> getAllTransactionsPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return transactionRepository.findAllByOrderByDateDesc(pageable);
    }

    @Transactional
    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = TRANSACTION_MAPPER.toTransactionEntity(transactionDTO);
        TransactionEntity savedEntity = transactionRepository.save(transactionEntity);
        TransactionDTO savedTransaction = TRANSACTION_MAPPER.toTransactionDTO(savedEntity);

        cacheService.cacheTransaction(savedTransaction.getDate().getTime(), savedTransaction);
        return savedTransaction;
    }

    @Transactional
    public List<TransactionDTO> saveTransactions(List<TransactionDTO> transactionDTOs) {
        List<TransactionEntity> entities = TRANSACTION_MAPPER.toTransactionEntityList(transactionDTOs);
        List<TransactionEntity> savedEntities = transactionRepository.saveAll(entities);
        List<TransactionDTO> savedTransactions = TRANSACTION_MAPPER.toTransactionDTOList(savedEntities);

        savedTransactions.forEach(tx -> cacheService.cacheTransaction(tx.getDate().getTime(), tx));
        return savedTransactions;
    }

    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(Long id) {
        TransactionDTO cachedTransaction = cacheService.getCachedTransaction(id);
        if (cachedTransaction != null) {
            return cachedTransaction;
        }

        TransactionDTO transaction = transactionRepository.findById(id)
                .map(TRANSACTION_MAPPER::toTransactionDTO)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id " + id + " not found"));

        cacheService.cacheTransaction(id, transaction);
        return transaction;
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
        TransactionDTO updatedTransaction = TRANSACTION_MAPPER.toTransactionDTO(updatedEntity);

        // Update cache
        cacheService.cacheTransaction(id, updatedTransaction);
        return updatedTransaction;
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
        TransactionDTO updatedTransaction = TRANSACTION_MAPPER.toTransactionDTO(updatedEntity);

        // Update cache
        cacheService.cacheTransaction(id, updatedTransaction);
        return updatedTransaction;
    }

    @Transactional
    public TransactionDTO updateTransactionCategory(Long id, CategoryUpdateDTO categoryUpdateDTO) {
        TransactionEntity transactionEntity = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with ID " + id + " not found"));

        transactionEntity.setCategoryId(categoryUpdateDTO.getCategoryId());

        TransactionEntity updatedTransaction = transactionRepository.save(transactionEntity);
        TransactionDTO updatedTransactionDTO = TRANSACTION_MAPPER.toTransactionDTO(updatedTransaction);

        cacheService.cacheTransaction(id, updatedTransactionDTO);
        return updatedTransactionDTO;
    }

    @Transactional
    public void deleteTransaction(Long id) {
        boolean exists = transactionRepository.existsById(id);
        if (!exists) {
            throw new TransactionNotFoundException("Transaction with ID " + id + " not found");
        }
        transactionRepository.deleteById(id);

        cacheService.evictTransactionCache(id);
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
