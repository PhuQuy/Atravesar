package com.example.npquy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npquy.entity.Address;
import com.example.npquy.entity.Location;
import com.example.npquy.entity.RetrieveQuote;
import com.example.npquy.service.GPSTracker;
import com.example.npquy.service.WebServiceTaskManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import flexjson.JSONSerializer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private EditText pickUp;
    private EditText dropOff;
    private Button book;
    private Button total;
    private Boolean isCheck = false;
    private Address pickUpAddress;
    private Address dropOffAddress;
    private TextView people;
    private TextView luggage;

    private LinearLayout carLayout;

    private NavigationView navigationView = null;
    private Toolbar toolbar = null;

    private ImageView swap;

    private int num_people, num_luggage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        pickUp = (EditText) findViewById(R.id.pick_up);
        people = (TextView) findViewById(R.id.people);
        luggage = (TextView) findViewById(R.id.luggage);
        dropOff = (EditText) findViewById(R.id.drop_off);
        pickUp.setInputType(InputType.TYPE_NULL);
        dropOff.setInputType(InputType.TYPE_NULL);
        book = (Button) findViewById(R.id.book);
        total = (Button) findViewById(R.id.total);
        swap = (ImageView) findViewById(R.id.swap_location);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        carLayout = (LinearLayout) findViewById(R.id.car);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView emailText = (TextView) headerView.findViewById(R.id.email);


        navigationView.setNavigationItemSelectedListener(MapsActivity.this);
        pickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickLocation(1);
            }
        });
        dropOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickLocation(2);
            }
        });
        dropOff.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pickLocation(2);
                }
            }
        });
        pickUp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pickLocation(1);
                }
            }
        });

        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickUpAddress != null && dropOff != null) {
                    Address address = pickUpAddress.clone();
                    pickUpAddress = dropOffAddress;
                    pickUp.setText(pickUpAddress.getFulladdress());
                    dropOffAddress = address;
                    dropOff.setText(dropOffAddress.getFulladdress());
                }
            }
        });

        carLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCarBox(MapsActivity.this);
            }
        });

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
                Log.e("response_quotation", response, null);
            }
        };

        RetrieveQuote quotation = new RetrieveQuote();
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
        Log.e("Quotation", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void pickLocation(int type) {
        //   hideSoftKeyboard(MapsActivity.this);
        Intent myIntent = new Intent(MapsActivity.this, GetAddressActivity.class);
        startActivityForResult(myIntent, type);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        double pickLat = 0.0;
        double pickLong = 0.0;
        double dropLat = 0.0;
        double dropLong = 0.0;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.hasExtra("pickUp")) {
                Address address = (Address) data.getExtras().get("pickUp");
                pickUpAddress = address;
                pickUp.setText(address.getFulladdress());
                pickLat = address.getLatitude();
                pickLong = address.getLongitude();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data.hasExtra("dropOff")) {
                Address address = (Address) data.getExtras().get("dropOff");
                dropOffAddress = address;
                dropOff.setText(address.getFulladdress());
                dropLat = address.getLatitude();
                dropLong = address.getLongitude();
            }
        }
        if (!pickUp.getText().toString().isEmpty() && !dropOff.getText().toString().isEmpty()) {
            postQuotation(pickLat, pickLong, dropLat, dropLong);
            book.setText("Continue \n Booking");
            total.setText("Total \n 30.00$");
            isCheck = true;
        }
    }

    public void booking(View v) {
        if (isCheck) {
            Intent myIntent = new Intent(MapsActivity.this, BookingActivity.class);
            Bundle bundle = new Bundle();
            String pickUpJson = new JSONSerializer().exclude("*.class").serialize(
                    pickUpAddress);
            String dropOffJson = new JSONSerializer().exclude("*.class").serialize(
                    dropOffAddress);
            bundle.putString("pickUpAddress", pickUpJson);
            bundle.putString("dropOffAddress", dropOffJson);
            myIntent.putExtra("data", bundle);
            startActivity(myIntent);
        }
    }

    public void showResponse(String response) {
        //   Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        Log.e("response", response, null);
    }

    private void hideImage(ImageView imageView1, ImageView imageView2, ImageView imageView3, ImageView imageView4) {
        imageView1.setVisibility(View.INVISIBLE);
        imageView2.setVisibility(View.INVISIBLE);
        imageView3.setVisibility(View.INVISIBLE);
        imageView4.setVisibility(View.INVISIBLE);
    }

    private void openCarBox(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.car_selection);

        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.imageView_close);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        LinearLayout salonLayout = (LinearLayout) dialog.findViewById(R.id.salon_layout);
        final ImageView salon_tick = (ImageView) dialog.findViewById(R.id.salon_tick);

        final LinearLayout mvp_layout = (LinearLayout) dialog.findViewById(R.id.mvp_layout);
        final ImageView mvp_tick = (ImageView) dialog.findViewById(R.id.mvp_tick);

        LinearLayout executive_layout = (LinearLayout) dialog.findViewById(R.id.executive_layout);
        final ImageView executive_tick = (ImageView) dialog.findViewById(R.id.executive_tick);

        LinearLayout estate_layout = (LinearLayout) dialog.findViewById(R.id.estate_layout);
        final ImageView estate_tick = (ImageView) dialog.findViewById(R.id.estate_tick);

        salonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImage(mvp_tick, executive_tick, salon_tick, estate_tick);
                salon_tick.setVisibility(View.VISIBLE);
                num_people = 4;
                num_luggage = 2;
            }
        });

        mvp_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImage(mvp_tick, executive_tick, salon_tick, estate_tick);
                mvp_tick.setVisibility(View.VISIBLE);
                num_people = 6;
                num_luggage = 5;
            }
        });

        executive_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImage(mvp_tick, executive_tick, salon_tick, estate_tick);
                executive_tick.setVisibility(View.VISIBLE);
                num_people = 4;
                num_luggage = 2;
            }
        });

        estate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImage(mvp_tick, executive_tick, salon_tick, estate_tick);
                estate_tick.setVisibility(View.VISIBLE);
                num_people = 4;
                num_luggage = 3;
            }
        });

        Button chooseCar = (Button) dialog.findViewById(R.id.choose_car);
        chooseCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num_people != 0 && num_luggage != 0) {
                    people.setText(num_people + "");
                    luggage.setText(num_luggage + "");
                }
                dialog.dismiss();
            }
        });

        dialog.show();
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
            if (mGPS.isCanGetLocation()) {
                mGPS.getLocation();
            }
        } catch (Exception e) {
            Log.e("Exception", e.getLocalizedMessage(), e);
        }
        mMap = googleMap;

        double latitude, longitude;

        latitude = mGPS.getLatitude();
        longitude = mGPS.getLongitude();

        if (latitude != 0 && longitude != 0) {
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
            findNearestDriver(sydney);
        } else {
            Toast.makeText(this, "Can't find your location! Please check your GPS!", Toast.LENGTH_LONG).show();
        }
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
        Log.e("NearestDriver", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        setTitle("ZETA-X");
        int id = item.getItemId();

        if (id == R.id.nav_newbooking) {
            Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_booking_history) {

            Intent intent = new Intent(MapsActivity.this, BookingHistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_currentbooking) {
            Intent intent = new Intent(MapsActivity.this, BookingDetailActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_profile) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
