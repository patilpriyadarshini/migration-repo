package com.modernized.repositories;

import com.modernized.entities.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, String> {
    
    @Query("SELECT c FROM Card c WHERE c.cardAcctId = :acctId AND c.cardNum = :cardNum")
    Page<Card> findByAcctIdAndCardNum(@Param("acctId") Long acctId, @Param("cardNum") String cardNum, Pageable pageable);
    
    @Query("SELECT c FROM Card c WHERE c.cardAcctId = :acctId")
    Page<Card> findByAcctId(@Param("acctId") Long acctId, Pageable pageable);
    
    @Query("SELECT c FROM Card c WHERE c.cardNum = :cardNum")
    Page<Card> findByCardNum(@Param("cardNum") String cardNum, Pageable pageable);
}
