package com.velocikey.android.learning.cinebox.webinfo.movie;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.velocikey.android.learning.cinebox.R;
import com.velocikey.android.learning.cinebox.SettingsActivity;
import com.velocikey.android.learning.cinebox.webinfo.movie.data.MovieContract;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMovieDetailFragmentListener} interface
 * to handle interaction events.
 */
public class MovieDetailFragment extends Fragment {// Class fields
    //TODO consider passing position sql database)
    public static final String ARG_movieId = MovieContract.MovieEntry.COL_id;
    public static final String ARG_title = MovieContract.MovieEntry.COL_title;
    public static final String ARG_releaseDate = MovieContract.MovieEntry.COL_releaseDate;
    public static final String ARG_overview = MovieContract.MovieEntry.COL_overview;
    public static final String ARG_popularity = MovieContract.MovieEntry.COL_popularity;
    public static final String ARG_rating = MovieContract.MovieEntry.COL_rating;
    public static final String ARG_posterPath = MovieContract.MovieEntry.COL_posterPath;
    public static final String TAG_MOVIE_DETAIL_FRAGMENT = "MovieDetail";
    // Class fields
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    // Object fields
    private static Context mContext;
    private static int movieId;
    private static String title;
    private static String releaseDate;
    private static String overview;
    private static Float popularity;
    private static Float rating;
    private static String posterPath;

    private OnMovieDetailFragmentListener mListener;

    public MovieDetailFragment() {
        // Required empty public constructor
    }
    //TODO not sure this is needed
    public static MovieDetailFragment newInstance(MovieInfo movieInfo) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        movieId = movieInfo.getMovieId();
        title = movieInfo.getTitle();
        releaseDate = movieInfo.getReleaseDate();
        overview = movieInfo.getOverview();
        popularity = movieInfo.getPopularity();
        rating = movieInfo.getRating();
        posterPath = movieInfo.getOverview();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(LOG_TAG, "-->onAttach(Context");
        handleOnAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(LOG_TAG, "-->onAttach(Activity)");
        handleOnAttach(activity);

    }

    private void handleOnAttach(Context context) {
        if (mListener == null) {
            try {
                mListener = (OnMovieDetailFragmentListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement OnMovieListFragmentListener");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Log.v(LOG_TAG, "-->onCreate: ");
        if (getArguments() == null) {
            Log.v(LOG_TAG, "no arguments");
        } else {
            Log.v(LOG_TAG, "argument count = " + getArguments().size());
            movieId = getArguments().getInt(ARG_movieId);
            title = getArguments().getString(ARG_title);
            releaseDate = getArguments().getString(ARG_releaseDate);
            overview = getArguments().getString(ARG_overview);
            popularity = getArguments().getFloat(ARG_popularity);
            rating = getArguments().getFloat(ARG_rating);
            posterPath = getArguments().getString(ARG_posterPath);
        }

        if (savedInstanceState == null) {
            Log.v(LOG_TAG, "-->onCreate: savedInstanceState is null");
        } else {
            Log.v(LOG_TAG, "-->onCreate: savedInstanceState is NOT null");
            movieId = savedInstanceState.getInt(ARG_movieId);
            title = savedInstanceState.getString(ARG_title);
            releaseDate = savedInstanceState.getString(ARG_releaseDate);
            overview = savedInstanceState.getString(ARG_overview);
            popularity = savedInstanceState.getFloat(ARG_popularity);
            rating = savedInstanceState.getFloat((ARG_rating));
            posterPath = savedInstanceState.getString(ARG_posterPath);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-->onCreateView:");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ((TextView) rootView.findViewById(R.id.detail_title)).setText("" + title);
        ((TextView) rootView.findViewById(R.id.detail_release_date)).setText("" + releaseDate);
        ((TextView) rootView.findViewById(R.id.detail_popularity)).setText("" + popularity);
        ((TextView) rootView.findViewById(R.id.detail_rating)).setText("" + rating);
        ((TextView) rootView.findViewById(R.id.detail_overview)).setText("" + overview);
        ImageView poster = (ImageView) rootView.findViewById(R.id.detail_poster);

        if (posterPath != null) {
            Uri uri = Uri.parse(posterPath);
            Picasso.with(mContext).load(uri).into(poster);
        }

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(LOG_TAG, "-->onDetach:");
        //TODO make more general .. call return to main fragment
        mListener.onMovieDetailFragmentInteraction("onDetach");
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(LOG_TAG, "-->onOptionsItemSelected:");
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMovieDetailFragmentListener {
        // TODO: Update argument type and name
        void onMovieDetailFragmentInteraction(String action);
    }
}
