package com.example.mvvm.data.db.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mvvm.data.models.Genre;
import com.example.mvvm.data.models.Movie;
import com.example.mvvm.data.models.Review;
import com.example.mvvm.data.models.Video;

import java.util.List;

@Dao
public interface ReviewsDao {
    @Query("SELECT * FROM reviews WHERE movieId=:movieId")
    public LiveData<List<Review>> getMovieReviews(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveReview(Review review);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveReviews(List<Review> review);
}
