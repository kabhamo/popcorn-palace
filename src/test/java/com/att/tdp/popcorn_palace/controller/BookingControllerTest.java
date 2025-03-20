package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.TicketDTO;
import com.att.tdp.popcorn_palace.exception.GlobalExceptionHandler;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.exception.SeatAlreadyBookedException;
import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private BookingController bookingController;

    private ObjectMapper objectMapper;
    private TicketDTO testTicketDTO;
    private Ticket testTicket;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Ensure Exception Handling is applied
                .build();

        objectMapper = new ObjectMapper();

        testTicketDTO = new TicketDTO();
        testTicketDTO.setShowtimeId(1L);
        testTicketDTO.setSeatNumber("A12");
        testTicketDTO.setUserId(UUID.randomUUID());

        testTicket = new Ticket();
        testTicket.setBookingId(UUID.randomUUID());
        testTicket.setShowtime(null); // No need for full showtime object
        testTicket.setSeatNumber("A12");
        testTicket.setUserId(testTicketDTO.getUserId());
    }

    // Test: Book a Ticket (Success)
    @Test
    void testBookTicket_Success() throws Exception {
        when(ticketService.bookTicket(any(TicketDTO.class))).thenReturn(testTicket);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTicketDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").exists());

        verify(ticketService, times(1)).bookTicket(any(TicketDTO.class));
    }

    // Test: Booking with Non-Existent Showtime (404)
    @Test
    void testBookTicket_ShowtimeNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Showtime with ID " + testTicketDTO.getShowtimeId() + " not found"))
                .when(ticketService).bookTicket(any(TicketDTO.class));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTicketDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Showtime with ID 1 not found"));

        verify(ticketService, times(1)).bookTicket(any(TicketDTO.class));
    }

    // Test: Booking a Seat That is Already Taken (409)
    @Test
    void testBookTicket_SeatAlreadyBooked() throws Exception {
        doThrow(new SeatAlreadyBookedException("Seat " + testTicketDTO.getSeatNumber() + " is already booked for this showtime."))
                .when(ticketService).bookTicket(any(TicketDTO.class));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTicketDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Seat A12 is already booked for this showtime."));

        verify(ticketService, times(1)).bookTicket(any(TicketDTO.class));
    }
}
