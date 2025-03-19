package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.exception.InvalidInputException;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;

    // Get Showtime by ID
    @GetMapping("/{showtimeId}")
    public ResponseEntity<ShowtimeDTO> getShowtimeById(@PathVariable Long showtimeId) {
        validateShowtimeId(showtimeId);
        Showtime showtime = showtimeService.getShowtimeById(showtimeId);
        return ResponseEntity.ok(new ShowtimeDTO(showtime));
    }

    // Add a new showtime
    @PostMapping
    public ResponseEntity<ShowtimeDTO> addShowtime(@Valid @RequestBody ShowtimeDTO showtimeDTO) {
        Showtime newShowtime = showtimeService.addShowtime(showtimeDTO);
        return ResponseEntity.ok(new ShowtimeDTO(newShowtime));
    }

    // Update a showtime by ID
    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<ShowtimeDTO> updateShowtime(@PathVariable Long showtimeId, @Valid @RequestBody ShowtimeDTO showtimeDTO) {
        validateShowtimeId(showtimeId);
        Showtime updatedShowtime = showtimeService.updateShowtime(showtimeId, showtimeDTO);
        return ResponseEntity.ok(new ShowtimeDTO(updatedShowtime));
    }

    // Delete a showtime by ID
    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long showtimeId) {
        validateShowtimeId(showtimeId);
        showtimeService.deleteShowtime(showtimeId);
        return ResponseEntity.ok().build();
    }

    // Validate Showtime ID
    private void validateShowtimeId(Long showtimeId) {
        if (showtimeId == null || showtimeId <= 0) {
            throw new InvalidInputException("Invalid Showtime ID. ID must be a positive number.");
        }
    }
}
