package com.example.npquy.map;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class BookingActivity extends AppCompatActivity implements
        View.OnClickListener {
    private EditText dateTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int hours, minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        dateTime = (EditText) findViewById(R.id.date_time);
        dateTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        if (v == dateTime) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            Date date = new Date(year, monthOfYear+1,dayOfMonth,hours,minutes);
                            dateTime.setText(date.toString());

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            hours = hourOfDay;
                            minutes = minute;
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        }
    }
}
