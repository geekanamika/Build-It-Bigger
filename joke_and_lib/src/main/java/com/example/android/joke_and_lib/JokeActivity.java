package com.example.android.joke_and_lib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    TextView tv_joke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tv_joke = findViewById(R.id.tv_joke);
        extractDataFromBundle();
    }

    private void extractDataFromBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                // set values on text and images
                String joke = data.getString(getString(R.string.key_joke_pass));
                tv_joke.setText(joke);
            }

        }
    }
}
