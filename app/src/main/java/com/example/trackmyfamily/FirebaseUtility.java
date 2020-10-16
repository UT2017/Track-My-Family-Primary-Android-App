package com.example.trackmyfamily;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtility {


    public static boolean checkHasChild(String uniq_id, final String child_num_str) {


        Log.v(MainActivity.TAG,"in FireBase Utility, in check has child");
        Log.v(MainActivity.TAG,"in FireBase Utility, in check has child, uniq _ id = "+uniq_id);
        Log.v(MainActivity.TAG,"in FireBase Utility, in check has child, child_num_str = "+child_num_str);

        final boolean[] result = {false};

        String path = uniq_id;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
        Log.v(MainActivity.TAG,"in FireBase Utility, in check has child, database Ref = "+databaseReference);

        if (databaseReference != null) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Log.v(MainActivity.TAG,"in FireBase Utility, in check has child, in onDataChanged");
                    Log.v(MainActivity.TAG,"in FireBase Utility, in check has child, in on data changed, snapshot = "+snapshot);

                    if (snapshot.hasChild(child_num_str)) {
                        result[0] = true;
                    } else {
                        result[0] = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.v(MainActivity.TAG,"in FireBase Utility, in check has child, in onCancelled");
                }
            });
        }


        Log.v(MainActivity.TAG,"in FireBase Utility, in check has child , returning result[0] = "+ result[0]);


        return result[0];

    }

    public static boolean checkHasData(String uniq_id, String child_num_str) {


        Log.v(MainActivity.TAG,"in FireBase Utility, in check has data");

        final boolean[] result = {false};


        String path = uniq_id+"/"+child_num_str;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);

        Log.v(MainActivity.TAG,"in FireBase Utility, in check has data, path = "+path);
        Log.v(MainActivity.TAG,"in FireBase Utility, in check has data, db ref = "+databaseReference);

        if (databaseReference != null) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Log.v(MainActivity.TAG,"in FireBase Utility, in check has data, onData change");
                    Log.v(MainActivity.TAG,"in FireBase Utility, in check has data, onData change, snapshot = "+snapshot);

                    if (snapshot.hasChild("latitude")&&snapshot.hasChild("longitude")) {
                        Log.v(MainActivity.TAG,"in FireBase Utility, in check has data, onData change, it has child lat and long");
                        result[0] = true;
                    } else {
                        Log.v(MainActivity.TAG,"in FireBase Utility, in check has data, onData change, it does not have child lat and long");
                        result[0] = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.v(MainActivity.TAG,"in FireBase Utility, in check has data, onData change, onCancelled");
                }
            });
        }


        Log.v(MainActivity.TAG,"in FireBase Utility, in check has data, returning result[0] = "+ result[0]);

        return result[0];
    }
}
