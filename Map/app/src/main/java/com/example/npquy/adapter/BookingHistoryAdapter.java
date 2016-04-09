package com.example.npquy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.npquy.activity.R;

import com.example.npquy.entity.Address;
import com.example.npquy.entity.JourneyHistory;

import java.util.ArrayList;

/**
 * Created by ITACHI on 3/27/2016.
 */
public class BookingHistoryAdapter extends ArrayAdapter<JourneyHistory> {
    Activity context = null;
    ArrayList<JourneyHistory> myArray = null;
    int layoutId;

    public BookingHistoryAdapter(Activity context, int resource, ArrayList<JourneyHistory> arr) {
        super(context, resource);
        this.context = context;
        this.layoutId = resource;
        this.myArray = arr;
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater =
                context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);

        if (myArray.size() > 0 && position >= 0) {
            final TextView status = (TextView)
                    convertView.findViewById(R.id.status);
            final TextView status_value = (TextView)
                    convertView.findViewById(R.id.booking_id);

            final TextView when = (TextView)
                    convertView.findViewById(R.id.date_time_history);
            final TextView pickUpHistory = (TextView)
                    convertView.findViewById(R.id.pick_up_history_value);
            final TextView dropOffHistory = (TextView)
                    convertView.findViewById(R.id.drop_off_history_value);

            JourneyHistory journeyHistory = myArray.get(position);
            if (journeyHistory != null) {
                pickUpHistory.setText(journeyHistory.getPickupAddress());
                dropOffHistory.setText(journeyHistory.getDropoffAddress());
            }
            when.setText(journeyHistory.getPickupDateTime());
        }
        return convertView;
    }
}
