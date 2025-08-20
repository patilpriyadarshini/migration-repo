package com.modernized.repositories;

import com.modernized.entities.DailyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyTransactionRepository extends JpaRepository<DailyTransaction, String> {
}
