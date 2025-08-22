package com.modernized.controllers;

import com.modernized.dto.CardResponse;
import com.modernized.dto.CardUpdateRequest;
import com.modernized.dto.PagedResponse;
import com.modernized.entities.Card;
import com.modernized.repositories.CardRepository;
import com.modernized.utils.EntityMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @MockBean
    private EntityMapper entityMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCards_ShouldReturnPagedCards() throws Exception {
        Card card = createTestCard();
        CardResponse cardResponse = createTestCardResponse();
        Page<Card> cardPage = new PageImpl<>(Arrays.asList(card), PageRequest.of(0, 7), 1);

        when(cardRepository.findAll(any(PageRequest.class))).thenReturn(cardPage);
        when(entityMapper.mapToCardResponse(any(Card.class))).thenReturn(cardResponse);

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getCard_ShouldReturnCard_WhenCardExists() throws Exception {
        Card card = createTestCard();
        CardResponse response = createTestCardResponse();

        when(cardRepository.findById(anyString())).thenReturn(Optional.of(card));
        when(entityMapper.mapToCardResponse(any(Card.class))).thenReturn(response);

        mockMvc.perform(get("/api/cards/1234567890123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNum").value("1234567890123456"));
    }

    @Test
    void updateCard_ShouldReturnUpdatedCard_WhenValidRequest() throws Exception {
        Card card = createTestCard();
        CardResponse response = createTestCardResponse();
        CardUpdateRequest updateRequest = createTestUpdateRequest();

        when(cardRepository.findById(anyString())).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(entityMapper.mapToCardResponse(any(Card.class))).thenReturn(response);

        mockMvc.perform(put("/api/cards/1234567890123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNum").value("1234567890123456"));
    }

    private Card createTestCard() {
        Card card = new Card();
        card.setCardNum("1234567890123456");
        card.setCardAcctId(123456789L);
        card.setCardEmbossedName("JOHN DOE");
        card.setCardActiveStatus("Y");
        card.setCardExpiraionDate("12/2025");
        return card;
    }

    private CardResponse createTestCardResponse() {
        CardResponse response = new CardResponse();
        response.setCardNum("1234567890123456");
        response.setAcctId(123456789L);
        response.setCardName("JOHN DOE");
        response.setCardStatus("Y");
        response.setExpiryMonth(12);
        response.setExpiryYear(2025);
        return response;
    }

    private CardUpdateRequest createTestUpdateRequest() {
        CardUpdateRequest request = new CardUpdateRequest();
        request.setCardName("JOHN SMITH");
        request.setCardStatus("Y");
        request.setExpiryMonth(12);
        request.setExpiryYear(2026);
        return request;
    }
}
