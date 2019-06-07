package com.example.mvvm.ui.movieDetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mvvm.data.models.Movie;
import com.example.mvvm.data.models.MovieWithDetails;
import com.example.mvvm.data.repositories.MovieRepository;

import java.util.List;

public class MovieDetailsViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    private LiveData<MovieWithDetails> movie;

    public UiState uiState;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        this.uiState = new MovieDetailsViewModel.UiState();
        this.movieRepository = new MovieRepository(application);
    }

    public LiveData<MovieWithDetails> getMovieDetails(int movie_id){
        if(movie == null){
            this.movie = movieRepository.getMovieDetails(movie_id);
        }
        return movie;
    }


    public class UiState extends BaseObservable {

        private boolean showError = false;
        private boolean showPageLoading = false;

        @Bindable
        public boolean isShowPageLoading() {
            return showPageLoading;
        }

        public void setShowPageLoading(boolean showPageLoading) {
            this.showPageLoading = showPageLoading;
            notifyChange();
        }

    }
}
