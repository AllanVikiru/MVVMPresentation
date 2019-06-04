package com.example.mvvm.data.db.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mvvm.data.models.Genre;
import com.example.mvvm.data.models.Movie;
import com.example.mvvm.data.models.MovieGenres;
import com.example.mvvm.data.models.Review;
import com.example.mvvm.data.models.Video;

@Database(entities = {
        Movie.class,
        Review.class,
        Genre.class,
        MovieGenres.class,
        Video.class
    },
    version = 1 // Used in migrations. Tells ROOM which version these database is. Any changes
        // to the database is like creating a new version of the database.
)
public abstract class MoviesDatabase extends RoomDatabase {

    private static MoviesDatabase INSTANCE;

    public static synchronized MoviesDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room
                    .databaseBuilder(
                            context.getApplicationContext(),
                            MoviesDatabase.class,
                            "movies_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }

// DAOS
    public abstract MoviesDao getMoviesDao();
    public abstract ReviewsDao getReviewsDao();
    public abstract GenreDao getGenreDao();

}