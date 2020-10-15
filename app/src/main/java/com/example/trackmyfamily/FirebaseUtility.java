package com.example.trackmyfamily;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtility {


    public static boolean checkHasChild(String uniq_id, final String child_num_str) {


        final boolean[] result = {false};


        String path = uniq_id;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);

        if (databaseReference != null) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(child_num_str)) {
                        result[0] = true;
                    } else {
                        result[0] = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return result[0];

    }

    public static boolean checkHasData(String uniq_id, String child_num_str) {

        final boolean[] result = {false};


        String path = uniq_id+"/"+child_num_str;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);

        if (databaseReference != null) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild("latitude")&&snapshot.hasChild("longitude")) {
                        result[0] = true;
                    } else {
                        result[0] = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return result[0];
    }
}
