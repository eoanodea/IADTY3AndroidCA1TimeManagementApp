package ie.wspace.timemanagementapp.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ie.wspace.timemanagementapp.utilities.SampleData;

public class AppRepository {
    private static AppRepository ourInstance;

    public LiveData<List<TaskEntity>> mTasks;
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AppRepository getInstance(Context context) {
        if(ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mTasks = getAllTasks();
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().insertAll(SampleData.getTasks());
            }
        });
    }

    private LiveData<List<TaskEntity>> getAllTasks() {
        return mDb.taskDao().getAll();
    }

    public void deleteAllTasks() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().deleteAll();
            }
        });
    }

    public TaskEntity getTaskById(int taskId) {
        return mDb.taskDao().getTaskById(taskId);
    }

    public void insertTask(final TaskEntity task) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().insertTask(task);
            }
        });
    }

    public void deleteTask(final TaskEntity task) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().deleteTask(task);
            }
        });
    }
}
