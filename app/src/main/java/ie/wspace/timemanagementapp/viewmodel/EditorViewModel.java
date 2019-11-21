package ie.wspace.timemanagementapp.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ie.wspace.timemanagementapp.database.AppRepository;
import ie.wspace.timemanagementapp.database.TaskEntity;

public class EditorViewModel extends AndroidViewModel {

    public MutableLiveData<TaskEntity> mLiveTask = new MutableLiveData<>();
    private AppRepository mRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public EditorViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(getApplication());
    }

    public void loadData(final int taskId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TaskEntity task = mRepository.getTaskById(taskId);
                mLiveTask.postValue(task);
            }
        });
    }

    public void saveTask(String taskText) {
        TaskEntity task = mLiveTask.getValue();

        if(task == null) {
            if(TextUtils.isEmpty(taskText.trim())) {
                return;
            }
            task = new TaskEntity(new Date(), taskText.trim());
        } else {
            task.setText(taskText.trim());
        }
        mRepository.insertTask(task);
    }

    public void deleteTask() {
        mRepository.deleteTask(mLiveTask.getValue());
    }
}
