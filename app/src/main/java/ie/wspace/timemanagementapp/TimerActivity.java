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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.Locale;

import ie.wspace.timemanagementapp.database.TaskEntity;
import ie.wspace.timemanagementapp.viewmodel.EditorViewModel;

import static ie.wspace.timemanagementapp.utilities.Constants.NOTE_ID_KEY;

/*
 * TimerActivity
 * Start, Pause and Stop a timer
 * Takes in either a intent of Task ID,
 * and continues from the current value, or if not
 * assumes it is a new task and starts from 0
 */

public class TimerActivity extends AppCompatActivity {

    private Chronometer mChronometer;
    private long pauseOffset;
    private boolean running;

    private Button mButtonStart;
    private Button mButtonPause;
    private Button mButtonStop;

    private int mDefaultValue, mTaskId;
    private String mTaskText;
    private EditorViewModel mViewModel;

    private Boolean isNew = false;

    /*
     * onCreate
     * Initializes the view, and sets the
     * on click listeners for start, pause and stop timer
     */
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

        initViewModel();
    }

    /*
     * initViewModel
     * Initializes the view model
     * Gets the tasks from the Database
     * If the task doesn't exist start from 0
     */
    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(EditorViewModel.class);

        mViewModel.mLiveTask.observe(this, new Observer<TaskEntity>() {
            @Override
            public void onChanged(TaskEntity taskEntity) {
                if(taskEntity != null) {
                    mDefaultValue = taskEntity.getTime();
                    mTaskText = taskEntity.getText();
                    mChronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset - mDefaultValue);
                }
            }
        });

        mTaskId = getIntent().getIntExtra(NOTE_ID_KEY, 0);
        if(mTaskId != 0) {
            mViewModel.loadData(mTaskId);
        } else {
            isNew = true;
        }
    }

    /*
     * startTimer
     * Starts the timer from either 0 or an existing time
     */
    private void startTimer() {
        if(!running) {
            mChronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset - mDefaultValue);
            mDefaultValue = 0;
            mChronometer.start();
            mButtonStart.setVisibility(View.INVISIBLE);
            mButtonPause.setVisibility(View.VISIBLE);
            running = true;
        }
    }

    /*
     * pauseTimer
     * Pauses the timer
     */
    private void pauseTimer() {
        if(running) {
            mChronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - mChronometer.getBase();
            mButtonStart.setVisibility(View.VISIBLE);
            mButtonPause.setVisibility(View.INVISIBLE);
            running = false;
        }
    }

    /*
     * stopTimer
     * Stops the timer and closes the runs the saveAndReturn function
     */
    private void stopTimer() {
        mDefaultValue = (int) (SystemClock.elapsedRealtime() - mChronometer.getBase());

        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.stop();
        mButtonStart.setVisibility(View.VISIBLE);
        mButtonPause.setVisibility(View.INVISIBLE);
        pauseOffset = 0;

        saveAndReturn();
    }

    /*
     * saveAndReturn
     * If the task is existing, save to the database
     * if not, pass the timeValue back to the EditorActivity
     * And close the activity
     */
    private void saveAndReturn() {
        if(!isNew) {
            mViewModel.saveTask(mTaskText, mDefaultValue);
        }
        setResult(mDefaultValue);
        finish();
    }
}
