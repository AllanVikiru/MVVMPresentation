package com.example.mvvm.data.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "movie_genres",
        primaryKeys = { "movieId", "genreId" },
        foreignKeys = {
                @ForeignKey(entity = Movie.class,
                        parentColumns = "id",
                        childColumns = "movieId"),
                @ForeignKey(entity = Genre.class,
                        parentColumns = "id",
                        childColumns = "genreId")
        })
public class MovieGenres {
    private int movieId;
    private int genreId;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
}
