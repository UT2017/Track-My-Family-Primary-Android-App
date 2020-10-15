package com.example.trackmyfamily;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AddChildFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout_file,container,false);

        v.setBackgroundColor(Color.WHITE);

        Button b = (Button) v.findViewById(R.id.next_button_screen_1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChildFragmentScreen2 fragmentScreen2 = new AddChildFragmentScreen2();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,fragmentScreen2).commit();
            }
        });
        return v;
    }


}
