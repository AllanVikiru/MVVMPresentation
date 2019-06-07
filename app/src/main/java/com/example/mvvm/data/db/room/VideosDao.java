package com.example.mvvm.data.db.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mvvm.data.models.Review;
import com.example.mvvm.data.models.Video;

import java.util.List;

@Dao
public interface VideosDao {

    @Query("SELECT * FROM videos WHERE movieId=:movieId")
    public LiveData<List<Video>> getMovieVideos(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveVideo(Video video);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveVideos(List<Video> video);
}
