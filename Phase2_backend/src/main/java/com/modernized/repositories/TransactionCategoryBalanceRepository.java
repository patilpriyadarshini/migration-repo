package com.modernized.repositories;

import com.modernized.entities.TransactionCategoryBalance;
import com.modernized.entities.TransactionCategoryBalanceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionCategoryBalanceRepository extends JpaRepository<TransactionCategoryBalance, TransactionCategoryBalanceId> {
}
