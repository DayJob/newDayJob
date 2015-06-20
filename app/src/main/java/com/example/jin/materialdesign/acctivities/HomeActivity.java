package com.example.jin.materialdesign.acctivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jin.materialdesign.R;
import com.example.jin.materialdesign.acctivities.auth.LoginActivity;
import com.example.jin.materialdesign.acctivities.auth.SignupActivity;
import com.example.jin.materialdesign.acctivities.auth.UserInfoManageActivity;

import java.io.IOException;
import java.util.List;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

                if(pref.getBoolean("is_logged_in", false)){
                    Intent main = new Intent(this, MainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(main);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                } else {
                    Toast.makeText(this, "로그인을 먼저 해주세요..", Toast.LENGTH_LONG).show();
                }

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
            editor.putString("username", "");
            editor.putBoolean("is_logged_in", false);
            editor.commit();

            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show();
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
                editor.putBoolean("is_logged_in", false);
                Toast.makeText(this, "접속 대기시간 초과, 인터넷 연결상태를 확인해주세요.", Toast.LENGTH_LONG).show();
            }

            editor.commit();
        }
    }

}
