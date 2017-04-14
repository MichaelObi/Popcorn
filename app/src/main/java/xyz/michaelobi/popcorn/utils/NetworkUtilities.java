package xyz.michaelobi.popcorn.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import xyz.michaelobi.popcorn.BuildConfig;

public class NetworkUtilities {

    final static String PARAM_QUERY = "q";
    private final static String PARAM_API_KEY = "api_key";
    private static final String BASE_API_URL =
            "https://api.themoviedb.org/3/movie/";
    private static final String BASE_IMAGE_URL =
            "http://image.tmdb.org/t/p/";
    private static final String DEFAULT_IMAGE_SIZE =
            "w185";

    public static Boolean isNetworkEnabled(Context c) {
        ConnectivityManager cManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cManager != null) {

            NetworkInfo[] netInfo = cManager.getAllNetworkInfo();

            if (netInfo != null) {
                for (int i = 0; i < netInfo.length; i++) {
                    if (netInfo[i].getState().equals(NetworkInfo.State.CONNECTED)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Builds the URL used to query TMDB.
     *
     * @return The URL to use to query the server.
     */
    public static URL buildUrl(String sortBy) {
        Uri uri = Uri.parse(BASE_API_URL + sortBy).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.TMDB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String buildImageUrl(String posterUrl) {
        return BASE_IMAGE_URL + DEFAULT_IMAGE_SIZE + posterUrl;
    }
}
