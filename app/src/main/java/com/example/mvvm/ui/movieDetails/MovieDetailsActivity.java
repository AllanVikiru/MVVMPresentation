package com.example.mvvm.ui.movieDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.mvvm.R;
import com.example.mvvm.data.Constants;
import com.example.mvvm.data.models.Movie;
import com.example.mvvm.data.models.MovieWithDetails;
import com.example.mvvm.databinding.MovieDetailsBindings;

import static com.example.mvvm.ui.popularMovies.PopularMoviesActivity.MOVIE_ID_BUNDLE_EXTRA;

public class MovieDetailsActivity extends AppCompatActivity {

    private MovieDetailsViewModel movieDetailsViewModel;

    private MovieDetailsBindings movieDetailsBindings;

    private RequestManager glideRequestManager;

//########################### ANDROID CALLBACKS ###########################//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieDetailsBindings = DataBindingUtil.setContentView(
                this,
                R.layout.activity_movie_details
        );

        setupToolbar();

        setupViewModel();

        setupReviewsRecyclerView();

        glideRequestManager = Glide.with(this);

        getMovieFromArgs();

    }

//########################### SETUP ###########################//


    private void setupToolbar() {
        setSupportActionBar(movieDetailsBindings.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupViewModel() {
        movieDetailsViewModel = ViewModelProviders
                .of(this)
                .get(MovieDetailsViewModel.class);
    }

    private void setupReviewsRecyclerView() {

    }

//########################### LOGIC ###########################//

    /**
     * Popular movies live data observer. We create it as a variable for easy removal and
     * addition of observers to the live data.
     * */
    private Observer<MovieWithDetails> movieDetailsObserver =  new Observer<MovieWithDetails>() {
        @Override
        public void onChanged(MovieWithDetails movieWithDetails) {

            movieDetailsViewModel.uiState.setShowPageLoading(true);

            // Backdrop
            setupMovieBackdrop(movieWithDetails);

            getSupportActionBar().setTitle(movieWithDetails.movie.getTitle());

            movieDetailsViewModel.uiState.setShowPageLoading(false);

        }
    };

    private void setupMovieBackdrop(MovieWithDetails movieWithDetails){
        glideRequestManager
                .load(Constants.TMDB_API_V3_POSTER_BASE_URL + movieWithDetails.movie.getBackdropPath())
                .apply(new RequestOptions()
                        .placeholder(getApplicationContext().getDrawable(R.drawable.image_placeholder))
                )
                .into(movieDetailsBindings.backdropImage);
    }

    private void getMovieFromArgs(){
        int movie_id = getIntent().getIntExtra(MOVIE_ID_BUNDLE_EXTRA,-1);
        if (movie_id > -1){
            movieDetailsViewModel.getMovieDetails(movie_id).observe(this,movieDetailsObserver);
        }
    }
}
