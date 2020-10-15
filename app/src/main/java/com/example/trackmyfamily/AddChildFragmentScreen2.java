package com.example.trackmyfamily;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class AddChildFragmentScreen2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout_file_screen_2,container,false);

        v.setBackgroundColor(Color.WHITE);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PreferencesFile",getActivity().MODE_PRIVATE);
        String uniq_ID = sharedPreferences.getString("uniq_id","");

        if(uniq_ID.isEmpty()){
            uniq_ID = generateRandomID();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uniq_id",uniq_ID);
            editor.apply();
        }


        TextView textViewEmailId = (TextView) v.findViewById(R.id.input_email_id);
        textViewEmailId.setText(uniq_ID);


        TextView textViewChild = (TextView) v.findViewById(R.id.input_child_number);
        final int[] children_num = {0};
        int child_num = 0;



        String path = uniq_ID;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);

        if(databaseReference!=null) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    children_num[0] = (int) snapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            child_num = children_num[0] + 1;
        }


        textViewChild.setText(String.valueOf(child_num));


        Button b2 = (Button) v.findViewById(R.id.next_button_screen_2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Next Button screen 2 clicked", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),MapsActivity.class);
                startActivity(i);
            }
        });

        return v;
    }


    private boolean checkIfChildPresent(final String toCheck){

        final boolean[] ans = {false};
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        if(databaseReference!=null) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(toCheck)){
                        ans[0] =  true;
                    }else{
                        ans[0] =  false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return ans[0];
    }




    private String generateRandomID() {

        String generatedID = generateAnId();

        while(checkIfChildPresent(generatedID)){
            generatedID = generateAnId();
        }

        return generatedID;
    }


    private char getRandomChar(){
        int max = 90;
        int min = 65;
        Random rand = new Random();
        int int_var = rand.nextInt((max - min) + 1) + min;
        char char_var = (char) int_var;
        return char_var;
    }

    private String generateAnId() {
        StringBuilder abc = new StringBuilder();
        for(int i=0;i<5;++i) {
            abc.append(getRandomChar());
        }
        return abc.toString();
    }
}
