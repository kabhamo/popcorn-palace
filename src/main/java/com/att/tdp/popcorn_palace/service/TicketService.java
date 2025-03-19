package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.TicketDTO;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.exception.SeatAlreadyBookedException;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    // Book a ticket for a showtime
    public Ticket bookTicket(TicketDTO ticketDTO) {
        Showtime showtime = showtimeRepository.findById(ticketDTO.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime with ID " + ticketDTO.getShowtimeId() + " not found"));

        // Check if the seat is already booked for this showtime
        boolean seatBooked = ticketRepository.existsByShowtimeAndSeatNumber(showtime, ticketDTO.getSeatNumber());
        if (seatBooked) {
            throw new SeatAlreadyBookedException("Seat " + ticketDTO.getSeatNumber() + " is already booked for this showtime.");
        }

        Ticket ticket = new Ticket();
        ticket.setShowtime(showtime);
        ticket.setSeatNumber(ticketDTO.getSeatNumber());
        ticket.setUserId(ticketDTO.getUserId());

        return ticketRepository.save(ticket);
    }

}
