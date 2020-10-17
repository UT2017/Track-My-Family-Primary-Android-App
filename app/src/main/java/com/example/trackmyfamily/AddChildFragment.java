package com.example.trackmyfamily;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.example.trackmyfamily.MapsActivity.APP_LOG_TAG;

public class AddChildFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(APP_LOG_TAG, "in oncreate of screen 1 ");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout_file,container,false);

        Log.v(APP_LOG_TAG, "in oncreateview of screen 1 ");


        Button b = (Button) v.findViewById(R.id.next_button_screen_1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(APP_LOG_TAG, "in onClick of next button screen 1 ");
                AddChildFragmentScreen2 fragmentScreen2 = new AddChildFragmentScreen2();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,fragmentScreen2).commit();
            }
        });
        return v;
    }


}
