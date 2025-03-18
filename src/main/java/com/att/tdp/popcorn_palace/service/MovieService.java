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

    // Get Movie By ID
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + id + " not found"));
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

    // Update Movie
    public Movie updateMovie(Long id, MovieDTO movieDTO) {
        Movie existingMovie = getMovieById(id); // Throws exception if not found
        existingMovie.setTitle(movieDTO.getTitle());
        existingMovie.setGenre(movieDTO.getGenre());
        existingMovie.setDuration(movieDTO.getDuration());
        existingMovie.setRating(movieDTO.getRating());
        existingMovie.setReleaseYear(movieDTO.getReleaseYear());
        return movieRepository.save(existingMovie);
    }

    // Delete Movie
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie with ID " + id + " not found");
        }
        movieRepository.deleteById(id);
    }
}

