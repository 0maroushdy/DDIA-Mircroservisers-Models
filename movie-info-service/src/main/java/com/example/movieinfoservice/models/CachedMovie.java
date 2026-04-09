package com.example.movieinfoservice.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movies")
public class CachedMovie {

    @Id
    private String movieId; // the lookup key
    private String name;
    private String description;
    private LocalDateTime cachedAt; // timestamp so you could expire old entries later

    // Default constructor (required by Spring Data)
    public CachedMovie() {}

    // Full constructor
    public CachedMovie(String movieId, String name, String description, LocalDateTime cachedAt) {
        this.movieId = movieId;
        this.name = name;
        this.description = description;
        this.cachedAt = cachedAt;
    }

    // Getters
    public String getMovieId() { return movieId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getCachedAt() { return cachedAt; }

    // Setters
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCachedAt(LocalDateTime cachedAt) { this.cachedAt = cachedAt; }
}