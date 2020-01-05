package ie.wspace.timemanagementapp;

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
import android.widget.TextView;

import java.text.DecimalFormat;

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
    //int

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
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(EditorViewModel.class);

        mViewModel.mLiveTask.observe(this, new Observer<TaskEntity>() {
            @Override
            public void onChanged(TaskEntity taskEntity) {
                if(taskEntity != null && !mEditing) {
                    mTextView.setText(taskEntity.getText());
                    if (mTimeView != null) {
                        mTimeView.setText(Double.toString(taskEntity.getTime()));
                    }
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
        String timeViewData = mTimeView.getText().toString();

        //Check if task time and text are not empty, and if so save
        if(!TextUtils.isEmpty(textViewData) && !TextUtils.isEmpty(timeViewData)) {
            mViewModel.saveTask(textViewData, Double.parseDouble(timeViewData));
        }

        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
