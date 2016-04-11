package com.example.npquy.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import com.example.npquy.activity.R;
import com.example.npquy.entity.JourneyHistory;

import java.util.ArrayList;

public class CurrentBookingAdapter extends ArrayAdapter<JourneyHistory>{
    Drawable drawable;
    Activity context = null;
    ArrayList<JourneyHistory> myArray = null;
    int layoutId;
    public CurrentBookingAdapter(Activity context, int resource, ArrayList<JourneyHistory> arr) {
        super(context, resource);
        this.context = context;
        this.layoutId = resource;
        this.myArray = arr;
    }
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater =
                context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.current_booking_item, null, true);

        //your stuff here

        final View popUpButton = rowView.findViewById(R.id.button_popup);
        popUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context, popUpButton);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int i = item.getItemId();
                        if (i == R.id.menu_repeat_journey) {
                            //do something
                            return true;
                        }
                        else if (i == R.id.menu_return_journey){
                            //do something
                            return true;
                        }
                        else if (i == R.id.cancel_booking) {
                            //do something
                            return true;
                        }
                        else {
                            return onMenuItemClick(item);
                        }
                    }
                });

                popup.show();

            }
        });

        return rowView;

    }
}

