package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.joke_and_lib.JokeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

/**
 * Created by Anamika Tripathi on 6/11/18.
 */
class EndpointAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static MyApi mJokeApi = null;
    private ProgressBar mProgressBar;
    private Context context;

    public EndpointAsyncTask(Context context,ProgressBar mProgressBar) {
        this.mProgressBar = mProgressBar;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mProgressBar!=null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(mProgressBar!=null) {
            mProgressBar.setVisibility(View.GONE);
        }
        Log.d("myTag","joke" + s);
        startJokeDisplayActivity(s);
    }

    private void startJokeDisplayActivity(String result) {
        Intent intent = new Intent(context, JokeActivity.class);
        intent.putExtra(context.getString(R.string.key_joke_pass), result);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected String doInBackground(Pair<Context, String>... pairs) {
        if(mJokeApi == null) {
            MyApi.Builder builder = new MyApi.Builder(
                    AndroidHttp.newCompatibleTransport()
                    , new AndroidJsonFactory(), null
            ).setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(
                            new GoogleClientRequestInitializer() {
                                @Override
                                public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                                    request.setDisableGZipContent(true);
                                }
                            }
                    );
            mJokeApi = builder.build();
        }
        String name = pairs[0].second;

        try {
            return mJokeApi.sayHi(name).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

}