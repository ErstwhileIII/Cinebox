package com.velocikey.android.learning.cinebox.webinfo.movie;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.velocikey.android.learning.cinebox.R;

import java.util.ArrayList;

/**
 * Created by Joseph White on 09-Aug-2015
 *
 * @since 1.0
 */
public class MovieInfoAdapter
        extends RecyclerView.Adapter<MovieInfoAdapter.MovieInfoViewHolder>{
    // Class fields
    private static final String LOG_TAG = MovieInfoAdapter.class.getSimpleName();

    private final Context mContext;
    private final MovieListFragment.onMovieListFragmentListener mMovieListListener;
    private ArrayList<MovieInfo> mMovieInformation;

    // Object Fields

    public MovieInfoAdapter(Context context, ArrayList<MovieInfo> movieInfo,
                            MovieListFragment.onMovieListFragmentListener listener) {
        Log.v(LOG_TAG, "-->Constructor");
        mContext = context;
        mMovieInformation = movieInfo;
        mMovieListListener = listener;
    }

    @Override
    public MovieInfoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.v(LOG_TAG, "-->onCreateViewHolder");
        View itemView = LayoutInflater
                            .from(viewGroup.getContext())
                            .inflate(R.layout.movie_list_poster, viewGroup, false);
        return new MovieInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieInfoViewHolder holder, int position) {
        Log.v(LOG_TAG, "-->onBindViewHolder:");
        String posterPath = mMovieInformation.get(position).getFullPosterPath(185);
        Uri uri = Uri.parse(posterPath);
        //TODO add error and placeholder drawings
        Picasso.with(mContext).load(uri).into(holder.poster);
    }

    /**
     * @return number of items in the adapter is currently handling
     */
    @Override
    public int getItemCount() {
        if (mMovieInformation == null) {
            return 0;
        } else {
            return mMovieInformation.size();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.v(LOG_TAG, "-->onAttachedToRecyclerView");
    }

    /**
     * Called when this adapter is detached from the specified recyclerview
     * @param recyclerView the recyclerview from which this adapter was detached.
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.v(LOG_TAG, "-->onDetachedFromRecyclerView");
    }

    /**
     * Set the movie information to a new array;
     *
     * @param movieInformation the
     */
    public void setMovie(ArrayList<MovieInfo> movieInformation) {
        Log.v(LOG_TAG, "-->setMovie");
        if (mMovieInformation == null || mMovieInformation.size() == 0) {
            mMovieInformation = movieInformation;
            this.notifyDataSetChanged();
        } else {
            //TODO better way to handle extensions?
            mMovieInformation = movieInformation;
            this.notifyItemRangeChanged(0, movieInformation.size());
        }
    }


    // Inner ViewHolder class
    class MovieInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Object fields
        public final ImageView poster;
        // Class fields
        private final String LOG_TAG = MovieInfoViewHolder.class.getSimpleName();

        public MovieInfoViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.movie_list_poster);
            poster.setOnClickListener(this);
        }
        public void onClick(View v) {
            int position = getAdapterPosition();
            MovieInfo movieInfo = mMovieInformation.get(position);
            mMovieListListener.onMovieListFragmentInteraction(movieInfo);
        }
    }
}
