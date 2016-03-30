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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.npquy.adapter.AddressAdapter;
import com.example.npquy.adapter.FrequentAdapter;
import com.example.npquy.database.AddressDb;
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
    private ListView lvGetAddress;
    //ArrayList<Address> addressesData = new ArrayList<>();
   // AddressAdapter addressArrayAdapter;
    private FrequentAdapter frequentAdapter;
    private ArrayList<Object> addressesData = new ArrayList<>();

    private AddressDb addressDb ;

    // Search EditText
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address);
        getSupportActionBar().setHomeButtonEnabled(true);
        lvGetAddress = (ListView) findViewById(R.id.frequent_view);
       // getDatabase();
        addressDb = new AddressDb(this);

        inputSearch = (EditText) findViewById(R.id.inputSearch);
       // addressesData.addAll(addressDb.getAddressFromDb());
        /*addressArrayAdapter = new AddressAdapter(this,
                R.layout.list_item,
                addressesData);*/

        Address homeAddress = new Address("Home Address ,,Tap to select","");
        addressesData.add("HOME");
        addressesData.add(homeAddress);
        addressesData.add("FREQUENT");
        addressesData.addAll(addressDb.getAddressFromDb());
       // lv.setAdapter(addressArrayAdapter);
        frequentAdapter = new FrequentAdapter(this, addressesData);
        lvGetAddress.setAdapter(frequentAdapter);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
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
        lvGetAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position,
                                    long id) {
                Address address = null;
                try {
                    address = (Address) frequentAdapter.getItem(position);
                }catch (ClassCastException ex) {
                    Log.e("Cast Exception",ex.toString());
                }
               // doInsertRecord(address);
                if(address != null) {
                    addressDb.insertAddress(address);
                    Intent data = new Intent();
                    data.putExtra("pickUp", address);
                    data.putExtra("dropOff", address);

                    setResult(RESULT_OK, data);
                    finish();
                }
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

    private void findSearchAddress(String text) {
        String url = WebServiceTaskManager.URL + "SearchAddress";

        WebServiceTaskManager wst = new WebServiceTaskManager(WebServiceTaskManager.GET_TASK, this, "") {

            @Override
            public void handleResponse(String response) {
                Log.e("response", response, null);
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
                    frequentAdapter.notifyDataSetChanged();
                    lvGetAddress.setAdapter(frequentAdapter);
                } catch (JSONException e) {
                    Log.e("Error", e.getLocalizedMessage(), e);
                }

            }
        };

        wst.addNameValuePair("prefix", text);

        wst.execute(new String[]{url});
    }
}
