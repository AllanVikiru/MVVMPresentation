package com.example.mvvm.ui.popularMovies;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.mvvm.R;
import com.example.mvvm.data.Constants;
import com.example.mvvm.data.models.Movie;

import java.text.DecimalFormat;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.flexibleadapter.items.IHolder;
import eu.davidea.flexibleadapter.utils.FlexibleUtils;
import eu.davidea.viewholders.FlexibleViewHolder;

public class MovieListItem extends AbstractFlexibleItem<MovieListItem.ViewHolder>
implements IFilterable<String>, IHolder<Movie> {

    private Movie movie;
    private RequestManager glide;
    private Context context;

    MovieListItem(
            Movie movie,
            RequestManager glide ,
            Context context
    ){
        this.movie = movie;
        this.glide = glide;
        this.context = context;
    }


    @Override
    public Movie getModel(){
        return movie;
    }

    @Override
    public boolean filter(@Nullable String constraint) {
        if (constraint != null) {
            return movie.getTitle().toLowerCase().contains(constraint.toLowerCase());
        } else return false;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof MovieListItem) {
            MovieListItem inItem = (MovieListItem) inObject;
            return this.movie.getTitle().toLowerCase().equals(inItem.movie.getTitle().toLowerCase());
        }
        return false;
    }


    @Override
    public int hashCode() {
        return movie.getTitle().hashCode();
    }


    @LayoutRes
    public int getLayoutRes(){
        return R.layout.movie_list_item;
    }

    /**
     * Creation of the ViewHolder. The view is already inflated!
     */
    public ViewHolder createViewHolder(
            View view,
            FlexibleAdapter<IFlexible> adapter
    ){
        return new ViewHolder(view,adapter);
    }

    /**
     * Binds the data of this item to the given Layout.
     */
    public void bindViewHolder(
            FlexibleAdapter<IFlexible> adapter,
            ViewHolder holder,
            int position,
            List<Object> payloads
    ){

        // Title
        if(holder.movie_title != null){
            if (adapter.getFilter(String.class) != null) {

                String filter = adapter.getFilter(String.class);

                FlexibleUtils
                        .highlightText(
                                holder.movie_title,
                                movie.getTitle(),
                                filter,
                                context.getResources().getColor(R.color.text_highlight_color)
                        );

            } else {
                holder.movie_title.setText(movie.getTitle());
            }
        }

        // Poster
        if (holder.movie_poster != null){

            glide
                .load(Constants.TMDB_API_V3_POSTER_BASE_URL + movie.getPosterPath())
                .apply(new RequestOptions()
                        .placeholder(context.getDrawable(R.drawable.image_placeholder))
                )
                .into(holder.movie_poster);
        }

        // Popularity
        double OldRange = (400.0f - 0.0f);
        double NewRange = (5.0f - 0.0f);
        double popularity_rating = (((movie.getPopularity() - 0.0f) * NewRange) / OldRange) + 0.0f;
        holder.movie_popularity.setRating((float) popularity_rating);
        holder.movie_popularity_text.setText(Float.valueOf(new DecimalFormat("#.#").format(popularity_rating)).toString());

    }


    class ViewHolder extends FlexibleViewHolder {

        CardView main_body;
        ImageView movie_poster;
        TextView movie_title;
        RatingBar movie_popularity;
        TextView movie_popularity_text;


        ViewHolder(
            View view,
            FlexibleAdapter<IFlexible> adapter
        ){
            super(view,adapter);
            main_body = view.findViewById(R.id.main_body);
            movie_poster = view.findViewById(R.id.movie_poster);
            movie_title = view.findViewById(R.id.movie_title);
            movie_popularity = view.findViewById(R.id.movie_popularity);
            movie_popularity_text = view.findViewById(R.id.movie_popularity_text);
        }

    }

}
