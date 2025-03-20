package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.exception.GlobalExceptionHandler;
import com.att.tdp.popcorn_palace.exception.InvalidInputException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Movie testMovie;
    private MovieDTO testMovieDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(movieController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

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

    // Test: Get All Movies (Success)
    @Test
    void testGetAllMovies_Success() throws Exception {
        List<Movie> movies = Arrays.asList(testMovie);
        when(movieService.getAllMovies()).thenReturn(movies);

        mockMvc.perform(get("/movies/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andExpect(jsonPath("$[0].genre").value("Sci-Fi"));

        verify(movieService, times(1)).getAllMovies();
    }

    // Test: Add a Movie (Success)
    @Test
    void testAddMovie_Success() throws Exception {
        when(movieService.addMovie(any(MovieDTO.class))).thenReturn(testMovie);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMovieDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.genre").value("Sci-Fi"));

        verify(movieService, times(1)).addMovie(any(MovieDTO.class));
    }

    // Test: Add a Movie (Invalid Input)
    @Test
    void testAddMovie_InvalidInput() throws Exception {
        MovieDTO invalidMovieDTO = new MovieDTO(); // Missing required fields

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMovieDTO)))
                .andExpect(status().isBadRequest());
    }

    // Test: Update a Movie (Success)
    @Test
    void testUpdateMovie_Success() throws Exception {
        when(movieService.updateMovieByTitle(eq("Inception"), any(MovieDTO.class))).thenReturn(testMovie);

        mockMvc.perform(post("/movies/update/Inception")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMovieDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.genre").value("Sci-Fi"));

        verify(movieService, times(1)).updateMovieByTitle(eq("Inception"), any(MovieDTO.class));
    }

    //Test: Update a Movie (Invalid Title)
    @Test
    void testUpdateMovie_InvalidTitle() throws Exception {
        mockMvc.perform(post("/movies/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMovieDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Movie Title. Title must not be blank or empty."));
    }

    // Test: Delete a Movie (Success)
    @Test
    void testDeleteMovie_Success() throws Exception {
        doNothing().when(movieService).deleteMovieByTitle("Inception");

        mockMvc.perform(delete("/movies/Inception"))
                .andExpect(status().isOk());

        verify(movieService, times(1)).deleteMovieByTitle("Inception");
    }

    // Test: Delete a Movie (Invalid Title)
    @Test
    void testDeleteMovie_InvalidTitle() throws Exception {
        mockMvc.perform(delete("/movies"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Movie Title. Title must not be blank or empty."));
    }
}
