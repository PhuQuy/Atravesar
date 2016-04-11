package com.example.npquy.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.npquy.adapter.CurrentBookingAdapter;
import com.example.npquy.adapter.FrequentAdapter;
import com.example.npquy.adapter.SelectHomeAddressAdapter;
import com.example.npquy.database.AddressDb;
import com.example.npquy.database.UserDb;
import com.example.npquy.entity.Address;
import com.example.npquy.entity.JourneyHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duong Phong on 4/10/2016.
 */
public class CurrentBookingActivity extends AppCompatActivity{
    private ListView lvCurrentBooking;
    private CurrentBookingAdapter currentBookingAdapter;
    private ArrayList<Object> addressesData = new ArrayList<>();
    private ArrayList<JourneyHistory> journeyHistories = new ArrayList<>();

    private List<Address> nearlyAddress = new ArrayList<>();
    private Address pickUpAddress, homeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_booking_list);
        //getSupportActionBar().setHomeButtonEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        lvCurrentBooking = (ListView) findViewById(R.id.current_booking_list);




        currentBookingAdapter = new CurrentBookingAdapter(this, R.layout.current_booking_item,journeyHistories);
        lvCurrentBooking.setAdapter(currentBookingAdapter);

    }
}
