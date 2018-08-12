package neu.edu.runningsquad;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import neu.edu.runningsquad.model.Record;
import neu.edu.runningsquad.model.Squad;
import neu.edu.runningsquad.model.User;
import neu.edu.runningsquad.util.Sessions;

public class RunningActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int LOCATION_PERMISSION = 0;
    private Location mLastKnownLocation;
    private boolean isRunning = false;
    private List<Location> locationList = new ArrayList<>();
    private float currDistance = 0;
    private Polyline route;
    private RoundCornerProgressBar bar;
    private float trailLength = 6000;
    private DatabaseReference mReference;
    private String username;
    private OnCompleteListener<Location> trailListener;
    private CircleButton startButton;
    private CircleButton stopButton;
    private float threshold = 2000;
    private String squadname;
    private User currUser;
    private Squad currSquad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mReference = FirebaseDatabase.getInstance().getReference();
        bar = findViewById(R.id.running_progress_bar);
        username = Sessions.getUsername(this);
        squadname = Sessions.getSquadName(this);
        startButton = findViewById(R.id.start_to_run);
        stopButton = findViewById(R.id.stop_running);
        startButton.setColor(R.color.md_blue_400);
        stopButton.setColor(R.color.md_red_400);
        stopButton.setVisibility(View.GONE);


        mReference.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child("squads").child(squadname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currSquad = dataSnapshot.getValue(Squad.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mMap.setMyLocationEnabled(true);
            prepareLocation();


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] result){
        super.onRequestPermissionsResult(requestCode, permissions, result);


        if(requestCode == LOCATION_PERMISSION && result[0] == PackageManager.PERMISSION_GRANTED){
            //do things as usual init map or something else when location permission is granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                mMap.setMyLocationEnabled(true);
                prepareLocation();
            }
        }
    }

    private void prepareLocation(){
        route = mMap.addPolyline( new PolylineOptions()
                .color(R.color.md_blue_400)
                .width(15)
                .geodesic(true));
        getDeviceLocation();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {

                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        // Called when a new location is found by the network location provider.
                        Log.d("Runner", "Is Running");
                        mLastKnownLocation = location;
                        Log.i("Runner", mLastKnownLocation.toString());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                        Log.i("Runner", isRunning + "") ;
                        if (isRunning) {
                            if (locationList.size() == 0){
                                locationList.add(location);
                            }
                            else {
                                Location prevLocation = locationList.get(locationList.size() - 1);
                                currDistance += prevLocation.distanceTo(location);
                                Log.d("Runner", "" + currDistance);
                                Log.d("Runner", "" + location.toString());


                                locationList.add(location);
                                refreshState();
                            }
                        }

                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }
                };

// Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } catch(SecurityException e)  {
            Log.e("Runner", e.getMessage());
        }
    }



    public void startToRun(View view){
        if (!isRunning){
            bar.setProgress(0);

            findViewById(R.id.start_running_bubble).setVisibility(View.GONE);
            isRunning = true;
            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
        }
        else{
            saveState();
            initialState();
            isRunning = false;
            stopButton.setVisibility(View.GONE);

        }

    }

    private void saveState(){
        String key =  mReference.child("user-records").push().getKey();
        if(key == null){
            key = "1";
        }

        int stars = Math.min((int) (currDistance/threshold), 3);
        currSquad.setTotalStars(currSquad.getTotalStars() + stars);
        currUser.setStar(currUser.getStar() + (int) (currDistance/threshold));

        Record record = new Record("6k", stars, System.currentTimeMillis());
        mReference.child("user-records").child(username).child(key).setValue(record);
        mReference.child("users").child(username).child("star").setValue(currUser.getStar());
        mReference.child("squads").child(squadname).child("totalStars").setValue(currSquad.getTotalStars());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congradualations");
        builder.setMessage("You have earned " + stars + " stars!!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        RunningActivity.this.finish();

                    }
                });
        AlertDialog mDialog = builder.show();
    }

    void initialState(){
        locationList.clear();
        bar.setProgress(0);
        route.setPoints(new ArrayList<LatLng>());
        currDistance = 0;
    }

    private void refreshState(){
        List<LatLng> points = new ArrayList<>();
        for(Location l : locationList){
            Log.i("Runner", "location drawing");
            points.add(new LatLng(l.getLatitude(), l.getLongitude()));
        }

        bar.setProgress(currDistance/trailLength);
        route.setPoints(points);
    }

}
