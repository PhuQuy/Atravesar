package com.example.npquy.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import com.example.npquy.database.UserDb;
import com.example.npquy.entity.Address;
import com.example.npquy.entity.RetrieveQuote;
import com.example.npquy.entity.User;
import com.example.npquy.service.WebServiceTaskManager;

import java.util.Calendar;
import java.util.Date;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class BookingActivity extends AppCompatActivity implements
        View.OnClickListener {
    private EditText dateTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int hours, minutes;
    private EditText pickUp;
    private EditText dropOff;
    private Button confirmBooking;
    private Switch waitAndReturn;
    private Switch childSeat;
    private Switch pet;
    private Switch eco;
    private EditText note;

    private Address pickUpAddress;
    private Address dropOffAddress;

    private Date dateBook;

    private Boolean isWaitAndReturn;
    private Boolean isChildSeat;
    private Boolean isPet;
    private Boolean isEco;
    private UserDb userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        dateTime = (EditText) findViewById(R.id.date_time);
        dateTime.setInputType(InputType.TYPE_NULL);
        pickUp = (EditText) findViewById(R.id.pick_up_booking);
        dropOff = (EditText) findViewById(R.id.drop_off_booking);
        confirmBooking = (Button) findViewById(R.id.book_booking);
        waitAndReturn = (Switch) findViewById(R.id.w8);
        childSeat = (Switch) findViewById(R.id.child_seat);
        pet = (Switch) findViewById(R.id.pet);
        eco = (Switch) findViewById(R.id.eco);
        note = (EditText) findViewById(R.id.content_note);

        userDb = new UserDb(this);

        Intent callerIntent = getIntent();
        Bundle packageFromCaller=
                callerIntent.getBundleExtra("data");
        String pickUpJson = packageFromCaller.getString("pickUpAddress");
        pickUpAddress = new JSONDeserializer<Address>().use(null,
                Address.class).deserialize(pickUpJson);
        String dropOffJson = packageFromCaller.getString("dropOffAddress");
        dropOffAddress = new JSONDeserializer<Address>().use(null,
                Address.class).deserialize(dropOffJson);

        pickUp.setText(pickUpAddress.getFulladdress());
        dropOff.setText(dropOffAddress.getFulladdress());

        dateTime.setOnClickListener(this);
        pickUp.setOnClickListener(this);
        dropOff.setOnClickListener(this);
        confirmBooking.setOnClickListener(this);

        waitAndReturn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isWaitAndReturn = isChecked;
            }
        });
        childSeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isChildSeat = isChecked;
            }
        });
        pet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPet = isChecked;
            }
        });
        eco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEco = isChecked;
            }
        });
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
                            dateBook = new Date(year, monthOfYear+1,dayOfMonth,hours,minutes);
                            dateTime.setText(dateBook.toString());

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

        }else if(v == pickUp) {
            Intent myIntent=new Intent(BookingActivity.this, GetAddressActivity.class);
            startActivityForResult(myIntent, 1);
        }else if(v == dropOff) {
            Intent myIntent=new Intent(BookingActivity.this, GetAddressActivity.class);
            startActivityForResult(myIntent, 2);
        }else if (v == confirmBooking) {
            postQuotation(pickUpAddress, dropOffAddress);
            User user = userDb.getCurrentUser();
            if(user == null) {
                Intent myIntent = new Intent(BookingActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }else {
                Intent myIntent = new Intent(BookingActivity.this, PaymentActivity.class);
                startActivity(myIntent);
            }
        }
    }

    private void postQuotation(Address pickUpAddress, Address dropOffAddress) {

        String url = WebServiceTaskManager.URL + "Quotation";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.POST_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("response", response, null);
            }
        };

        RetrieveQuote quotation = new RetrieveQuote();
        quotation.setCustid(0);
        quotation.setPickLat(pickUpAddress.getLatitude());
        quotation.setPickLong(pickUpAddress.getLongitude());
        quotation.setDoffLat(dropOffAddress.getLatitude());
        quotation.setDoffLong(dropOffAddress.getLongitude());
        if(dateBook != null) {
            quotation.setBookingdate(dateBook.toLocaleString());
        }else {
            quotation.setBookingdate("0001-01-01T00:00:00");
        }
        quotation.setPaq(0);
        quotation.setBags(0);
        quotation.setNote(note.getText().toString());
        quotation.setChildseat(isChildSeat);
        quotation.setPetfriendly(isPet);

        String json = new JSONSerializer().exclude("*.class").serialize(
                quotation);
        Log.e("json", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});

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
    }
}
