package com.velocikey.android.learning.cinebox.webinfo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Joseph White on 16-Aug-2015
 */
public class WebApi {
    // Class fields
    private static final String LOG_TAG = WebApi.class.getSimpleName();
    // Object fields

    /** Return the JSON from the provided web query
     *
     * @param url the full URL for the query
     * @param method (one of GET, PUT, POST, DELETE)
     * @return string containing the full JSON response
     */
    protected String getJson(URL url, String method) {
        //TODO ensure Method is legitimate (GET, PUT, POST, DELETE)
        String results = "";
        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        Log.v(LOG_TAG, "URL: "+url.toString());

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.connect();
            Log.v(LOG_TAG, "About to perform movie info update");
            int responseCode = urlConnection.getResponseCode();
            Log.v(LOG_TAG, "Response code is " + responseCode);

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
            }
            if (buffer.length() > 0) {
                results = buffer.toString();
            }

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Malformed URL", e);
        } catch (ProtocolException e) {
            Log.e(LOG_TAG, "Protocol problem", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IO error:", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing reader:", e);
                }
            }
        }

        return results;
    }
}
