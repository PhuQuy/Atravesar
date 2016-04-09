package com.example.npquy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.npquy.adapter.BookingHistoryAdapter;
import com.example.npquy.database.UserDb;
import com.example.npquy.entity.JourneyHistory;
import com.example.npquy.service.WebServiceTaskManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import flexjson.JSONDeserializer;

public class BookingHistoryActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<JourneyHistory> journeyHistories = new ArrayList<>();
    private BookingHistoryAdapter bookingHistoryAdapter;
    private UserDb userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_booking);

        listView = (ListView) findViewById(R.id.booking_history);

        userDb = new UserDb(this);

        getBookingHistory(userDb.getCurrentUser().getMobile());
/*        bookingHistoryAdapter = new BookingHistoryAdapter(this,
                R.layout.current_booking_list_custom,
                journeyHistories);

        listView.setAdapter(bookingHistoryAdapter);*/
    }

    private void getBookingHistory(String phone) {
        String url = WebServiceTaskManager.URL + "JourneyHistory";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.GET_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("response", response, null);
      /*         */
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray addressArray = root.getJSONArray("jlist");
                    String message = root.getString("message");
                    String code = root.getString("code");
                    List<JourneyHistory> journeyHistories = new ArrayList<>();
                    if(addressArray.length() != 0) {
                        for (int i = 0; i < addressArray.length(); i++) {
                            JourneyHistory journeyHistory = new JSONDeserializer<JourneyHistory>().use(null,
                                    JourneyHistory.class).deserialize(addressArray.get(i).toString());
                            journeyHistories.add(journeyHistory);
                        }
                    }
                    Log.e("message", message.toString(), null);
                    Log.e("code", code.toString(), null);
                    bookingHistoryAdapter.addAll(journeyHistories);
                    bookingHistoryAdapter.notifyDataSetChanged();
                    listView.setAdapter(bookingHistoryAdapter);
                } catch (JSONException e) {
                    Log.e("Error", e.getLocalizedMessage(), e);
                }

            }
        };

        wst.addNameValuePair("Phone", phone);

        wst.execute(new String[]{url});
    }
}
