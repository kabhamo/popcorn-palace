package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.TicketDTO;
import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private TicketService ticketService;

    // POST: Book a Ticket
    @PostMapping
    public ResponseEntity<Map<String, String>> bookTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        Ticket newBookingTicket = ticketService.bookTicket(ticketDTO);
        return ResponseEntity.ok(Collections.singletonMap("bookingId", newBookingTicket.getBookingId().toString()) );
    }
}