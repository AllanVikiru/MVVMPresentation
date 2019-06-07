package com.example.mvvm.data.models;

import androidx.room.Embedded;
import androidx.room.Query;
import androidx.room.Relation;

import java.util.List;

public class MovieWithDetails {
    @Embedded
    public Movie movie;

    @Relation(parentColumn = "id",entityColumn = "movieId")
    public List<Review> reviews;

    @Relation(parentColumn = "id",entityColumn = "movieId")
    public List<Video> videos;
}
