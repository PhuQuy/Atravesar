package com.example.npquy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npquy.database.UserDb;
import com.example.npquy.entity.Address;
import com.example.npquy.entity.Location;
import com.example.npquy.entity.NearestDriver;
import com.example.npquy.entity.RetrieveQuote;
import com.example.npquy.entity.RetrieveQuoteResult;
import com.example.npquy.entity.SaveBooking;
import com.example.npquy.entity.User;
import com.example.npquy.service.GPSTracker;
import com.example.npquy.service.WebServiceTaskManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import flexjson.JSONDeserializer;
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

    private LatLng yourLocation;
    private LatLng lastLocation;
    private LatLng currentLocation;

    private LinearLayout carLayout;

    private NearestDriver yourNearestDriver;
    private NavigationView navigationView = null;
    private Toolbar toolbar = null;

    private ImageView swap;
    public static SharedPreferences prefs;
    private Double totalFare;
    private int num_people, num_luggage;
    private RetrieveQuote retrieveQuote;
    private View marker;
    private View homeMarker;
    private TextView numTxt;
    private User user;
    private AutoCompleteTextView mEmailView;
    private EditText phoneNumber;
    private EditText mPhoneNumber;
    private AutoCompleteTextView signUpEmail;
    private EditText name;
    private UserDb userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
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
        marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        homeMarker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_home_marker, null);
        numTxt = (TextView) marker.findViewById(R.id.num_txt);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
       // toggle.set("Zeta-X");
        toolbar.setTitle("Zeta-X");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setDrawingCacheBackgroundColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView emailText = (TextView) headerView.findViewById(R.id.email);
        setVisibleItem();


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
                SharedPreferences.Editor editor = MapsActivity.prefs.edit();
                editor.putBoolean("isLogin", true);
                editor.commit();
            }
        });
        retrieveQuote = new RetrieveQuote();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void postQuotation(Address pickUpAdd, Address dropOffAdd) {

        String url = WebServiceTaskManager.URL + "Quotation";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.POST_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("response_quotation", response, null);
                RetrieveQuoteResult bookingSaved = new JSONDeserializer<RetrieveQuoteResult>().use(null,
                        RetrieveQuoteResult.class).deserialize(response);
                Log.e("Booking save", bookingSaved.toString());
                if(bookingSaved != null && bookingSaved.getTotalfare() != null) {
                    totalFare = Double.parseDouble(bookingSaved.getTotalfare());
                    total.setText("Total \n £" + totalFare);
                    book.setText("Continue \n Booking");
                    book.setClickable(bookingSaved.getInServiceArea());
                }
            }
        };

        retrieveQuote.setCustid(0);
        retrieveQuote.setPick(pickUpAdd.getFulladdress());
        retrieveQuote.setPickLat(pickUpAdd.getLatitude());
        retrieveQuote.setPickLong(pickUpAdd.getLongitude());
        retrieveQuote.setDoffLat(dropOffAdd.getLatitude());
        retrieveQuote.setDoffLong(dropOffAdd.getLongitude());
        retrieveQuote.setDoff(dropOffAdd.getFulladdress());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date today = new Date();
        String date=sdf.format(today);
        retrieveQuote.setBookingdate(date);
        retrieveQuote.setPaq(Integer.parseInt(people.getText().toString()));
        retrieveQuote.setBags(Integer.parseInt(luggage.getText().toString()));
        retrieveQuote.setPickpostcode(pickUpAdd.getPostcode());
        retrieveQuote.setDroppostcode(dropOffAdd.getPostcode());
        retrieveQuote.setPetfriendly(false);
        retrieveQuote.setExecutive(false);
        retrieveQuote.setChildseat(false);
        String json = new JSONSerializer().exclude("*.class").serialize(
                retrieveQuote);
        Log.e("Quotation", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});

    }

    public void pickLocation(int type) {
        Intent myIntent = new Intent(MapsActivity.this, GetAddressActivity.class);
        Bundle bundle = new Bundle();
        if(pickUpAddress != null) {
            bundle.putString("postCode", pickUpAddress.getPostcode());
        }else {
            bundle.putString("postCode", "");
        }
        myIntent.putExtra("data", bundle);
        startActivityForResult(myIntent, type);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.hasExtra("pickUp")) {
                Address address = (Address) data.getExtras().get("pickUp");
                pickUpAddress = address;
                pickUp.setText(address.getFulladdress());
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data.hasExtra("dropOff")) {
                Address address = (Address) data.getExtras().get("dropOff");
                dropOffAddress = address;
                dropOff.setText(address.getFulladdress());
            }
        }
        if (!pickUp.getText().toString().isEmpty() && !dropOff.getText().toString().isEmpty()) {
            postQuotation(pickUpAddress, dropOffAddress);
            isCheck = true;
        }
    }

    public void booking(View v) {
        if (isCheck) {
            if(!book.isClickable()) {
                Toast.makeText(MapsActivity.this, "This app does not operate in that area", Toast.LENGTH_LONG).show();
            }else {
                Intent myIntent = new Intent(MapsActivity.this, BookingActivity.class);
                Bundle bundle = new Bundle();
                String pickUpJson = new JSONSerializer().exclude("*.class").serialize(
                        pickUpAddress);
                String dropOffJson = new JSONSerializer().exclude("*.class").serialize(
                        dropOffAddress);
                String retrieveQuoteJson = new JSONSerializer().exclude("*.class").serialize(retrieveQuote);

                Log.e("retrieveQuoteJson", retrieveQuoteJson);
                bundle.putString("pickUpAddress", pickUpJson);
                bundle.putString("dropOffAddress", dropOffJson);
                bundle.putInt("people", Integer.parseInt(people.getText().toString()));
                bundle.putInt("luggage", Integer.parseInt(luggage.getText().toString()));
                bundle.putString("retrieveQuote", retrieveQuoteJson);
                bundle.putDouble("totalFare", totalFare);
                myIntent.putExtra("data", bundle);
                startActivity(myIntent);
            }
        }
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
                if (num_people != 0 && num_luggage != 0) {
                    people.setText(num_people + "");
                    luggage.setText(num_luggage + "");
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

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
            yourLocation = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());
            //  mMap.add
          //  mMap.addMarker(new MarkerOptions().position(yourLocation).title("You're here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLocation, 12.0f));
            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                public void onCameraChange(CameraPosition arg0) {
                    mMap.clear();
                    MarkerOptions home = new MarkerOptions().position(yourLocation)
                            .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapsActivity.this, homeMarker)));
                    mMap.addMarker(home).showInfoWindow();


                    currentLocation = arg0.target;

                    if (lastLocation != null && calculationByDistance(currentLocation, lastLocation) > 20) {
                        findNearestDriver(currentLocation);

                        MarkerOptions dragMark = new MarkerOptions().position(currentLocation)
                                .title("")
                                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapsActivity.this, marker)));
                        mMap.addMarker(dragMark).showInfoWindow();
                    }
                    lastLocation = arg0.target;
                    pickUpAddress = getLocationByGeoCode(currentLocation);
                    pickUp.setText(pickUpAddress.getFulladdress());


                }
            });
            findNearestDriver(yourLocation);
        } else {
            Toast.makeText(this, "Can't find your location! Please check your GPS!", Toast.LENGTH_LONG).show();
        }
    }

    private Address getLocationByGeoCode(LatLng location) {
        Geocoder geocoder;
        List<android.location.Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        Address yourAddressPick = new Address();
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if(!addresses.isEmpty()) {
                android.location.Address firstAddress = addresses.get(0);
                yourAddressPick.setPostcode(firstAddress.getPostalCode());
                String fullAddress = "";
                for (int i = 0; i < firstAddress.getMaxAddressLineIndex(); i++) {
                    fullAddress += firstAddress.getAddressLine(i) + ",";
                }

                yourAddressPick.setFulladdress(fullAddress);
                if(firstAddress.hasLongitude()) {
                    yourAddressPick.setLongitude(firstAddress.getLongitude());
                }
                if(firstAddress.hasLatitude()) {
                    yourAddressPick.setLatitude(firstAddress.getLatitude());
                }
            }
        } catch (IOException e) {
            Log.e("Error", e.getLocalizedMessage());
        }

        return yourAddressPick;
    }

    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        double meter = valueResult % 1000;
        Log.i("Meter", km * 1000 + meter + "");

        return km * 1000 + meter;
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void findNearestDriver(LatLng location) {
        String url = WebServiceTaskManager.URL + "NearestDriver";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.POST_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("NearestDriver_response", response, null);
                try {
                    yourNearestDriver = new JSONDeserializer<NearestDriver>().use(null,
                            NearestDriver.class).deserialize(response);
                    numTxt.setText((int) (yourNearestDriver.getTravelTime() / 1) + " mins");
                }catch (Exception e) {
                    Log.e("Error Parse Json",e.getLocalizedMessage());
                }
            }
        };

        Location locate = new Location(location.latitude, location.longitude);
        String json = new JSONSerializer().exclude("*.class").serialize(
                locate);
        Log.e("NearestDriver", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});
    }

    public void setVisibleItem(){
        if(!prefs.getBoolean("isLogin",false)){
            navigationView.getMenu().findItem(R.id.nav_booking_history).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_currentbooking).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
        }
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
        } else if (id == R.id.nav_login) {
                openDialogSignIn(this);

        } else if (id == R.id.nav_profile) {

        }else if (id == R.id.nav_profile) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void openDialogSignIn(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_login);

        //   dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_dialog_box);
        //   TextView title = (TextView) dialog.findViewById(R.id.title_dialog);
        //   title.setText("Sign In");

        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.imageView_close);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);

        TextView createAccount = (TextView) dialog.findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openDialogSignUp(MapsActivity.this);
            }
        });

        Button mEmailSignInButton = (Button) dialog.findViewById(R.id.email_sign_in_button);
        mEmailView = (AutoCompleteTextView) dialog.findViewById(R.id.email_sign_up_string);

        phoneNumber = (EditText) dialog.findViewById(R.id.phone_number);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setEmail(mEmailView.getText().toString());
                user.setMobile(phoneNumber.getText().toString());
                String android_id = Settings.Secure.getString(MapsActivity.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                user.setDeviceID(android_id);
                login();
                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void login() {

        String url = WebServiceTaskManager.URL + "SignIn";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.POST_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("response", response, null);
                try {
                    JSONObject root = new JSONObject(response);
                    String cusId = root.getString("CustID");
                    if(cusId != null) {
                        Log.e("CusId", cusId, null);
                        user.setCusID(cusId);
                        userDb.login(user);
                    }
                }catch (Exception e) {
                    Log.e("Exception Sign Up", e.getLocalizedMessage());
                }
            }
        };

        String json = new JSONSerializer().exclude("name", "*.class").serialize(
                user);
        Log.e("json", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});
        SharedPreferences.Editor editor = MapsActivity.prefs.edit();
        editor.putBoolean("isLogin", true);
        editor.commit();

    }

    private void openDialogSignUp(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_sign_up);

        // dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_dialog_box);
        //  TextView title = (TextView) dialog.findViewById(R.id.title_dialog);
        //  title.setText("Sign Up");

        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.imageView_close);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView signIn = (TextView) dialog.findViewById(R.id.login_from_sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openDialogSignIn(MapsActivity.this);
            }
        });

        Button signUpButton = (Button) dialog.findViewById(R.id.sign_up_button);
        signUpEmail = (AutoCompleteTextView) dialog.findViewById(R.id.email_sign_up);
        name = (EditText) dialog.findViewById(R.id.name);
        mPhoneNumber = (EditText) dialog.findViewById(R.id.phone_number_sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setEmail(signUpEmail.getText().toString());
                user.setName(name.getText().toString());
                user.setMobile(mPhoneNumber.getText().toString());
                String android_id = Settings.Secure.getString(MapsActivity.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                user.setDeviceID(android_id);
                signUp(user);

                dialog.dismiss();
                openDialogSignIn(MapsActivity.this);
            }
        });

        dialog.show();
    }
    private void signUp(User user) {

        String url = WebServiceTaskManager.URL + "SignUp";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.POST_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("response", response, null);
            }
        };

        String json = new JSONSerializer().exclude("cusID", "*.class").serialize(
                user);
        Log.e("json", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});

    }
}
