package com.velocikey.android.learning.cinebox;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.velocikey.android.learning.cinebox.webinfo.movie.MovieDetailFragment;
import com.velocikey.android.learning.cinebox.webinfo.movie.MovieInfo;
import com.velocikey.android.learning.cinebox.webinfo.movie.MovieListFragment;


public class MainActivity extends Activity
        implements MovieListFragment.onMovieListFragmentListener
                 , MovieDetailFragment.OnMovieDetailFragmentListener{
    // Class fields
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int DISPLAYED_FRAGMENT_MOVIELIST = 0;
    private static final int DISPLAYED_FRAGMENT_MOVIEDETAIL = DISPLAYED_FRAGMENT_MOVIELIST + 1;


    // Object fields
    private FragmentManager fragmentManager;
    private String INSTANCE_CURRENTFRAGMENT_NAME = "CurrentFragment";
    private int mCurrentFragment = -1;

    /** Mainactiviyty controlling movie information
     *
     * @param savedInstanceState information if the status of this Activity was saved.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fragment currentFragment;
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "-->onCreate: ");

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            Log.v(LOG_TAG, "savedInstanceState info present");
            Log.v(LOG_TAG, savedInstanceState.toString());
            mCurrentFragment = savedInstanceState.getInt(INSTANCE_CURRENTFRAGMENT_NAME);
        } else {
            mCurrentFragment = DISPLAYED_FRAGMENT_MOVIELIST;
        }
        switch (mCurrentFragment) {
            case DISPLAYED_FRAGMENT_MOVIELIST:
                currentFragment = new MovieListFragment();
                break;
            case DISPLAYED_FRAGMENT_MOVIEDETAIL:
                currentFragment = new MovieDetailFragment();
                break;
            default:
                Log.e(LOG_TAG, "Should not get here");
                currentFragment = new MovieListFragment();
                mCurrentFragment = DISPLAYED_FRAGMENT_MOVIELIST;
        }
        setContentView(R.layout.activity_main);
        // load the main movie list fragment
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_frame, currentFragment)
                .addToBackStack("Movies")
                .commit();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onRestoreInstanceState");
        int fragmentDesignation = savedInstanceState.getInt(INSTANCE_CURRENTFRAGMENT_NAME);
        attachFragment(fragmentDesignation);
    }

    private void attachFragment(int fragmentDesignation) {
        Fragment fragment;
        switch (fragmentDesignation) {
            case DISPLAYED_FRAGMENT_MOVIELIST: {
                fragment = new MovieListFragment();
                break;
            }
            case DISPLAYED_FRAGMENT_MOVIEDETAIL: {
                fragment = new MovieDetailFragment();
                break;
            }
            default:
                fragment = new MovieListFragment();

        }
        getFragmentManager().beginTransaction()
                .add(R.id.main_frame, fragment)
                .addToBackStack("Movies")
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onSaveInstanceState");
        //TODO handle
        savedInstanceState.putInt(INSTANCE_CURRENTFRAGMENT_NAME, mCurrentFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(LOG_TAG, "onCreateOptionsMenu");
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

    /** Called when the user selects a particular movie list item.
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

        fragmentManager.beginTransaction()
                .replace(R.id.main_frame,movieDetailFragment)
                .addToBackStack("detail")
                .commit();
        mCurrentFragment = DISPLAYED_FRAGMENT_MOVIEDETAIL;
    }

    /**
     * listener for selections in the MovieDetailFragment view spresented
     *
     * @param uri dummy or now
     */
    @Override
    public void onMovieDetailFragmentInteraction(Uri uri) {
        //TODO adjsut arguments and functions provided

    }
}
