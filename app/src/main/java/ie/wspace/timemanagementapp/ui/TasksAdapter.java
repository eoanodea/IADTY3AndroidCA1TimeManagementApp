package ie.wspace.timemanagementapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ie.wspace.timemanagementapp.EditorActivity;
import ie.wspace.timemanagementapp.R;
import ie.wspace.timemanagementapp.database.TaskEntity;

import static ie.wspace.timemanagementapp.utilities.Constants.NOTE_ID_KEY;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private final List<TaskEntity> mTasks;
    private final Context mContext;

    public TasksAdapter(List<TaskEntity> mTasks, Context mContext) {
        this.mTasks = mTasks;
        this.mContext = mContext;
    }

    /*
     * onCreateViewHolder
     * Called when the RecyclerView needs a new ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_list_item, parent, false);
        return new ViewHolder(view);
    }

    /*
     * onBindViewHolder
     * Called by RecyclerView to display data at the specified position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TaskEntity task = mTasks.get(position);
        holder.mTextView.setText(task.getText());
        holder.mTimeView.setText(calculateTime(task.getTime()));


        holder.mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditorActivity.class);
                intent.putExtra(NOTE_ID_KEY, task.getId());
                if(mContext instanceof Activity) {
                    ((Activity) mContext).startActivityForResult(intent, 0);
                }
            }
        });
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
     * getItemCount
     * Counts the number of tasks in the Database
     */
    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    /*
     * ViewHolder
     * Gets the task text, time and fab and assigns
     * values to it
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.task_text)
        TextView mTextView;
        @Nullable
        @BindView(R.id.task_time)
        TextView mTimeView;
        @BindView(R.id.fab)
        FloatingActionButton mFab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
