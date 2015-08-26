package com.velocikey.android.learning.cinebox.webinfo.movie;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.velocikey.android.learning.cinebox.R;
import com.velocikey.android.learning.cinebox.webinfo.movie.data.MovieContract;

/**
 * Created by Joseph White on 09-Aug-2015
 *
 * @since 1.0
 */
public class MovieInfoAdapterCursor
        extends RecyclerView.Adapter<MovieInfoAdapterCursor.MovieInfoViewHolder> {
    // Class fields
    private static final String LOG_TAG = MovieInfoAdapterCursor.class.getSimpleName();
    // Object fields

    private final Context mContext;
    private final MovieListFragment.onMovieListFragmentListener mMovieListListener;
    private Cursor mCursor;
    // Data source information .. for database cursor
    private int mColumnId;
    private int mColumnTitle;
    private int mColumnReleaseDate;
    private int mColumnPopularity;
    private int mColumnRating;
    private int mColumnPosterPath;
    private int mColumnOverview;
    private boolean isDataValid;
//    private ArrayList<MovieInfo> mMovieInformation;

    public MovieInfoAdapterCursor(Context context,
                                  Cursor c,
                                  MovieListFragment.onMovieListFragmentListener listener) {
        Log.v(LOG_TAG, "-->Constructor:");
        // Retain context
        mContext = context;
        // retain listener
        mMovieListListener = listener;
        // Handle cursor
        mCursor = c;
        setHasStableIds(true);
        if (mCursor == null) {
            isDataValid = false;
            mColumnId = -1;
        } else {
            isDataValid = true;
            setCursorColumnValues();
        }

    }

    private void setCursorColumnValues() {
//        Log.v(LOG_TAG,"-->setCursorColumnValues:");
        //TODO consider making this static
        mColumnId = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COL_id);
        mColumnTitle = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COL_title);
        mColumnReleaseDate = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COL_releaseDate);
        mColumnPopularity = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COL_popularity);
        mColumnRating = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COL_rating);
        mColumnPosterPath = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COL_posterPath);
        mColumnOverview = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COL_overview);
    }

    //TODO Where are these methods needed?
    public Cursor getCursor() {
        return mCursor;
    }

    public void setDataSource(Cursor cursor) {
        Log.v(LOG_TAG, "-->setDataSource: ");
        if (mCursor == null) {
            createCursor(cursor);
        } else {
            swapCursor(cursor);
        }
    }

    /**
     * Check whether the data contains a BaseColumns._ID column and extract its instance.
     * Call notifyDataSetChanged to indicate that the RecyclerView should be refreshed.
     *
     * @param cursor the rows from a database to be shown in the RecyclerView
     */
    private void createCursor(Cursor cursor) {
        Log.v(LOG_TAG, "-->createCursor:");
        mCursor = cursor;
        isDataValid = (cursor != null);
        mColumnId = isDataValid ? this.mCursor.getColumnIndex(BaseColumns._ID) : -1;
        //TODO adjust here if headers / footers are implemented
        if (mCursor == null) {
            isDataValid = false;
            mColumnId = -1;
        } else {
            isDataValid = true;
            setCursorColumnValues();
            notifyItemRangeInserted(0, mCursor.getCount());
        }
    }

    public void swapCursor(Cursor cursor) {
//        Log.v(LOG_TAG, "-->swapCursor: ");
        if (mCursor == cursor) {
            return; // same cursor, nothing to do.
        }
        final Cursor oldCursor = mCursor;
        mCursor = cursor;
        if (mCursor != null) {
            isDataValid = true;
            setCursorColumnValues();
            notifyDataSetChanged();
        } else {
            mColumnId = -1;
            isDataValid = false;
            if (oldCursor != null) {
                //TODO Ensure that getCount is right on for old range!!
                notifyItemRangeRemoved(0, oldCursor.getCount());
            }
        }
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    @Override
    public MovieInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.v(LOG_TAG, "onCreateViewHolder: ");
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_list_poster, parent, false);
        return new MovieInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieInfoViewHolder holder, int position) {
        String posterPath;
//        Log.v(LOG_TAG,"onBindViewHolder: position is " + position);
        if (!isDataValid) {
            throw new IllegalStateException("Cursor is invalid!");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException(("Cursor could not be moved to position " + position));
        }
        //TODO extract fullPosterPath from cursor position
        posterPath = MovieInfo.getFullPosterPath(mCursor.getString(mColumnPosterPath), 185);

        Uri uri = Uri.parse(posterPath);
        //TODO add error and placeholder drawings
        Picasso.with(mContext).load(uri).into(holder.poster);
    }

    @Override
    public long getItemId(int position) {
        long result;
        //TODO if this class is extneded to handle headers and footers we will need to change
        if (isDataValid && mCursor.moveToPosition(position)) {
            result = mCursor.getLong(mColumnId);
        } else {
            result = RecyclerView.NO_ID;
        }
        return result;
    }

    /**
     * @return number of items in the adapter is currently handling
     */
    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.v(LOG_TAG, "-->onAttachedToRecyclerView");
    }

    /**
     * Called when this adapter is detached from the specified recyclerview
     *
     * @param recyclerView the recyclerview from which this adapter was detached.
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.v(LOG_TAG, "-->onDetachedFromRecyclerView");
    }


    // Inner ViewHolder class
    class MovieInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Object fields
        public final ImageView poster;
        // Class fields
        private final String LOG_TAG = MovieInfoViewHolder.class.getSimpleName();

        public MovieInfoViewHolder(View itemView) {
            super(itemView);
//            Log.v(LOG_TAG, "-->Constructor");
            poster = (ImageView) itemView.findViewById(R.id.movie_list_poster);
            poster.setOnClickListener(this);

        }

        public void onClick(View v) {
            Log.v(LOG_TAG, "-->onClick");
            int position = getAdapterPosition();
            // TODO create a movieInformation object to pass to the listener
            mCursor.moveToPosition(position);
            int id = mCursor.getInt(mColumnId);
            String title = mCursor.getString(mColumnTitle);
            String releaseDate = mCursor.getString(mColumnReleaseDate);
            Float popularity = mCursor.getFloat(mColumnPopularity);
            Float rating = mCursor.getFloat(mColumnRating);
            String posterPath = mCursor.getString(mColumnPosterPath);
            String overview = mCursor.getString(mColumnOverview);

            MovieInfo movieInfo = new MovieInfo(id, title, releaseDate, popularity, rating,
                    posterPath, overview);

            Log.v(LOG_TAG, "returning movie information from position: " + position);
            //TODO consider returning position instead
            mMovieListListener.onMovieListFragmentInteraction(movieInfo);
        }
    }
}
