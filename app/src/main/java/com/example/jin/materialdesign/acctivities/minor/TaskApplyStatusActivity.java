package com.example.jin.materialdesign.acctivities.minor;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jin.materialdesign.R;
import com.example.jin.materialdesign.adapters.UserListAdapter;
import com.example.jin.materialdesign.models.User;
import com.example.jin.materialdesign.network.VolleySingleton;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class TaskApplyStatusActivity extends ActionBarActivity implements UserListAdapter.ClickListener {

    public JSONArray ja;
    private TextView pay, description, time, phone, location, createTime;
    private Intent intent;
    private UserListAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<User> alist;
    private SwipyRefreshLayout swipyRefreshLayout;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_apply_status);
        intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                if (swipyRefreshLayout.isRefreshing()) {
                    swipyRefreshLayout.setRefreshing(false);
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.userlist);

        adapter = new UserListAdapter(this);
        adapter.setClickListener(this);
        setData();

        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        pay = (TextView) findViewById(R.id.pay);
        description = (TextView) findViewById(R.id.description);
        time = (TextView) findViewById(R.id.time);
        phone = (TextView) findViewById(R.id.phone);
        location = (TextView) findViewById(R.id.location);
        createTime = (TextView) findViewById(R.id.create_at);

        pay.setText(intent.getStringExtra("pay"));
        description.setText(intent.getStringExtra("description"));

        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm:ss", Locale.KOREA);
        SimpleDateFormat showFormat = new SimpleDateFormat("a hh : mm", Locale.KOREA);
        try {
            time.setText(showFormat.format(parseFormat.parse(intent.getStringExtra("time"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        phone.setText(intent.getStringExtra("phone"));
        location.setText(intent.getStringExtra("location"));
        createTime.setText(intent.getStringExtra("createTime"));
        ImageButton img = (ImageButton) findViewById(R.id.imageButton);
        img.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent showLocationIntetn = new Intent(TaskApplyStatusActivity.this, ShowLocationActivity.class);
                showLocationIntetn.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                showLocationIntetn.putExtra("lat", Double.valueOf(intent.getStringExtra("latitude")));
                showLocationIntetn.putExtra("lng", Double.valueOf(intent.getStringExtra("longitude")));

                startActivity(showLocationIntetn);

            }
        });
    }

    public void setData() {
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        String url = "http://feering.zc.bz/php/taskApplyStatus.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                alist = new ArrayList<>();

                try {
                    ja = new JSONArray(response);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject data = ja.getJSONObject(i);
                        alist.add(new User(data.getInt("id"), data.getString("user_name"), data.getString("address"), data.getString("phone"), data.getString("sex"), data.getString("birth")));
                    }

                    adapter.setTaskList(alist);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TaskApplyStatusActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("MYTAG", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(intent.getStringExtra("id")));
                return params;
            }
        };

        requestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClick(View view, int position) {

    }
}
