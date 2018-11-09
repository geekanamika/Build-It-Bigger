package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.android.joke_and_lib.JokeActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements EndpointAsyncTask.ExecutionListener {

    private ProgressBar progressBar;
    private InterstitialAd mInterstitialAd;

    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MobileAds.initialize(getContext(), BuildConfig.Ad_app_id);
        initAddMob(view);
        progressBar = view.findViewById(R.id.progressbar);

        Button tellJokeButton = view.findViewById(R.id.tellJokeButton);
        tellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EndpointAsyncTask(MainActivityFragment.this).execute();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getActivity()!=null) {
            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId(BuildConfig.Interstitial_ad_unit_id);
        }

    }

    private void initAddMob(@NonNull View view) {


        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("myTag", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d("myTag", "onAdfailed " + errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d("myTag", "onAdLoaded");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    @Override
    public void changeProgressBarViewStatus(boolean var) {
        if(var){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void startDisplayActivity(String result) {

        initInterestialAddMob(result);
    }

    private void initInterestialAddMob(final String result) {
            mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("myTag", "inter add loaded");
                mInterstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                startJokeDisplayActivity(result);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("myTag", "inter add load failed " + i);
                startJokeDisplayActivity(result);
            }
        });

        AdRequest ar = new AdRequest
                .Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(ar);

    }

    private void startJokeDisplayActivity(String result) {
        Intent intent = new Intent(getActivity(), JokeActivity.class);
        intent.putExtra(getString(R.string.key_joke_pass), result);
        startActivity(intent);
    }


}
