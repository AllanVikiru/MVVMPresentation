package com.example.mvvm.ui.popularMovies;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.mvvm.R;
import com.example.mvvm.data.models.Movie;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.flexibleadapter.items.IHolder;
import eu.davidea.viewholders.FlexibleViewHolder;

public class MovieCategoryListItem extends AbstractFlexibleItem<MovieCategoryListItem.ViewHolder>
        implements IHolder<String>, MovieListItem.MovieListItemListener {

    private String category;
    private RequestManager glide;
    private Context context;

    MovieCategoryListItem(
        String category,
        RequestManager glide ,
        Context context
    ){
        this.category = category;
        this.glide = glide;
        this.context = context;
    }

    @Override
    public String getModel(){
        return category;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof MovieListItem) {
            MovieListItem inItem = (MovieListItem) inObject;
            return this.category.toLowerCase().equals(this.category.toLowerCase());
        }
        return false;
    }


    @Override
    public int hashCode() {
        return this.category.hashCode();
    }


    @LayoutRes
    public int getLayoutRes(){
        return R.layout.movie_category_list_item;
    }

    /**
     * Creation of the ViewHolder. The view is already inflated!
     */
    public MovieCategoryListItem.ViewHolder createViewHolder(
            View view,
            FlexibleAdapter<IFlexible> adapter
    ){
        return new MovieCategoryListItem.ViewHolder(view,adapter);
    }

    public void bindViewHolder(
            FlexibleAdapter<IFlexible> adapter,
            ViewHolder holder,
            int position,
            List<Object> payloads
    ){
        holder.category_name.setText(category);

    }

    private Observer<List<Movie>> moviesObserver =  new Observer<List<Movie>>() {
        @Override
        public void onChanged(List<Movie> movies) {


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
                            glide,
                            context
                    ).addListener(MovieCategoryListItem.this) // Add listener for click events
                );
            }




        }
    };

    class ViewHolder extends FlexibleViewHolder {

        LinearLayout main_body;
        RecyclerView recycler_view;
        TextView category_name;

        ViewHolder(
                View view,
                FlexibleAdapter<IFlexible> adapter
        ){
            super(view,adapter);
            main_body = view.findViewById(R.id.movie_body);
            recycler_view = view.findViewById(R.id.recycler_view);
            category_name = view.findViewById(R.id.category_name);
        }

    }

    @Override
    public void onMovieClicked(Movie movie) {

    }


}
