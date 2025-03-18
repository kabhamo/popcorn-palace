package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieDTO;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // Get All Movies
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Add a New Movie
    public Movie addMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setGenre(movieDTO.getGenre());
        movie.setDuration(movieDTO.getDuration());
        movie.setRating(movieDTO.getRating());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        return movieRepository.save(movie);
    }

    // Update Movie by Title
    public Movie updateMovieByTitle(String title, MovieDTO movieDTO) {
        Movie existingMovie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with title '" + title + "' not found"));

        existingMovie.setGenre(movieDTO.getGenre());
        existingMovie.setDuration(movieDTO.getDuration());
        existingMovie.setRating(movieDTO.getRating());
        existingMovie.setReleaseYear(movieDTO.getReleaseYear());

        return movieRepository.save(existingMovie);
    }

    // Delete Movie by Title
    public void deleteMovieByTitle(String title) {
        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with title '" + title + "' not found"));
        movieRepository.delete(movie);
    }
}

