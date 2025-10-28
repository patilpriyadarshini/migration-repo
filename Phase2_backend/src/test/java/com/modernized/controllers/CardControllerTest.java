package com.modernized.controllers;

import com.modernized.dto.CardResponse;
import com.modernized.dto.CardUpdateRequest;
import com.modernized.dto.PagedResponse;
import com.modernized.entities.Card;
import com.modernized.repositories.CardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardRepository cardRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Card testCard;

    @BeforeEach
    void setUp() {
        testCard = new Card();
        testCard.setCardNum("4111111111111111");
        testCard.setCardAcctId(12345678901L);
        testCard.setCardEmbossedName("John Doe");
        testCard.setCardActiveStatus("Y");
        testCard.setCardExpirationDate("12/2025");
    }

    @Test
    void getCards_ShouldReturnPagedCards_WhenNoFilters() throws Exception {
        Page<Card> cardPage = new PageImpl<>(Arrays.asList(testCard), PageRequest.of(0, 7), 1);
        when(cardRepository.findAll(any(PageRequest.class))).thenReturn(cardPage);

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].cardNum").value("4111111111111111"))
                .andExpect(jsonPath("$.content[0].cardName").value("John Doe"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getCards_ShouldReturnFilteredCards_WhenAccountIdProvided() throws Exception {
        Page<Card> cardPage = new PageImpl<>(Arrays.asList(testCard), PageRequest.of(0, 7), 1);
        when(cardRepository.findByAcctId(any(Long.class), any(PageRequest.class))).thenReturn(cardPage);

        mockMvc.perform(get("/api/cards?accountId=12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].cardNum").value("4111111111111111"));
    }

    @Test
    void getCard_ShouldReturnCard_WhenCardExists() throws Exception {
        when(cardRepository.findById("4111111111111111")).thenReturn(Optional.of(testCard));

        mockMvc.perform(get("/api/cards/4111111111111111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNum").value("4111111111111111"))
                .andExpect(jsonPath("$.cardName").value("John Doe"))
                .andExpect(jsonPath("$.expiryMonth").value(12))
                .andExpect(jsonPath("$.expiryYear").value(2025));
    }

    @Test
    void getCard_ShouldReturnNotFound_WhenCardDoesNotExist() throws Exception {
        when(cardRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cards/9999999999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCard_ShouldReturnUpdatedCard_WhenValidRequest() throws Exception {
        CardUpdateRequest updateRequest = new CardUpdateRequest();
        updateRequest.setCardName("Jane Smith");
        updateRequest.setCardStatus("Y");
        updateRequest.setExpiryMonth(6);
        updateRequest.setExpiryYear(2026);

        Card updatedCard = new Card();
        updatedCard.setCardNum("4111111111111111");
        updatedCard.setCardEmbossedName("Jane Smith");
        updatedCard.setCardActiveStatus("Y");
        updatedCard.setCardExpirationDate("6/2026");

        when(cardRepository.findById("4111111111111111")).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(updatedCard);

        mockMvc.perform(put("/api/cards/4111111111111111")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNum").value("4111111111111111"))
                .andExpect(jsonPath("$.cardName").value("Jane Smith"));
    }

    @Test
    void updateCard_ShouldReturnNotFound_WhenCardDoesNotExist() throws Exception {
        CardUpdateRequest updateRequest = new CardUpdateRequest();
        updateRequest.setCardName("Jane Smith");
        updateRequest.setCardStatus("Y");
        updateRequest.setExpiryMonth(6);
        updateRequest.setExpiryYear(2026);

        when(cardRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/cards/9999999999999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }
}
