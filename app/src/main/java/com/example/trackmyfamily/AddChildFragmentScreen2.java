package com.example.trackmyfamily;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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

import static com.example.trackmyfamily.MapsActivity.APP_LOG_TAG;

public class AddChildFragmentScreen2 extends Fragment {

    private String uniq_ID;
    private String child_num_str;
    private String random_id_generated;
    private ProgressBar mProgressBarCode;
    private ProgressBar mProgressBarChild;
    private TextView code_text;
    private TextView child_text;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(APP_LOG_TAG, "in oncreateview of screen 2 ");


        final View v = inflater.inflate(R.layout.fragment_layout_file_screen_2,container,false);

        mProgressBarCode = (ProgressBar) v.findViewById(R.id.progressBarCode);
        mProgressBarChild = (ProgressBar) v.findViewById(R.id.progressBarChild);
        code_text = (TextView) v.findViewById(R.id.input_code);
        child_text = (TextView) v.findViewById(R.id.input_child_number);


        code_text.setText("");
        child_text.setText("");

        Log.v(MainActivity.TAG,"in AddChildFragmentScreen2, in onCreateView");

        sharedPreferences = getActivity().getSharedPreferences("PreferencesFile",getActivity().MODE_PRIVATE);
        uniq_ID = sharedPreferences.getString("uniq_id","");


        Log.v(MainActivity.TAG,"in AddChildFragmentScreen2, in onCreateView, uniq = "+uniq_ID);

        if(uniq_ID.isEmpty()) {
            Log.v(MainActivity.TAG, "in AddChildFragmentScreen2, in onCreateView, uniq id is empty");
            generateRandomID();
            Log.v(MainActivity.TAG, "in after generating random id, generated_id = " + random_id_generated);
        }else{
            code_text.setText(uniq_ID);
            mProgressBarCode.setVisibility(View.GONE);

            String path = uniq_ID;
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
            Log.v(MainActivity.TAG, "generate Random ID, database Ref = " + databaseReference);

            if (databaseReference != null) {

                Log.v(MainActivity.TAG, "uniq id was already not null, database Ref is not null");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.v(MainActivity.TAG, "in generate random id, in onDataChanged");
                        Log.v(MainActivity.TAG, "in generate random id, in on data changed, snapshot = " + snapshot);

                        if (snapshot.exists()) {
                            child_num_str = String.valueOf(snapshot.getChildrenCount() + 1);
                            child_text.setText(child_num_str);
                            mProgressBarChild.setVisibility(View.GONE);
                        } else {
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.v(MainActivity.TAG, "in FireBase Utility, in check has child, in onCancelled");
                    }
                });
            }
        }


        Button b2 = (Button) v.findViewById(R.id.next_button_screen_2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDataAndStartActivity();
            }
        });


        return v;
    }

    private void validateDataAndStartActivity() {


        Log.v(MainActivity.TAG,"in validate data");

        final boolean[] result = {false};


        String path = uniq_ID+"/"+child_num_str+"/"+"latitude";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);

        Log.v(MainActivity.TAG,"in validate data, path = "+path);
        Log.v(MainActivity.TAG,"in validate data, db ref = "+databaseReference);

        if (databaseReference != null) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    Log.v(MainActivity.TAG,"in validate data, on Data change");
                    Log.v(MainActivity.TAG,"in validate data onData change, snapshot = "+snapshot);

                    if (snapshot.exists()) {
                        Log.v(MainActivity.TAG,"in validate data onData change, it has child lat");
                        result[0] = true;
                    } else {
                        Log.v(MainActivity.TAG,"in validate data, onData change, it does not have child lat");
                        result[0] = false;
                    }

                    Intent i = new Intent(getActivity(),MapsActivity.class);

                    if(result[0]){
                        i.putExtra("SUCCESS_VAL",1);
                        Toast.makeText(getActivity(), "ADDITION OF CHILD SUCCESSFUL", Toast.LENGTH_LONG).show();
                    }else{
                        i.putExtra("SUCCESS_VAL",0);
                        Toast.makeText(getActivity(), "ADDITION OF CHILD FAILED, TRY AGAIN", Toast.LENGTH_LONG).show();
                    }

                    startActivity(i);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.v(MainActivity.TAG,"in FireBase Utility, in check has data, onData change, onCancelled");
                }
            });
        }


        Log.v(MainActivity.TAG,"in FireBase Utility, in check has data, returning result[0] = "+ result[0]);

    }




    private char getRandomChar(){
        int max = 90;
        int min = 65;
        Random rand = new Random();
        int int_var = rand.nextInt((max - min) + 1) + min;
        char char_var = (char) int_var;
        return char_var;
    }





    public void generateRandomID() {

        Log.v(MainActivity.TAG,"in generate Random ID");

        String path = null;
        String generatedID = null;
        generatedID = generateAnId();


        Log.v(MainActivity.TAG,"in generate Random ID, outer loop");
        Log.v(MainActivity.TAG,"in generate Random ID, path = "+ path);
        Log.v(MainActivity.TAG,"in generate Random ID, generated ID = "+generatedID);

        path = generatedID;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
        Log.v(MainActivity.TAG, "generate Random ID, database Ref = " + databaseReference);

        if (databaseReference != null) {

            Log.v(MainActivity.TAG, "generate Random ID, database Ref is not null");

            final String finalGeneratedID = generatedID;
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    Log.v(MainActivity.TAG, "in generate random id, in onDataChanged");
                    Log.v(MainActivity.TAG, "in generate random id, in on data changed, snapshot = " + snapshot);

                    if (snapshot.exists()) {
                        generateRandomID();
                    } else{
                        random_id_generated = finalGeneratedID;
                        mProgressBarCode.setVisibility(View.GONE);
                        mProgressBarChild.setVisibility(View.GONE);
                        code_text.setText(random_id_generated);
                        child_num_str = String.valueOf(snapshot.getChildrenCount()+1);
                        child_text.setText(child_num_str);
                        uniq_ID = finalGeneratedID;

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("uniq_id",uniq_ID);
                        editor.apply();


                        return;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.v(MainActivity.TAG, "in FireBase Utility, in check has child, in onCancelled");
                }
            });
    }

    }


    private String generateAnId() {
        StringBuilder abc = new StringBuilder();
        for(int i=0;i<5;++i) {
            abc.append(getRandomChar());
        }
        return abc.toString();
    }


}
