package com.example.jin.materialdesign.acctivities.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jin.materialdesign.acctivities.MainActivity;
import com.example.jin.materialdesign.R;
import com.example.jin.materialdesign.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends ActionBarActivity {

    private String username, password;
    private EditText et1, et2;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_left);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("pref",
                MODE_PRIVATE);
        editor = pref.edit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et1 = (EditText) findViewById(R.id.username);
        et2 = (EditText) findViewById(R.id.password);

        et1.setText(pref.getString("username", ""));
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                username = et1.getText().toString();
                password = et2.getText().toString();

                if (username.equals("") || password.equals("")){

                    Toast.makeText(this, "양식을 모두 채워주세요", Toast.LENGTH_SHORT).show();

                } else {
                    getLoginData();
                }

                break;
        }
    }

    public void getLoginData() {
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        String url = "http://feering.zc.bz/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray ja = new JSONArray(response);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject loginInfo = ja.getJSONObject(i);
                        editor.putString("username",
                                loginInfo.getString("name"));
                        editor.putBoolean("is_logged_in",
                                loginInfo.getBoolean("is_logged_in"));
                        editor.commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (pref.getBoolean("is_logged_in", false)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                    finish();
                } else {
                    editor.putString("username","");
                    editor.commit();
                    Toast.makeText(LoginActivity.this, "로그인 실패, 아이디나 패스워드가 틀립니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "서버와 통신할수 없습니다. 인터넷 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("MYTAG", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", username);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/x-www-form-urlencoded");
                headers.put("abc", "value");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
