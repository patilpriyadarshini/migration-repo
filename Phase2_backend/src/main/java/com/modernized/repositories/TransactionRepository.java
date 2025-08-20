package com.modernized.repositories;

import com.modernized.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    @Query("SELECT t FROM Transaction t WHERE t.tranId LIKE %:tranId%")
    Page<Transaction> findByTranIdContaining(@Param("tranId") String tranId, Pageable pageable);
}
