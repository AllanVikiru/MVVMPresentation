package com.example.mvvm.data.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mvvm.data.db.room.MoviesDao;
import com.example.mvvm.data.db.room.MoviesDatabase;
import com.example.mvvm.data.models.Movie;
import com.example.mvvm.data.network.retrofit.MoviesService;
import com.example.mvvm.data.network.retrofit.RetrofitClient;
import com.example.mvvm.data.network.retrofit.models.Results;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private MoviesDao moviesDao;
    private MoviesService moviesService;

    public MovieRepository(Context context) {
        this.moviesDao = MoviesDatabase.getInstance(context).getMoviesDao();
        this.moviesService = RetrofitClient.getInstance(context).getMoviesService();
    }

    private void fetchPopularMovies(){

        moviesService.getPopularMovies().enqueue(new Callback<Results<Movie>>()  {
            @Override
            public void onResponse(Call<Results<Movie>> call, Response<Results<Movie>> response) {
                if (response.isSuccessful()) {
                    if(response.body() != null) {
                        new saveMoviesToDB(moviesDao).execute(response.body().getResults());
                    }
                } else{
                    // Handle errors
                }
            }

            @Override
            public void onFailure(Call<Results<Movie>> call, Throwable t) {
                // Handle errors
            }
        });
    }

    public LiveData<List<Movie>> getPopularMovies() {
        // Check whether the database has any values cached and initiate a
        // fetch if it does not.
        if(
            moviesDao.getMovies().getValue() == null ||
            moviesDao.getMovies().getValue().isEmpty()
        ){
            fetchPopularMovies();
        }

        return moviesDao.getMovies();
    }

    /**
     * Create a background task to save movies to the database.
     * */
    private static class saveMoviesToDB extends AsyncTask<List<Movie>, Void, Void> {

        private MoviesDao moviesDao;

        saveMoviesToDB(MoviesDao dao) {
            moviesDao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Movie>... movies) {
            moviesDao.clear();
            moviesDao.saveMovies(movies[0]);
            return null;
        }

    }


}
