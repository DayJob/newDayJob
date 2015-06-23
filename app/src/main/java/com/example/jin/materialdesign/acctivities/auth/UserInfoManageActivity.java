package com.example.jin.materialdesign.acctivities.auth;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jin.materialdesign.R;
import com.example.jin.materialdesign.acctivities.MainActivity;
import com.example.jin.materialdesign.network.VolleySingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserInfoManageActivity extends ActionBarActivity {

    private String phone, address, sex, birth;
    private EditText addressText, phoneText, sexText, birthText;
    private Button modify;
    private SharedPreferences pref;
    private Calendar myCalendar;
    int y, m, d;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_manage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getSharedPreferences("pref", MODE_PRIVATE);

        modify = (Button) findViewById(R.id.modify);
        phoneText = (EditText) findViewById(R.id.phone);
        addressText = (EditText) findViewById(R.id.address);
        sexText = (EditText) findViewById(R.id.sex);
        birthText = (EditText) findViewById(R.id.birth);

        phoneText.setText(pref.getString("phone", ""));
        addressText.setText(pref.getString("myAddress", ""));
        sexText.setText(pref.getString("sex", ""));
        birthText.setText(pref.getString("birth", ""));

        phoneText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    phoneText.setText(phoneText.getText().toString().replaceAll("\\D", ""));
                } else {
                    phoneText.setText(PhoneNumberUtils.formatNumber(phoneText.getText().toString()));
                }
            }
        });

        sexText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new AlertDialog.Builder(UserInfoManageActivity.this)
                            .setTitle("카테고리 선택")
                            .setSingleChoiceItems(R.array.sex, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sex = getResources().getStringArray(R.array.sex)[which];
                                    sexText.setText(sex);
                                    dialog.cancel();
                                }
                            }).show();
                }
            }
        });

        birthText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    try {
                        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                        SimpleDateFormat showFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
                        y = Integer.parseInt(showFormat.format(parseFormat.parse(birthText.getText().toString())));

                        showFormat = new SimpleDateFormat("MM", Locale.KOREA);
                        m = Integer.parseInt(showFormat.format(parseFormat.parse(birthText.getText().toString())));

                        showFormat = new SimpleDateFormat("dd", Locale.KOREA);
                        d = Integer.parseInt(showFormat.format(parseFormat.parse(birthText.getText().toString())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    myCalendar = Calendar.getInstance();

                    new DatePickerDialog(UserInfoManageActivity.this, new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            String show = "yyyy-MM-dd"; //In which you need put here
                            SimpleDateFormat showFormat = new SimpleDateFormat(show, Locale.KOREA);

                            birthText.setText(showFormat.format(myCalendar.getTime()));
                        }
                    }, y, m-1, d ).show();
                }
            }
        });

    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.modify:
                phone = phoneText.getText().toString();
                address = addressText.getText().toString();
                sex = sexText.getText().toString();
                birth = birthText.getText().toString();

                if (address.equals("") || phone.equals("") || sex.equals("") || birth.equals("")) {
                    Toast.makeText(this, "양식을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                } else {
                    modify.setEnabled(false);
                    sendAuthData();
                }
                break;
        }
    }

    public void sendAuthData() {
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        String url = "http://feering.zc.bz/php/updateUser.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(UserInfoManageActivity.this, "수정하였습니다.", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("phone", phone);
                editor.putString("address", address);
                editor.putString("sex", sex);
                editor.putString("birth", birth);

                editor.commit();

                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserInfoManageActivity.this, "서버와 통신할 수 없습니다. 인터넷 연결상태를 확인하세요", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", pref.getString("username", ""));
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
