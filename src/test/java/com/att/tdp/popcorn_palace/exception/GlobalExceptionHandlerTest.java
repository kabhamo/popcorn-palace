package com.att.tdp.popcorn_palace.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    // Test: Handle Validation Errors
    @Test
    void testHandleValidationExceptions() {
        BindException bindException = new BindException(new Object(), "objectName");
        bindException.addError(new FieldError("objectName", "fieldName", "Field is required"));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(
                new MethodArgumentNotValidException(null, bindException));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Field is required", response.getBody().get("fieldName"));
    }

    // Test: Handle Resource Not Found
    @Test
    void testHandleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Movie not found");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Movie not found", response.getBody().get("error"));
    }

    // Test: Handle Duplicate Entry (409 Conflict)
    @Test
    void testHandleDuplicateMovieException() {
        ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("Movie title already exists");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleDuplicateMovieException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Movie title already exists", response.getBody().get("error"));
    }

    // Test: Handle Showtime Overlap (409 Conflict)
    @Test
    void testHandleShowtimeOverlap() {
        ShowtimeOverlapException ex = new ShowtimeOverlapException("Showtime conflicts with another");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleShowtimeOverlap(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Showtime conflicts with another", response.getBody().get("error"));
    }

    // Test: Handle Seat Already Booked (409 Conflict)
    @Test
    void testHandleSeatAlreadyBooked() {
        SeatAlreadyBookedException ex = new SeatAlreadyBookedException("Seat A10 is already booked");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleSeatAlreadyBooked(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Seat A10 is already booked", response.getBody().get("error"));
    }

    // Test: Handle Invalid Input (400 Bad Request)
    @Test
    void testHandleInvalidInput() {
        InvalidInputException ex = new InvalidInputException("Invalid input provided");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleInvalidInput(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input provided", response.getBody().get("error"));
    }

    // Test: Handle General Exception (500 Internal Server Error)
    @Test
    void testHandleGeneralException() {
        Exception ex = new Exception("Unexpected error occurred");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("Unexpected error occurred"));
    }
}
