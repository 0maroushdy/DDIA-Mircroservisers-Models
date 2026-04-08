package com.moviecatalogservice.resources;

import com.moviecatalogservice.models.CatalogItem;
import com.moviecatalogservice.models.Rating;
import com.moviecatalogservice.models.TrendingMovieItem;
import com.moviecatalogservice.models.UserRating;
import com.moviecatalogservice.services.MovieInfoService;
import com.moviecatalogservice.services.TrendingClient;
import com.moviecatalogservice.services.UserRatingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final RestTemplate restTemplate;

    private final MovieInfoService movieInfoService;

    private final UserRatingService userRatingService;

    private final TrendingClient trendingClient;

    public MovieCatalogResource(RestTemplate restTemplate,
                                MovieInfoService movieInfoService,
                                UserRatingService userRatingService,
                                TrendingClient trendingClient) {

        this.restTemplate = restTemplate;
        this.movieInfoService = movieInfoService;
        this.userRatingService = userRatingService;
        this.trendingClient = trendingClient;
    }

    /**
     * Makes a call to MovieInfoService to get movieId, name and description,
     * Makes a call to RatingsService to get ratings
     * Accumulates both data to create a MovieCatalog
     * @param userId
     * @return CatalogItem that contains name, description and rating
     */
    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {
        List<Rating> ratings = userRatingService.getUserRating(userId).getRatings();
        return ratings.stream().map(movieInfoService::getCatalogItem).collect(Collectors.toList());
    }

    @GetMapping("/trending")
    public List<TrendingMovieItem> getTrendingMovies() {
        return trendingClient.getTopMovies(10).getMoviesList().stream()
                .map(m -> new TrendingMovieItem(
                        m.getMovieId(),
                        m.getAverageRating(),
                        m.getRatingCount()))
                .collect(Collectors.toList());
    }
}
