package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.TicketDTO;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.exception.SeatAlreadyBookedException;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @InjectMocks
    private TicketService ticketService;

    private Showtime testShowtime;
    private TicketDTO testTicketDTO;
    private Ticket testTicket;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        testShowtime = new Showtime();
        testShowtime.setId(1L);
        testShowtime.setTheater("IMAX");

        testTicketDTO = new TicketDTO();
        testTicketDTO.setShowtimeId(1L);
        testTicketDTO.setSeatNumber("A10");
        testTicketDTO.setUserId(userId);

        testTicket = new Ticket();
        testTicket.setShowtime(testShowtime);
        testTicket.setSeatNumber("A10");
        testTicket.setUserId(userId);
    }

    // Test: bookTicket() - Success
    @Test
    void testBookTicket_Success() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(testShowtime));
        when(ticketRepository.existsByShowtimeAndSeatNumber(testShowtime, "A10")).thenReturn(false);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);

        Ticket result = ticketService.bookTicket(testTicketDTO);

        assertNotNull(result);
        assertEquals("A10", result.getSeatNumber());
        verify(showtimeRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).existsByShowtimeAndSeatNumber(testShowtime, "A10");
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    // Test: bookTicket() - Showtime Not Found
    @Test
    void testBookTicket_ShowtimeNotFound() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.bookTicket(testTicketDTO));

        verify(showtimeRepository, times(1)).findById(1L);
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    // Test: bookTicket() - Seat Already Booked
    @Test
    void testBookTicket_SeatAlreadyBooked() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(testShowtime));
        when(ticketRepository.existsByShowtimeAndSeatNumber(testShowtime, "A10")).thenReturn(true);

        assertThrows(SeatAlreadyBookedException.class, () -> ticketService.bookTicket(testTicketDTO));

        verify(showtimeRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).existsByShowtimeAndSeatNumber(testShowtime, "A10");
        verify(ticketRepository, never()).save(any(Ticket.class));
    }
}
