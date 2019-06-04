package com.example.mvvm.data.network.retrofit;

import android.content.Context;

import com.example.mvvm.data.Constants;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient INSTANCE;

    private Retrofit client;
    private MoviesService moviesService;

    private RetrofitClient(Context context) {
        client = createRetrofitClient(context);
        moviesService = client.create(MoviesService.class);
    }

    private Retrofit createRetrofitClient(Context context) {
        return new Retrofit.Builder()
                .baseUrl(Constants.TMDB_API_V3_BASE_URL)
                .client(createHTTPClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient createHTTPClient(Context context) {
        return new OkHttpClient
                .Builder()
                .addInterceptor(createAPIKeyInterceptor())
                .cache(new Cache(context.getCacheDir(), (100 * 1024 * 1024))) // 100 MB cache
                .build();
    }

    private Interceptor createAPIKeyInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request
                        .url()
                        .newBuilder()
                        .addQueryParameter("api_key",Constants.TMDB_API_V3_AUTH)
                        .build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };
    }

    public MoviesService getMoviesService(){
        return moviesService;
    }

    public static synchronized RetrofitClient getInstance(Context context){

        if(INSTANCE == null){
            INSTANCE = new RetrofitClient(context);
        }

        return INSTANCE;
    }
}
