package com.example.mvvm.data.db.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mvvm.data.models.Movie;

import java.util.List;

import retrofit2.http.DELETE;

/**
 * Data Access Object.
 * This is a class that allows you to access the database.
 * You can perform queries and mutations of data through this class.
 * They directly affect the database.
 * */
@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movies")
    public LiveData<List<Movie>> getMovies();

    @Query("SELECT * FROM movies WHERE id=:id")
    public LiveData<Movie> getMovie(int id);

    @Query("SELECT * FROM movies WHERE title LIKE :title")
    public Movie searchForMovie(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveMovie(Movie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveMovies(List<Movie> movies);

    @Delete
    public void removeMovie(Movie movie);

    @Query("DELETE FROM movies")
    public void clear();
}
