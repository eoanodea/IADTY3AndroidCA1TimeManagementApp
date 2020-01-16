package ie.wspace.timemanagementapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import ie.wspace.timemanagementapp.database.TaskEntity;
import ie.wspace.timemanagementapp.utilities.Constants;
import ie.wspace.timemanagementapp.viewmodel.EditorViewModel;

import static ie.wspace.timemanagementapp.utilities.Constants.EDITING_KEY;
import static ie.wspace.timemanagementapp.utilities.Constants.NOTE_ID_KEY;

/*
 * EditorActivity
 * Add / Edit a task in the Database
 */

public class EditorActivity extends AppCompatActivity {

    @BindView(R.id.task_text)
    TextView mTextView;

    @Nullable
    @BindView(R.id.task_time)
    TextView mTimeView;

    @BindView(R.id.open_timer)
    Button mButton;

    private int mTaskId;
    private int timeValue;
    private EditorViewModel mViewModel;
    private boolean mNewTask, mEditing;

    /*
     * onCreate
     * Initializes the view, and sets the
     * on click listeners for save, delete, and start timer
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();

        /*
         * Listens for a click on the start Timer button
         * On click, starts the TimerActivity and passes the TaskID
         * as an intent
         * startActivityForResult function waits for a response from this activity
         */
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                intent.putExtra(NOTE_ID_KEY, mTaskId);
                startActivityForResult(intent, timeValue);
            }
        });

    }

    /*
     * onActivityResult
     * Waits for a response from TimerActivity.
     * TimerActivity returns the timeValue from the counter in miliseconds
     * Replaces the old timeValue with the new returned value
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        timeValue = resultCode;
        mTimeView.setText(calculateTime(timeValue));
    }

    /*
     * calculateTime
     * Takes time as an integer in milliseconds
     * Returns a string of a formatted time for the user
     */
    private String calculateTime(Integer time) {
        int minutes = (time / 1000) / 60;
        int seconds = (time / 1000) % 60;

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    /*
     * initViewModel
     * Initializes the view model
     * Gets the tasks from the Database
     */
    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(EditorViewModel.class);

        mViewModel.mLiveTask.observe(this, new Observer<TaskEntity>() {
            @Override
            public void onChanged(TaskEntity taskEntity) {
                if(taskEntity != null && !mEditing) {
                    mTextView.setText(taskEntity.getText());

                    timeValue = taskEntity.getTime();
                    mTimeView.setText(calculateTime(timeValue));

                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            setTitle(R.string.new_task);
            mNewTask = true;
        } else {
            setTitle(R.string.edit_task);
            int taskId = extras.getInt(NOTE_ID_KEY);
            mViewModel.loadData(taskId);

            mTaskId = taskId;
        }
    }

    /*
     * onCreateOptionsMenu
     * Creates the options menu
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        if(!mNewTask) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * onOptionsItemSelected
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            mViewModel.deleteTask();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * onBackPressed
     * Handles the back button
     */
    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    /*
     * saveAndReturn
     * Get task text and task time and
     * saves it to the database
     * Closes the Activity
     */
    private void saveAndReturn() {
        String textViewData = mTextView.getText().toString();
        Integer timeViewData = timeValue;

        mViewModel.saveTask(textViewData, timeViewData);

        finish();
    }

    /*
     * onSaveInstanceState
     * Saves state information to an instance state bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
