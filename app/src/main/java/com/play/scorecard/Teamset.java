package com.play.scorecard;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.play.scorecard.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class Teamset extends AppCompatActivity {

    private EditText Team1,Team2;
    Button btn_start;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teamsets);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"\n" +
                        "ca-app-pub-7456254131970827/8808586201", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Teamset.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }

            }
        },4000);

        Team1 =findViewById(R.id.Team1);
        Team2 =findViewById(R.id.Team2);

        btn_start= findViewById(R.id.btn_start);
        getSupportActionBar().hide();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String team1 = Team1.getText().toString().trim();
                String team2 = Team2.getText().toString().trim();

                if(team1.length() == 0 && team2.length() == 0) {
                    Toast.makeText(Teamset.this, "Please enter the team name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(Teamset.this, score.class);
                    intent.putExtra("team1",Team1.getText().toString());
                    intent.putExtra("team2",Team2.getText().toString());
                    startActivity(intent);
                }

            }
        });

    }
}