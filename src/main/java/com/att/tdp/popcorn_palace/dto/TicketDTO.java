package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private Long id; // Read-only in responses

    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    private LocalDateTime bookingTime; // Auto-generated in the service layer
}

