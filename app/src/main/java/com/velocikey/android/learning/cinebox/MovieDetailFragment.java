package com.velocikey.android.learning.cinebox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMovieDetailFragmentListener} interface
 * to handle interaction events.
 */
public class MovieDetailFragment extends Fragment {
    // Class fields
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();//TODO consider passing position in data array (and the array of course)
    protected static final String ARG_movieId = "movieId";
    protected static final String ARG_title = "title";
    protected static final String ARG_releaseDate = "releaseDate";
    protected static final String ARG_overview = "overview";
    protected static final String ARG_popularity = "popularity";
    protected static final String ARG_rating = "rating";
    protected static final String ARG_posterPath = "posterPath";
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
        fragment.movieId = movieInfo.getMovieId();
        fragment.title = movieInfo.getTitle();
        fragment.releaseDate = movieInfo.getReleaseDate();
        fragment.overview = movieInfo.getOverview();
        fragment.popularity = movieInfo.getPopularity();
        fragment.rating = movieInfo.getRating();
        fragment.posterPath = movieInfo.getOverview();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Log.v(LOG_TAG, "onCreate: ");
        if (getActivity() != null) {
            Log.v(LOG_TAG, "onCreate: found arguments");
            movieId = getArguments().getInt(ARG_movieId);
            title = getArguments().getString(ARG_title);
            releaseDate = getArguments().getString(ARG_releaseDate);
            overview = getArguments().getString(ARG_overview);
            popularity = getArguments().getFloat(ARG_popularity);
            rating = getArguments().getFloat(ARG_rating);
            posterPath = getArguments().getString(ARG_posterPath);
        }
        if (savedInstanceState == null) {
            Log.v(LOG_TAG, "onCreate: savedInstanceState is null");
        } else {
            Log.v(LOG_TAG, "onCreate: savedInstanceState is NOT null");
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            //((TextView) rootView.findViewById(R.id.detail_movie_id)).setText("" + movieId);
            ((TextView) rootView.findViewById(R.id.detail_title)).setText("" + title);
            ((TextView) rootView.findViewById(R.id.detail_release_date)).setText("" + releaseDate);
            ((TextView) rootView.findViewById(R.id.detail_popularity)).setText("" + new Float(popularity).toString());
            ((TextView) rootView.findViewById(R.id.detail_rating)).setText("" + new Float(rating).toString());
            ((TextView) rootView.findViewById(R.id.detail_overview)).setText("" + overview);
        ImageView poster = (ImageView) rootView.findViewById(R.id.detail_poster);

        if (posterPath != null) {
            Log.v(LOG_TAG, "About to load poster from " + posterPath);
            Uri uri = Uri.parse(posterPath);

            Picasso.with(mContext).load(uri).into(poster);
        }

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMovieDetailFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMovieDetailFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMovieListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        public void onMovieDetailFragmentInteraction(Uri uri);
    }

}
