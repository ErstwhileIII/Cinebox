package com.velocikey.android.learning.cinebox.webinfo.movie;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.velocikey.android.learning.cinebox.R;
import com.velocikey.android.learning.cinebox.webinfo.movie.data.MovieContract;
import com.velocikey.android.learning.cinebox.webinfo.movie.data.MovieDBUtility;
import com.velocikey.android.learning.cinebox.webinfo.movie.data.MovieProvider;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link onMovieListFragmentListener} interface
 * to handle interaction events.
 */
public class MovieListFragment extends Fragment {
    // Class fields
    private static final String LOG_TAG = MovieListFragment.class.getSimpleName();
    private static final int MOVIE_DISPLAY_ROWS = 3;

    // Object fields
    private static final int totalPages = 4; // total number of pages to request
    /**
     * Listener in activity that attached this fragment
     */
    private onMovieListFragmentListener mListener;
    private MovieProvider mMovieProvider = new MovieProvider();
    private ArrayList<MovieInfo> mMovieInfoList;
    private MovieInfoAdapter mMovieInfoAdapter;
    private MovieInfoAdapterCursor mMovieInfoAdapterCursor;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    /**
     * The next page to get information from using the asynchronous task
     */
    private int currentPage = 1;

    public MovieListFragment() {
        Log.v(LOG_TAG, "Constructor");
        // Required empty public constructor
    }

    /**
     * (Lifecycle order #1)
     * Save the calling activity for use as a click listener when an item is selected in the
     * list of movies. Make sure that the appropriate interface is implemented in the calling
     * activity (throwing an exception if not).
     *
     * @param activity containing activity
     *
     * @throws ClassCastException if the {@link onMovieListFragmentListener} interface is not
     * implemented by the calling activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(LOG_TAG, "onAttach:");
        try {
            mListener = (onMovieListFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMovieListFragmentListener");
        }
    }

    /**
     * (Lifecycle order #2)
     *
     * @param savedInstanceState the previous state of the fragment, if it was saved
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate: (savedInstanceState is null? " + (savedInstanceState == null));
        if (savedInstanceState == null) {
            //TODO Handle restoration
        }
        mMovieInfoList = new ArrayList<>();
        getMovies();
    }

    private void getMovies() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        long lastQuery = preferences.getLong("lastQueried", 0);
        Log.v(LOG_TAG, "lastquery time was " + lastQuery);
        // TODO remove force reset
        lastQuery = Math.min(lastQuery, 1);
        if (lastQuery < (System.currentTimeMillis() - 24 * 60 * 60 * 1000)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("lastQueried", System.currentTimeMillis());
            editor.commit();
            if (lastQuery > 0) {
                Log.v(LOG_TAG, "getMovies: delete all rows");
                int deletedRows = this.getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, "1", null);
                Log.v(LOG_TAG, "Rows deleted count = " + deletedRows);
            }
            String movieOrder = preferences.getString(getString(R.string.pref_movie_order_key),
                    getString(R.string.pref_movie_order_defaultvalue));
            Log.v(LOG_TAG, "Requested movie order is: " + movieOrder);
            FetchMovieInfoTask movieInfoTask = new FetchMovieInfoTask();
            //TODO adjust to pass page to return
            movieInfoTask.execute(movieOrder);
        }

    }

    /**
     * (Lifecycle #3)
     *
     * @param inflater           to be used to inflate views
     * @param parent             the container for this fragment
     * @param savedInstanceState and saved state (null if none)
     * @return the inflated RecyclerView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        Log.v(LOG_TAG, "onCreateView: (savedInstanceState null? " + (savedInstanceState == null) + ")");
        //TODO handle savedInstanceState

        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_movie_list, parent, false);
        rv.setLayoutManager(new GridLayoutManager(rv.getContext(), MOVIE_DISPLAY_ROWS, GridLayoutManager.HORIZONTAL, false));
        mMovieInfoAdapter = new MovieInfoAdapter(getActivity(), mMovieInfoList, mListener);
        rv.setAdapter(mMovieInfoAdapter);
        rv.setHasFixedSize(true);

        return rv;
    }

    /**
     * (Lifecycle #4)
     *
     * @param savedInstanceState The previous state of the fragment, if saved
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG, "onActivityCreated: ");
    }

    /**
     * (Lifecycle 5 (optional)
     *
     * @param savedInstanceState the prefious state of the fragment, if saved
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.v(LOG_TAG, "onViewStateRestored: ");
    }

    /**
     * (Lifecycle #6)
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "onStart: ");
    }

    /**
     * (Lifecycle #7
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume: ");
    }

    /**
     * (Lifecycle #8)
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "onPause: ");
    }

    /**
     * (Lifecycle #9)
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "onStop: ");
    }

    /**
     * (Lifecycle #10)
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(LOG_TAG, "onDestroyView: ");
    }

    /**
     * (Lifecycle #11)
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy: ");
    }

    /**
     * (LIfecycle #12)
     */
    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(LOG_TAG, "onDetach: ");
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
    public interface onMovieListFragmentListener {
        /**
         * Called when a particular movie is selected in the user interface
         *
         * @param movieInfo the selected movie
         */
        void onMovieListFragmentInteraction(MovieInfo movieInfo);
    }


    // Asynchronous class definitions
    // Asynchronous task class definition for Movies
    private class FetchMovieInfoTask extends AsyncTask<String, Void, ArrayList<MovieInfo>> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected ArrayList<MovieInfo> doInBackground(String... params) {

            WebApiTMDB tmdb = new WebApiTMDB();
            Log.v(LOG_TAG, "about to get movies");
            //String result = tmdb.getRawMovie("sort_by=popularity.desc");
            Log.v(LOG_TAG, "params=" + params[0]);
            String sortByValue = params[0] + ".desc";
            Log.v(LOG_TAG, "order clause = " + sortByValue);
            if (params.length > 0) {
                //TODO handle these strings in preferences
                if (params[0].equalsIgnoreCase("popularity")) {
                    sortByValue = "popularity.desc";
                } else if (params[0].equalsIgnoreCase("vote_average")) {
                    sortByValue = "vote_average.desc";
                } else if (params[0].equalsIgnoreCase("release_date")) {
                    sortByValue = "release_date.desc";
                }
            }

            int pagesToGet = 3;
            ArrayList<MovieInfo> result = tmdb.getMovieInfo(sortByValue, currentPage, pagesToGet);
            currentPage = currentPage + pagesToGet;
            Log.v(LOG_TAG, "back from movie request");
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> result) {

            Log.v(LOG_TAG, "onPostExecite, old size(" + mMovieInfoList.size()
                    + " additional movies (" + result.size() + ")");
            //TODO optimize to handle new informatoin and added information
            if (mMovieInfoList.size() < 1) {
                mMovieInfoList = result;
            } else {
                mMovieInfoList.addAll(result);
            }
            Log.v(LOG_TAG, "Now mMovieList size is " + mMovieInfoList.size());
            // handle putting posters into the movie adapter view
            mMovieInfoAdapter.setMovie(mMovieInfoList);

            // put information into a database using the MovieProvider
            ContentValues[] movieContentValues = new ContentValues[result.size()];
            for (int i = 0; i < result.size(); i++) {
                movieContentValues[i] = MovieDBUtility.getMovieValues(result.get(i));
            }
            Log.v(LOG_TAG, "onPostExecute: about to try to get ContentResolver");

            ContentResolver resolver = getActivity().getContentResolver();
            Log.v(LOG_TAG, "resolver obtained, now try to insert content values");


            resolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI, movieContentValues);
            // mMovieProvider.bulkInsert(MovieContract.MovieEntry.CONTENT_URI, movieContentValues);

        }
    }
}
