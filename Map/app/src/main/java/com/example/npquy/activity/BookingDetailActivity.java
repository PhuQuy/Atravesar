package com.example.npquy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.npquy.database.UserDb;
import com.example.npquy.entity.JourneyHistory;
import com.example.npquy.entity.SaveBooking;
import com.example.npquy.entity.User;
import com.example.npquy.service.WebServiceTaskManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import flexjson.JSONDeserializer;

public class BookingDetailActivity extends AppCompatActivity {

    private EditText pickUp;
    private EditText dropOff;
    private SaveBooking saveBooking;
    private UserDb userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        pickUp = (EditText) findViewById(R.id.pick_up_detail);
        dropOff = (EditText) findViewById(R.id.drop_off_detail);

        userDb = new UserDb(this);

      /*  Intent callerIntent = getIntent();
        Bundle packageFromCaller =
                callerIntent.getBundleExtra("data");
        String data = packageFromCaller.getString("savedBooking");
        saveBooking = new JSONDeserializer<SaveBooking>().use(null,
                SaveBooking.class).deserialize(data);
        Log.d("SaveBooking", saveBooking.toString());
        if(saveBooking != null) {
            pickUp.setText(saveBooking.getPick().getFulladdress());
            dropOff.setText(saveBooking.getDoff().getFulladdress());
        }*/

        User user = userDb.getCurrentUser();
        getBookingHistory(user.getCusID(), user.getDeviceID());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.booking_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
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

    private void getBookingHistory(String cusId, String deviceId) {
        String url = WebServiceTaskManager.URL + "CurrentBookings";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.GET_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("response", response, null);
      /*         */
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject journeyHistoryJson = root.getJSONObject("a");
                    String message = root.getString("message");
                    String code = root.getString("code");
                    JourneyHistory journeyHistory = new JSONDeserializer<JourneyHistory>().use(null,
                            JourneyHistory.class).deserialize(journeyHistoryJson.toString());

                    pickUp.setText(journeyHistory.getPickupAddress().getFulladdress());
                    dropOff.setText(journeyHistory.getDropoffAddress().getFulladdress());
                } catch (JSONException e) {
                    Log.e("Error", e.getLocalizedMessage(), e);
                }

            }
        };

        wst.addNameValuePair("CustID", cusId);
        wst.addNameValuePair("DeviceID", deviceId);
        Log.d("send data", url);
        wst.execute(new String[]{url});
    }
}
