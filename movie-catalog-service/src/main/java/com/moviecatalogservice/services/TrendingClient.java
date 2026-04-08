package com.moviecatalogservice.services;

import com.example.trending.grpc.TopMoviesRequest;
import com.example.trending.grpc.TopMoviesResponse;
import com.example.trending.grpc.TrendingMoviesServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class TrendingClient {

    @GrpcClient("trending-movies-service")
    private TrendingMoviesServiceGrpc.TrendingMoviesServiceBlockingStub stub;

    public TopMoviesResponse getTopMovies(int count) {
        TopMoviesRequest req = TopMoviesRequest.newBuilder()
                .setCount(count)
                .build();
        return stub.getTopMovies(req);
    }
}
