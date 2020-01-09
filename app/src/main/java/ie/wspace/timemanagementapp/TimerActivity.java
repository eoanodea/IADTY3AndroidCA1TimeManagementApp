package ie.wspace.timemanagementapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private Chronometer mChronometer;
    private long pauseOffset;
    private boolean running;

    private Button mButtonStart;
    private Button mButtonPause;
    private Button mButtonStop;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_timer);
        mChronometer = findViewById(R.id.task_view_timer);
        mButtonStart = findViewById(R.id.timer_start);
        mButtonPause = findViewById(R.id.timer_pause);
        mButtonStop = findViewById(R.id.timer_stop);

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
        mButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });
        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });
    }

    private void startTimer() {
        if(!running) {
            mChronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            mChronometer.start();
            mButtonStart.setVisibility(View.INVISIBLE);
            mButtonPause.setVisibility(View.VISIBLE);
            mButtonStop.setVisibility(View.VISIBLE);
            running = true;
        }
    }

    private void pauseTimer() {
        if(running) {
            mChronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - mChronometer.getBase();
            mButtonStart.setVisibility(View.VISIBLE);
            mButtonPause.setVisibility(View.INVISIBLE);
            mButtonStop.setVisibility(View.INVISIBLE);
            running = false;
        }
    }

    private void stopTimer() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.stop();
        mButtonStart.setVisibility(View.VISIBLE);
        mButtonPause.setVisibility(View.INVISIBLE);
        mButtonStop.setVisibility(View.INVISIBLE);
        pauseOffset = 0;
    }
}
