package xyz.michaelobi.popcorn.data.remote;


import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.michaelobi.popcorn.BuildConfig;
import xyz.michaelobi.popcorn.utils.NetworkUtilities;

/**
 * Popcorn
 * Michael Obi
 * 12 05 2017 6:25 PM
 */

public class Client {

    private static okhttp3.OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        builder.addInterceptor(logging);
        builder.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();
            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                    .build();
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        return builder.build();
    }

    public static MovieDbService getApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .baseUrl(NetworkUtilities.BASE_API_URL)
                .build();
        return retrofit.create(MovieDbService.class);
    }
}