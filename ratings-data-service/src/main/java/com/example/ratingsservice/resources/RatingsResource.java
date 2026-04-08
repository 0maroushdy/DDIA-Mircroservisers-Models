package com.example.ratingsservice.resources;

import com.example.ratingsservice.entity.RatingEntity;
import com.example.ratingsservice.models.Rating;
import com.example.ratingsservice.models.UserRating;
import com.example.ratingsservice.repository.RatingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ratings")
public class RatingsResource {

    private final RatingRepository ratingRepository;

    public RatingsResource(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @GetMapping("/{userId}")
    public UserRating getRatingsOfUser(@PathVariable String userId) {
        List<RatingEntity> entities = ratingRepository.findByUserId(userId);
        List<Rating> ratings = entities.stream()
                .map(e -> new Rating(e.getMovieId(), e.getRating()))
                .collect(Collectors.toList());
        return new UserRating(ratings);
    }

    @PostMapping
    public RatingEntity addRating(@RequestBody RatingEntity rating) {
        return ratingRepository.save(rating);
    }
}
