package com.example.trackmyfamily;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class AddChildFragmentScreen2 extends Fragment {

    private String uniq_ID;
    private String child_num_str;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout_file_screen_2,container,false);

        Log.v(MainActivity.TAG,"in AddChildFragmentScreen2, in onCreateView");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PreferencesFile",getActivity().MODE_PRIVATE);
        uniq_ID = sharedPreferences.getString("uniq_id","");


        Log.v(MainActivity.TAG,"in AddChildFragmentScreen2, in onCreateView, uniq = "+uniq_ID);

        if(uniq_ID.isEmpty()){

            Log.v(MainActivity.TAG,"in AddChildFragmentScreen2, in onCreateView, uniq id is empty");

            uniq_ID = generateRandomID();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uniq_id",uniq_ID);
            editor.apply();
        }


        TextView textViewEmailId = (TextView) v.findViewById(R.id.input_code);
        textViewEmailId.setText(uniq_ID);


        TextView textViewChild = (TextView) v.findViewById(R.id.input_child_number);
        final int[] children_num = {0};
        int child_num = 0;

        String path = uniq_ID;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);

        Log.v(MainActivity.TAG,"in AddChildFragmentScreen2, in onCreateView, data base ref = "+databaseReference);

        if(databaseReference!=null) {


            Log.v(MainActivity.TAG,"in AddChildFragmentScreen2, in onCreateView, data base ref is not null");



            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.v(MainActivity.TAG,"in AddChildFragmentScreen2, in onCreateView, onDataChange");
                    children_num[0] = (int) snapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.v(MainActivity.TAG,"in AddChildFragmentScreen2, in onCreateView, in ONcancelled");

                }

            });
            child_num = children_num[0] + 1;
        }


        child_num_str = String.valueOf(child_num);
        textViewChild.setText(child_num_str);


        Button b2 = (Button) v.findViewById(R.id.next_button_screen_2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        return v;
    }

    private void validateData() {

        Log.v(MainActivity.TAG,"in AddChildFragmentScreen2, in onCreateView, in validate Data");


        boolean result =  FirebaseUtility.checkHasData(uniq_ID,child_num_str);

        Intent i = new Intent(getActivity(),MapsActivity.class);

        Log.v(MainActivity.TAG,"in AddChildFragmentScreen2, in onCreateView, in validate data, result = "+result);


        if(result){
            i.putExtra("SUCCESS_VAL",1);
            Toast.makeText(getActivity(), "ADDITION OF CHILD SUCCESSFUL", Toast.LENGTH_LONG).show();
        }else{
            i.putExtra("SUCCESS_VAL",0);
            Toast.makeText(getActivity(), "ADDITION OF CHILD FAILED, TRY AGAIN", Toast.LENGTH_LONG).show();
        }

        startActivity(i);
    }




    private String generateRandomID() {

        String generatedID = generateAnId();

        while(FirebaseUtility.checkHasChild(generatedID,"")){
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
