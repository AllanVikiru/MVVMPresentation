package com.example.mvvm.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import javax.annotation.Nullable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "videos",
        foreignKeys = {
            @ForeignKey(entity = Movie.class,
                        parentColumns = "id",
                        childColumns = "movieId",
                        onDelete = CASCADE)
        })
public class Video {

    @PrimaryKey
    @NonNull
    private String id = "-1";

    @ColumnInfo(index = true)
    private int movieId;

    private String name;

    @Nullable
    private String key;

    @Nullable
    private String site;

    @Nullable
    private String type;

    public Video() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getKey() {
        return key;
    }

    public void setKey(@Nullable String key) {
        this.key = key;
    }

    @Nullable
    public String getSite() {
        return site;
    }

    public void setSite(@Nullable String site) {
        this.site = site;
    }

    @Nullable
    public String getType() {
        return type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }
}
