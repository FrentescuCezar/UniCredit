package com.pfm.transaction.service;

import com.pfm.transaction.repository.model.TransactionEntity;
import com.pfm.transaction.repository.TransactionRepository;
import com.pfm.transaction.service.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static com.pfm.transaction.service.mapper.TransactionMapper.*;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    //private final TransactionMapper transactionMapper;


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
    public Optional<TransactionDTO> getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(TRANSACTION_MAPPER::toTransactionDTO);
    }

    @Transactional
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transactionEntity = TRANSACTION_MAPPER.toTransactionEntity(transactionDTO);
        TransactionEntity updatedEntity = transactionRepository.save(transactionEntity);
        return TRANSACTION_MAPPER.toTransactionDTO(updatedEntity);
    }


    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Transactional
    public List<TransactionDTO> getTransactionBetweenDates(Date startDate, Date endDate) {
        return transactionRepository.getTransactionBetweenDates(startDate, endDate).stream()
                .map(TRANSACTION_MAPPER::toTransactionDTO)
                .toList();
    }
}