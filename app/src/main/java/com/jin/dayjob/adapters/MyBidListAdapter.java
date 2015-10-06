package com.jin.dayjob.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jin.dayjob.acctivities.minor.ShowLocationActivity;
import com.jin.dayjob.network.VolleySingleton;
import com.jin.dayjob.R;
import com.jin.dayjob.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jin on 2015-06-16.
 */
public class MyBidListAdapter extends RecyclerView.Adapter<MyBidListAdapter.TaskListViewHolder>  {
    private final LayoutInflater layout_inflater;
    private ArrayList<Task> listTasks;
    private Context context;
    private ClickListener clickListener;

    public MyBidListAdapter(Context context) {
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

    public void removeAllItem() {
        listTasks.removeAll(listTasks);
        notifyItemRangeChanged(0, listTasks.size());
    }

    public Task getItemData(int position) {
        return listTasks.get(position);
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layout_inflater.inflate(R.layout.my_bid, parent, false);
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
    }

    class TaskListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        ImageView icon;

        public TaskListViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.textView1);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);
            textView3 = (TextView) itemView.findViewById(R.id.textView3);
            textView4 = (TextView) itemView.findViewById(R.id.textView4);
            icon = (ImageView) itemView.findViewById(R.id.imageView1);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            if (clickListener != null) {
//                context.startActivity(new Intent(context, SubActivity.class));
                clickListener.itemClick(v, getPosition());
            }
        }

    }

    public void deleteItemInDB(final int id) {

        String url = "http://feering.zc.bz/php/delete.php";

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MYTAG", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id + "");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(request);

    }
}

