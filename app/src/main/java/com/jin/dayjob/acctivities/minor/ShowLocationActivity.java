package com.jin.dayjob.acctivities.minor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.jin.dayjob.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Jin on 2015-06-07.
 */
public class ShowLocationActivity extends ActionBarActivity implements GoogleMap.OnMapClickListener {

    private GoogleMap map;
    private TextView tv;
    private Intent intent;
    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);

        intent = getIntent();
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 객체 선언부
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        tv = (TextView) findViewById(R.id.myLocation);


        // 구글맵 설정
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lng", 0)), 15));
        map.setOnMapClickListener(this);

        map.addMarker(new MarkerOptions().position(new LatLng(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lng", 0))).icon(BitmapDescriptorFactory.fromResource(R.drawable.basic_marker)));


            tv.setText("현재위치 : " + pref.getString("address", "알수없음"));

    }

    @Override
    public void onMapClick(final LatLng latLng) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_map, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
