package com.velocikey.android.learning.cinebox.webinfo.movie;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

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
public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // Class fields
    private static final String LOG_TAG = MovieListFragment.class.getSimpleName();
    private static final int MOVIE_DISPLAY_ROWS = 3;
    private static final long MOVIE_QUERY_STALE = 24 * 60 * 60 * 1000;

    // Object fields
    private static final int totalPages = 4; // total number of pages to request
    /**
     * Listener in activity that attached this fragment
     */
    private MovieListFragment me;
    private onMovieListFragmentListener mListener;
    private MovieProvider mMovieProvider = new MovieProvider();
    private ArrayList<MovieInfo> mMovieInfoList;
    //    private MovieInfoAdapter mMovieInfoAdapter;
    private MovieInfoAdapterCursor mMovieInfoAdapterCursor;
    private Cursor mMovieInfoCursor;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    /**
     * The next page to get information from using the asynchronous task
     */
    private int currentPage = 1;

    public MovieListFragment() {
        Log.v(LOG_TAG, "-->Constructor");
        me = this;
    }

    @Override
    public void onInflate(Context context, AttributeSet attributeSet, Bundle savedInstanceState) {
        super.onInflate(context, attributeSet, savedInstanceState);
        Log.v(LOG_TAG, "-->onInflate(Context)");
    }

    //TODO remove since depracated nandle using context instead . but check linke onAttach
    @Override
    public void onInflate(Activity activity, AttributeSet attributeSet, Bundle savedInstanceState) {
        super.onInflate(activity, attributeSet, savedInstanceState);
        Log.v(LOG_TAG, "-->onInflate(Activity)");
    }

    /**
     * (Lifecycle order #1) onAttack
     * Save the calling activity for use as a click listener when an item is selected in the
     * list of movies. Make sure that the appropriate interface is implemented in the calling
     * activity (throwing an exception if not).
     *
     * @param context containing context for the attaching activity
     * @throws ClassCastException if the {@link onMovieListFragmentListener} interface is not
     *                            implemented by the calling activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(LOG_TAG, "-->onAttach: Context argument");
        handleOnAttach(context);
    }

    /**
     * Provide deprecated form for use with lower that API level 23
     * @param activity
     */
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(LOG_TAG, "-->onAttach: Activity argument");
        handleOnAttach(activity);
    }

    /**
     * (Lifecycle order #2) onCreate
     *
     * @param savedInstanceState the previous state of the fragment, if it was saved
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mMovieInfoList = new ArrayList<>();
        Log.v(LOG_TAG, "-->onCreate: ");

        if (savedInstanceState != null) {
            //TODO Handle restoration
            Log.v(LOG_TAG, "restoring savedInstanceState");
            if (mMovieInfoList != null && mMovieInfoList.size() == 0) {
                Log.v(LOG_TAG, "still handling async task ?!!");
                getMovies();
            }
        } else {
            getMovies();
        }

    }

    private void getMovies() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        long lastQuery = preferences.getLong("lastQueried", 0);

        Log.v(LOG_TAG, "lastquery time was " + lastQuery);
        long now = System.currentTimeMillis();
        Log.v(LOG_TAG, "now = " + now + ", stale cutoff = " + (now - MOVIE_QUERY_STALE));
        boolean isStale = lastQuery < (now - MOVIE_QUERY_STALE);
        Log.v(LOG_TAG, "movie data stale? " + isStale);

        if (isStale) {

            if (lastQuery > 0) {
                Log.v(LOG_TAG, "getMovies: delete all rows");
                int deletedRows = this.getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, "1", null);
                Log.v(LOG_TAG, "Rows deleted count = " + deletedRows);
            }
            String movieOrder = preferences.getString(getString(R.string.pref_movie_order_key), getString(R.string.pref_movie_order_defaultvalue));
            Log.v(LOG_TAG, "Requested movie order is: " + movieOrder);
            FetchMovieInfoTask movieInfoTask = new FetchMovieInfoTask();
            //TODO adjust to pass page to return
            movieInfoTask.execute(movieOrder);
        } else {
            Log.v(LOG_TAG, "About to get a loader manager");
            getLoaderManager().initLoader(0, null, me);
        }
    }

    /**
     * (Lifecycle #3) onCreateView
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
        Log.v(LOG_TAG, "-->onCreateView: (savedInstanceState null? " + (savedInstanceState == null) + ")");
        //TODO handle savedInstanceState
        if (savedInstanceState != null) {
            //TODO handle here
        }

        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_movie_list, parent, false);
        rv.setLayoutManager(new GridLayoutManager(rv.getContext(), MOVIE_DISPLAY_ROWS, GridLayoutManager.HORIZONTAL, false));

        mMovieInfoAdapterCursor = new MovieInfoAdapterCursor(getActivity(), mMovieInfoCursor, mListener);
        rv.setAdapter(mMovieInfoAdapterCursor);
        rv.setHasFixedSize(true);

        return rv;
    }

    /**
     * (Lifecycle #4) on ActivityCreated
     *
     * @param savedInstanceState The previous state of the fragment, if saved
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG, "-->onActivityCreated: ");
    }

    /**
     * (Lifecycle 5 (optional)
     *
     * @param savedInstanceState the prefious state of the fragment, if saved
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.v(LOG_TAG, "-->onViewStateRestored: ");
    }

    /**
     * (Lifecycle #6)
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "-->onStart: ");
    }

    /**
     * (Lifecycle #7
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "-->onResume: ");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-->onSaveInstanceState: ");
        //TODO save what will be needed to restore on configuration change
    }

    /**
     * (Lifecycle #8)
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "-->onPause: ");
    }

    /**
     * (Lifecycle #9)
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "-->onStop: ");
    }

    /**
     * (Lifecycle #10)
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(LOG_TAG, "-->onDestroyView: ");
    }

    /**
     * (Lifecycle #11)
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "-->onDestroy: ");
    }

    /**
     * (LIfecycle #12)
     */
    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(LOG_TAG, "-->onDetach: ");
        mListener = null;
    }

    private void handleOnAttach(Context context) {
        Log.v(LOG_TAG, "... working");
        if (mMovieInfoList == null) {
            try {
                mListener = (onMovieListFragmentListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.getClass().getName()
                        + " must implement OnMovieListFragmentListener");
            }
        }
    }

    private void handleOnInflate(Context context, AttributeSet attributeSet, Bundle savedInstanceState) {
        Log.v(LOG_TAG, " handlikng OnFlate");
    }


    //---------------- LoaderManager stuff

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //TODO extend to more than one loader, so ignore id for now
        final String[] projection = {
                BaseColumns._ID,
                MovieContract.MovieEntry.COL_title,
                MovieContract.MovieEntry.COL_releaseDate,
                MovieContract.MovieEntry.COL_popularity,
                MovieContract.MovieEntry.COL_rating,
                MovieContract.MovieEntry.COL_posterPath,
                MovieContract.MovieEntry.COL_overview};

        Uri baseUri = MovieContract.MovieEntry.CONTENT_URI;
        //TODO get sort order from preferences
        return new CursorLoader(getActivity(),
                baseUri,
                projection,
                null,
                null,
                MovieContract.MovieEntry.COL_popularity + " DESC");
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "-->onLoadFinished: ");
        mMovieInfoAdapterCursor.swapCursor(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v(LOG_TAG, "-->onLoaderReset");
        mMovieInfoAdapterCursor.swapCursor(null);
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

            // TODO handle new pages requests here in fragment?
            // TODO handle database insertion in "genMovieInfo"
            int pagesToGet = 3;
            ArrayList<MovieInfo> result = tmdb.getMovieInfo(sortByValue, currentPage, pagesToGet);
            currentPage = currentPage + pagesToGet;
            Log.v(LOG_TAG, "back from movie request");
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieInfo> result) {
            Log.v(LOG_TAG, "-->onPostExecute: ");

            Log.v(LOG_TAG, "old size(" + mMovieInfoList.size()
                    + " additional movies (" + result.size() + ")");
            //TODO optimize to handle new informatoin and added information
            if (mMovieInfoList.size() < 1) {
                mMovieInfoList = result;
            } else {
                mMovieInfoList.addAll(result);
            }
            Log.v(LOG_TAG, "Now mMovieList size is " + mMovieInfoList.size());
            // handle putting posters into the movie adapter view
//            mMovieInfoAdapter.setMovie(mMovieInfoList);

            // put information into a database using the MovieProvider
            ContentValues[] movieContentValues = new ContentValues[result.size()];
            for (int i = 0; i < result.size(); i++) {
                movieContentValues[i] = MovieDBUtility.getMovieValues(result.get(i));
            }
            Log.v(LOG_TAG, "about to try to get ContentResolver");

            ContentResolver resolver = getActivity().getContentResolver();
            Log.v(LOG_TAG, "resolver obtained, now try to insert content values");

            //TODO this should be done as an asynch task
            int x = resolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI, movieContentValues);
            Log.v(LOG_TAG, "finished bulkinsert with count = " + x);

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
            editor.putLong("lastQueried", System.currentTimeMillis());
            editor.commit();
            //

            Log.v(LOG_TAG, "About to get a loader manager");
            getLoaderManager().initLoader(0, null, me);
            //TODO how to update cursor
        }
    }
}
