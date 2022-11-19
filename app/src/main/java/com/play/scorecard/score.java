package com.play.scorecard;

import static com.play.scorecard.R.raw.kb;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class score extends Activity{

    public TextView Team1, Team2;
    public Button plus1, plus2, plus3, minus1, plus_1, plus_2, plus_3, minus_1, bonus_red, bounus_blue, allout_red, allout_blue,Set_Time,play, pause, reset, raid_play, raid_reset,raid_pause;

    public int count_red = 0, count_blue = 0;

    private boolean mTimerRunning,raid_running;

    public static final long sec= 30000;
    public MediaPlayer mediaPlayer = null;

    public TextView Time, Match_Time,raid_Time;
    public String time;
    private CountDownTimer mCountDownTimer,raid_count;
    private long mStartTimeInMillis;
    private long mEndTime;
    private long mTimeLeftInMillis;
    private long mTimeLeftInMillis_raid = sec;
    private long mEndTime_raid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        Team1 = (TextView) findViewById(R.id.Team1);
        Team2 = (TextView) findViewById(R.id.Team2);

        Team1.setText(getIntent().getExtras().getString("team1"));
        Team2.setText(getIntent().getExtras().getString("team2"));

        //Timer part

        Time = findViewById(R.id.Time);
        Match_Time = findViewById(R.id.Match_Time);
        Set_Time = findViewById(R.id.Set_Time);
        play = findViewById(R.id.Play);
        pause = findViewById(R.id.Pause);
        reset = findViewById(R.id.Reset);
        raid_play = findViewById(R.id.Raid_Play);
        raid_reset = findViewById(R.id.Raid_Reset);
        raid_pause = findViewById(R.id.Raid_Pause);
        raid_Time = findViewById(R.id.Raid_Time);


        //Red buttton
        plus1 = findViewById(R.id.plus1);
        plus2 = findViewById(R.id.plus2);
        plus3 = findViewById(R.id.plus3);
        minus1 = findViewById(R.id.minus1);
        bonus_red = findViewById(R.id.bonus_red);
        allout_red = findViewById(R.id.allout_red);

        //Blue button

        plus_1 = findViewById(R.id.plus_1);
        plus_2 = findViewById(R.id.plus_2);
        plus_3 = findViewById(R.id.plus_3);
        minus_1 = findViewById(R.id.minus1);
        bounus_blue = findViewById(R.id.bonus_blue);
        allout_blue = findViewById(R.id.allout_blue);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        Set_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 time = Time.getText().toString().trim();
                if (time.length() == 0) {
                    Toast.makeText(score.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(time) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(score.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                Match_Time.setText("");
            }
        });
        

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimerRunning)
                {
                    pause();
                }
                else
                {
                    start();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        updateCountDownText();

        raid_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                mediaPlayer = MediaPlayer.create(score.this,R.raw.kb);
                mediaPlayer.start();
                if(raid_running)
                {
                    pauseTimer();
                }
                else {
                    startTimer();
                }
            }
        });

        raid_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });


    }
    public void stopPlaying(){
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void resetTimer() {
        mTimeLeftInMillis_raid = sec;
        updateCountDownText_raid();
    }

    private void updateCountDownText_raid() {
        int second = (int) (mTimeLeftInMillis_raid / 1000) % 60;
        String timeformed = String.format(Locale.getDefault(),"%02d",second);
        raid_Time.setText(timeformed);
    }

    private void startTimer() {

        raid_count = new CountDownTimer(mTimeLeftInMillis_raid,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis_raid = millisUntilFinished;
                updateCountDownText_raid();
            }

            @Override
            public void onFinish() {
                raid_running = false;
                raid_play.setVisibility(View.VISIBLE);
                raid_reset.setVisibility(View.VISIBLE);
            }
        }.start();
        raid_running = true;
        raid_reset.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        raid_count.cancel();
        raid_running = false;
        raid_reset.setVisibility(View.VISIBLE);
    }

    private void start() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();
        mTimerRunning = true;
        updateWatchInterface();
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            Time.setVisibility(View.VISIBLE);
            Set_Time.setVisibility(View.VISIBLE);
            reset.setVisibility(View.VISIBLE);
            pause.setText("Pause");
        } else {
            Time.setVisibility(View.VISIBLE);
            Set_Time.setVisibility(View.VISIBLE);
            pause.setText("Start");

            if (mTimeLeftInMillis < 1000) {
                play.setVisibility(View.VISIBLE);
            } else {
                pause.setVisibility(View.INVISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                reset.setVisibility(View.VISIBLE);
            } else {
                reset.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        reset();
        close();
    }

    private void close() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                start();
            }
        }
    }

    private void updateCountDownText(){
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        Match_Time.setText(timeLeftFormatted);
    }



    private void reset() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    private void pause() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    //Red functions
    public void Plus1(View v) {
        count_red++;
        red_display(count_red);
    }

    public void Plus2(View v) {
        count_red = count_red + 2;
        red_display(count_red);
    }

    public void Plus3(View v) {
        count_red = count_red + 3;
        red_display(count_red);
    }

    public void Minus1(View v) {
        count_red--;
        red_display(count_red);
    }

    public void red_bonus(View v) {
        count_red++;
        red_display(count_red);
    }

    public void red_allout(View v) {
        count_red = count_red + 2;
        red_display(count_red);
    }

    //Red functoin

    public void Plus_1(View v) {
        count_blue++;
        blue_display(count_blue);
    }

    public void Plus_2(View v) {
        count_blue = count_blue + 2;
        blue_display(count_blue);
    }

    public void Plus_3(View v) {
        count_blue = count_blue + 3;
        blue_display(count_blue);
    }

    public void Minus_1(View v) {
        count_blue--;
        blue_display(count_blue);
    }

    public void blue_bonus(View v) {
        count_blue++;
        blue_display(count_blue);
    }

    public void blue_allout(View v) {
        count_blue = count_blue + 2;
        blue_display(count_blue);
    }

    private void blue_display(int count_blue) {
        TextView ScoreB = (TextView) findViewById(R.id.scoreB);
        ScoreB.setText("" + count_blue);
    }


    private void red_display(int count_red) {

        TextView ScoreA = (TextView) findViewById(R.id.scoreA);
        ScoreA.setText("" + count_red);

    }
}
