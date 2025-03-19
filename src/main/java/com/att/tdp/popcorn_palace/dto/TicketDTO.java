package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private UUID bookingId; // Read-only in responses

    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "User ID is required")
    private UUID userId;
}

