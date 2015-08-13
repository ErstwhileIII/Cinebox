package com.velocikey.android.learning.cinebox;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
        implements MovieListFragment.onMovieListFragmentListener
                 , MovieDetailFragment.OnMovieDetailFragmentListener{
    // Class fields
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // Object fields
    private FragmentManager fragmentManager;
    private MovieListFragment movieListFragment;

    /** Mainactiviyty controlling movie information
     *
     * @param savedInstanceState information if the status of this Activity was saved.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate: establish content for main view (" + R.layout.activity_main);
        setContentView(R.layout.activity_main);
        // load the main movie list fragment
        fragmentManager = getFragmentManager();
        movieListFragment = new MovieListFragment();

        fragmentManager.beginTransaction()
                .add(R.id.main_frame, movieListFragment)
                .commit();
        Log.v(LOG_TAG, "onCreate: movie list fragment attached");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    }

    @Override
    public void onMovieDetailFragmentInteraction(Uri uri) {

    }

}
