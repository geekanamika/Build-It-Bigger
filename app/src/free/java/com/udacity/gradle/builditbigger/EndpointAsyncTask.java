package com.udacity.gradle.builditbigger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.joke_and_lib.JokeActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

/**
 * Created by Anamika Tripathi on 6/11/18.
 */
public class EndpointAsyncTask extends AsyncTask<Void, Void, String> {
    private static MyApi myApiService = null;
    private InterstitialAd mInterstitialAd;
    private String result;
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private ProgressBarUpdate progressBarUpdateListener;

    EndpointAsyncTask(Context context, ProgressBarUpdate listener) {
        this.progressBarUpdateListener = listener;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        if (myApiService == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl(context.getString(R.string.root_url_api))
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            myApiService = builder.build();
        }

        try {
            Log.d("myTag", "get data from joke library");
            return myApiService.getJokeFromBackend().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBarUpdateListener.changeProgressBarViewStatus(true);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        this.result = result;
        Log.d("myTag", result);
        // Setting the interstitial ad
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("myTag", "inter add loaded");
                progressBarUpdateListener.changeProgressBarViewStatus(false);
                mInterstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                startJokeDisplayActivity();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("myTag", "inter add load failed " + i);
                progressBarUpdateListener.changeProgressBarViewStatus(false);
                startJokeDisplayActivity();
            }
        });

        AdRequest ar = new AdRequest
                .Builder()
                .build();
        mInterstitialAd.loadAd(ar);
    }

    private void startJokeDisplayActivity() {
        Intent intent = new Intent(context, JokeActivity.class);
        intent.putExtra(context.getString(R.string.key_joke_pass), result);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    interface ProgressBarUpdate {
        void changeProgressBarViewStatus(boolean var);
    }

}
