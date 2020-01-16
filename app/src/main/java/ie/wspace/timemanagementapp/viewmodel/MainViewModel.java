package ie.wspace.timemanagementapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ie.wspace.timemanagementapp.database.AppRepository;
import ie.wspace.timemanagementapp.database.TaskEntity;

public class MainViewModel extends AndroidViewModel {

    public LiveData<List<TaskEntity>> mTasks;
    private AppRepository mRepository;

    /*
     * MainViewModel
     * Gets tasks from the AppRepository
     */
    public MainViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(application.getApplicationContext());
        mTasks = mRepository.mTasks;
    }

    /*
     * addSampleData
     * Runs the addSampleData function in the App Repository
     */
    public void addSampleData() {
        mRepository.addSampleData();
    }

    /*
     * deleteAllTasks
     * Runs the deleteAllTasks function in the App Repository
     */
    public void deleteAllTasks() {
        mRepository.deleteAllTasks();
    }
}
