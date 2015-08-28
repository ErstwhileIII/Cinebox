package com.velocikey.android.learning.cinebox;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.velocikey.android.learning.cinebox.utility.DebugHelper;
import com.velocikey.android.learning.cinebox.webinfo.movie.MovieDetailFragment;
import com.velocikey.android.learning.cinebox.webinfo.movie.MovieInfo;
import com.velocikey.android.learning.cinebox.webinfo.movie.MovieListFragment;

import java.util.Set;


public class MainActivity extends Activity
        implements MovieListFragment.onMovieListFragmentListener
        , MovieDetailFragment.OnMovieDetailFragmentListener {
    // Class fields
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int DISPLAYED_FRAGMENT_MOVIELIST = 0;
    private static final int DISPLAYED_FRAGMENT_MOVIEDETAIL = DISPLAYED_FRAGMENT_MOVIELIST + 1;


    // Object fields
    private FragmentManager fragmentManager;
    private String INSTANCE_CURRENTFRAGMENT_NAME = "CurrentFragment";
    private int mCurrentFragment = -1;
    private boolean isTwoFrame = false;
    private String mMovieOrder = "";

    /**
     * Mainactiviyty controlling movie information
     *
     * @param savedInstanceState information if the status of this Activity was saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "-->onCreate: ");
        setContentView(R.layout.activity_main);
        isTwoFrame = findViewById(R.id.movie_detail_container) != null;
        Log.v(LOG_TAG, "isTwoFrame " + isTwoFrame);

        if (isTwoFrame) {

            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.movie_list_container, new MovieListFragment(), MovieListFragment.TAG_MOVIE_LIST_FRAGMENT)
                        .commit();
            } else {
                Log.v(LOG_TAG, "**** Handle saved state *****");
                // Shouldn't have to do anything here since both fragments are visible
            }
        } else {
            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new MovieListFragment(), MovieListFragment.TAG_MOVIE_LIST_FRAGMENT)
                        .commit();
                mCurrentFragment = DISPLAYED_FRAGMENT_MOVIELIST;
            } else {
                Log.v(LOG_TAG, "**** Handle saved state *****");
                int backCount = getFragmentManager().getBackStackEntryCount();
                Log.v(LOG_TAG, "Back stack entry count = " + backCount);
                for (int i = 0; i < backCount; i++) {
                    FragmentManager.BackStackEntry xxx = getFragmentManager().getBackStackEntryAt(i);
                    Log.v(LOG_TAG, i + ". name-" + xxx.getName());
                }
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-->onRestoreInstanceState");
        Log.v(LOG_TAG, "***** Restore saved state *****");
        if (isTwoFrame) {
            Log.v(LOG_TAG, "Why are we here with two fragments visible?!");
        }
        //TODO optional place to add other information stored (if any)
        mCurrentFragment = savedInstanceState.getInt(INSTANCE_CURRENTFRAGMENT_NAME);
        Log.v(LOG_TAG, "(mCurrentFragment=" + mCurrentFragment + ")");
        MovieListFragment movieListFragment = (MovieListFragment) getFragmentManager()
                .getFragment(savedInstanceState, MovieListFragment.TAG_MOVIE_LIST_FRAGMENT);
        MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getFragmentManager()
                .getFragment(savedInstanceState, MovieDetailFragment.TAG_MOVIE_DETAIL_FRAGMENT);
        Log.v(LOG_TAG, DebugHelper.getMessage("movieListFragment", movieListFragment));
        Log.v(LOG_TAG, DebugHelper.getMessage("movieDetailFragment", movieDetailFragment));

        Set<String> keySet = savedInstanceState.keySet();
        for (String key : keySet) {
            Log.v(LOG_TAG, "Key - " + key + "=" + savedInstanceState.get(key).toString());
        }
        //TODO consider same fragment tag name (not value) for all fragments as a model
        //TODO handle layouts where both fragments are present
        Log.v(LOG_TAG, "isTwoFrame = " + isTwoFrame);

        switch (mCurrentFragment) {
            case DISPLAYED_FRAGMENT_MOVIELIST: {
                if (isTwoFrame) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.movie_list_container, new MovieListFragment(), MovieListFragment.TAG_MOVIE_LIST_FRAGMENT)
                            .commit();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, new MovieDetailFragment(), MovieDetailFragment.TAG_MOVIE_DETAIL_FRAGMENT)
                            .addToBackStack("detail")
                            .commit();
                } else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, new MovieListFragment(), MovieListFragment.TAG_MOVIE_LIST_FRAGMENT)
                            .commit();
                }
                break;
            }
            case DISPLAYED_FRAGMENT_MOVIEDETAIL: {
                if (isTwoFrame) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.movie_list_container, new MovieListFragment(), MovieListFragment.TAG_MOVIE_LIST_FRAGMENT)
                            .commit();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, new MovieDetailFragment(), MovieDetailFragment.TAG_MOVIE_DETAIL_FRAGMENT)
                            .addToBackStack("detail")
                            .commit();
                } else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, new MovieListFragment(), MovieListFragment.TAG_MOVIE_LIST_FRAGMENT)
                            .commit();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_frame, new MovieDetailFragment(), MovieDetailFragment.TAG_MOVIE_DETAIL_FRAGMENT)
                            .addToBackStack("detail")
                            .commit();
                }
                break;
            }
            default:
                Log.e(LOG_TAG, "Unknown fragment state (" + mCurrentFragment + ")");
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new MovieListFragment(), MovieListFragment.TAG_MOVIE_LIST_FRAGMENT)
                        .commit();
                mCurrentFragment = DISPLAYED_FRAGMENT_MOVIELIST;
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "-->onResume");

        MovieListFragment movieListFragment = (MovieListFragment) getFragmentManager().findFragmentByTag(MovieListFragment.TAG_MOVIE_LIST_FRAGMENT);
        if (movieListFragment != null) {
            Log.v(LOG_TAG, "calling getMovies in MovieListFragment");
            movieListFragment.getMovies();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-->onSaveInstanceState (mCurrentFragment=" + mCurrentFragment + ")");
        //TODO handle
        savedInstanceState.putInt(INSTANCE_CURRENTFRAGMENT_NAME, mCurrentFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(LOG_TAG, "-->onCreateOptionsMenu");
        //TODO add menu item for settings
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.v(LOG_TAG, "onOptionsItemSelected with id = " + id);
        //TODO handle settings
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Methods called from fragments

    /**
     * Called when the user selects a particular movie list item.
     *
     * @param movieInfo particular movie inforamtion item selected
     */
    @Override
    public void onMovieListFragmentInteraction(MovieInfo movieInfo) {
        Log.v(LOG_TAG, "onMovieListFragmentInteraction: " + movieInfo.getTitle());
        //TODO replace fragment with detail fragment
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putInt(MovieDetailFragment.ARG_movieId, movieInfo.getMovieId());
        args.putString(MovieDetailFragment.ARG_title, movieInfo.getTitle());
        args.putString(MovieDetailFragment.ARG_releaseDate, movieInfo.getReleaseDate());
        args.putFloat(MovieDetailFragment.ARG_popularity, movieInfo.getPopularity());
        args.putFloat(MovieDetailFragment.ARG_rating, movieInfo.getRating());
        args.putString(MovieDetailFragment.ARG_overview, movieInfo.getOverview());
        args.putString(MovieDetailFragment.ARG_posterPath, movieInfo.getFullPosterPath(300));
        //TODO add posterpath (larger)
        movieDetailFragment.setArguments(args);

        int fragmentId;
        Log.v(LOG_TAG, "is two frame? " + isTwoFrame);
        if (isTwoFrame) {
            fragmentId = R.id.movie_detail_container;
        } else {
            fragmentId = R.id.main_frame;
        }
        getFragmentManager().beginTransaction()
                .replace(fragmentId, movieDetailFragment)
                .addToBackStack("detail")
                .commit();
        mCurrentFragment = DISPLAYED_FRAGMENT_MOVIEDETAIL;
    }

    /**
     * listener for selections in the MovieDetailFragment view presented
     *
     * @param action name of action to handle (for now)
     */
    @Override
    public void onMovieDetailFragmentInteraction(String action) {
        Log.v(LOG_TAG, "-->onMovieDetailFragmentInteraction");
        //TODO adjsut arguments and functions provided
        if ("onDetach".equalsIgnoreCase(action)) {
            mCurrentFragment = DISPLAYED_FRAGMENT_MOVIELIST;
        }

    }
}
