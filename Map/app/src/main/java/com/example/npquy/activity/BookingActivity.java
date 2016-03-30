package com.example.npquy.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.npquy.database.UserDb;
import com.example.npquy.entity.Address;
import com.example.npquy.entity.ElectronicPayment;
import com.example.npquy.entity.RetrieveQuote;
import com.example.npquy.entity.SaveBooking;
import com.example.npquy.entity.User;
import com.example.npquy.service.CustomEditText;
import com.example.npquy.service.DrawableClickListener;
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
    private CustomEditText dropOff;
    private Button confirmBooking;
    private Switch waitAndReturn;
    private Switch childSeat;
    private Switch pet;
    private Switch eco;
    private EditText note;
    private AutoCompleteTextView mEmailView;
    private EditText phoneNumber;
    private EditText mPhoneNumber;

    private Boolean isClickOnImage = false;

    private AutoCompleteTextView signUpEmail;
    private EditText name;
    private EditText pay_by;

    private Address pickUpAddress;
    private Address dropOffAddress;

    private TextView people;
    private TextView luggage;

    private Date dateBook;

    private Boolean isWaitAndReturn;
    private Boolean isChildSeat;
    private Boolean isPet;
    private Boolean isEco;
    private UserDb userDb;

    private String payType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_booking);
        dateTime = (EditText) findViewById(R.id.date_time);
        dateTime.setInputType(InputType.TYPE_NULL);
        people = (TextView) findViewById(R.id.people1);
        luggage = (TextView) findViewById(R.id.luggage1);
        pickUp = (EditText) findViewById(R.id.pick_up_booking);
        dropOff = (CustomEditText) findViewById(R.id.drop_off_booking);
        confirmBooking = (Button) findViewById(R.id.book_booking);
        waitAndReturn = (Switch) findViewById(R.id.w8);
        childSeat = (Switch) findViewById(R.id.child_seat);
        pet = (Switch) findViewById(R.id.pet);
        eco = (Switch) findViewById(R.id.eco);
        note = (EditText) findViewById(R.id.content_note);
        pay_by = (EditText) findViewById(R.id.pay_by_edit);
        pay_by.setInputType(InputType.TYPE_NULL);


        userDb = new UserDb(this);

        Intent callerIntent = getIntent();
        if (callerIntent != null) {
            Bundle packageFromCaller =
                    callerIntent.getBundleExtra("data");
            String pickUpJson = packageFromCaller.getString("pickUpAddress");
            pickUpAddress = new JSONDeserializer<Address>().use(null,
                    Address.class).deserialize(pickUpJson);
            String dropOffJson = packageFromCaller.getString("dropOffAddress");
            dropOffAddress = new JSONDeserializer<Address>().use(null,
                    Address.class).deserialize(dropOffJson);

            Integer num_people = packageFromCaller.getInt("people");
            Integer num_luggage = packageFromCaller.getInt("luggage");
            pickUp.setText(pickUpAddress.getFulladdress());
            dropOff.setText(dropOffAddress.getFulladdress());
            people.setText(num_people + "");
            luggage.setText(num_luggage + "");
        }
        dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateBook = new Date(year, monthOfYear + 1, dayOfMonth, hours, minutes);
                                dateTime.setText(dateBook.toString());

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookingActivity.this,
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
        });
        dateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                if (hasFocus) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    dateBook = new Date(year, monthOfYear + 1, dayOfMonth, hours, minutes);
                                    dateTime.setText(dateBook.toString());

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                    TimePickerDialog timePickerDialog = new TimePickerDialog(BookingActivity.this,
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
        });
        pickUp.setOnClickListener(this);
        dropOff.setOnClickListener(this);
        dropOff.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawablePosition target) {
                isClickOnImage = true;
                switch (target) {
                    case RIGHT:
                        Toast.makeText(BookingActivity.this, "aaa", Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }
            }

        });
        confirmBooking.setOnClickListener(this);
        pay_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogPayment(BookingActivity.this);
            }
        });
        pay_by.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openDialogPayment(BookingActivity.this);
                }
            }
        });

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

    @Override
    public void onClick(View v) {
        if (v == pickUp) {
            Intent myIntent = new Intent(BookingActivity.this, GetAddressActivity.class);
            startActivityForResult(myIntent, 1);
        } else if (v == dropOff) {
            Intent myIntent = new Intent(BookingActivity.this, GetAddressActivity.class);
            startActivityForResult(myIntent, 2);
        } else if (v == confirmBooking) {
            postQuotation(pickUpAddress, dropOffAddress);
            User user = userDb.getCurrentUser();
            if (user == null) {
                openDialogSignIn(this);
            } else {
                postElectronicPayment(new ElectronicPayment());
                SaveBooking saveBooking = new SaveBooking();
                saveBooking.setCusid(0);
                saveBooking.setRoutedistance(0.0);
                saveBooking.setVehTypeID(0);
                saveBooking.setTravelTime(0.0);
                saveBooking.setTotalfare(0.0);
                saveBooking.setFare(0.0);
                saveBooking.setPick(pickUpAddress);
                saveBooking.setDoff(dropOffAddress);
                saveBooking.setPkLat(pickUpAddress.getLatitude());
                saveBooking.setPkLong(pickUpAddress.getLongitude());
                saveBooking.setPaq(0);
                saveBooking.setBags(0);
                saveBooking.setPetfriendly(isPet);
                saveBooking.setChildseat(isChildSeat);
                saveBooking.setExecutive(false);
                saveBooking.setDoLat(dropOffAddress.getLatitude());
                saveBooking.setDoLong(dropOffAddress.getLongitude());
                saveBooking.setBookingdate(dateBook.toString());
                getSaveBooking(saveBooking);

                Intent myIntent = new Intent(BookingActivity.this, BookingSaved.class);

                Bundle bundle = new Bundle();
                String savedBooking = new JSONSerializer().exclude("*.class").serialize(
                        saveBooking);
                bundle.putString("savedBooking", savedBooking);
                myIntent.putExtra("data", bundle);
                startActivity(myIntent);
            }
        }
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
                openDialogSignUp(BookingActivity.this);
            }
        });

        Button mEmailSignInButton = (Button) dialog.findViewById(R.id.email_sign_in_button);
        mEmailView = (AutoCompleteTextView) dialog.findViewById(R.id.email);

        phoneNumber = (EditText) dialog.findViewById(R.id.phone_number);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setEmail(mEmailView.getText().toString());
                user.setMobile(phoneNumber.getText().toString());
                String android_id = Settings.Secure.getString(BookingActivity.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                user.setDeviceId(android_id);
                login(user);
                userDb.login(user);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void login(User user) {

        String url = WebServiceTaskManager.URL + "SignIn";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.POST_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("response", response, null);
            }
        };

        String json = new JSONSerializer().exclude("name", "*.class").serialize(
                user);
        Log.e("json", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});

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
                openDialogSignIn(BookingActivity.this);
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
                String android_id = Settings.Secure.getString(BookingActivity.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                user.setDeviceId(android_id);
                signUp(user);

                dialog.dismiss();
                openDialogSignIn(BookingActivity.this);
            }
        });

        dialog.show();
    }

    private void hideImage(ImageView imageView1, ImageView imageView2) {
        imageView1.setVisibility(View.INVISIBLE);
        imageView2.setVisibility(View.INVISIBLE);
    }

    private void openDialogPayment(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_payment);

        dialog.setCanceledOnTouchOutside(true);

        final LinearLayout payCashLayout = (LinearLayout) dialog.findViewById(R.id.pay_cash_layout);
        final ImageView cash_tick = (ImageView) dialog.findViewById(R.id.cash_tick);

        LinearLayout payCardLayout = (LinearLayout) dialog.findViewById(R.id.pay_card_layout);
        final ImageView card_tick = (ImageView) dialog.findViewById(R.id.card_tick);

        payCashLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImage(cash_tick, card_tick);
                cash_tick.setVisibility(View.VISIBLE);
                payType = "Cash";
            }
        });

        payCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImage(cash_tick, card_tick);
                card_tick.setVisibility(View.VISIBLE);
                payType = "Card";
            }
        });

        Button paymentButton = (Button) dialog.findViewById(R.id.payment_button);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!payType.isEmpty()) {
                    pay_by.setText(payType);
                }
                dialog.dismiss();
            }
        });

        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.imageView_close);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ElectronicPayment electronicPayment = new ElectronicPayment();
        electronicPayment.setNonce(null);
        electronicPayment.setAmount(0.0);
        electronicPayment.setCustID(0);
        postElectronicPayment(electronicPayment);
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

        String json = new JSONSerializer().exclude("cusId", "*.class").serialize(
                user);
        Log.e("json", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});

    }

    private void postElectronicPayment(ElectronicPayment electronicPayment) {
        String url = WebServiceTaskManager.URL + "ElectronicPayment";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.POST_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("response", response, null);
            }
        };

        String json = new JSONSerializer().exclude("*.class").serialize(
                electronicPayment);
        Log.e("json", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});
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
        if (dateBook != null) {
            quotation.setBookingdate(dateBook.toLocaleString());
        } else {
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

    private void getSaveBooking(SaveBooking saveBooking) {
        String url = WebServiceTaskManager.URL + "SaveBooking";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.POST_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("response", response, null);
            }
        };

        String json = new JSONSerializer().exclude("*.class").serialize(
                saveBooking);
        Log.e("json", json, null);
        wst.addNameValuePair("", json);

        wst.execute(new String[]{url});
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.hasExtra("pickUp")) {
                Address address = (Address) data.getExtras().get("pickUp");
                pickUp.setText(address.getFulladdress());
                pickUpAddress.setLatitude(address.getLatitude());
                pickUpAddress.setLongitude(address.getLongitude());
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data.hasExtra("dropOff")) {
                Address address = (Address) data.getExtras().get("dropOff");
                dropOff.setText(address.getFulladdress());
                dropOffAddress.setLatitude(address.getLatitude());
                dropOffAddress.setLongitude(address.getLongitude());
            }
        }
    }
}
