package com.example.jin.materialdesign.acctivities.minor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jin.materialdesign.R;
import com.example.jin.materialdesign.acctivities.MainActivity;
import com.example.jin.materialdesign.acctivities.SubActivity;
import com.example.jin.materialdesign.network.VolleySingleton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddTaskActivity extends ActionBarActivity {

    private Intent intent;
    private EditText locationText, dateText, timeText, phoneText, categoryButton;
    private Button btn;
    String username, pay, description, location, date, time, phone, category, latitude, longitude, image_name;
    private Geocoder mCoder;
    private List<Address> addr;
    private Calendar myCalendar;
    DatePickerDialog.OnDateSetListener datePicker;
    private TimePickerDialog.OnTimeSetListener timePicker;
    private SharedPreferences pref;
    private static ClickListener clickListener;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_left);
    }

    public void setOnClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        intent = getIntent();
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        mCoder = new Geocoder(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        btn = (Button) findViewById(R.id.button2);

        locationText = (EditText) findViewById(R.id.editText3);
        phoneText = (EditText) findViewById(R.id.editText5);
        dateText = (EditText) findViewById(R.id.date);
        timeText = (EditText) findViewById(R.id.time);
        categoryButton = (EditText) findViewById(R.id.category);

        dateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(AddTaskActivity.this, datePicker, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        timeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new TimePickerDialog(AddTaskActivity.this, timePicker, myCalendar
                            .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
                }
            }
        });

        categoryButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog alert = new AlertDialog.Builder(AddTaskActivity.this)
                        .setTitle("카테고리 선택")
                        .setSingleChoiceItems(R.array.categoryText, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                category = getResources().getStringArray(R.array.categoryText)[which];
                                image_name = getResources().getStringArray(R.array.categoryImage)[which];
                                categoryButton.setText(category);
                                dialog.cancel();
                            }
                        }).show();
            }
        });

        myCalendar = Calendar.getInstance();

        datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        timePicker = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);

                String show = "a hh : mm"; //In which you need put here
                SimpleDateFormat showFormat = new SimpleDateFormat(show, Locale.KOREA);

                String save = "kk:mm:ss"; //In which you need put here
                SimpleDateFormat saveFormat = new SimpleDateFormat(save, Locale.KOREA);

                timeText.setText(showFormat.format(myCalendar.getTime()));
                time = saveFormat.format(myCalendar.getTime());
            }

        };

        try {

            addr = mCoder.getFromLocation(Double.valueOf(intent.getStringExtra("latitude")), Double.valueOf(intent.getStringExtra("longitude")), 5);
            locationText.setText(addr.get(0).getAddressLine(0));

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(AddTaskActivity.this, "서버와 통신할 수 없습니다. 인터넷 연결상태를 확인하세요", Toast.LENGTH_LONG).show();
            finish();
            e.printStackTrace();
        }


        phoneText.setText(pref.getString("phone", ""));
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

        username = pref.getString("username", "");

    }

    private void updateLabel() {

        String show = "yyyy년 MM월 dd일"; //In which you need put here
        SimpleDateFormat showFormat = new SimpleDateFormat(show, Locale.KOREA);

        String save = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat saveFormat = new SimpleDateFormat(save, Locale.KOREA);

        dateText.setText(showFormat.format(myCalendar.getTime()));
        date = saveFormat.format(myCalendar.getTime());
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

    public void mOnClick(View v) {

        switch (v.getId()) {
            case R.id.button1:
                finish();
                break;
            case R.id.button2:
                pay = ((EditText) (findViewById(R.id.editText1))).getText()
                        .toString();
                description = ((EditText) (findViewById(R.id.editText2))).getText()
                        .toString();
                location = ((EditText) (findViewById(R.id.editText3))).getText()
                        .toString();
                phone = ((EditText) (findViewById(R.id.editText5))).getText()
                        .toString();

                if (pay.equals("") || description.equals("") || location.equals("") || date == null
                        || time == null || phone.equals("")
                        || category == null) {

                    Toast.makeText(this, "양식을 모두 채워주세요", Toast.LENGTH_SHORT).show();

                } else {
                    btn.setEnabled(false);
                    sendTaskData();
                }

                break;
        }
    }

    public void sendTaskData() {
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        String url = "http://feering.zc.bz/php/insertByName.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                clickListener.listUpdate();
                Toast.makeText(AddTaskActivity.this, "등록했습니다. 내게시물 탭에서 확인해보세요.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddTaskActivity.this, "서버와 통신할 수 없습니다. 인터넷 연결상태를 확인하세요", Toast.LENGTH_LONG).show();
                finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("pay", pay);
                params.put("description", description);
                params.put("location", location);
                params.put("date", date);
                params.put("time", time);
                params.put("phone", phone);
                params.put("category", category);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("image_name", image_name);
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

    public interface ClickListener {
        public void listUpdate();
    }
}
