package com.pfm.transaction.service.mapper;

import com.pfm.transaction.repository.model.TransactionEntity;
import com.pfm.transaction.service.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper TRANSACTION_MAPPER = Mappers.getMapper(TransactionMapper.class);

    TransactionDTO toTransactionDTO(TransactionEntity transactionEntity);

    TransactionEntity toTransactionEntity(TransactionDTO transactionDTO);
}