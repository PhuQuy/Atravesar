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
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.npquy.entity.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import flexjson.JSONDeserializer;

public class GetAddressActivity extends AppCompatActivity {

    // List view
    private ListView lv;
    ArrayList<Address> addressesData = new ArrayList<>();
    AddressAdapter addressArrayAdapter;

    // Search EditText
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address);

        lv = (ListView) findViewById(R.id.live_search_view);

        inputSearch = (EditText) findViewById(R.id.inputSearch);

        addressArrayAdapter = new AddressAdapter(this,
                R.layout.list_item,
                addressesData);

        lv.setAdapter(addressArrayAdapter);

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
                Address address = addressArrayAdapter.getItem(position);
                Intent data = new Intent();
                data.putExtra("pickUp", address);
                data.putExtra("dropOff", address);
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
      /*         */
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray addressArray = root.getJSONArray("addresses");
                    String message = root.getString("message");
                    String code = root.getString("code");
                    List<Address> addresses = new JSONDeserializer<List<Address>>()
                            .use(null, ArrayList.class).use("values", Address.class).deserialize(addressArray.toString());
                    Log.e("message", message.toString(), null);
                    Log.e("code", code.toString(), null);
                    addressesData.addAll(addresses);
                    addressArrayAdapter.notifyDataSetChanged();
                    lv.setAdapter(addressArrayAdapter);
                } catch (JSONException e) {
                    Log.e("Error", e.getLocalizedMessage(),e);
                }

            }
        };

        wst.addNameValuePair("prefix", text);

        wst.execute(new String[]{url});
    }
}
