package com.example.mvvm.ui.popularMovies;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.mvvm.R;
import com.example.mvvm.data.models.Movie;
import com.example.mvvm.databinding.MovieDetailsBindings;
import com.example.mvvm.databinding.PopularMoviesBindings;
import com.example.mvvm.ui.animators.SlideInUpItemAnimator;
import com.example.mvvm.ui.movieDetails.MovieDetailsActivity;
import com.ferfalk.simplesearchview.SimpleSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemAnimator;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.common.SmoothScrollStaggeredLayoutManager;
import eu.davidea.flexibleadapter.helpers.AnimatorHelper;
import eu.davidea.flexibleadapter.helpers.EmptyViewHelper;
import eu.davidea.flexibleadapter.utils.Log;

public class PopularMoviesActivity extends AppCompatActivity implements MovieListItem.MovieListItemListener {

    private PopularMoviesViewModel popularMoviesViewModel;

    private PopularMoviesBindings popularMoviesBindings;

    private FlexibleAdapter<MovieListItem> mainListAdapter;

    private RequestManager glideRequestManager;

    public final static String MOVIE_ID_BUNDLE_EXTRA = "movie_id";

//########################### ANDROID CALLBACKS ###########################//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        popularMoviesBindings = DataBindingUtil.setContentView(
                this,
                R.layout.activity_popular_movies
        );

        setupViewModel();

        setupToolbar();

        setupSearchView();

        setupRecyclerView();

        setupSwipeToRefresh();

        glideRequestManager = Glide.with(this);

        fetchPopularMovies();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.popular_movies_options_menu, menu);

        /*
        * Notify the SearchView of the search option in the options menu.
        * When this option is clicked the SearchView will open.
        * */
        if(menu.findItem(R.id.action_search) != null){
            popularMoviesBindings.searchView.setMenuItem(menu.findItem(R.id.action_search));
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        /*
         * Any activity results belonging to the SearchView should be handled by itself
         * */
        if (popularMoviesBindings.searchView.onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        /*
         * Dismiss the search view on back pressed instead of moving to a new
         * location or activity, if the search view is open.
         * */
        if (popularMoviesBindings.searchView.onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }


//########################### SETUP ###########################//

    /**
     * Popular movies live data observer. We create it as a variable for easy removal and
     * addition of observers to the live data.
     * */
    private Observer<List<Movie>> popularMoviesObserver =  new Observer<List<Movie>>() {
        @Override
        public void onChanged(List<Movie> movies) {

            /*
            * We use data binding to change the state of the ui without
            * having to change it using the views individually.
            * */
            popularMoviesViewModel.uiState.setShowPageLoading(true);

            /*
            *  We transform the movies into MovieListItems for use in our Recycler View.
            *  We could have done it on the View Model but we want Glide to know the activities
            *  lifecycle, remember we can't keep an instance of the activity in the View Model.
            *  */
            List<MovieListItem> movieListItems = new ArrayList<>();
            for (Movie movie : movies){
                movieListItems.add(
                    new MovieListItem(
                        movie,
                        glideRequestManager,
                        getApplicationContext()
                    ).addListener(PopularMoviesActivity.this) // Add listener for click events
                );
            }

            setPopularMovies(movieListItems);

            popularMoviesViewModel.uiState.setShowPageLoading(false);

        }
    };

    private void setupViewModel() {
        popularMoviesViewModel = ViewModelProviders
                .of(this)
                .get(PopularMoviesViewModel.class);
    }


    private void setupToolbar() {
        setSupportActionBar(popularMoviesBindings.toolbar);
        getSupportActionBar().setTitle("Popular Movies");
    }

    private void setupSearchView(){
        popularMoviesBindings.searchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearchQuery(newText);
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                onSearchQuery("");
                return false;
            }
        });
    }

    private void setupSwipeToRefresh() {
        popularMoviesBindings.swipeToRefreshLayout
            .setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Remove any searching
                        popularMoviesBindings.searchView.closeSearch();

                        fetchPopularMovies();

                        popularMoviesBindings.swipeToRefreshLayout.setRefreshing(false);
                    }
                }
            );
    }

    private void setupRecyclerView(){
        FlexibleAdapter.enableLogs(Log.Level.DEBUG);
        FlexibleAdapter.useTag("PopularMoviesAdapter");

        //ADAPTER
        mainListAdapter = new FlexibleAdapter<>(null);
        mainListAdapter
                .addListener(this)
                .setAnimationEntryStep(true)
                .setAnimationOnForwardScrolling(true)
                .setAnimationOnReverseScrolling(true)
                .setAnimationInterpolator(new DecelerateInterpolator())
                .setAnimationDuration(300L);

        // Animations
        popularMoviesBindings.recyclerView.setItemAnimator(new SlideInUpItemAnimator(new OvershootInterpolator(1f)));
        popularMoviesBindings.recyclerView.getItemAnimator().setAddDuration(500);
        popularMoviesBindings.recyclerView.getItemAnimator().setRemoveDuration(500);


        // Used to show a view if there are no movies or no results when searching for a movie
        EmptyViewHelper.create(
            mainListAdapter,
            popularMoviesBindings.empty,
            popularMoviesBindings.noResults
        );

        //LAYOUT MANAGER
        SmoothScrollStaggeredLayoutManager layoutManager =
                new SmoothScrollStaggeredLayoutManager(this,2);
        popularMoviesBindings.recyclerView.setLayoutManager(layoutManager);

        // Finally
        popularMoviesBindings.recyclerView.setHasFixedSize(true);
        popularMoviesBindings.recyclerView.setAdapter(mainListAdapter);

    }

//########################### LOGIC ###########################//

    /**
     * Tell view model to fetch popular movies and start observing the
     * movie's data source for any changes.
     * */
    private void fetchPopularMovies() {
        // Remove any previous observers
        popularMoviesViewModel.getMoviePopularMovies().removeObserver(popularMoviesObserver);

        // Bind observer
        popularMoviesViewModel.getMoviePopularMovies().observe(this,popularMoviesObserver);
    }


    /**
     * Replace PopularMovies in the adapter with the new ones provided.
     * */
    private void setPopularMovies(List<MovieListItem> popularMovies){
        mainListAdapter.clear();
        mainListAdapter.addItems(0, popularMovies);
        mainListAdapter.notifyDataSetChanged();
    }

    /**
     * Filter items in adapter according to query string provided.
     * */
    private void onSearchQuery(String query){

        if(query.equals("")){

            mainListAdapter.setFilter(null);

            mainListAdapter.filterItems(400);

        } else if(mainListAdapter.hasNewFilter(query)) {

            mainListAdapter.setFilter(query);

            mainListAdapter.filterItems(400);

        }

    }

    /**
     * Switch to MovieDetailsActivity to view a movie in
     * details.
     * */
    @Override
    public void onMovieClicked(Movie movie) {
        Bundle args = new Bundle();
        args.putInt(MOVIE_ID_BUNDLE_EXTRA,movie.getId());
        Intent i = new Intent(
                PopularMoviesActivity.this,
                MovieDetailsActivity.class
        );
        i.putExtras(args);
        startActivity(i);
    }
}
