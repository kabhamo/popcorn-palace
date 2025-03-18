package com.att.tdp.popcorn_palace.dto;

import com.att.tdp.popcorn_palace.model.Ticket;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private UUID id; // Read-only in responses

    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    private LocalDateTime bookingTime; // Auto-generated in the service layer

    public TicketDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.showtimeId = ticket.getShowtime().getId(); // Extract Showtime ID instead of full object
        this.customerName = ticket.getCustomerName();
        this.seatNumber = ticket.getSeatNumber();
        this.bookingTime = ticket.getBookingTime();
    }
}

