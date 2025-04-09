package com.test.paymenttransfer.repository;

import com.test.paymenttransfer.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySourceIdOrDestinationId(Long fromId, Long toId);

    @Query("SELECT t FROM Transaction t " +
            "WHERE (t.sourceId = :accountId OR t.destinationId = :accountId) " +
            "AND t.timestamp BETWEEN :start AND :end")
    List<Transaction> findAllByAccountAndDateRange(
            @Param("accountId") Long accountId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
