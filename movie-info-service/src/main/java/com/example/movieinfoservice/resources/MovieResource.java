package com.example.movieinfoservice.resources;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieinfoservice.models.CachedMovie;
import com.example.movieinfoservice.models.CachedMovieRepository;
import com.example.movieinfoservice.models.Movie;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Autowired
    private CachedMovieRepository cacheRepo;

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId) {

        // 1. Check MongoDB cache first
        Optional<CachedMovie> cached = cacheRepo.findById(movieId);
        if (cached.isPresent()) {
            CachedMovie c = cached.get();
            return new Movie(c.getMovieId(), c.getName(), c.getDescription());
        }

        // 2. Cache miss — use mock data with delay
        Movie movie = fetchMockMovie(movieId);

        // 3. Save to MongoDB
        cacheRepo.save(new CachedMovie(
            movieId,
            movie.getName(),
            movie.getDescription(),
            LocalDateTime.now()
        ));

        return movie;
    }

    private Movie fetchMockMovie(String movieId) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new Movie(movieId, "Movie " + movieId, "Description for movie " + movieId);
    }
}