package com.example.npquy.map;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.npquy.entity.Address;
import com.example.npquy.entity.Location;
import com.example.npquy.entity.Quotation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

import flexjson.JSONSerializer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText pickUp;
    private EditText dropOff;
    private Button book;
    private Button total;
    private Boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        pickUp = (EditText) findViewById(R.id.pick_up);;
        dropOff = (EditText) findViewById(R.id.drop_off);
        book = (Button) findViewById(R.id.book);
        total = (Button) findViewById(R.id.total);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void postQuotation(double pickLat, double pickLong, double dropLat, double dropLong) {

        String url = WebServiceTaskManager.URL + "Quotation";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.POST_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                showResponse(response);
            }
        };

        Quotation quotation = new Quotation();
        quotation.setCustid(0);
        quotation.setPickLat(pickLat);
        quotation.setPickLong(pickLong);
        quotation.setDoffLat(dropLat);
        quotation.setDoffLong(dropLong);
        quotation.setBookingdate("0001-01-01T00:00:00");
        quotation.setPaq(0);
        quotation.setBags(0);

        String json = new JSONSerializer().exclude("*.class").serialize(
                quotation);
        Log.e("json", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});

    }

    public void getPickUp (View v) {
        Intent myIntent=new Intent(MapsActivity.this, GetAddressActivity.class);
        startActivityForResult(myIntent, 1);
    }

    public void getDropOff (View v) {
        Intent myIntent=new Intent(MapsActivity.this, GetAddressActivity.class);
        startActivityForResult(myIntent, 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        double pickLat = 0.0;
        double pickLong = 0.0;
        double dropLat = 0.0;
        double dropLong = 0.0;
        if(requestCode == 1 && resultCode == RESULT_OK) {
            if (data.hasExtra("pickUp")) {
                Address address = (Address) data.getExtras().get("pickUp");
                pickUp.setText(address.getFulladdress());
                pickLat = address.getLatitude();
                pickLong = address.getLongitude();
            }
        }else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data.hasExtra("dropOff")) {
                Address address = (Address) data.getExtras().get("dropOff");
                dropOff.setText(address.getFulladdress());
                dropLat = address.getLatitude();
                dropLong = address.getLongitude();
            }
        }
        if(!pickUp.getText().toString().isEmpty() && !dropOff.getText().toString().isEmpty()) {
            postQuotation(pickLat, pickLong, dropLat, dropLong);
            book.setText("Continue \n Booking");
            total.setText("Total \n 30$");
            isCheck = true;
        }
    }

    public void booking(View v) {
        Intent myIntent=new Intent(MapsActivity.this, BookingActivity.class);
        startActivity(myIntent);
    }

    public void showResponse(String response) {
     //   Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        Log.e("response", response, null);
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
        try {
            if (mGPS.canGetLocation) {
                mGPS.getLocation();
            }
        }catch (Exception e) {
            Log.e("Exception", e.getLocalizedMessage(), e);
        }
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
        findNearestDriver(sydney);
    }

    private void findNearestDriver(LatLng location) {
        String url = WebServiceTaskManager.URL + "NearestDriver";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.POST_TASK, this, "Retrieving the nearest driver ...") {

            @Override
            public void handleResponse(String response) {
                showResponse(response);
            }
        };

        Location locate = new Location();
        locate.setLat(location.latitude);
        locate.setLgn(location.longitude);
        String json = new JSONSerializer().exclude("*.class").serialize(
                location);
        Log.e("json", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});
    }
}
