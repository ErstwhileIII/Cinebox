package com.velocikey.android.learning.cinebox.webinfo.movie;

/**
 * Created by Joseph White on 09-Aug-2015
 *
 * @since 1.0
 */
public class MovieInfo {
    // Class fields
    private static final String LOG_TAG = MovieInfo.class.getSimpleName();
    // Object Fields

    private final int movieId;
    private final String title;
    private final String releaseDate;
    private final float popularity;
    private final float rating;
    private final String posterPath;
    private final String overview;

    public MovieInfo(int movieId, String title, String releaseDate, float popularity, float rating
            , String posterPath, String overview) {
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.rating = rating;
        this.posterPath = posterPath;
        this.overview = overview;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Float getPopularity() {
        return (Math.round(popularity * 10F)) / 10F;
    }

    public Float getRating() {
        return rating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    /**
     * Get the full poster path URL for the size specified.
     *
     * @param size use -1 for "original", other valid values are 45, 92, 154,185,300 and 500.
     *             If an invalid value is specified, a 185 pixel widht size will be used.
     * @return
     */
    public String getFullPosterPath(int size) {
        String sizeName;
        int chosenSize;
        chosenSize = size;
        if (chosenSize < 0) {
            sizeName = "original";
        } else {
            switch (chosenSize) {
                case 45:
                    break;
                case 92:
                    break;
                case 154:
                    break;
                case 185:
                    break;
                case 300:
                    break;
                case 500:
                    break;
                default:
                    chosenSize = 185;
            }
            sizeName = "w" + chosenSize;
        }
        return "https://image.tmdb.org/t/p/" + sizeName + posterPath;
    }

    //TODO add equals and hashcode overrides

}
