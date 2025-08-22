package com.modernized.controllers;

import com.modernized.dto.CardResponse;
import com.modernized.dto.CardUpdateRequest;
import com.modernized.dto.PagedResponse;
import com.modernized.entities.Card;
import com.modernized.repositories.CardRepository;
import com.modernized.utils.EntityMapper;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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

    private Card testCard;
    private CardResponse testCardResponse;
    private CardUpdateRequest testUpdateRequest;

    @BeforeEach
    void setUp() {
        testCard = new Card();
        testCard.setCardNum("1234567890123456");
        testCard.setCardAcctId(123456789L);
        testCard.setCardEmbossedName("JOHN DOE");
        testCard.setCardActiveStatus("Y");
        testCard.setCardExpiraionDate("12/2025");

        testCardResponse = new CardResponse();
        testCardResponse.setCardNum("1234567890123456");
        testCardResponse.setAcctId(123456789L);
        testCardResponse.setCardName("JOHN DOE");
        testCardResponse.setCardStatus("Y");
        testCardResponse.setExpiryMonth(12);
        testCardResponse.setExpiryYear(2025);

        testUpdateRequest = new CardUpdateRequest();
        testUpdateRequest.setCardName("JANE SMITH");
        testUpdateRequest.setCardStatus("Y");
        testUpdateRequest.setExpiryMonth(6);
        testUpdateRequest.setExpiryYear(2026);
    }

    @Test
    void getCards_NoFilters_ReturnsPagedResponse() throws Exception {
        List<Card> cards = Arrays.asList(testCard);
        Page<Card> cardPage = new PageImpl<>(cards, PageRequest.of(0, 7), 1);
        
        when(cardRepository.findAll(any(PageRequest.class))).thenReturn(cardPage);
        when(entityMapper.mapToCardResponse(any(Card.class))).thenReturn(testCardResponse);

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].cardNum").value("1234567890123456"))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(7))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(cardRepository).findAll(any(PageRequest.class));
        verify(entityMapper).mapToCardResponse(testCard);
    }

    @Test
    void getCards_WithAccountIdFilter_ReturnsFilteredResults() throws Exception {
        List<Card> cards = Arrays.asList(testCard);
        Page<Card> cardPage = new PageImpl<>(cards, PageRequest.of(0, 7), 1);
        
        when(cardRepository.findByAcctId(anyLong(), any(PageRequest.class))).thenReturn(cardPage);
        when(entityMapper.mapToCardResponse(any(Card.class))).thenReturn(testCardResponse);

        mockMvc.perform(get("/api/cards?accountId=123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(cardRepository).findByAcctId(eq(123456789L), any(PageRequest.class));
    }

    @Test
    void getCard_ValidCardNumber_ReturnsCardResponse() throws Exception {
        when(cardRepository.findById(anyString())).thenReturn(Optional.of(testCard));
        when(entityMapper.mapToCardResponse(any(Card.class))).thenReturn(testCardResponse);

        mockMvc.perform(get("/api/cards/1234567890123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cardNum").value("1234567890123456"))
                .andExpect(jsonPath("$.acctId").value(123456789L))
                .andExpect(jsonPath("$.cardName").value("JOHN DOE"));

        verify(cardRepository).findById("1234567890123456");
        verify(entityMapper).mapToCardResponse(testCard);
    }

    @Test
    void getCard_InvalidCardNumber_ThrowsEntityNotFoundException() throws Exception {
        when(cardRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cards/9999999999999999"))
                .andExpect(status().isNotFound());

        verify(cardRepository).findById("9999999999999999");
        verify(entityMapper, never()).mapToCardResponse(any());
    }

    @Test
    void updateCard_ValidRequest_ReturnsUpdatedCard() throws Exception {
        when(cardRepository.findById(anyString())).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);
        when(entityMapper.mapToCardResponse(any(Card.class))).thenReturn(testCardResponse);

        mockMvc.perform(put("/api/cards/1234567890123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cardNum").value("1234567890123456"));

        verify(cardRepository).findById("1234567890123456");
        verify(entityMapper).updateCardFromRequest(eq(testCard), any(CardUpdateRequest.class));
        verify(cardRepository).save(testCard);
        verify(entityMapper).mapToCardResponse(testCard);
    }

    @Test
    void updateCard_InvalidCardNumber_ThrowsEntityNotFoundException() throws Exception {
        when(cardRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/cards/9999999999999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUpdateRequest)))
                .andExpect(status().isNotFound());

        verify(cardRepository).findById("9999999999999999");
        verify(cardRepository, never()).save(any());
    }
}
