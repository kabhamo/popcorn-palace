package com.att.tdp.popcorn_palace.dto;

import com.att.tdp.popcorn_palace.model.Showtime;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeDTO {

    private Long id; // Read-only in responses

    @NotNull(message = "Movie ID is required")
    private Long movieId; // Instead of returning the whole Movie entity

    @NotBlank(message = "Theater name is required")
    private String theater;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @Positive(message = "Price must be greater than 0")
    private double price;

    public ShowtimeDTO(Showtime showtime) {
        this.id = showtime.getId();
        this.movieId = showtime.getMovie().getId(); // Extracting Movie ID instead of entire Movie object
        this.theater = showtime.getTheater();
        this.startTime = showtime.getStartTime();
        this.endTime = showtime.getEndTime();
        this.price = showtime.getPrice();
    }
}

