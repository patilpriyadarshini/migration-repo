package com.modernized.dto;

import jakarta.validation.constraints.*;

public class CardUpdateRequest {
    
    @NotBlank(message = "Card name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Card name must contain only alphabetic characters and spaces")
    private String cardName;

    @NotBlank(message = "Card status cannot be empty")
    @Pattern(regexp = "[YN]", message = "Card status must be Y or N")
    private String cardStatus;

    @NotNull(message = "Expiry month cannot be null")
    @Min(value = 1, message = "Expiry month must be between 1 and 12")
    @Max(value = 12, message = "Expiry month must be between 1 and 12")
    private Integer expiryMonth;

    @NotNull(message = "Expiry year cannot be null")
    @Min(value = 1950, message = "Expiry year must be between 1950 and 2099")
    @Max(value = 2099, message = "Expiry year must be between 1950 and 2099")
    private Integer expiryYear;

    public CardUpdateRequest() {}

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }

    public String getCardStatus() { return cardStatus; }
    public void setCardStatus(String cardStatus) { this.cardStatus = cardStatus; }

    public Integer getExpiryMonth() { return expiryMonth; }
    public void setExpiryMonth(Integer expiryMonth) { this.expiryMonth = expiryMonth; }

    public Integer getExpiryYear() { return expiryYear; }
    public void setExpiryYear(Integer expiryYear) { this.expiryYear = expiryYear; }
}
