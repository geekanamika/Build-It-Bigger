package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.android.joke_and_lib.JokeActivity;
import com.example.android.joke_provider.JokeProvider;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

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

        Button tellJokeButton = view.findViewById(R.id.tellJokeButton);
        tellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                JokeProvider joke = new JokeProvider();
//                //Toast.makeText(getContext(), joke.getJoke(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), JokeActivity.class);
//                //intent.putExtra(getString(R.string.key_joke), joke.getJoke());
//                startActivity(intent);
                ProgressBar progressBar = view.findViewById(R.id.progressbar);
                new EndpointAsyncTask(getActivity(), progressBar ).execute(new Pair<Context, String>(getActivity(), "Random"));
            }
        });
    }
}
