package com.example.npquy.map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.npquy.entity.Address;
import com.example.npquy.entity.BundleAddress;
import com.example.npquy.entity.Location;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class GetAddressActivity extends AppCompatActivity {

    // List view
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    ArrayAdapter<Address> addressArrayAdapter;

    // Search EditText
    EditText inputSearch;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address);
        // Listview Data
        String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
                "iPhone 4S", "Samsung Galaxy Note 800",
                "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};

        lv = (ListView) findViewById(R.id.live_search_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        // Adding items to listview
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, products);
        lv.setAdapter(adapter);

        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
               // GetAddressActivity.this.adapter.getFilter().filter(cs);
                findSearchAddress(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position,
                                    long id) {
                String result = adapter.getItem(position);
                Intent data = new Intent();
                data.putExtra("pickUp", result);
                data.putExtra("dropOff", result);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    public void back(View v) {
        finish();
    }

    private void findSearchAddress (String text) {
        String url = WebServiceTaskManager.URL + "SearchAddress";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.GET_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("respone", response, null);
      /*          BundleAddress bundleAddress = new JSONDeserializer<BundleAddress>().use(null,
                        BundleAddress.class).deserialize(response);*/
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray addressArray = root.getJSONArray("addresses");
                    List<String> data = new ArrayList<>();
                    for(int i = 0; i<addressArray.length(); i++) {
                        data.add(addressArray.getJSONObject(i).getString("Fulladdress"));
                    }
                    Log.e("data", data.size() + "", null);
                    adapter = new ArrayAdapter<String>(GetAddressActivity.this, R.layout.list_item, R.id.product_name, data);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e("Error", e.getLocalizedMessage(),e);
                }

            }
        };

        wst.addNameValuePair("prefix", text);

        wst.execute(new String[]{url});
    }
}
