package com.example.jin.materialdesign.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.jin.materialdesign.R;
import com.example.jin.materialdesign.acctivities.minor.TaskApplyActivity;
import com.example.jin.materialdesign.models.TaskMarker;
import com.example.jin.materialdesign.network.VolleySingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jin on 2015-06-07.
 */
public class TaskMapFragment extends Fragment {

    GoogleMap map;
    SharedPreferences pref;
    private JSONArray ja;
    private ClusterManager<TaskMarker> mClusterManager;
    private TextView tv;

    //    private Intent intent;
    public static final String DRAWABLES_PATH = ":drawable/";
    private ArrayList<TaskMarker> alist = new ArrayList<>();
    TaskMapFragment taskMapFragment;

    public TaskMapFragment() {
    }

    public TaskMapFragment getInstance() {
        if (taskMapFragment == null) {
            taskMapFragment = new TaskMapFragment();
        }
        return taskMapFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.task_map_fragment, container, false);
        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        pref = getActivity().getSharedPreferences("pref", getActivity().MODE_PRIVATE);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(
                        Double.valueOf(pref.getString("latitude", "0")),
                        Double.valueOf(pref.getString("longitude", "0"))),
                15));

        tv = (TextView) layout.findViewById(R.id.myLocation);
        tv.setText("현재위치 : " + pref.getString("address", "알수없음"));

        getData();

        return layout;
    }

    public void getData() {

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        String url = "http://feering.zc.bz/php/testTaskSelect.php";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ja = response;
                setUpClusterer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getActivity(), "서버와 통신할 수 없습니다. 인터넷 연결상태를 확인하세요", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);

    }

    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<TaskMarker>(getActivity(), map);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        map.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        // For cluster marker
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(
                new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker arg0) {
                        // TODO Auto-generated method stub
                        return null;
                    }
                });

        // For normal marker
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getActivity().getLayoutInflater().inflate(
                                R.layout.info_window, null);

                        TextView tv1 = (TextView) v
                                .findViewById(R.id.textView1);
                        TextView tv2 = (TextView) v
                                .findViewById(R.id.textView2);

                        ImageView img = (ImageView) v
                                .findViewById(R.id.imageView1);

                        tv1.setText(marker.getTitle());
                        tv2.setText(alist.get(Integer.valueOf(marker.getSnippet())).getSnippet());
                        img.setImageResource(alist.get(Integer.valueOf(marker.getSnippet())).getIcon());


                        return v;
                    }
                });

        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<TaskMarker>() {

                    @Override
                    public boolean onClusterClick(Cluster<TaskMarker> cluster) {
                        // FragmentManager manager =
                        // getSupportFragmentManager();
                        // MyDialogFragment dialog = new MyDialogFragment();
                        //
                        // dialog.show(manager, "dialog");

                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(cluster.getPosition().latitude,
                                                cluster.getPosition().longitude), map
                                                .getCameraPosition().zoom + 2), 500,
                                null);

                        return true;
                    }
                });

        mClusterManager
                .setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<TaskMarker>() {

                    @Override
                    public boolean onClusterItemClick(TaskMarker marker) {
//                        AlertDialog alert = new AlertDialog.Builder(
//                                getActivity())
//                                .setTitle(marker.getTitle())
//                                .setMessage(marker.getSnippet())
//                                .setPositiveButton("신청",
//                                        new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int which) {
//                                                dialog.dismiss();
//
//                                            }
//                                        })
//                                .setNegativeButton("취소",
//                                        new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int which) {
//                                                dialog.dismiss();
//
//                                            }
//                                        }).show();
                        return false;
                    }
                });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), TaskApplyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                intent.putExtra("id", String.valueOf(alist.get(Integer.valueOf(marker.getSnippet())).getId()));
                intent.putExtra("pay", String.valueOf(alist.get(Integer.valueOf(marker.getSnippet())).getPay()));
                intent.putExtra("description", String.valueOf(alist.get(Integer.valueOf(marker.getSnippet())).getDescription()));
                intent.putExtra("date", String.valueOf(alist.get(Integer.valueOf(marker.getSnippet())).getDate()));
                intent.putExtra("time", String.valueOf(alist.get(Integer.valueOf(marker.getSnippet())).getTime()));
                intent.putExtra("phone", String.valueOf(alist.get(Integer.valueOf(marker.getSnippet())).getPhone()));
                intent.putExtra("location", String.valueOf(alist.get(Integer.valueOf(marker.getSnippet())).getLocation()));
                intent.putExtra("latitude", String.valueOf(alist.get(Integer.valueOf(marker.getSnippet())).getPosition().latitude));
                intent.putExtra("longitude", String.valueOf(alist.get(Integer.valueOf(marker.getSnippet())).getPosition().longitude));
                intent.putExtra("username", String.valueOf(alist.get(Integer.valueOf(marker.getSnippet())).getUsername()));
                intent.putExtra("createTime", String.valueOf(alist.get(Integer.valueOf(marker.getSnippet())).getCreateTime()));

                startActivity(intent);

                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
            }
        });

        // Add cluster items (markers) to the cluster manager.
        addItems();

    }

    private void addItems() {

        try {

            for (int i = 0; i < ja.length(); i++) {
                JSONObject task = ja.getJSONObject(i);

                    alist.add(new TaskMarker(i, task.getInt("id"), task.getDouble("latitude"),
                            task.getDouble("longitude"),
                            task.getString("category"),
                            "보수: " + task.getString("pay")
                                    + " \n하는일 : " + task.getString("description")
                                    + " \n날짜 : " + task.getString("date")
                                    + " \n연락처 : " + task.getString("phone"),
                            getResources().getIdentifier(
                                    "com.example.jin.materialdesign" + DRAWABLES_PATH
                                            + task.getString("image_name"),
                                    null, null), getResources()
                            .getIdentifier(
                                    "com.example.jin.materialdesign" + DRAWABLES_PATH
                                            + task.getString("image_name")
                                            + "_small", null, null), task.getString("pay"), task.getString("description"), task.getString("location"),
                            task.getString("date"), task.getString("time"), task.getString("phone"),
                            task.getString("category"), task.getString("username"), task.getString("create_at")));

                mClusterManager.addItem(alist.get(i));

                mClusterManager.setRenderer(new MyClusterRenderer(getActivity(), map,
                        mClusterManager));

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class MyClusterRenderer extends DefaultClusterRenderer<TaskMarker> {

        private static final int MIN_CLUSTER_SIZE = 4;

        public MyClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<TaskMarker> clusterManager) {
            super(context, map, clusterManager);

        }

        @Override
        protected void onBeforeClusterItemRendered(TaskMarker item,
                                                   MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);

        }

        @Override
        protected void onClusterItemRendered(TaskMarker clusterItem,
                                             Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);

            marker.setTitle(clusterItem.getTitle());
            marker.setSnippet(clusterItem.getIndex() + "");
            marker.setIcon(BitmapDescriptorFactory.fromResource(clusterItem
                    .getSmallIcon()));


        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<TaskMarker> cluster) {
            // start clustering if at least 5 items overlap
            return cluster.getSize() > MIN_CLUSTER_SIZE;
        }
    }
}
