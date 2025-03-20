package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.exception.ResourceAlreadyExistsException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie testMovie;
    private MovieDTO testMovieDTO;

    @BeforeEach
    void setUp() {
        testMovie = new Movie();
        testMovie.setTitle("Inception");
        testMovie.setGenre("Sci-Fi");
        testMovie.setDuration(148);
        testMovie.setRating(8.8);
        testMovie.setReleaseYear(2010);

        testMovieDTO = new MovieDTO();
        testMovieDTO.setTitle("Inception");
        testMovieDTO.setGenre("Sci-Fi");
        testMovieDTO.setDuration(148);
        testMovieDTO.setRating(8.8);
        testMovieDTO.setReleaseYear(2010);
    }

    // Test: getAllMovies()
    @Test
    void testGetAllMovies() {
        List<Movie> movies = Arrays.asList(testMovie);
        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> result = movieService.getAllMovies();

        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        verify(movieRepository, times(1)).findAll();
    }

    // Test: addMovie() - Success
    @Test
    void testAddMovie_Success() {
        when(movieRepository.findByTitle(testMovieDTO.getTitle())).thenReturn(Optional.empty());
        when(movieRepository.save(any(Movie.class))).thenReturn(testMovie);

        Movie result = movieService.addMovie(testMovieDTO);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        verify(movieRepository, times(1)).findByTitle(testMovieDTO.getTitle());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    // Test: addMovie() - Movie Already Exists
    @Test
    void testAddMovie_AlreadyExists() {
        when(movieRepository.findByTitle(testMovieDTO.getTitle())).thenReturn(Optional.of(testMovie));

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            movieService.addMovie(testMovieDTO);
        });

        verify(movieRepository, times(1)).findByTitle(testMovieDTO.getTitle());
        verify(movieRepository, never()).save(any(Movie.class));
    }

    // Test: updateMovieByTitle() - Success
    @Test
    void testUpdateMovieByTitle_Success() {
        when(movieRepository.findByTitle("Inception")).thenReturn(Optional.of(testMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(testMovie);

        MovieDTO updatedMovieDTO = new MovieDTO();
        updatedMovieDTO.setTitle("Inception");
        updatedMovieDTO.setGenre("Thriller");
        updatedMovieDTO.setDuration(150);
        updatedMovieDTO.setRating(9.0);
        updatedMovieDTO.setReleaseYear(2012);

        Movie result = movieService.updateMovieByTitle("Inception", updatedMovieDTO);

        assertNotNull(result);
        assertEquals("Thriller", result.getGenre());
        assertEquals(150, result.getDuration());
        assertEquals(9.0, result.getRating());
        assertEquals(2012, result.getReleaseYear());
        verify(movieRepository, times(1)).findByTitle("Inception");
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    // Test: updateMovieByTitle() - Movie Not Found
    @Test
    void testUpdateMovieByTitle_NotFound() {
        when(movieRepository.findByTitle("Nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.updateMovieByTitle("Nonexistent", testMovieDTO);
        });

        verify(movieRepository, times(1)).findByTitle("Nonexistent");
        verify(movieRepository, never()).save(any(Movie.class));
    }

    // Test: deleteMovieByTitle() - Success
    @Test
    void testDeleteMovieByTitle_Success() {
        when(movieRepository.findByTitle("Inception")).thenReturn(Optional.of(testMovie));
        doNothing().when(movieRepository).delete(any(Movie.class));

        assertDoesNotThrow(() -> movieService.deleteMovieByTitle("Inception"));

        verify(movieRepository, times(1)).findByTitle("Inception");
        verify(movieRepository, times(1)).delete(testMovie);
    }

    // Test: deleteMovieByTitle() - Movie Not Found
    @Test
    void testDeleteMovieByTitle_NotFound() {
        when(movieRepository.findByTitle("Nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.deleteMovieByTitle("Nonexistent");
        });

        verify(movieRepository, times(1)).findByTitle("Nonexistent");
        verify(movieRepository, never()).delete(any(Movie.class));
    }
}
