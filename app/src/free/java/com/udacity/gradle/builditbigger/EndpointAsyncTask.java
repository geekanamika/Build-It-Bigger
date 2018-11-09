package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
    private ExecutionListener executionListener;
    private final Context context;

    EndpointAsyncTask(Context context, ExecutionListener listener) {
        this.executionListener = listener;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        if (myApiService == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl(Constant.ROOT_URL)
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
            Log.d("myTag", e.getMessage());
            return "";
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        EspressoIdlingResource.increment();
        executionListener.changeProgressBarViewStatus(true);
    }

    @Override
    protected void onPostExecute(final String result) {
        super.onPostExecute(result);
        // Setting the interstitial ad
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(BuildConfig.Interstitial_ad_unit_id);
        // now that the data has been loaded, we can mark the app as idle
        // first, make sure the app is still marked as busy then decrement, there might be cases
        // when other components finished their asynchronous tasks and marked the app as idle
        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
            EspressoIdlingResource.decrement(); // Set app as idle.
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("myTag", "inter add loaded");
                executionListener.changeProgressBarViewStatus(false);
                mInterstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                executionListener.startDisplayActivity(result);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("myTag", "inter add load failed " + i);
                executionListener.changeProgressBarViewStatus(false);
                executionListener.startDisplayActivity(result);
            }
        });

        AdRequest ar = new AdRequest
                .Builder()
                .build();
        mInterstitialAd.loadAd(ar);
    }

    interface ExecutionListener {
        void changeProgressBarViewStatus(boolean var);
        void startDisplayActivity(String result);
    }

}
