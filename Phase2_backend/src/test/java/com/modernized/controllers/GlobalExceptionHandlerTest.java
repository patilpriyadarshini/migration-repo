package com.modernized.controllers;

import com.modernized.controllers.GlobalExceptionHandler.EntityNotFoundException;
import com.modernized.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleEntityNotFound_ShouldReturnNotFound() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        EntityNotFoundException ex = new EntityNotFoundException("Test entity not found");

        var response = handler.handleEntityNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Test entity not found", response.getBody().getMessage());
    }

    @Test
    void handleIllegalArgument_ShouldReturnBadRequest() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        
        var response = handler.handleIllegalArgument(new IllegalArgumentException("Invalid input"));

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid input", response.getBody().getMessage());
    }

    @Test
    void handleGenericError_ShouldReturnInternalServerError() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        
        var response = handler.handleGenericError(new RuntimeException("Unexpected error"));

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }

    private void assertEquals(int expected, int actual) {
        assert expected == actual;
    }

    private void assertEquals(String expected, String actual) {
        assert expected.equals(actual);
    }
}
