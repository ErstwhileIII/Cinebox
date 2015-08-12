package com.velocikey.android.learning.cinebox;

import java.util.ArrayList;

/**
 * Created by Joseph White on 05-Aug-2015
 *
 * @since 1.0
 */
class DebugMovieInfo {
    // Class fields
    private static final String LOG_TAG = DebugMovieInfo.class.getSimpleName();

    // Object fields

    /** Use this class for debugging or testing only.
     *
     * @return list of movies from static contents
     */
    public static ArrayList<MovieInfo> getMovieInfoList() {
        ArrayList<MovieInfo> debugMovieInfoList = new ArrayList<MovieInfo>();
        debugMovieInfoList.add(new MovieInfo(194662,"Birdman","2014-10-17",17.367626F,7.4F,"/rSZs93P0LLxqlVEbI001UKoeCQC.jpg",""));
        debugMovieInfoList.add(new MovieInfo(102899,"Ant-Man","2015-07-17",62.21761F,7.1F,"/7SGGUiTE6oc2fh9MjIk5M00dsQd.jpg",""));
        debugMovieInfoList.add(new MovieInfo(168259,"Furious 7","2015-04-03",20.920847F,7.6F,"/dCgm7efXDmiABSdWDHBDBx2jwmn.jpg",""));
        debugMovieInfoList.add(new MovieInfo(194662,"Birdman","2014-10-17",17.367626F,7.4F,"/rSZs93P0LLxqlVEbI001UKoeCQC.jpg",""));
        debugMovieInfoList.add(new MovieInfo(194662,"Birdman","2014-10-17",17.367626F,7.4F,"/rSZs93P0LLxqlVEbI001UKoeCQC.jpg",""));
        debugMovieInfoList.add(new MovieInfo(194662,"Birdman","2014-10-17",17.367626F,7.4F,"/rSZs93P0LLxqlVEbI001UKoeCQC.jpg",""));

        return debugMovieInfoList;
    }
}
