package com.example.npquy.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.npquy.adapter.AddressAdapter;
import com.example.npquy.entity.Address;
import com.example.npquy.service.WebServiceTaskManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import flexjson.JSONDeserializer;

public class GetAddressActivity extends AppCompatActivity {

    // List view
    private ListView lv;
    ArrayList<Address> addressesData = new ArrayList<>();
    AddressAdapter addressArrayAdapter;
    private SQLiteDatabase database = null;

    // Search EditText
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address);

        lv = (ListView) findViewById(R.id.live_search_view);
        getDatabase();

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        addressesData.addAll(getAddressFromDb());
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
                doInsertRecord(address);
                Intent data = new Intent();
                data.putExtra("pickUp", address);
                data.putExtra("dropOff", address);

                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    public SQLiteDatabase getDatabase() {
        try {
            database = openOrCreateDatabase("addresses.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (database != null) {
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String createData = "create TABLE IF NOT EXISTS Addresses ("
                        + "id integer primary key autoincrement,"
                        + "Outcode text, "
                        + "Postcode text, "
                        + "Fulladdress varchar, "
                        + "Category text, "
                        + "Icon_Path text, "
                        + "Latitude real, "
                        + "Longitude real)";
                database.execSQL(createData);
            }
        } catch (Exception e) {
            Log.e("error", e.getLocalizedMessage(), e);
        }
        return database;
    }

    public List<Address> getAddressFromDb () {

        List<Address> addresses = new ArrayList<>();
        if(database != null) {
            try {
                Cursor cursor = database.query("Addresses", null, null, null, null, null, null);
                cursor.moveToFirst();
                while (cursor.isAfterLast() == false) {
                    Address address = new Address();
                    address.setOutcode(cursor.getString(0));
                    address.setPostcode(cursor.getString(1));
                    address.setFulladdress(cursor.getString(2));
                    address.setCategory(cursor.getString(3));
                    address.setIcon_Path(cursor.getString(4));
                    address.setLatitude(cursor.getDouble(5));
                    address.setLongitude(cursor.getDouble(6));
                    addresses.add(address);
                    cursor.moveToNext();
                }
            } catch (Exception ex) {
                Toast.makeText(GetAddressActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return addresses;
    }

    public void doInsertRecord(Address address) {
        ContentValues values = new ContentValues();
        values.put("Outcode",address.getOutcode());
        values.put("Postcode",address.getPostcode());
        values.put("Fulladdress",address.getFulladdress());
        values.put("Category",address.getCategory());
        values.put("Icon_Path",address.getIcon_Path());
        values.put("Latitude",address.getLatitude());
        values.put("Longitude",address.getLongitude());

        if(database.insert("Addresses", null, values) == -1) {
            Toast.makeText(this, "Can't insert data to addresses.db", Toast.LENGTH_LONG).show();
        }
    }

    private void findSearchAddress(String text) {
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
                    addressesData.clear();
                    addressesData.addAll(addresses);
                    addressArrayAdapter.notifyDataSetChanged();
                    lv.setAdapter(addressArrayAdapter);
                } catch (JSONException e) {
                    Log.e("Error", e.getLocalizedMessage(), e);
                }

            }
        };

        wst.addNameValuePair("prefix", text);

        wst.execute(new String[]{url});
    }
}
