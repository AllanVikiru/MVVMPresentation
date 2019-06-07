package com.example.mvvm.data.db.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mvvm.data.models.Genre;
import com.example.mvvm.data.models.Movie;
import com.example.mvvm.data.models.MovieGenre;

import java.util.List;

@Dao
public interface GenreDao {
    @Query("SELECT movies.* FROM movies INNER JOIN movie_genres " +
            "ON movies.id = movie_genres.movieId " +
            "WHERE movie_genres.genreId=:genreId")
    public List<Movie> getMoviesOfGenre(int genreId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveGenre(Genre genre);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveGenres(List<Genre> genre);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveMovieGenre(MovieGenre movieGenre);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveMovieGenres(List<MovieGenre> movieGenres);
}
