package com.velocikey.android.learning.cinebox;

import android.net.Uri;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

/** The class provides all interaction with The MovieDatabase web api.
 *
 * Created by Joseph White on 2015 Jul 13.
 */
public class WebApiTMDB extends WebApi {
    // Class fields
    private static final String LOG_TAB = WebApiTMDB.class.getSimpleName();

    // replace api key as needed
    // TODO consider putting apikey into configuration?
    private static final String TMDB_PROTOCOL = "https://";
    private static final String API_KEY_NAME = "api_key";
    private static final String API_KEY_VALUE = "e914a9254a8c1d706f92d0474b4a7ab5";
    private static final String API_BASE_URL = TMDB_PROTOCOL + "api.themoviedb.org";
    private static final String API_Version = "3";

    // Useful base TMDB query bases
    private static final String DISCOVER_MOVIES = API_BASE_URL + "/" + API_Version + "/discover/movie?";
    // Images from TMDB .. w185 is 184w, 278h
    private static final String IMAGE_BASE_URL = TMDB_PROTOCOL + "image.tmdb.org/t/p";
    private static final String IMAGE_SIZE = "w185";

    // Discover movie paramaters
    private static final String PARAM_ORDERBY = "sort by";
    private static final String PARAM_PAGE = "page";


    // Discover/movie results structure
    private static final String TMDB_PAGE = "page";
    private static final String TMDB_REPORTS = "results";
    private static final String THDB_PAGES = "total_pages";
    private static final String TMDB_RESULTS = "total_results";
    // strucure within each result

    private static final String TMDB_ID = "id";
    private static final String TMDB_POSTERPATH = "poster_path";
    private static final String TMDB_TITLE = "title";
    private static final String TMDB_OVERVIEW = "overview";
    private static final String TMDB_RELEASEDATE = "release_date";
    private static final String TMDB_POPULARITY = "popularity";
    private static final String TMDB_VOTEAVERAGE = "vote_average";

    // Object fields
    Uri builtUri;

    public ArrayList<MovieInfo> getMovieInfo(String orderBy, int resultsWanted) {
        ArrayList<MovieInfo> results = new ArrayList<MovieInfo>(resultsWanted);
        JSONObject movieList;
        JSONArray movies;
        int page;
        int pages;
        int reportCount;
        int resultCount = 0;

        page = 0;
        URL url;
        boolean error = false;
        // Continue making web api requests until sufficient results are obtained (unless there is an error)
        while (results.size() < resultsWanted & !error) {
            // TMDB pages start at 1, not zero
            page = page + 1;
            Uri builtUri = Uri.parse(DISCOVER_MOVIES).buildUpon()
                    .appendQueryParameter(PARAM_ORDERBY, orderBy)
                    .appendQueryParameter(PARAM_PAGE, "" + page)
                    .appendQueryParameter(API_KEY_NAME, API_KEY_VALUE)
                    .build();

            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                Log.e(LOG_TAB, "Malformed TMDB URL:");
                //TODO handle error condition
                error = true;
                break;
            }

            // get the Movie JSON information to work on
            long qStart = System.currentTimeMillis();
            String rawJson = getJson(url, "GET");
            if (rawJson.equalsIgnoreCase("")) {
                Log.e(LOG_TAB, "No output from TMDB");
            }
            long qDuration = System.currentTimeMillis() - qStart;
            Log.v(LOG_TAB, "Queary time: " + qDuration);
//            Log.v(LOG_TAB, " raw JSON size is " + rawJson.length());
//            Log.v(LOG_TAB, " initial info |" + rawJson.substring(0,Math.min(rawJson.length(),10)));

            //TODO handle JSON parsing exception
            try {
                movieList = new JSONObject(rawJson);

                movies = movieList.getJSONArray(TMDB_REPORTS);
                page = movieList.getInt(TMDB_PAGE);
                pages = movieList.getInt(THDB_PAGES);
                reportCount = movieList.getInt(TMDB_RESULTS);

                // Now add info for each movie
                for (int i = 0; i < movies.length(); i++) {
                    JSONObject rawMovieInfo = movies.getJSONObject(i);

                    int id = rawMovieInfo.getInt(TMDB_ID);
                    String title = rawMovieInfo.getString(TMDB_TITLE);
                    String overview = rawMovieInfo.getString(TMDB_OVERVIEW);
                    String posterPath = rawMovieInfo.getString(TMDB_POSTERPATH);
                    String releaseDate = rawMovieInfo.getString(TMDB_RELEASEDATE);
                    float popularity = new Double(rawMovieInfo.getDouble(TMDB_POPULARITY)).floatValue();
                    float rating = new Double(rawMovieInfo.getDouble(TMDB_VOTEAVERAGE)).floatValue();
                    results.add(new MovieInfo(id, title, releaseDate, popularity, rating, posterPath, overview));
                }
            } catch (JSONException e) {
                Log.e(LOG_TAB, "JSON parsing error", e);
                error = true;
            }
        }
        Log.v(LOG_TAB, "Number of queries is " + page);
        return results;
    }

    public static String getImageURL(String imagePath, int size) {
        String result;
        //TODO handle small and large image results
        result = IMAGE_BASE_URL + "/" + IMAGE_SIZE + imagePath;
        return result;
    }
}

