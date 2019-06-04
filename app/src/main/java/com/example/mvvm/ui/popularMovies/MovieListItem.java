package com.example.mvvm.ui.popularMovies;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
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
            return movie.getTitle().contains(constraint);
        } else return false;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof MovieListItem) {
            MovieListItem inItem = (MovieListItem) inObject;
            return this.movie.getTitle().equals(inItem.movie.getTitle());
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

        if (holder.movie_poster != null){

            glide
                .load(Constants.TMDB_API_V3_POSTER_BASE_URL + movie.getPosterPath())
                .apply(new RequestOptions()
                        .placeholder(context.getDrawable(R.drawable.image_placeholder))
                )
                .into(holder.movie_poster);
        }

    }


    class ViewHolder extends FlexibleViewHolder {

        CardView main_body;
        ImageView movie_poster;
        TextView movie_title;


        ViewHolder(
            View view,
            FlexibleAdapter<IFlexible> adapter
        ){
            super(view,adapter);
            main_body = view.findViewById(R.id.main_body);
            movie_poster = view.findViewById(R.id.movie_poster);
            movie_title = view.findViewById(R.id.movie_title);
        }

    }

}
