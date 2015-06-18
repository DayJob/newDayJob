package com.example.jin.materialdesign.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jin.materialdesign.R;
import com.example.jin.materialdesign.models.Task;
import com.example.jin.materialdesign.acctivities.minor.TaskApplyActivity;
import com.example.jin.materialdesign.adapters.MyBidListAdapter;
import com.example.jin.materialdesign.network.VolleySingleton;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jin on 2015-06-09.
 */
public class MyBidListFragment extends Fragment implements MyBidListAdapter.ClickListener{

    private MyBidListFragment myBidListFragment;
    private ArrayList<Task> alist;
    private RecyclerView recyclerView;
    private MyBidListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    public static final String DRAWABLES_PATH = ":drawable/";
    public JSONArray ja;
    private SwipyRefreshLayout swipyRefreshLayout;

    public MyBidListFragment getInstance() {
        if (myBidListFragment == null) {
            myBidListFragment = new MyBidListFragment();
        }
        return myBidListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_my_bid_list, container, false);

        swipyRefreshLayout = (SwipyRefreshLayout) layout.findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                setData();
            }
        });

        recyclerView = (RecyclerView) layout.findViewById(R.id.myBidList);
        adapter = new MyBidListAdapter(getActivity());

        adapter.setClickListener(this);
        setData();

        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        return layout;
    }

    public void setData() {

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        String url = "http://feering.zc.bz/php/selectByApplyUser.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                alist = new ArrayList<>();

                try {
                    ja = new JSONArray(response);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject task = ja.getJSONObject(i);
                        alist.add(new Task(task.getInt("id"), task.getString("pay"), task
                                .getString("description"), task
                                .getString("location"), task.getString("date"), task.getString("time"),
                                task.getString("phone"),
                                task.getString("category"),
                                getResources().getIdentifier(
                                        "com.example.jin.materialdesign" + DRAWABLES_PATH
                                                + task.getString("image_name"),
                                        null, null), Double.valueOf(task
                                .getString("latitude")), Double
                                .valueOf(task.getString("longitude")),task.getString("username"), task.getString("create_at")));
                    }

                    adapter.setTaskList(alist);

                    if (swipyRefreshLayout.isRefreshing()) {
                        swipyRefreshLayout.setRefreshing(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                SharedPreferences pref = getActivity().getSharedPreferences("pref", getActivity().MODE_PRIVATE);
                params.put("username", pref.getString("username", null));
                return params;
            }
        };

        requestQueue.add(request);

    }

    @Override
    public void itemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), TaskApplyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent.putExtra("id", String.valueOf(adapter.getItemData(position).getId()));
        intent.putExtra("pay", String.valueOf(adapter.getItemData(position).getPay()));
        intent.putExtra("description", String.valueOf(adapter.getItemData(position).getDescription()));
        intent.putExtra("date", String.valueOf(adapter.getItemData(position).getDate()));
        intent.putExtra("time", String.valueOf(adapter.getItemData(position).getTime()));
        intent.putExtra("phone", String.valueOf(adapter.getItemData(position).getPhone()));
        intent.putExtra("location", String.valueOf(adapter.getItemData(position).getLocation()));
        intent.putExtra("latitude", String.valueOf(adapter.getItemData(position).getLatitude()));
        intent.putExtra("longitude", String.valueOf(adapter.getItemData(position).getLongitude()));
        intent.putExtra("username", String.valueOf(adapter.getItemData(position).getUsername()));
        intent.putExtra("createTime", String.valueOf(adapter.getItemData(position).getCreateTime()));

        startActivity(intent);

        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
    }
}