package com.example.mvvm.data.network.retrofit;

import androidx.lifecycle.LiveData;

import com.example.mvvm.data.models.Movie;
import com.example.mvvm.data.network.retrofit.models.Results;
import com.github.leonardoxh.livedatacalladapter.Resource;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesService {

    @GET("movie/{movie_id}")
    public Call<Movie> getMovieDetails(@Path("movie_id") int movie_id);

    @GET("movie/popular")
    public Call<Results<Movie>> getPopularMovies();

    @GET("search/movie")
    public Call<Results<Movie>> searchMovies(@Query("query") String query);
}
