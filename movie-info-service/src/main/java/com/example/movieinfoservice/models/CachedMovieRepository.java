package com.example.movieinfoservice.models;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CachedMovieRepository extends MongoRepository<CachedMovie, String> {
    // findById(), save() are inherited
}