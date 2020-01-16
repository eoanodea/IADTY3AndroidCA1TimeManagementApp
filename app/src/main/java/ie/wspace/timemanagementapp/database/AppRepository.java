package ie.wspace.timemanagementapp.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ie.wspace.timemanagementapp.utilities.SampleData;

/*
 * AppRepository
 * Manages all the business logic for the tasks in the database
 */
public class AppRepository {
    private static AppRepository ourInstance;

    public LiveData<List<TaskEntity>> mTasks;
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    /*
     * getInstance
     * Gets an AppRepository instance using context as a param
     */
    public static AppRepository getInstance(Context context) {
        if(ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    /*
     * AppRepository
     * Gets an instance of the AppDatabase and all Tasks
     */
    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mTasks = getAllTasks();
    }

    /*
     * addSampleData
     * Adds the sample data to the database
     */
    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().insertAll(SampleData.getTasks());
            }
        });
    }

    /*
     * LiveData
     * Gets all tasks
     */
    private LiveData<List<TaskEntity>> getAllTasks() {
        return mDb.taskDao().getAll();
    }

    /*
     * deleteAllTasks
     * Deletes all tasks from the database
     */
    public void deleteAllTasks() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().deleteAll();
            }
        });
    }

    /*
     * getTaskById
     * Gets a task by it's ID
     */
    public TaskEntity getTaskById(int taskId) {
        return mDb.taskDao().getTaskById(taskId);
    }

    /*
     * insertTask
     * Inserts a task into the database
     */
    public void insertTask(final TaskEntity task) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().insertTask(task);
            }
        });
    }

    /*
     * deleteTask
     * Deletes a task from the database
     */
    public void deleteTask(final TaskEntity task) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().deleteTask(task);
            }
        });
    }
}
