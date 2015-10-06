package com.jin.dayjob;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
        View layout = inflater.inflate(com.jin.dayjob.R.layout.add_task, container, false);
        locationText = (EditText) layout.findViewById(com.jin.dayjob.R.id.editText3);



        return layout;
    }

}
