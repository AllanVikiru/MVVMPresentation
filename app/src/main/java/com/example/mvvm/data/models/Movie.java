package com.example.mvvm.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.mvvm.data.network.retrofit.models.Results;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey(/*autoGenerate = true*/)
    private int id = -1;

    private String title;

    @SerializedName("adult")
    private boolean is_for_adult_age;

    @SerializedName("video")
    private boolean has_video;

    private String overview;

    private double popularity;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("vote_count")
    private int voteCount;

    @Nullable
    @Ignore
    private List<Genre> genres;

    @Nullable
    @Ignore
    private Results<Review> reviews;

    @Nullable
    @Ignore
    private Results<Video> videos;

    public Movie(){ }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isIs_for_adult_age() {
        return is_for_adult_age;
    }

    public void setIs_for_adult_age(boolean is_for_adult_age) {
        this.is_for_adult_age = is_for_adult_age;
    }

    public boolean isHas_video() {
        return has_video;
    }

    public void setHas_video(boolean has_video) {
        this.has_video = has_video;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Nullable
    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(@Nullable List<Genre> genres) {
        this.genres = genres;
    }

    @Nullable
    public Results<Review> getReviews() {
        return reviews;
    }

    public void setReviews(@Nullable Results<Review> reviews) {
        this.reviews = reviews;
    }

    @Nullable
    public Results<Video> getVideos() {
        return videos;
    }

    public void setVideos(@Nullable Results<Video> videos) {
        this.videos = videos;
    }
}
