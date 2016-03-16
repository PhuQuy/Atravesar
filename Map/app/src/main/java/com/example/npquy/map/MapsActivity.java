package com.example.npquy.map;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.npquy.entity.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import flexjson.JSONSerializer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText edLongitude;
    private EditText edLatitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        edLongitude = (EditText) findViewById(R.id.pick_up);;
        edLatitude = (EditText) findViewById(R.id.drop_off);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void getNearestDriver(View v) {

        String url = WebServiceTaskManager.URL + "NearestDriver";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.POST_TASK, this, "Retrieving the nearest driver ...") {

            @Override
            public void handleResponse(String response) {
                showResponse(response);
            }
        };

        Location location = new Location();
        String latitude = edLatitude.getText().toString();
        String longitude = edLongitude.getText().toString();
        if(longitude.length() == 0 || latitude.length() == 0) {
            longitude = "0";
            latitude = "0";
        }
        location.setLat(Double.parseDouble(latitude));
        location.setLgn(Double.parseDouble(longitude));
        String json = new JSONSerializer().exclude("*.class").serialize(
                location);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});

    }

    public void showResponse(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        Log.e("aaaa", response, null);
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
        GPSTracker mGPS = new GPSTracker(this);
        if(mGPS.canGetLocation ) {
            mGPS.getLocation();
        }
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
    }
}
