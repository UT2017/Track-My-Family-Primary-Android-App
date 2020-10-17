package com.example.trackmyfamily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.trackmyfamily.R.id.fragment_container;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String APP_LOG_TAG = "MainActivity";
    private GoogleMap mMap;
    private HashMap<String , Marker> mMarkerHashMap = new HashMap<>();
    public static String EXTRA_KEY = "StartedFromMapsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Button button = (Button) findViewById(R.id.add_first_child_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(APP_LOG_TAG, "Add first child button clicked");
                onClickAddChild();
            }
        });

        Log.v(APP_LOG_TAG, "in onCreate, setting visibility of my empty view = GONE");
        View v = findViewById(R.id.empty_view);
        v.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.v(APP_LOG_TAG, "in onResume");

        SharedPreferences sharedPreferences = getSharedPreferences("PreferencesFile",MODE_PRIVATE);
        String uniq_ID = sharedPreferences.getString("uniq_id","");


        Log.v(APP_LOG_TAG, "in onResume, uniq_id = "+uniq_ID);


        if(!uniq_ID.isEmpty()) {
            Log.v(APP_LOG_TAG, "in onresume, flow 1");
            checkHasAtleastOneChild(uniq_ID);
        }else{
            Log.v(APP_LOG_TAG, "in onresume, flow 2");
            Log.v(APP_LOG_TAG, "in onresume, flow 2 , setting visibility of my empty view = visible");
            View v = findViewById(R.id.empty_view);
            v.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.v(APP_LOG_TAG, "in onPause, setting visibility of my empty view = GONE");
        View v = findViewById(R.id.empty_view);
        v.setVisibility(View.GONE);


    }

    private void checkHasAtleastOneChild(String uniq_ID) {

        Log.v(APP_LOG_TAG, "in onresume, in checkHasAtleastOneChild");

        Log.v(APP_LOG_TAG,"uniq id = "+uniq_ID);

        final boolean []result = {false};
        String path = uniq_ID;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path);
        Log.v(MainActivity.TAG, "generate Random ID, database Ref = " + databaseReference);

        if (databaseReference != null) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    Log.v(MainActivity.TAG, "in generate random id, in check has child, in onDataChanged");
                    Log.v(MainActivity.TAG, "in generate random id, in check has child, in on data changed, snapshot = " + snapshot);

                    if (snapshot.exists()) {

                        result[0] = true;

                        View v = findViewById(R.id.empty_view);
                        v.setVisibility(View.GONE);

                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(MapsActivity.this);

                    } else {
                        result[0] = false;

                        View v = findViewById(R.id.empty_view);
                        v.setVisibility(View.VISIBLE);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.v(MainActivity.TAG, "in FireBase Utility, in check has child, in onCancelled");
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuAddChild: onClickAddChild();
                break;
            case R.id.menuLogout: onClickLogout();
                break;
        }
        return true;
    }

    private void onClickLogout() {
        Log.v(APP_LOG_TAG,"in onClickLogout");
        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        mGoogleSignInClient.signOut();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void onClickAddChild() {
        Log.v(APP_LOG_TAG,"in onClickChild");
        //Toast.makeText(this, "onClickChild", Toast.LENGTH_LONG).show();

        AddChildFragment fragment = new AddChildFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(fragment_container , fragment).commit();

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
        //loginToFirebase();
        subscribeToUpdates();
    }





    private void subscribeToUpdates() {

        Log.v(APP_LOG_TAG,"in subscribeToUpdates");


        SharedPreferences sharedPreferences = getSharedPreferences("PreferencesFile",MODE_PRIVATE);
        String uniq_ID = sharedPreferences.getString("uniq_id","");


        String path = uniq_ID;
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
            Log.v(APP_LOG_TAG,
                    "in setMarker - doesn't contain key");
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