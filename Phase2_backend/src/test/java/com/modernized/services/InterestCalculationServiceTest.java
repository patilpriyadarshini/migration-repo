package com.modernized.services;

import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InterestCalculationServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private InterestCalculationService interestCalculationService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setAcctId(123456789L);
        testAccount.setAcctCurrBal(new BigDecimal("1000.00"));
    }

    @Test
    void testServiceExists() {
        assertNotNull(interestCalculationService);
    }

    @Test
    void testAccountCreation() {
        assertEquals(123456789L, testAccount.getAcctId());
        assertEquals(new BigDecimal("1000.00"), testAccount.getAcctCurrBal());
    }
}
