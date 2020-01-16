package ie.wspace.timemanagementapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ie.wspace.timemanagementapp.database.TaskEntity;
import ie.wspace.timemanagementapp.ui.TasksAdapter;
import ie.wspace.timemanagementapp.viewmodel.MainViewModel;


/*
 * MainActivity
 * Entry point for the application
 * Displays a list of tasks from the DB
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<TaskEntity> tasksData = new ArrayList<>();
    private TasksAdapter mAdapter;
    private MainViewModel mViewModel;

    /*
     * onCreate
     * Initializes the view, and sets the
     * on click listener
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();


        /*
         * Gets the floating action button by ID
         * Sets an onClickListener to add a new task
         * Passes the application context as an intent
         */
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    /*
     * onActivityResult
     * Waits for a response from EditorActivity.
     * Notifies the user with a message
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (resultCode) {
            case 0:
                notifyUser("Task deleted successfully");
                break;
            case 1:
                notifyUser("Task saved successfully");
                break;
            case 2:
                notifyUser("Empty Task was not saved");
                break;
        }
    }

    /*
     * initViewModel
     * Initializes the view model
     * Gets the tasks from the Database
     * and displays them in a list
     */
    private void initViewModel() {
        final Observer<List<TaskEntity>> tasksObserver = new Observer<List<TaskEntity>>() {
            @Override
            public void onChanged(List<TaskEntity> taskEntities) {
                tasksData.clear();
                tasksData.addAll(taskEntities);

                if (mAdapter == null) {
                    mAdapter = new TasksAdapter(tasksData, MainActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        mViewModel = ViewModelProviders.of(this)
            .get(MainViewModel.class);
        mViewModel.mTasks.observe(this, tasksObserver);
    }

    /*
     * initRecyclerView
     * Initializes the RecyclerView
     */
    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /*
     * onCreateOptionsMenu
     * Creates the options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
     * onOptionsItemSelected
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_sample_data) {
            addSampleData();
            notifyUser("Added Sample Tasks");
            return true;
        } else if (id == R.id.action_delete_all) {
            deleteAllTasks();
            notifyUser("All Tasks Deleted");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * notifyUser
     * Notifies the user with a Toast
     * Takes in a String message param
     */
    public void notifyUser(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /*
     * deleteAllTasks
     * Deletes all tasks from the Database
     */
    private void deleteAllTasks() {
        mViewModel.deleteAllTasks();
    }

    /*
     * addSampleData
     * Adds sample tasks to the DB
     */
    private void addSampleData() {
        mViewModel.addSampleData();
    }
}
