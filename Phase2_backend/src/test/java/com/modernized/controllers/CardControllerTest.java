package com.modernized.controllers;

import com.modernized.entities.Card;
import com.modernized.repositories.CardRepository;
import com.modernized.utils.BaseControllerTest;
import com.modernized.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
class CardControllerTest extends BaseControllerTest {

    @MockBean
    private CardRepository cardRepository;

    @Test
    void getCards_ShouldReturnPagedCards_WhenNoFilters() throws Exception {
        Card card = TestDataBuilder.createTestCard();
        Page<Card> cardPage = new PageImpl<>(Arrays.asList(card), PageRequest.of(0, 7), 1);

        when(cardRepository.findAll(any(PageRequest.class))).thenReturn(cardPage);

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cardNum").value("4000123456789012"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getCard_ShouldReturnCard_WhenCardExists() throws Exception {
        Card card = TestDataBuilder.createTestCard();

        when(cardRepository.findById("4000123456789012")).thenReturn(Optional.of(card));

        mockMvc.perform(get("/api/cards/4000123456789012"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNum").value("4000123456789012"))
                .andExpect(jsonPath("$.cardName").value("JOHN DOE"));
    }

    @Test
    void getCard_ShouldReturnNotFound_WhenCardDoesNotExist() throws Exception {
        when(cardRepository.findById("9999999999999999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cards/9999999999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCard_ShouldUpdateCard_WhenValidRequest() throws Exception {
        Card card = TestDataBuilder.createTestCard();

        when(cardRepository.findById("4000123456789012")).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        String updateRequest = "{\"cardName\":\"JANE DOE\",\"cardStatus\":\"Y\",\"expiryMonth\":6,\"expiryYear\":2026}";

        mockMvc.perform(put("/api/cards/4000123456789012")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNum").value("4000123456789012"));
    }
}
