package ie.wspace.timemanagementapp.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "tasks")

public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date;
    private String text;
    private int time;

    @Ignore
    public TaskEntity() {
    }

    public TaskEntity(int id, Date date, String text, int time) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.time = time;
    }

    @Ignore
    public TaskEntity(Date date, String text, int time) {
        this.date = date;
        this.text = text;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) { this.time = time; }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text +
                ", time=" + time + '\'' +
                '}';
    }
}
