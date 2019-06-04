package com.example.mvvm.ui.popularMovies;

import android.app.Application;
import android.print.PrinterId;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvm.data.models.Movie;
import com.example.mvvm.data.repositories.MovieRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * We extend the AndroidViewModel to get the Application context
 * easily.
 * */
public class PopularMoviesViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    private LiveData<List<Movie>> movies;

    PopularMoviesViewModel(Application application) {
        super(application);
        this.movieRepository = new MovieRepository(application);
        this.movies = movieRepository.getPopularMovies();
    }

    LiveData<List<Movie>> getMoviePopularMovies() {
        return movies;
    }

    public UiState uiState = new UiState();

    public class UiState extends BaseObservable {

        private boolean showError = false;
        private boolean showPageLoading = false;
        private boolean showNoMovies = false;

        @Bindable
        public boolean isShowPageLoading() {
            return showPageLoading;
        }

        public void setShowPageLoading(boolean showPageLoading) {
            this.showPageLoading = showPageLoading;
            notifyChange();
        }

        @Bindable
        public boolean isShowNoMovies() {
            return showNoMovies;
        }

        public void setShowNoMovies(boolean showNoMovies) {
            this.showNoMovies = showNoMovies;
            notifyChange();
        }

    }
}
