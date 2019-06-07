package com.example.mvvm.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import javax.annotation.Nullable;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;

@Entity(tableName = "reviews",
        foreignKeys = {
            @ForeignKey(entity = Movie.class,
                        parentColumns = "id",
                        childColumns = "movieId",
                        onDelete = CASCADE,
                        onUpdate = NO_ACTION) // NO_ACTION - don't disturb the child
                                              // RESTRICT - don't touch the parent
                                              // SET_DEFAULT - add default on child's foreign key
        }
//        indices = {
//            @Index(value = "movieId", name = "movieId")
//        }
    )
public class Review {

    @PrimaryKey
    @NonNull
    private String id = "-1";

    @ColumnInfo(index = true)
    private int movieId;

    private String author;

    @Nullable
    private String content;

    public Review(){ }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }
}
