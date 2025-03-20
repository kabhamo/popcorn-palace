package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.exception.ShowtimeOverlapException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class  ShowtimeServiceTest {

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private ShowtimeService showtimeService;

    private Showtime testShowtime;
    private ShowtimeDTO testShowtimeDTO;
    private Movie testMovie;

    @BeforeEach
    void setUp() {
        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Inception");

        testShowtime = new Showtime();
        testShowtime.setId(10L);
        testShowtime.setMovie(testMovie);
        testShowtime.setTheater("IMAX");
        testShowtime.setStartTime(LocalDateTime.of(2025, 6, 10, 14, 0));
        testShowtime.setEndTime(LocalDateTime.of(2025, 6, 10, 16, 0));
        testShowtime.setPrice(20.0);

        testShowtimeDTO = new ShowtimeDTO();
        testShowtimeDTO.setMovieId(1L);
        testShowtimeDTO.setTheater("IMAX");
        testShowtimeDTO.setStartTime(LocalDateTime.of(2025, 6, 10, 14, 0));
        testShowtimeDTO.setEndTime(LocalDateTime.of(2025, 6, 10, 16, 0));
        testShowtimeDTO.setPrice(20.0);
    }

    // Test: getShowtimeById() - Success
    @Test
    void testGetShowtimeById_Success() {
        when(showtimeRepository.findById(10L)).thenReturn(Optional.of(testShowtime));

        Showtime result = showtimeService.getShowtimeById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("IMAX", result.getTheater());
        verify(showtimeRepository, times(1)).findById(10L);
    }

    // Test: getShowtimeById() - Showtime Not Found
    @Test
    void testGetShowtimeById_NotFound() {
        when(showtimeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> showtimeService.getShowtimeById(99L));

        verify(showtimeRepository, times(1)).findById(99L);
    }

    // Test: addShowtime() - Success
    @Test
    void testAddShowtime_Success() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(showtimeRepository.existsByTheaterAndTimeRange(any(), any(), any())).thenReturn(false);
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(testShowtime);

        Showtime result = showtimeService.addShowtime(testShowtimeDTO);

        assertNotNull(result);
        assertEquals("IMAX", result.getTheater());
        verify(movieRepository, times(1)).findById(1L);
        verify(showtimeRepository, times(1)).existsByTheaterAndTimeRange(any(), any(), any());
        verify(showtimeRepository, times(1)).save(any(Showtime.class));
    }

    // Test: addShowtime() - Movie Not Found
    @Test
    void testAddShowtime_MovieNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> showtimeService.addShowtime(testShowtimeDTO));

        verify(movieRepository, times(1)).findById(1L);
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    // Test: addShowtime() - Overlapping Showtime
    @Test
    void testAddShowtime_Overlap() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(showtimeRepository.existsByTheaterAndTimeRange(any(), any(), any())).thenReturn(true);

        assertThrows(ShowtimeOverlapException.class, () -> showtimeService.addShowtime(testShowtimeDTO));

        verify(movieRepository, times(1)).findById(1L);
        verify(showtimeRepository, times(1)).existsByTheaterAndTimeRange(any(), any(), any());
        verify(showtimeRepository, never()).save(any(Showtime.class));
    }

    // Test: deleteShowtime() - Success
    @Test
    void testDeleteShowtime_Success() {
        when(showtimeRepository.findById(10L)).thenReturn(Optional.of(testShowtime));
        doNothing().when(showtimeRepository).delete(any(Showtime.class));

        assertDoesNotThrow(() -> showtimeService.deleteShowtime(10L));

        verify(showtimeRepository, times(1)).findById(10L);
        verify(showtimeRepository, times(1)).delete(testShowtime);
    }

    // Test: deleteShowtime() - Showtime Not Found
    @Test
    void testDeleteShowtime_NotFound() {
        when(showtimeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> showtimeService.deleteShowtime(99L));

        verify(showtimeRepository, times(1)).findById(99L);
        verify(showtimeRepository, never()).delete(any(Showtime.class));
    }
}
