package com.example.mvvm.data.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mvvm.data.db.room.GenreDao;
import com.example.mvvm.data.db.room.MoviesDao;
import com.example.mvvm.data.db.room.MoviesDatabase;
import com.example.mvvm.data.db.room.ReviewsDao;
import com.example.mvvm.data.db.room.VideosDao;
import com.example.mvvm.data.models.Movie;
import com.example.mvvm.data.models.MovieGenre;
import com.example.mvvm.data.models.MovieWithDetails;
import com.example.mvvm.data.models.Review;
import com.example.mvvm.data.network.retrofit.MoviesService;
import com.example.mvvm.data.network.retrofit.RetrofitClient;
import com.example.mvvm.data.network.retrofit.models.Results;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private MoviesDao moviesDao;
    private GenreDao genreDao;
    private VideosDao videosDao;
    private ReviewsDao reviewsDao;
    private MoviesService moviesService;

    public MovieRepository(Context context) {
        this.moviesDao = MoviesDatabase.getInstance(context).getMoviesDao();
        this.genreDao = MoviesDatabase.getInstance(context).getGenreDao();
        this.videosDao = MoviesDatabase.getInstance(context).getVideosDao();
        this.reviewsDao = MoviesDatabase.getInstance(context).getReviewsDao();
        this.moviesService = RetrofitClient.getInstance(context).getMoviesService();
    }

    /**
     * Get popular movies. Fetch the movies from the database. If
     * the database does not have data make a request to update the
     * database. Any observers of the databases's LiveData will be
     * notified of the update.
     * */
    public LiveData<List<Movie>> getPopularMovies() {
        // Check whether the database has any movies cached and initiate a
        // fetch if it does not.
        if(
            moviesDao.getMovies().getValue() == null ||
            moviesDao.getMovies().getValue().isEmpty()
        ){
            fetchPopularMovies();
        }

        return moviesDao.getMovies();
    }

    /**
     * Get specific details of a movie by id.
     * */
    public LiveData<MovieWithDetails> getMovieDetails(int movie_id){
        // Check whether the details of the movie are cached in the database. If not fetch the
        // details of the movie.
        if(
            moviesDao.getMovieWithDetails(movie_id).getValue() == null ||
            moviesDao.getMovieWithDetails(movie_id).getValue().reviews == null ||
            moviesDao.getMovieWithDetails(movie_id).getValue().videos == null
        ){
            fetchMovieDetails(movie_id);
        }

        return moviesDao.getMovieWithDetails(movie_id);
    }

    /**
     * Fetch movie details from remote service (TMDB). This includes its reviews, genres and
     * videos associated with it.
     * */
    private void fetchMovieDetails(int movie_id){
        moviesService.getMovieDetails(movie_id).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    if(response.body() != null) {
                        new saveMovieDetailsToDB(moviesDao,genreDao,videosDao,reviewsDao).execute(response.body());
                    }
                } else{
                    // Handle errors
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                t.getMessage();
                // Handle errors
            }
        });
    }

    /**
     * Fetch popular movies from remote service (TMDB)
     * */
    private void fetchPopularMovies(){

        moviesService.getPopularMovies().enqueue(new Callback<Results<Movie>>()  {
            @Override
            public void onResponse(Call<Results<Movie>> call, Response<Results<Movie>> response) {
                if (response.isSuccessful()) {
                    if(response.body() != null) {
                        new saveMoviesToDB(moviesDao).execute(response.body().getResults());
                    }
                } else{
                    // Handle errors
                }
            }

            @Override
            public void onFailure(Call<Results<Movie>> call, Throwable t) {
                t.getMessage();
                // Handle errors
            }
        });
    }

    /**
     * Create a background task to save movies to the database.
     * */
    private static class saveMoviesToDB extends AsyncTask<List<Movie>, Void, Void> {

        private MoviesDao moviesDao;

        saveMoviesToDB(MoviesDao dao) {
            moviesDao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Movie>... movies) {
            moviesDao.clear();
            moviesDao.saveMovies(movies[0]);
            return null;
        }

    }

    /**
     * Create a background task to save movies to the database.
     * */
    private static class saveMovieDetailsToDB extends AsyncTask<Movie, Void, Void> {

        private MoviesDao moviesDao;
        private GenreDao genreDao;
        private VideosDao videosDao;
        private ReviewsDao reviewsDao;

        saveMovieDetailsToDB(MoviesDao moviesDao, GenreDao genreDao, VideosDao videosDao, ReviewsDao reviewsDao) {
            this.moviesDao = moviesDao;
            this.genreDao = genreDao;
            this.videosDao = videosDao;
            this.reviewsDao = reviewsDao;
        }

        @Override
        protected final Void doInBackground(Movie... movie) {

            moviesDao.saveMovie(movie[0]);

            movie[0].getReviews().getResults().forEach(review -> review.setMovieId(movie[0].getId()));

            movie[0].getVideos().getResults().forEach(video -> video.setMovieId(movie[0].getId()));

            List<MovieGenre> movieGenres = movie[0].getGenres().stream().map(genre ->
                    new MovieGenre(movie[0].getId(),genre.getId())
            ).collect(Collectors.toList());

            reviewsDao.saveReviews(movie[0].getReviews().getResults());
            videosDao.saveVideos(movie[0].getVideos().getResults());
            genreDao.saveGenres(movie[0].getGenres());
            genreDao.saveMovieGenres(movieGenres);

            return null;
        }

    }

}
