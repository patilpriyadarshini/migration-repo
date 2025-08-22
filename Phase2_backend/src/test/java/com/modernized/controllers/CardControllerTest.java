package com.modernized.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modernized.dto.CardResponse;
import com.modernized.dto.CardUpdateRequest;
import com.modernized.dto.PagedResponse;
import com.modernized.entities.Card;
import com.modernized.repositories.CardRepository;
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

    @Test
    void getCards_ShouldReturnPagedCards() throws Exception {
        Card card = createTestCard();
        Page<Card> cardPage = new PageImpl<>(Arrays.asList(card), PageRequest.of(0, 7), 1);
        when(cardRepository.findAll(any(PageRequest.class))).thenReturn(cardPage);

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cardNum").value("1234567890123456"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getCard_ShouldReturnCard_WhenCardExists() throws Exception {
        Card card = createTestCard();
        when(cardRepository.findById("1234567890123456")).thenReturn(Optional.of(card));

        mockMvc.perform(get("/api/cards/1234567890123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNum").value("1234567890123456"))
                .andExpect(jsonPath("$.cardName").value("John Doe"));
    }

    @Test
    void getCard_ShouldReturnNotFound_WhenCardDoesNotExist() throws Exception {
        when(cardRepository.findById("1234567890123456")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cards/1234567890123456"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCard_ShouldUpdateAndReturnCard_WhenValidRequest() throws Exception {
        Card card = createTestCard();
        CardUpdateRequest updateRequest = createTestUpdateRequest();
        
        when(cardRepository.findById("1234567890123456")).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        mockMvc.perform(put("/api/cards/1234567890123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNum").value("1234567890123456"));
    }

    private Card createTestCard() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardAcctId(1L);
        card.setCardEmbossedName("John Doe");
        card.setCardActiveStatus("Y");
        card.setCardExpiraionDate("12/2025");
        return card;
    }

    private CardUpdateRequest createTestUpdateRequest() {
        CardUpdateRequest request = new CardUpdateRequest();
        request.setCardName("Jane Doe");
        request.setCardStatus("Y");
        request.setExpiryMonth(12);
        request.setExpiryYear(2026);
        return request;
    }
}
