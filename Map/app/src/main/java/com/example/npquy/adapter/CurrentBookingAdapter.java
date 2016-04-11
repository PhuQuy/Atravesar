package com.example.npquy.adapter;

import com.example.npquy.entity.JourneyHistory;



import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;


import com.example.npquy.activity.R;
import com.example.npquy.entity.JourneyHistory;

import java.util.ArrayList;



public class CurrentBookingAdapter extends ArrayAdapter<JourneyHistory> {
    private Activity context;
    private ArrayList<JourneyHistory> myArray = new ArrayList<>();
    private int layoutId;


    public CurrentBookingAdapter(Activity context, int resource, ArrayList<JourneyHistory> arr) {
        super(context, resource);
        this.context = context;
        this.layoutId = resource;
        this.myArray = arr;
    }


    public View getView(int position, View convertView,
                        ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater inflater =
                    context.getLayoutInflater();
            convertView = inflater.inflate(layoutId, null);
        }

        if (myArray.size() > 0 && position >= 0 && (myArray.size() > position)) {
            final TextView bookingDate = (TextView)
                    convertView.findViewById(R.id.booking_current_date);
            final TextView paymentType = (TextView)
                    convertView.findViewById(R.id.payment_type);

            final TextView pickUpED = (TextView) convertView.findViewById(R.id.pick_up_current_booking);
            final TextView dropUpED = (TextView) convertView.findViewById(R.id.drop_off_current_booking);

            final TextView price = (TextView)
                    convertView.findViewById(R.id.current_booking_price);

            JourneyHistory journeyHistory = myArray.get(position);
            if (journeyHistory != null) {
                bookingDate.setText(journeyHistory.getPickupDateTime());
                paymentType.setText(journeyHistory.getPaymentMethod());
                pickUpED.setText(journeyHistory.getPickupAddress());
                dropUpED.setText(journeyHistory.getDropoffAddress());
                price.setText("£" + journeyHistory.getTotalFare());
            }
        }
        return convertView;
    }
}

