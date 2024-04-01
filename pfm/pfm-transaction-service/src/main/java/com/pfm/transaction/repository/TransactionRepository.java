package com.pfm.transaction.repository;

import com.pfm.transaction.repository.model.TransactionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT te FROM TransactionEntity te WHERE te.date >= ?1 AND te.date <= ?2")
    public List<TransactionEntity> getTransactionBetweenDates(Date startDate, Date endDate);

}