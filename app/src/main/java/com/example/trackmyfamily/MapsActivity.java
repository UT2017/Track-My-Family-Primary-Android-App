package com.example.trackmyfamily;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String APP_LOG_TAG = "MainActivity";
    private GoogleMap mMap;
    private HashMap<String , Marker> mMarkerHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.v(APP_LOG_TAG,"in onCreate");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * *
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v(APP_LOG_TAG,"in onMapReady");

        mMap = googleMap;
        mMap.setMaxZoomPreference(16);
        loginToFirebase();
    }

    private void loginToFirebase() {

        Log.v(APP_LOG_TAG,"in loginToFirebase");

        String email = getString(R.string.firebase_email);
        String pwd = getString(R.string.firebase_password);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.v(APP_LOG_TAG,"Authentication is successful");
                    subscribeToUpdates();
                }else{
                    Log.v(APP_LOG_TAG,"Authentication failed");
                }
            }
        });

    }

    private void subscribeToUpdates() {

        Log.v(APP_LOG_TAG,"in subscribeToUpdates");

        String path = getString(R.string.firebase_path);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);

        Log.v(APP_LOG_TAG,"Data base reference = "+databaseReference);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(APP_LOG_TAG,"in onChildAdded");
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(APP_LOG_TAG,"in onChildChanged");
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(APP_LOG_TAG,"in onChildMoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(APP_LOG_TAG,"in onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(APP_LOG_TAG,"in onCancelled");
                Log.v(APP_LOG_TAG,"in onCancelled, Failed to read value");
            }
        });
    }

    private void setMarker(DataSnapshot dataSnapshot) {

        Log.v(APP_LOG_TAG,"in setMarker");

        String key = dataSnapshot.getKey();
        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        Double lat = Double.parseDouble(value.get("latitude").toString());
        Double longitude = Double.parseDouble(value.get("longitude").toString());
        LatLng latLng = new LatLng(lat,longitude);
        if(!mMarkerHashMap.containsKey(key)){
            Log.v(APP_LOG_TAG,"in setMarker - doesn't contain key");
            mMarkerHashMap.put(key,mMap.addMarker(new MarkerOptions().title(key).position(latLng)));
        }else{
            Log.v(APP_LOG_TAG,"in setMarker - already contains key");
            mMarkerHashMap.get(key).setPosition(latLng);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(Marker m : mMarkerHashMap.values()){
            Log.v(APP_LOG_TAG,"marker m = "+m);
            builder.include(m.getPosition());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),1));

    }
}