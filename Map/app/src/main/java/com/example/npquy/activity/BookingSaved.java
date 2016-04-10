package com.example.npquy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npquy.entity.SaveBooking;
import com.example.npquy.service.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import flexjson.JSONDeserializer;

public class BookingSaved extends AppCompatActivity implements  OnMapReadyCallback {

    private Button closeBook;
    private Button bookingDetail;
    private TextView date;
    private GoogleMap mMap;
    private SaveBooking saveBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_saved);

        Intent callerIntent = getIntent();
        Bundle packageFromCaller =
                callerIntent.getBundleExtra("data");
        String data = packageFromCaller.getString("savedBooking");
        saveBooking = new JSONDeserializer<SaveBooking>().use(null,
                SaveBooking.class).deserialize(data);

        date = (TextView) findViewById(R.id.date_booked);
        date.setText(saveBooking.getBookingdate());
        closeBook = (Button) findViewById(R.id.close_book);

        closeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingSaved.this, MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        bookingDetail = (Button) findViewById(R.id.booking_detail);
        bookingDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingDetailIntent = new Intent(BookingSaved.this, CurrentBookingActivity.class);
/*                Bundle bundle = new Bundle();
                String savedBooking = new JSONSerializer().exclude("*.class").serialize(
                        saveBooking);
                bundle.putString("savedBooking", savedBooking);
                bookingDetailIntent.putExtra("data", bundle);*/
                startActivity(bookingDetailIntent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GPSTracker mGPS = new GPSTracker(this);
        try {
            if (mGPS.isCanGetLocation()) {
                mGPS.getLocation();
            }
        }catch (Exception e) {
            Log.e("Exception", e.getLocalizedMessage(), e);
        }
        mMap = googleMap;

        double latitude, longitude;

        latitude = mGPS.getLatitude();
        longitude = mGPS.getLongitude();

        if(latitude != 0 && longitude != 0) {
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
        }else {
            Toast.makeText(this, "Can't find your location! Please check your GPS!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
