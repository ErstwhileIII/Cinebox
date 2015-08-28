package com.velocikey.android.learning.cinebox.data;

import android.test.AndroidTestCase;
import android.util.Log;

import com.velocikey.android.learning.cinebox.webinfo.movie.MovieVideoInfo;
import com.velocikey.android.learning.cinebox.webinfo.movie.WebApiTMDB;

import java.util.ArrayList;

/**
 * Created by Joseph White on 27-Aug-2015
 */
public class TestMovieVideoInfo extends AndroidTestCase {
    // Class fields
    private static final String LOG_TAG = TestMovieVideoInfo.class.getSimpleName();
    // Object Fields
    private static int movieId = 76341;
    private ArrayList<MovieVideoInfo> expectedVideo = new ArrayList<>(1);

    public void testMovieVideoInfo() {
        WebApiTMDB tmdb = new WebApiTMDB();
        expectedVideo = getExpectedVideo();

        ArrayList<MovieVideoInfo> results = tmdb.getMovieVideos(movieId);

        assertEquals(results.size(), expectedVideo.size());
        for (int i = 0; i < expectedVideo.size(); i++) {
            assertEquals(results.get(i).getId(), expectedVideo.get(i).getId());
            assertEquals(results.get(i).getLanguage(), expectedVideo.get(i).getLanguage());
            assertEquals(results.get(i).getKey(), expectedVideo.get(i).getKey());
            assertEquals(results.get(i).getName(), expectedVideo.get(i).getName());
            assertEquals(results.get(i).getSite(), expectedVideo.get(i).getSite());
            assertEquals(results.get(i).getSize(), expectedVideo.get(i).getSize());
            assertEquals(results.get(i).getType(), expectedVideo.get(i).getType());
            assertEquals(results.get(i).getYouTubeUri(), expectedVideo.get(i).getYouTubeUri());
            Log.v(LOG_TAG, "try: " + results.get(i).getYouTubeUri());
            //TODO add get for youtube url
        }
    }

    private ArrayList<MovieVideoInfo> getExpectedVideo() {

        ArrayList<MovieVideoInfo> expectedVideo = new ArrayList<>(3);
        expectedVideo.add(new MovieVideoInfo(
                "559198cac3a3685710000b58",
                "en",
                "FRDdRto_3SA",
                "Trailers From Hell",
                "YouTube",
                1080,
                "Featurette"));
        expectedVideo.add(new MovieVideoInfo(
                "551afc679251417fd70002b1",
                "en",
                "jnsgdqppAYA",
                "Trailer 2",
                "YouTube",
                720,
                "Trailer"));
        expectedVideo.add(new MovieVideoInfo(
                "548ce4e292514122ed002d99",
                "en",
                "YWNWi-ZWL3c",
                "Official Trailer #1",
                "YouTube",
                1080,
                "Trailer"));

        return expectedVideo;
    }
}
