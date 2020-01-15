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

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                intent.putExtra(NOTE_ID_KEY, mTaskId);
                startActivityForResult(intent, timeValue);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        timeValue = resultCode;
        mTimeView.setText(calculateTime(timeValue));
    }

    private String calculateTime(Integer time) {
        int minutes = (time / 1000) / 60;
        int seconds = (time / 1000) % 60;

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

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

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        if(!mNewTask) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

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

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        //Get task text and task time
        String textViewData = mTextView.getText().toString();
        Integer timeViewData = timeValue;

        //Check if task time and text are not empty, and if so save
        if(!TextUtils.isEmpty(textViewData)) {
            mViewModel.saveTask(textViewData, timeViewData);
        }

        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
