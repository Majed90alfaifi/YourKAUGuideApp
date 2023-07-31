package com.kau.yourkauguideapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.kau.yourkauguideapp.R;

public class SplashActivity extends AppCompatActivity {
    View logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.logo);


        Animation animation = new TranslateAnimation(0, 100, 0, 0);
        animation.setDuration(3500);
        // Apply the animation to the logo image
        logo.startAnimation(animation);
        // Simulate progress or perform any additional tasks
        ProgressBar progressBar = findViewById(R.id.ProgressBar);
        progressBar.setProgress(0);
        simulateLoading(progressBar);
    }

    private void simulateLoading(final ProgressBar progressBar) {
        final int totalProgress = 2500;
        final int interval = 1;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int currentProgress = 0;

            @Override
            public void run() {
                currentProgress += 1;
                progressBar.setProgress(currentProgress);

                if (currentProgress < totalProgress) {
                    handler.postDelayed(this, interval);
                } else {
                    // After the loading is complete, start the next activity
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, interval);
    }
}
