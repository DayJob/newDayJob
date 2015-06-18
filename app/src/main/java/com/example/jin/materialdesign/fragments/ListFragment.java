package com.example.jin.materialdesign.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
 * Created by Jin on 2015-06-05.
 */
public class ListFragment extends Fragment implements MyBidListAdapter.ClickListener{

    private RecyclerView recyclerView;
    private MyBidListAdapter adapter;
    private ArrayList<Task> data;
    public static final String DRAWABLES_PATH = ":drawable/";
    private int counter = 0;
    private static final int LIMIT = 5;
    LinearLayoutManager linearLayoutManager;
    private SwipyRefreshLayout swipyRefreshLayout;
    private boolean is_last_data = false;
    private View layout;
    private Button button1, button2;
    private String category = "전체보기";
    private String filter = "거리순";
    private SharedPreferences pref;

    public ListFragment getInstance(int position) {

        ListFragment listFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        listFragment.setArguments(args);
        return listFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_my, container, false);
        data = new ArrayList<>();

        pref = getActivity().getSharedPreferences("pref", getActivity().MODE_PRIVATE);
        swipyRefreshLayout = (SwipyRefreshLayout) layout.findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {

                if (is_last_data) {
                    Toast.makeText(getActivity(), "마지막 데이터 입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    setDataByCategory();
                }

                if (swipyRefreshLayout.isRefreshing()) {
                    swipyRefreshLayout.setRefreshing(false);
                }
            }
        });

        recyclerView = (RecyclerView) layout.findViewById(R.id.taskList);
        adapter = new MyBidListAdapter(getActivity());
        adapter.setTaskList(data);
        setDataByCategory();
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                visibleItemCount = recyclerView.getChildCount();
//                totalItemCount = linearLayoutManager.getItemCount();
//                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
//
//                if (loading) {
//                    if (totalItemCount > previousTotal) {
//                        loading = false;
//                        previousTotal = totalItemCount;
//                    }
//                }
//                if (!loading && (totalItemCount - visibleItemCount)
//                        <= (firstVisibleItem + visibleThreshold)) {
//                    // End has been reached
//
//                    setData();
//
//                    loading = true;
//                }
//
//            }
//        });

//        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int current_page) {
//                setData();
//            }
//        });

        Bundle bundle = getArguments();
        if (bundle != null) {
//            textView.setText("The page Number is " + bundle.getInt("position"));
        }


//        RequestQueue queue = Volley.newRequestQueue(getActivity());


//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                result.setText("RESPONSE : " + response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                result.setText("ERROR : " + error.getMessage());
//            }
//        });
//
//        queue.add(stringRequest);

        button1 = (Button) layout.findViewById(R.id.category);
        button2 = (Button) layout.findViewById(R.id.filter);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);

        return layout;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.category:
                    AlertDialog alert = new AlertDialog.Builder(getActivity())
                            .setTitle("카테고리")
                            .setItems(R.array.category, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    category = getResources().getStringArray(R.array.category)[which];
                                    button1.setText(category);
                                    adapter.removeAllItem();
                                    counter = 0;

                                    if (which == 0) {
                                        setDataByCategory();
                                    } else {
                                        setDataByCategory();
                                    }

                                }
                            }).show();
                    break;
                case R.id.filter:
                    AlertDialog alert2 = new AlertDialog.Builder(getActivity())
                            .setTitle("정렬순서")
                            .setItems(R.array.filter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    filter = getResources().getStringArray(R.array.filter)[which];
                                    button2.setText(filter);

                                    adapter.removeAllItem();
                                    counter = 0;
                                    setDataByCategory();

                                }
                            }).show();
                    break;
            }
        }
    };

    public void setDataByCategory() {

        String url = "";

        if (filter.equals("거리순")) {
            url = "http://feering.zc.bz/php/selectOrderByDistance.php";
        } else if (filter.equals("최신순")){
            url = "http://feering.zc.bz/php/selectOrderByDatetime.php";
        } else {
            url = "http://feering.zc.bz/php/selectOrderByPay.php";
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray ja = new JSONArray(response);
                    is_last_data = ja.length() == 0;
                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject task = ja.getJSONObject(i);
                        adapter.addItem(new Task(task.getInt("id"), task.getString("pay"), task
                                .getString("description"), task
                                .getString("location"), task.getString("date"), task.getString("time"),
                                task.getString("phone"),
                                task.getString("category"),
                                getResources().getIdentifier(
                                        "com.example.jin.materialdesign" + DRAWABLES_PATH
                                                + task.getString("image_name"),
                                        null, null), Double.valueOf(task
                                .getString("latitude")), Double
                                .valueOf(task.getString("longitude")),task.getString("username") ,task.getString("create_at")));

                    }

                    counter = adapter.getItemCount();

                    if (is_last_data) {
                        Toast.makeText(getActivity(), "마지막 데이터 입니다.", Toast.LENGTH_SHORT).show();
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
                params.put("category", category);
                params.put("offset", counter + "");
                params.put("limit", LIMIT + "");
                params.put("latitude", pref.getString("latitude", "0"));
                params.put("longitude", pref.getString("longitude", "0") );

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
        Log.d("MTTAG", "Executed!!!!!!");

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