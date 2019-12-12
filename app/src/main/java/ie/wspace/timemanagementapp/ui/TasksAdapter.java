package ie.wspace.timemanagementapp.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TaskEntity task = mTasks.get(position);
        holder.mTextView.setText(task.getText());
        if (holder.mTimeView != null) {
            holder.mTimeView.setText(Double.toString(task.getTime()));
        }

        holder.mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditorActivity.class);
                intent.putExtra(NOTE_ID_KEY, task.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

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
