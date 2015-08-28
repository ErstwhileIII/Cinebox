package com.velocikey.android.learning.cinebox.webinfo.movie;

import android.net.Uri;
import android.util.Log;

import com.velocikey.android.learning.cinebox.webinfo.WebApi;
import com.velocikey.android.learning.cinebox.webinfo.WebApiKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * The class provides all interaction with The MovieDatabase web api.
 * <p/>
 * Created by Joseph White on 2015 Jul 13.
 */
public class WebApiTMDB extends WebApi {
    // Class fields
    private static final String LOG_TAB = WebApiTMDB.class.getSimpleName();

    // replace api key as needed
    // TODO consider putting apikey into configuration?
    private static final String TMDB_PROTOCOL = "https://";
    private static final String API_KEY_NAME = "api_key";
    private static final String API_KEY_VALUE = WebApiKeys.getTMDBApiKey();
    private static final String API_BASE_URL = TMDB_PROTOCOL + "api.themoviedb.org";
    private static final String API_Version = "3";

    // Useful base TMDB query bases
    private static final String DISCOVER_MOVIES = API_BASE_URL + "/" + API_Version + "/discover/movie?";
    // Images from TMDB .. w185 is 184w, 278h
    private static final String IMAGE_BASE_URL = TMDB_PROTOCOL + "image.tmdb.org/t/p";
    private static final String IMAGE_SIZE = "w185";
    private static final String GET_MOVIE_VIDEOS = API_BASE_URL + "/" + API_Version + "/movie/";

    // Discover movie paramaters
    private static final String PARAM_ORDERBY = "sort_by";
    private static final String PARAM_PAGE = "page";


    // Discover/movie results structure
    private static final String TMDB_PAGE = "page";
    private static final String TMDB_REPORTS = "results";
    private static final String THDB_PAGES = "total_pages";
    private static final String TMDB_RESULTS = "total_results";

    // structure within each result
    private static final String TMDB_ID = "id";
    private static final String TMDB_POSTERPATH = "poster_path";
    private static final String TMDB_TITLE = "title";
    private static final String TMDB_OVERVIEW = "overview";
    private static final String TMDB_RELEASEDATE = "release_date";
    private static final String TMDB_POPULARITY = "popularity";
    private static final String TMDB_VOTEAVERAGE = "vote_average";

    // structure witin videos (/movie/<id>/videos
    private static final String TMDB_VIDEO_MOVIEID = "id";
    private static final String TMDB_VIDEO_RESULTS = "results";
    // id, results[ see below ]
    private static final String TMDB_VIDEO_ID = "id";
    private static final String TMDB_VIDEO_LANGUASGE = "iso_639_1";
    private static final String TMDB_VIDEO_KEY = "key";
    private static final String TMDB_VIDEO_NAME = "name";
    private static final String TMDB_VIDEO_SITE = "site";
    private static final String TMDB_VIDEO_SIZE = "size";
    private static final String TMDB_VIDEO_TYPE = "type";

    // Object fields
    Uri builtUri;

    /**
     * Obtain movie information for the pages specified from The Movie Database (TMDB)
     *
     * @param orderBy     the order to be used when retrieving movie information
     * @param startPage   what is the page to start obtianing information
     * @param pagesWanted how many pages to get
     * @return list of movie information items
     */
    public ArrayList<MovieInfo> getMovieInfo(String orderBy, int startPage, int pagesWanted) {
        ArrayList<MovieInfo> results = new ArrayList<>(20);
        JSONObject movieList;
        JSONArray movies;
        int page;
        int pageNumber;
        int pages;
        int reportCount;
        int resultCount = 0;

        page = startPage;
        URL url;
        boolean error = false;
        // Continue making web api requests until sufficient results are obtained (unless there is an error)
        while ((page < startPage + pagesWanted) && !error) {
            // TMDB pages start at 1, not zero
            Uri movieUri = Uri.parse(DISCOVER_MOVIES).buildUpon()
                    .appendQueryParameter(PARAM_ORDERBY, orderBy)
                    .appendQueryParameter(PARAM_PAGE, "" + page)
                    .appendQueryParameter(API_KEY_NAME, API_KEY_VALUE)
                    .build();
            page++;
            try {
                url = new URL(movieUri.toString());
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
            //TODO keep stats .. add statistics framework to use
//            Log.v(LOG_TAB, "Queary time: " + qDuration);
//            Log.v(LOG_TAB, " raw JSON size is " + rawJson.length());
//            Log.v(LOG_TAB, " initial info |" + rawJson.substring(0,Math.min(rawJson.length(),10)));

            //TODO handle JSON parsing exception
            try {
                movieList = new JSONObject(rawJson);

                movies = movieList.getJSONArray(TMDB_REPORTS);
                //pageNumber = movieList.getInt(TMDB_PAGE);
                //pages = movieList.getInt(THDB_PAGES);
                //reportCount = movieList.getInt(TMDB_RESULTS);

                // Now add info for each movie
                for (int i = 0; i < movies.length(); i++) {
                    JSONObject rawMovieInfo = movies.getJSONObject(i);

                    int id = rawMovieInfo.getInt(TMDB_ID);
                    String title = rawMovieInfo.getString(TMDB_TITLE);
                    String overview = rawMovieInfo.getString(TMDB_OVERVIEW);
                    String posterPath = rawMovieInfo.getString(TMDB_POSTERPATH);
                    String releaseDate = rawMovieInfo.getString(TMDB_RELEASEDATE);
                    float popularity = Double.valueOf(rawMovieInfo.getDouble(TMDB_POPULARITY)).floatValue();
                    float rating = Double.valueOf(rawMovieInfo.getDouble(TMDB_VOTEAVERAGE)).floatValue();
                    results.add(new MovieInfo(id, title, releaseDate, popularity, rating, posterPath, overview));
                }
            } catch (JSONException e) {
                Log.e(LOG_TAB, "JSON parsing error", e);
                error = true;
            }
        }
        return results;
    }

    public ArrayList<MovieVideoInfo> getMovieVideos(int movieId) {
        URL url;
        JSONObject videoList;
        ArrayList<MovieVideoInfo> results = new ArrayList<>(1);

//        Log.v(LOG_TAB, "--> getMovieVideos (" + movieId + ")");
//        Log.v(LOG_TAB, "************");

        // form the TMDB query to get videos for the movie
        Uri getVideosUri = Uri.parse(GET_MOVIE_VIDEOS + movieId + "/videos?").buildUpon()
                .appendQueryParameter(API_KEY_NAME, API_KEY_VALUE)
                .build();


        try {
            url = new URL(getVideosUri.toString());
            Log.v(LOG_TAB, "URL is " + url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAB, "Malformed TMDB URL:", e);
            //TODO handle error condition
            return null;
        }

        // get the video information for the movie JSON information to work on
        long qStart = System.currentTimeMillis();
//        Log.v(LOG_TAB, "about to call method to get JSON information");
        String rawJson = getJson(url, "GET");
        if (rawJson.equalsIgnoreCase("")) {
            Log.e(LOG_TAB, "No output from TMDB");
        }
        long qDuration = System.currentTimeMillis() - qStart;
        //TODO keep stats .. add statistics framework to use
//            Log.v(LOG_TAB, "Queary time: " + qDuration);
//            Log.v(LOG_TAB, " raw JSON size is " + rawJson.length());
//            Log.v(LOG_TAB, " initial info |" + rawJson.substring(0,Math.min(rawJson.length(),10)));

        //TODO handle JSON parsing exception
        try {
            videoList = new JSONObject(rawJson);
            JSONArray videos = videoList.getJSONArray(TMDB_REPORTS);

            // Now add info for each video
            for (int i = 0; i < videos.length(); i++) {
                JSONObject rawMovieInfo = videos.getJSONObject(i);

                String id = rawMovieInfo.getString(TMDB_VIDEO_ID);
                String language = rawMovieInfo.getString(TMDB_VIDEO_LANGUASGE);
                String key = rawMovieInfo.getString(TMDB_VIDEO_KEY);
                String name = rawMovieInfo.getString(TMDB_VIDEO_NAME);
                String site = rawMovieInfo.getString(TMDB_VIDEO_SITE);
                int size = rawMovieInfo.getInt(TMDB_VIDEO_SIZE);
                String type = rawMovieInfo.getString(TMDB_VIDEO_TYPE);
                results.add(new MovieVideoInfo(id, language, key, name, site, size, type));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAB, "JSON parsing error", e);
            return null;
        }
//        Log.v(LOG_TAB, "returning " + results.size());
        return results;
    }
}

