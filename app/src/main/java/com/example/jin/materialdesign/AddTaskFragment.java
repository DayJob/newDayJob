package com.example.jin.materialdesign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jin on 2015-06-07.
 */
public class AddTaskFragment extends Fragment {

    private AddTaskFragment addTaskFragment;
    private static EditText locationText;

    public AddTaskFragment getInstance() {
        if (addTaskFragment == null) {
            addTaskFragment = new AddTaskFragment();
        }
        return addTaskFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.add_task, container, false);
        locationText = (EditText) layout.findViewById(R.id.editText3);



        return layout;
    }

}
