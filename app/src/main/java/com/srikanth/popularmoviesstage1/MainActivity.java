package com.srikanth.popularmoviesstage1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import adapter.ImageAdapter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.MovieResponse;
import retrofit.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private GridView mGridView;
    public static final String URL = "https://api.themoviedb.org/3/";
    private String mSort;
    private BroadcastReceiver broadcastReceiver;

    /**
     * Don't declare the receiver in the Manifest for app targeting API 26 & above
     * for listening to Network Connectivity Changes
     * the receiver will not be called
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = findViewById(R.id.img_grid_view);
        constraintLayout = findViewById(R.id.snackbarView);
        mSort = "now_playing";
        checkInternetConnection();
    }

    private void checkInternetConnection() {
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        NetworkInfo networkInfo = extras.getParcelable("networkInfo");
                        if (networkInfo != null) {
                            NetworkInfo.State state = networkInfo.getState();
                            if (state == NetworkInfo.State.CONNECTED) {
                                getPosterPath(mSort);
                                Log.d("connected internet", "connected internaet");
                                Snackbar.make(constraintLayout, "Internet is connected", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(constraintLayout, "No Connection", Snackbar.LENGTH_SHORT).show();
                                Log.d("no internet", "no internet");
                            }
                        }
                    }

                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(broadcastReceiver, intentFilter);
        }
    }


    private void getPosterPath(String sort) {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        final MovieInterface movieInterface = retrofit.create(MovieInterface.class);
        final Call<MovieResponse> movieResponseCall = movieInterface.getNowPlaying(sort, "");
        movieResponseCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    List<Result> movieResponses = response.body().getResults();
                    Log.d("Response", "" + response.body());
                    Log.d("Total number of movies", "" + movieResponses.size());
                    mGridView.setAdapter(new ImageAdapter(movieResponses, MainActivity.this));
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.topRated:
                Toast.makeText(MainActivity.this, "Top Rated", Toast.LENGTH_LONG).show();
                mSort = "top_rated";
                getPosterPath(mSort);
                break;
            case R.id.mostPopular:
                Toast.makeText(MainActivity.this, "Most Popular", Toast.LENGTH_LONG).show();
                mSort = "popular";
                getPosterPath(mSort);
                break;
        }
        return true;
    }
}
