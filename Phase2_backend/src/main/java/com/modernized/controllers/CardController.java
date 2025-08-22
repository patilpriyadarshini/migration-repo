package com.modernized.controllers;

import com.modernized.dto.CardResponse;
import com.modernized.dto.CardUpdateRequest;
import com.modernized.dto.PagedResponse;
import com.modernized.entities.Card;
import com.modernized.repositories.CardRepository;
import com.modernized.utils.ControllerUtils;
import com.modernized.utils.EntityMapper;
import com.modernized.utils.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * Card Management Controller
 * Handles card listing, viewing, and updating based on SCREEN-006 (Card Listing), 
 * SCREEN-007 (Card Detail View), and SCREEN-008 (Card Update)
 * Provides paginated card management with search capabilities
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardRepository cardRepository;

    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    /**
     * Get Paginated Card List
     * GET /api/cards
     * 
     * Displays paginated list of credit cards with optional filtering.
     * Based on SCREEN-006 (Card Listing) with 7 cards per page.
     * 
     * @param accountId Optional account number filter
     * @param cardNumber Optional card number filter
     * @param page Page number (0-based)
     * @param size Page size (default 7 as per screen flow)
     * @return PagedResponse with card list
     */
    @GetMapping
    public ResponseEntity<PagedResponse<CardResponse>> getCards(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Card> cardPage;
        
        if (accountId != null && cardNumber != null) {
            cardPage = cardRepository.findByAcctIdAndCardNum(accountId, cardNumber, pageable);
        } else if (accountId != null) {
            cardPage = cardRepository.findByAcctId(accountId, pageable);
        } else if (cardNumber != null) {
            cardPage = cardRepository.findByCardNum(cardNumber, pageable);
        } else {
            cardPage = cardRepository.findAll(pageable);
        }
        
        PagedResponse<CardResponse> response = PaginationUtils.buildPagedResponse(
                cardPage, 
                EntityMapper::mapToCardResponse
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get Card Details
     * GET /api/cards/{cardNumber}
     * 
     * Displays detailed information for a specific credit card.
     * Based on SCREEN-007 (Card Detail View) functionality.
     * 
     * @param cardNumber 16-digit card number
     * @return CardResponse with card details
     */
    @GetMapping("/{cardNumber}")
    public ResponseEntity<CardResponse> getCard(@PathVariable String cardNumber) {
        Card card = ControllerUtils.findEntityOrThrow(
            cardRepository.findById(cardNumber), 
            "Card not found"
        );
        
        CardResponse response = EntityMapper.mapToCardResponse(card);
        return ResponseEntity.ok(response);
    }

    /**
     * Update Card Information
     * PUT /api/cards/{cardNumber}
     * 
     * Updates credit card information with validation.
     * Based on SCREEN-008 (Card Update) functionality.
     * 
     * @param cardNumber Card number to update
     * @param updateRequest Updated card data
     * @return CardResponse with updated information
     */
    @PutMapping("/{cardNumber}")
    public ResponseEntity<CardResponse> updateCard(
            @PathVariable String cardNumber,
            @Valid @RequestBody CardUpdateRequest updateRequest) {
        
        Card card = ControllerUtils.findEntityOrThrow(
            cardRepository.findById(cardNumber), 
            "Card not found"
        );
        
        EntityMapper.updateCardFromRequest(card, updateRequest);
        
        Card savedCard = cardRepository.save(card);
        CardResponse response = EntityMapper.mapToCardResponse(savedCard);
        
        return ResponseEntity.ok(response);
    }

}
