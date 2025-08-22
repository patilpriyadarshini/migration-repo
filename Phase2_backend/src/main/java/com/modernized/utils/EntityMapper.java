package com.modernized.utils;

import com.modernized.dto.*;
import com.modernized.entities.*;

public class EntityMapper {

    public static AccountResponse mapToAccountResponse(Account account) {
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
        CardResponse response = new CardResponse();
        response.setCardNum(card.getCardNum());
        response.setAcctId(card.getCardAcctId());
        response.setCardName(card.getCardEmbossedName());
        response.setCardStatus(card.getCardActiveStatus());
        
        String expirationDate = card.getCardExpirationDate();
        if (expirationDate == null || expirationDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Card expiration date cannot be null or empty");
        }
        
        String[] parts = expirationDate.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid expiration date format: " + expirationDate + ". Expected MM/YYYY format.");
        }
        
        try {
            response.setExpiryMonth(Integer.parseInt(parts[0].trim()));
            response.setExpiryYear(Integer.parseInt(parts[1].trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid expiration date format: " + expirationDate + ". Could not parse month/year: " + e.getMessage());
        }
        
        return response;
    }

    public static TransactionResponse mapToTransactionResponse(Transaction transaction) {
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
        UserResponse response = new UserResponse();
        response.setUserId(user.getSecUsrId());
        response.setFirstName(user.getSecUsrFname());
        response.setLastName(user.getSecUsrLname());
        response.setUserType(user.getSecUsrType());
        return response;
    }

    public static void updateAccountFromRequest(Account account, AccountUpdateRequest request) {
        account.setAcctActiveStatus(request.getAcctActiveStatus());
        account.setAcctCurrBal(request.getAcctCurrBal());
        account.setAcctCreditLimit(request.getAcctCreditLimit());
        account.setAcctCashCreditLimit(request.getAcctCashCreditLimit());
        account.setAcctOpenDate(request.getAcctOpenDate());
        account.setAcctExpirationDate(request.getAcctExpirationDate());
        account.setAcctReissueDate(request.getAcctReissueDate());
        account.setAcctCurrCycCredit(request.getAcctCurrCycCredit());
        account.setAcctCurrCycDebit(request.getAcctCurrCycDebit());
        account.setAcctAddrZip(request.getAcctAddrZip());
        account.setAcctGroupId(request.getAcctGroupId());
        
        if (account.getCustomer() != null) {
            Customer customer = account.getCustomer();
            customer.setCustFirstName(request.getCustomerFirstName());
            customer.setCustLastName(request.getCustomerLastName());
            String ssnString = request.getCustomerSsn();
            if (ssnString != null && !ssnString.trim().isEmpty()) {
                String cleanSsn = ssnString.replace("-", "").trim();
                customer.setCustSsn(Long.parseLong(cleanSsn));
            }
            customer.setCustDobYyyyMmDd(request.getCustomerDateOfBirth());
            customer.setCustFicoCreditScore(request.getCustomerFicoScore());
            customer.setCustPhoneNum1(request.getCustomerPhone1());
            customer.setCustPhoneNum2(request.getCustomerPhone2());
            customer.setCustAddrLine1(request.getCustomerAddress1());
            customer.setCustAddrLine2(request.getCustomerAddress2());
            customer.setCustAddrLine3(request.getCustomerCity());
            customer.setCustAddrStateCd(request.getCustomerState());
            customer.setCustAddrZip(request.getCustomerZipCode());
            customer.setCustAddrCountryCd(request.getCustomerCountry());
            customer.setCustGovtIssuedId(request.getCustomerGovtIssuedId());
            customer.setCustEftAccountId(request.getCustomerEftAccountId());
            customer.setCustPriCardHolderInd(request.getCustomerPriCardHolderInd());
        }
    }

    public static void updateCardFromRequest(Card card, CardUpdateRequest request) {
        card.setCardEmbossedName(request.getCardName());
        card.setCardActiveStatus(request.getCardStatus());
        String formattedDate = String.format("%02d/%d", request.getExpiryMonth(), request.getExpiryYear());
        card.setCardExpirationDate(formattedDate);
    }
}
