package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDTO;
import com.att.tdp.popcorn_palace.exception.GlobalExceptionHandler;
import com.att.tdp.popcorn_palace.exception.ShowtimeOverlapException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.service.MovieService;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ShowtimeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShowtimeService showtimeService;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private ShowtimeController showtimeController;

    private ObjectMapper objectMapper;
    private Movie testMovie;
    private Showtime testShowtime;
    private ShowtimeDTO testShowtimeDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(showtimeController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Ensures Exception Handling works
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // To handle Java 8 DateTime Serialization

        testMovie = new Movie(1L, "Inception", "Sci-Fi", 148, 8.8, 2010);

        testShowtime = new Showtime();
        testShowtime.setId(1L);
        testShowtime.setMovie(testMovie);
        testShowtime.setTheater("IMAX");
        testShowtime.setStartTime(LocalDateTime.of(2025, 6, 10, 14, 0));
        testShowtime.setEndTime(LocalDateTime.of(2025, 6, 10, 16, 0));
        testShowtime.setPrice(20.0);

        testShowtimeDTO = new ShowtimeDTO(1L, 1L, "IMAX",
                LocalDateTime.of(2025, 6, 10, 14, 0),
                LocalDateTime.of(2025, 6, 10, 16, 0),
                20.0);
    }

    //Test: Add Showtime (Success)
    @Test
    void testAddShowtime_Success() throws Exception {
        when(showtimeService.addShowtime(any(ShowtimeDTO.class))).thenReturn(testShowtime);

        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testShowtimeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.theater").value("IMAX"))
                .andExpect(jsonPath("$.price").value(20.0));

        verify(showtimeService, times(1)).addShowtime(any(ShowtimeDTO.class));
    }

    //Test: Add Showtime (Overlapping)
    @Test
    void testAddShowtime_Overlap() throws Exception {
        doThrow(new ShowtimeOverlapException("Showtime conflicts with another showtime in the same theater."))
                .when(showtimeService).addShowtime(any(ShowtimeDTO.class));

        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testShowtimeDTO)))
                .andExpect(status().isConflict()) // Expecting HTTP 409 Conflict
                .andExpect(jsonPath("$.error").value("Showtime conflicts with another showtime in the same theater."));
    }

    //Test: Get Showtime by ID (Success)
    @Test
    void testGetShowtimeById_Success() throws Exception {
        when(showtimeService.getShowtimeById(1L)).thenReturn(testShowtime);

        mockMvc.perform(get("/showtimes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.theater").value("IMAX"))
                .andExpect(jsonPath("$.price").value(20.0));

        verify(showtimeService, times(1)).getShowtimeById(1L);
    }

    //Test: Get Showtime by ID (Invalid ID)
    @Test
    void testGetShowtimeById_InvalidId() throws Exception {
        mockMvc.perform(get("/showtimes/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Showtime ID. ID must be a positive number."));
    }

    //Test: Update Showtime (Success)
    @Test
    void testUpdateShowtime_Success() throws Exception {
        when(showtimeService.updateShowtime(eq(1L), any(ShowtimeDTO.class))).thenReturn(testShowtime);

        mockMvc.perform(post("/showtimes/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testShowtimeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.theater").value("IMAX"))
                .andExpect(jsonPath("$.price").value(20.0));

        verify(showtimeService, times(1)).updateShowtime(eq(1L), any(ShowtimeDTO.class));
    }

    //Test: Update Showtime (Invalid ID)
    @Test
    void testUpdateShowtime_InvalidId() throws Exception {
        mockMvc.perform(post("/showtimes/update/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testShowtimeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Showtime ID. ID must be a positive number."));
    }

    //Test: Delete Showtime (Success)
    @Test
    void testDeleteShowtime_Success() throws Exception {
        doNothing().when(showtimeService).deleteShowtime(1L);

        mockMvc.perform(delete("/showtimes/1"))
                .andExpect(status().isOk());

        verify(showtimeService, times(1)).deleteShowtime(1L);
    }

    //Test: Delete Showtime (Invalid ID)
    @Test
    void testDeleteShowtime_InvalidId() throws Exception {
        mockMvc.perform(delete("/showtimes/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Showtime ID. ID must be a positive number."));
    }
}
