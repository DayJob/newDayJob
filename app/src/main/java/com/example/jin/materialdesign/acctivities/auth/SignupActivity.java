package com.example.jin.materialdesign.acctivities.auth;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jin.materialdesign.R;
import com.example.jin.materialdesign.acctivities.MainActivity;
import com.example.jin.materialdesign.models.Task;
import com.example.jin.materialdesign.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignupActivity extends ActionBarActivity {

    private TextView warning;
    private String username, password, password_confirm, address, phone, sex, birth;
    private boolean isIdUeable = true;
    private Button btn1, btn2;
    private EditText usernameEdit, phoneEdit;
    private Calendar myCalendar;
    private JSONArray ja;
    private ArrayList<String> alist;

    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_left);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        alist = new ArrayList<>();
        getUserNameList();
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn1 = (Button) findViewById(R.id.sex);
        btn2 = (Button) findViewById(R.id.birth);
        usernameEdit = (EditText) findViewById(R.id.username);
        phoneEdit = (EditText) findViewById(R.id.phone);
        warning = (TextView) findViewById(R.id.warning);
        warning.setVisibility(View.INVISIBLE);

        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                warning.setVisibility(View.INVISIBLE);
                isIdUeable = true;

                    for (int i = 0; i < alist.size(); i++) {
                        if (usernameEdit.getText().toString().equals(alist.get(i))) {
                            warning.setVisibility(View.VISIBLE);
                            isIdUeable = false;
                            break;
                        }
                    }
            }
        });

        phoneEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    phoneEdit.setText(phoneEdit.getText().toString().replaceAll("\\D", ""));
                } else {
                    phoneEdit.setText(PhoneNumberUtils.formatNumber(phoneEdit.getText().toString()));
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.signUp:

                username = ((EditText) findViewById(R.id.username)).getText().toString();
                password = ((EditText) findViewById(R.id.password)).getText().toString();
                password_confirm = ((EditText) findViewById(R.id.password_confirm)).getText().toString();
                address = ((EditText) findViewById(R.id.address)).getText().toString();
                phone = phoneEdit.getText().toString();

                if (username.equals("") || password.equals("") || password_confirm.equals("") || address.equals("")
                        || phone.equals("") || sex.equals("") || birth.equals("")) {

                    Toast.makeText(this, "양식을 모두 채워주세요", Toast.LENGTH_SHORT).show();

                } else {

                    if (password.equals(password_confirm)) {
                        if (isIdUeable) {
                            sendAuthData();
                        } else {
                            Toast.makeText(this, "중복되는 아이디입니다. 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "비밀번호가 다릅니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.sex:
                new AlertDialog.Builder(this)
                        .setTitle("카테고리 선택")
                        .setSingleChoiceItems(R.array.sex, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sex = getResources().getStringArray(R.array.sex)[which];
                                btn1.setText(sex);
                                dialog.cancel();
                            }
                        }).show();
                break;
            case R.id.birth:

                myCalendar = Calendar.getInstance();

                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String show = "yyyy년 MM월 dd일"; //In which you need put here
                        SimpleDateFormat showFormat = new SimpleDateFormat(show, Locale.KOREA);

                        String save = "yyyy/MM/dd"; //In which you need put here
                        SimpleDateFormat saveFormat = new SimpleDateFormat(save, Locale.KOREA);

                        btn2.setText(showFormat.format(myCalendar.getTime()));
                        birth = saveFormat.format(myCalendar.getTime());
                    }
                }, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    public void getUserNameList() {
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        String url = "http://feering.zc.bz/php/selectUserName.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    ja = new JSONArray(response);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject data = ja.getJSONObject(i);

                        alist.add(data.getString("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("MYTAG", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("abc", "value");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    public void sendAuthData() {
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        String url = "http://feering.zc.bz/php/signUp.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(SignupActivity.this, "가입을 축하드립니다~", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = getSharedPreferences("pref",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("username",
                        username);
                editor.putBoolean("is_logged_in",
                        true);
                editor.commit();

                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(intent);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("MYTAG", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", username);
                params.put("password", password);
                params.put("address", address);
                params.put("phone", phone);
                params.put("sex", sex);
                params.put("birth", birth);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("abc", "value");
                return headers;
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
}
