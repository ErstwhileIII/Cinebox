package com.velocikey.android.learning.cinebox;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
        mContext = context;
        mMovieInformation = movieInfo;
        mMovieListListener = listener;
    }

    @Override
    public MovieInfoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.v(LOG_TAG, "onCreateViewHolder: viewType is " + viewType);
        View itemView = LayoutInflater
                            .from(viewGroup.getContext())
                            .inflate(R.layout.movie_list_poster, viewGroup, false);
        MovieInfoViewHolder mMovieInfoViewHolder = new MovieInfoViewHolder(itemView);
        return mMovieInfoViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieInfoViewHolder holder, int position) {
        Log.v(LOG_TAG,"onBindViewHolder: position is " + position);
        String posterPath = mMovieInformation.get(position).getFullPosterPath(185);
        Log.v(LOG_TAG,"loading image from: " + posterPath);
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
        Log.v(LOG_TAG, "onAttachedToRecyclerView");
    }

    /**
     * Called when this adapter is detached from the specified recyclerview
     * @param recyclerView the recyclerview from which this adapter was detached.
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.v(LOG_TAG, "onDetachedFromRecyclerView");
    }

    /**
     * Set the movie information to a new array;
     *
     * @param movieInformation the
     */
    public void setMovie(ArrayList<MovieInfo> movieInformation) {
        Log.v(LOG_TAG, "setMovie");
        if (mMovieInformation == null || mMovieInformation.size() == 0) {
            Log.v(LOG_TAG, "setMovie: mMovieInformation null? " + (mMovieInformation == null));
            mMovieInformation = movieInformation;
            this.notifyDataSetChanged();
        } else {
            //TODO better way to handle extensions?
            Log.v(LOG_TAG, "setMovie: old size (" + mMovieInformation.size()+ ") , new size=" + movieInformation.size());
            mMovieInformation = movieInformation;
            this.notifyItemRangeChanged(0, movieInformation.size());
        }
    }


    // Inner ViewHolder class
    class MovieInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Class fields
        private final String LOG_TAG = MovieInfoViewHolder.class.getSimpleName();
        // Object fields
        public ImageView poster;

        public MovieInfoViewHolder(View itemView) {
            super(itemView);
            Log.v(LOG_TAG, "id: " + itemView.getId());


            poster = (ImageView) itemView.findViewById(R.id.movie_list_poster);
            poster.setOnClickListener(this);

        }
        public void onClick(View v) {
            int position = getAdapterPosition();
            MovieInfo movieInfo = mMovieInformation.get(position);
            Log.v(LOG_TAG, "onClick: returning movie information from position: " + position);
            mMovieListListener.onMovieListFragmentInteraction(movieInfo);
        }
    }
}
