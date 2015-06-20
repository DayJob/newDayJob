package com.example.jin.materialdesign.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jin.materialdesign.R;
import com.example.jin.materialdesign.acctivities.minor.ShowLocationActivity;
import com.example.jin.materialdesign.models.Task;
import com.example.jin.materialdesign.network.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jin on 2015-06-06.
 */
public class MyTaskListAdapter extends RecyclerView.Adapter<MyTaskListAdapter.TaskListViewHolder> {

    private final LayoutInflater layout_inflater;
    private ArrayList<Task> listTasks;
    private Context context;
    private ClickListener clickListener;

    public MyTaskListAdapter(Context context) {
        this.layout_inflater = LayoutInflater.from(context);
        this.context = context;
        this.listTasks = new ArrayList<>();
    }

    public void setTaskList(ArrayList<Task> listTasks) {
        this.listTasks = listTasks;
        notifyItemRangeChanged(0, listTasks.size());
    }

    public void addItem(Task item) {
        listTasks.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        listTasks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, listTasks.size());
    }

    public Task getItemData(int position) {
        return listTasks.get(position);
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layout_inflater.inflate(R.layout.task, parent, false);
        TaskListViewHolder holder = new TaskListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(TaskListViewHolder holder, final int position) {
        Task currentTask = listTasks.get(position);
        holder.textView1.setText(currentTask.getDescription());
        holder.textView2.setText(currentTask.getPay());
        holder.textView3.setText(currentTask.getLocation());
        holder.textView4.setText(currentTask.getPhone());
        holder.icon.setImageResource(currentTask.getIcon());
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowLocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                intent.putExtra("lat", Double.valueOf(listTasks.get(position).getLatitude()));
                intent.putExtra("lng", Double.valueOf(listTasks.get(position).getLongitude()));

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {

        public void itemClick(View view, int position);

        public void itemRemove(int position);
    }

    class TaskListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        ImageView icon;
        ImageButton deleteBtn;

        public TaskListViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.textView1);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);
            textView3 = (TextView) itemView.findViewById(R.id.textView3);
            textView4 = (TextView) itemView.findViewById(R.id.textView4);
            icon = (ImageView) itemView.findViewById(R.id.imageView1);
            deleteBtn = (ImageButton) itemView.findViewById(R.id.deleteBtn);

            itemView.setOnClickListener(this);
            deleteBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.deleteBtn:
                   if (clickListener != null) {
                       clickListener.itemRemove(getPosition());
                   }
                   break;
               default:
                   if (clickListener != null) {
                       clickListener.itemClick(v, getPosition());
                   }
                   break;
            }

        }
    }
}
