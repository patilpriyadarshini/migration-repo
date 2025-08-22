package com.modernized.utils;

import com.modernized.controllers.GlobalExceptionHandler.EntityNotFoundException;
import com.modernized.entities.Account;
import com.modernized.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerUtilsTest {

    @Mock
    private AccountRepository accountRepository;

    @Test
    void findEntityById_ExistingEntity_ReturnsEntity() {
        Account testAccount = new Account();
        testAccount.setAcctId(123456789L);
        
        when(accountRepository.findById(123456789L)).thenReturn(Optional.of(testAccount));

        Account result = ControllerUtils.findEntityById(accountRepository, 123456789L, "Account");

        assertEquals(testAccount, result);
        verify(accountRepository).findById(123456789L);
    }

    @Test
    void findEntityById_NonExistingEntity_ThrowsEntityNotFoundException() {
        when(accountRepository.findById(999999999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> 
            ControllerUtils.findEntityById(accountRepository, 999999999L, "Account"));

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository).findById(999999999L);
    }

    @Test
    void findAccountById_ExistingAccount_ReturnsAccount() {
        Account testAccount = new Account();
        testAccount.setAcctId(123456789L);
        
        when(accountRepository.findById(123456789L)).thenReturn(Optional.of(testAccount));

        Account result = ControllerUtils.findAccountById(accountRepository, 123456789L);

        assertEquals(testAccount, result);
        verify(accountRepository).findById(123456789L);
    }

    @Test
    void findAccountById_NonExistingAccount_ThrowsEntityNotFoundException() {
        when(accountRepository.findById(999999999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> 
            ControllerUtils.findAccountById(accountRepository, 999999999L));

        assertEquals("Account ID NOT found not found", exception.getMessage());
        verify(accountRepository).findById(999999999L);
    }
}
