package com.example.trending.service;

import com.example.trending.grpc.TopMoviesRequest;
import com.example.trending.grpc.TopMoviesResponse;
import com.example.trending.grpc.TrendingMovie;
import com.example.trending.grpc.TrendingMoviesServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@GrpcService
public class TrendingServiceImpl extends TrendingMoviesServiceGrpc.TrendingMoviesServiceImplBase {

    private final JdbcTemplate jdbcTemplate;

    public TrendingServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void getTopMovies(TopMoviesRequest request,
                             StreamObserver<TopMoviesResponse> observer) {

        int count = request.getCount() > 0 ? request.getCount() : 10;

        String sql = "SELECT movie_id, AVG(rating) as avg_r,"
                + " COUNT(*) as cnt FROM ratings"
                + " GROUP BY movie_id"
                + " ORDER BY avg_r DESC LIMIT ?";

        List<TrendingMovie> movies = jdbcTemplate.query(sql,
                (rs, i) -> TrendingMovie.newBuilder()
                        .setMovieId(rs.getString("movie_id"))
                        .setAverageRating(rs.getDouble("avg_r"))
                        .setRatingCount(rs.getInt("cnt"))
                        .build(),
                count);

        observer.onNext(TopMoviesResponse.newBuilder()
                .addAllMovies(movies)
                .build());
        observer.onCompleted();
    }
}
