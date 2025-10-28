package com.modernized.utils;

import com.modernized.dto.AccountResponse;
import com.modernized.dto.CardResponse;
import com.modernized.dto.TransactionResponse;
import com.modernized.dto.UserResponse;
import com.modernized.entities.Account;
import com.modernized.entities.Card;
import com.modernized.entities.Customer;
import com.modernized.entities.Transaction;
import com.modernized.entities.User;

public class ResponseMapper {

    public static AccountResponse mapToAccountResponse(Account account) {
        if (account == null) {
            return null;
        }
        
        AccountResponse response = new AccountResponse();
        response.setAcctId(account.getAcctId());
        response.setAcctActiveStatus(account.getAcctActiveStatus());
        response.setAcctCurrBal(account.getAcctCurrBal());
        response.setAcctCreditLimit(account.getAcctCreditLimit());
        response.setAcctCashCreditLimit(account.getAcctCashCreditLimit());
        response.setAcctOpenDate(account.getAcctOpenDate());
        response.setAcctExpirationDate(account.getAcctExpirationDate());
        response.setAcctReissueDate(account.getAcctReissueDate());
        response.setAcctCurrCycCredit(account.getAcctCurrCycCredit());
        response.setAcctCurrCycDebit(account.getAcctCurrCycDebit());
        response.setAcctAddrZip(account.getAcctAddrZip());
        response.setAcctGroupId(account.getAcctGroupId());
        
        if (account.getCustomer() != null) {
            Customer customer = account.getCustomer();
            response.setCustomerFirstName(customer.getCustFirstName());
            response.setCustomerLastName(customer.getCustLastName());
            response.setCustomerSsn(String.valueOf(customer.getCustSsn()));
            response.setCustomerDateOfBirth(customer.getCustDobYyyyMmDd());
            response.setCustomerFicoScore(customer.getCustFicoCreditScore());
            response.setCustomerPhone1(customer.getCustPhoneNum1());
            response.setCustomerPhone2(customer.getCustPhoneNum2());
            response.setCustomerAddress1(customer.getCustAddrLine1());
            response.setCustomerAddress2(customer.getCustAddrLine2());
            response.setCustomerCity(customer.getCustAddrLine3());
            response.setCustomerState(customer.getCustAddrStateCd());
            response.setCustomerZipCode(customer.getCustAddrZip());
            response.setCustomerCountry(customer.getCustAddrCountryCd());
            response.setCustomerGovtIssuedId(customer.getCustGovtIssuedId());
            response.setCustomerEftAccountId(customer.getCustEftAccountId());
            response.setCustomerPriCardHolderInd(customer.getCustPriCardHolderInd());
        }
        
        return response;
    }

    public static CardResponse mapToCardResponse(Card card) {
        if (card == null) {
            return null;
        }
        
        CardResponse response = new CardResponse();
        response.setCardNum(card.getCardNum());
        response.setAcctId(card.getCardAcctId());
        response.setCardName(card.getCardEmbossedName());
        response.setCardStatus(card.getCardActiveStatus());
        response.setExpiryMonth(Integer.parseInt(card.getCardExpirationDate().substring(0, 2)));
        response.setExpiryYear(Integer.parseInt(card.getCardExpirationDate().substring(3, 7)));
        return response;
    }

    public static TransactionResponse mapToTransactionResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        
        TransactionResponse response = new TransactionResponse();
        response.setTranId(transaction.getTranId());
        response.setCardNum(transaction.getTranCardNum());
        response.setTranTypeCd(String.valueOf(transaction.getTranTypeCd()));
        response.setTranCatCd(String.valueOf(transaction.getTranCatCd()));
        response.setTranSource(transaction.getTranSource());
        response.setTranDesc(transaction.getTranDesc());
        response.setTranAmt(transaction.getTranAmt());
        response.setOrigTs(transaction.getTranOrigTs());
        response.setProcTs(transaction.getTranProcTs());
        response.setMerchantId(String.valueOf(transaction.getTranMerchantId()));
        response.setMerchantName(transaction.getTranMerchantName());
        response.setMerchantCity(transaction.getTranMerchantCity());
        response.setMerchantZip(transaction.getTranMerchantZip());
        return response;
    }

    public static UserResponse mapToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponse response = new UserResponse();
        response.setUserId(user.getSecUsrId());
        response.setFirstName(user.getSecUsrFname());
        response.setLastName(user.getSecUsrLname());
        response.setUserType(user.getSecUsrType());
        return response;
    }
}
