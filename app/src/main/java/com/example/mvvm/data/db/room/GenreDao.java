package com.example.mvvm.data.db.room;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.mvvm.data.models.Movie;

import java.util.List;

@Dao
public interface GenreDao {
    @Query("SELECT * FROM movies INNER JOIN movie_genres " +
            "ON movies.id = movie_genres.movieId " +
            "WHERE movie_genres.genreId=:genreId")
    List<Movie> getMoviesOfGenre(int genreId);
}
