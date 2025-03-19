package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.exception.ShowtimeOverlapException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    // Get showtime by ID
    public Showtime getShowtimeById(Long showtimeId) {
        return showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime with ID " + showtimeId + " not found"));
    }

    // Add a new showtime (Prevents Overlaps)
    public Showtime addShowtime(ShowtimeDTO showtimeDTO) {
        Movie movie = movieRepository.findById(showtimeDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + showtimeDTO.getMovieId() + " not found"));

        // Check for overlapping showtimes before saving
        boolean conflictExists = showtimeRepository.existsByTheaterAndTimeRange(
                showtimeDTO.getTheater(), showtimeDTO.getStartTime(), showtimeDTO.getEndTime());

        if (conflictExists) {
            throw new ShowtimeOverlapException("Showtime conflicts with an existing showtime in the same theater.");
        }

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheater(showtimeDTO.getTheater());
        showtime.setStartTime(showtimeDTO.getStartTime());
        showtime.setEndTime(showtimeDTO.getEndTime());
        showtime.setPrice(showtimeDTO.getPrice());

        return showtimeRepository.save(showtime);
    }

    // Update a showtime (Prevents Overlaps)
    public Showtime updateShowtime(Long showtimeId, ShowtimeDTO showtimeDTO) {
        Showtime existingShowtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime with ID " + showtimeId + " not found"));

        boolean conflictExists = showtimeRepository.existsByTheaterAndTimeRange(
                showtimeDTO.getTheater(), showtimeDTO.getStartTime(), showtimeDTO.getEndTime());

        if (conflictExists) {
            throw new ShowtimeOverlapException("Updated showtime conflicts with an existing showtime in the same theater.");
        }

        existingShowtime.setTheater(showtimeDTO.getTheater());
        existingShowtime.setStartTime(showtimeDTO.getStartTime());
        existingShowtime.setEndTime(showtimeDTO.getEndTime());
        existingShowtime.setPrice(showtimeDTO.getPrice());

        return showtimeRepository.save(existingShowtime);
    }

    // Delete a showtime
    public void deleteShowtime(Long showtimeId){
        Showtime showtime = showtimeRepository.findById(showtimeId)
                        .orElseThrow(() -> new ResourceNotFoundException("Showtime with id '" + showtimeId + "' not found"));
        showtimeRepository.delete(showtime);
    }
}
