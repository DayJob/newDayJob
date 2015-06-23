package com.example.jin.materialdesign.acctivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jin.materialdesign.R;
import com.example.jin.materialdesign.acctivities.auth.LoginActivity;
import com.example.jin.materialdesign.acctivities.auth.SignupActivity;
import com.example.jin.materialdesign.acctivities.auth.UserInfoManageActivity;
import com.example.jin.materialdesign.network.VolleySingleton;
import com.example.jin.materialdesign.push.PreferenceUtil;
import com.example.jin.materialdesign.push.WakeLocker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class HomeActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private List<Address> addr;
    private Geocoder mCoder;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button btn1, btn2, btn3;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SENDER_ID = "819012735242";

    private GoogleCloudMessaging _gcm;
    private String _regId;
    private AtomicInteger msgId = new AtomicInteger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (checkPlayServices())
        {
            _gcm = GoogleCloudMessaging.getInstance(this);
            _regId = getRegistrationId();

        }

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setTitle("");
        setCurrentLocation();

        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.login);

        btn2.setVisibility(View.GONE);
        btn3.setVisibility(View.GONE);

    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                btn1.setEnabled(false);
                if(pref.getBoolean("is_logged_in", false)){
                    Intent main = new Intent(this, MainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(main);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                } else {
                    Toast.makeText(this, "로그인을 먼저 해주세요..", Toast.LENGTH_LONG).show();
                }
                btn1.setEnabled(true);

                break;
            case R.id.button2:
                Intent sub = new Intent(this, SubActivity.class);
                sub.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(sub);
                overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                break;
            case R.id.login:
                Intent login = new Intent(this, LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(login);
                overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (pref.getBoolean("is_logged_in", false)) {
            MenuItem item = menu.findItem(R.id.login);
            item.setVisible(false);
            MenuItem item2 = menu.findItem(R.id.signUp);
            item2.setVisible(false);
            this.invalidateOptionsMenu();

            MenuItem username = menu.findItem(R.id.username);
            username.setTitle(pref.getString("username", ""));
        } else {
            MenuItem item = menu.findItem(R.id.logout);
            item.setVisible(false);
            MenuItem item3 = menu.findItem(R.id.userInfo);
            item3.setVisible(false);
            this.invalidateOptionsMenu();

            MenuItem username = menu.findItem(R.id.username);
            username.setTitle("");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {

            update_regid();

            return true;
        }

        if (id == R.id.login) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }

        if (id == R.id.signUp) {
            Intent intent = new Intent(this, SignupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }

        if (id == R.id.userInfo) {
            Intent intent = new Intent(this, UserInfoManageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationProvider = locationManager.getBestProvider(new Criteria(), true);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {

            editor.putString("latitude",
                    String.valueOf(location.getLatitude()));
            editor.putString("longitude",
                    String.valueOf(location.getLongitude()));

            try {
                mCoder = new Geocoder(this);
                addr = mCoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 5);
                editor.putString("address",
                        addr.get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "접속 대기시간 초과, 인터넷 연결상태를 확인해주세요.", Toast.LENGTH_LONG).show();
            }

            editor.commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        // display received msg
        String msg = intent.getStringExtra("msg");
        Log.i("GcmActivity", "|" + msg + "|");
    }

    // google play service가 사용가능한가
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Log.i("GcmActivity", "|This device is not supported.|");
                finish();
            }
            return false;
        }
        return true;
    }

    // registration  id를 가져온다.
    private String getRegistrationId()
    {
        String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();
        if (TextUtils.isEmpty(registrationId))
        {
            Log.i("GcmActivity", "|Registration not found.|");
            return "";
        }
        int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion)
        {
            Log.i("GcmActivity", "|App version changed.|");
            return "";
        }
        return registrationId;
    }

    // app version을 가져온다. 뭐에 쓰는건지는 모르겠다.
    private int getAppVersion()
    {
        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    // gcm 서버에 접속해서 registration id를 발급받는다.
    private void registerInBackground()
    {
        new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                String msg = "";
                try
                {
                    if (_gcm == null)
                    {
                        _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    _regId = _gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + _regId;

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(_regId);
                }
                catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg)
            {
                Log.i("GcmActivity", "|" + msg + "|");
            }
        }.execute(null, null, null);
    }

    // registraion id를 preference에 저장한다.
    private void storeRegistrationId(String regId)
    {
        int appVersion = getAppVersion();
        Log.i("GcmActivity", "|" + "Saving regId on app version " + appVersion + "|");
        PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
        PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
    }

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString("message");
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    public void update_regid() {
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        String url = "http://feering.zc.bz/php/gcm/update_regid.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                editor.putString("username", "");
                editor.putBoolean("is_logged_in", false);
                editor.commit();
                Toast.makeText(HomeActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "서버와 통신할수 없습니다. 정상적으로 로그아웃되지 않았습니다.", Toast.LENGTH_SHORT).show();
                Log.d("MYTAG", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", pref.getString("username", ""));
                params.put("gcm_regid", "");
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

}
