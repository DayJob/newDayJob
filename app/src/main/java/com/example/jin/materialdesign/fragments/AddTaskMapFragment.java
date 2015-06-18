package com.example.jin.materialdesign.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jin.materialdesign.acctivities.minor.AddTaskActivity;
import com.example.jin.materialdesign.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by Jin on 2015-06-08.
 */
public class AddTaskMapFragment extends Fragment implements GoogleMap.OnMapClickListener {

    private GoogleMap map;
    private TextView tv;
    private AddTaskMapFragment addTaskMapFragment;
    private SharedPreferences pref;

    public AddTaskMapFragment getInstance() {
        if (addTaskMapFragment == null) {
            addTaskMapFragment = new AddTaskMapFragment();
        }
        return addTaskMapFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.add_task_map, container, false);

        // 객체 선언부
        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        pref = getActivity().getSharedPreferences("pref", getActivity().MODE_PRIVATE);
        tv = (TextView) layout.findViewById(R.id.myLocation);

        // 구글맵 설정
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(Double.valueOf(pref.getString("latitude", "0")), Double.valueOf(pref.getString("longitude", "0"))), 15));
        map.setOnMapClickListener(this);

        tv.setText("현재위치 : " + pref.getString("address", "알수없음"));

        return layout;
    }

    @Override
    public void onMapClick(final LatLng latLng) {
        AlertDialog alert = new AlertDialog.Builder(getActivity())
                .setTitle("등록 위치 선택")
                .setPositiveButton("선택", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("latitude",
                                String.valueOf(latLng.latitude));
                        intent.putExtra("longitude",
                                String.valueOf(latLng.longitude));

                        startActivity(intent);

                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
    }
}
