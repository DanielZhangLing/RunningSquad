package neu.edu.runningsquad;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import neu.edu.runningsquad.model.Record;
import neu.edu.runningsquad.util.Sessions;

import static neu.edu.runningsquad.util.Sessions.saveLoginInfo;

public class RunningActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private Location mLastKnownLocation;
    private boolean isRunning = false;
    private List<Location> locationList = new ArrayList<>();
    private float currDistance = 0;
    private Polyline route;
    private RoundCornerProgressBar bar;
    private float trailLength = 6;
    private DatabaseReference mReference;
    private String username;
    private OnCompleteListener<Location> trailListener;
    private CircleButton startButton;

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
        startButton = findViewById(R.id.start_to_run);
        startButton.setColor(R.color.md_blue_400);

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
            Log.i("Runner", "run Location");

        } else {
            // Show rationale and request permission.
        }

        getDeviceLocation();
        route = mMap.addPolyline( new PolylineOptions()
                .color(R.color.md_blue_400)
                .width(2 )
                .geodesic(true));
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            Log.i("Runner", mLastKnownLocation.toString());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            if(isRunning){
                                Log.d("Runner", "Is Running");
                                Location prevLocation = locationList.get(locationList.size() - 1);
                                currDistance += prevLocation.distanceTo(mLastKnownLocation);
                                Log.d("Runner", "" + currDistance);
                                Log.d("Runner", "" + mLastKnownLocation.toString());
                                locationList.add(mLastKnownLocation);

                                refreshState();
                            }

                        } else {
                            Log.d("Runner", "Current location is null. Using defaults.");
                            Log.e("Runner", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Runner", e.getMessage());
        }
    }



    public void startToRun(View view){
        if (!isRunning){
            bar.setProgress(0);
            isRunning = true;
            startButton.setColor(R.color.md_red_400);
        }
        else{
            saveState();
            initialState();
            isRunning = false;
            startButton.setColor(R.color.md_blue_400);
        }

    }

    private void saveState(){
        String key =  mReference.child("user-records").push().getKey();
        if(key == null){
            key = "1";
        }
        Record record = new Record("6k", (int)currDistance/2, System.currentTimeMillis());
        mReference.child("user-records").child(username).child(key).setValue(record);
    }

    void initialState(){
        locationList.clear();
        bar.setProgress(0);
        route.setPoints(new ArrayList<LatLng>());
    }

    private void refreshState(){
        List<LatLng> points = new ArrayList<>();
        for(Location l : locationList){
            points.add(new LatLng(l.getLatitude(), l.getLongitude()));
        }

        bar.setProgress(currDistance/trailLength);
        route.setPoints(points);
    }

}
