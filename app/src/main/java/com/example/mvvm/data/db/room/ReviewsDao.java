package com.example.mvvm.data.db.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.mvvm.data.models.Movie;
import com.example.mvvm.data.models.Review;

import java.util.List;

@Dao
public interface ReviewsDao {
    @Query("SELECT * FROM reviews WHERE movieId=:movieId")
    public LiveData<List<Review>> getMovieReviews(int movieId);
}
