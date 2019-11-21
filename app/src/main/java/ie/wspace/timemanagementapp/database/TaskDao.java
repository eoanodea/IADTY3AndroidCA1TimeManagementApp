package ie.wspace.timemanagementapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao

public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(TaskEntity taskEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TaskEntity> tasks);

    @Delete
    void deleteTask(TaskEntity taskEntity);

    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskEntity getTaskById(int id);

    @Query("SELECT * FROM tasks ORDER BY date DESC")
    LiveData<List<TaskEntity>> getAll();

    @Query("DELETE FROM tasks")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM tasks")
    int getCount();

}
