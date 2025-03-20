package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.exception.InvalidInputException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // GET All Movies
    @GetMapping("/all")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies()
                .stream()
                .map(MovieDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movies);
    }

    // POST: Add a new Movie
    @PostMapping
    public ResponseEntity<MovieDTO> addMovie(@Valid @RequestBody MovieDTO movieDTO) {
        Movie newMovie = movieService.addMovie(movieDTO);
        return ResponseEntity.ok(new MovieDTO(newMovie));
    }

    // POST: Update a Movie by Title
    @RequestMapping(value = {"/update/{movieTitle}", "/update"}, method = RequestMethod.POST)
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable(required = false) String movieTitle, @Valid @RequestBody MovieDTO movieDTO) {
        validateMovieTitle(movieTitle);
        Movie updatedMovie = movieService.updateMovieByTitle(movieTitle, movieDTO);
        return ResponseEntity.ok(new MovieDTO(updatedMovie));
    }

    // DELETE: Remove a Movie by Title (200 OK)
    @RequestMapping(value = {"/{movieTitle}", ""}, method = RequestMethod.DELETE)
    public ResponseEntity<MovieDTO> deleteMovie(@PathVariable(required = false) String movieTitle) {
        validateMovieTitle(movieTitle);
        movieService.deleteMovieByTitle(movieTitle);
        return ResponseEntity.ok().build();
    }

    // Validate Movie Title
    private void validateMovieTitle(String movieTitle) {
        if (movieTitle == null || movieTitle.trim().isEmpty()) {
            throw new InvalidInputException("Invalid Movie Title. Title must not be blank or empty.");
        }
    }
}

