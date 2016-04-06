package com.example.npquy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import com.example.npquy.database.UserDb;
import com.example.npquy.entity.User;

public class ProfileActivity extends AppCompatActivity {

    private TextView fullNameTextView, mobileTextView, emailTextView, homeTextView, passwordTextView;
    private Switch enableSnoozeSwitch;

    private UserDb userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config();
        userDb = new UserDb(this);
        setData();
        setContentView(R.layout.activity_profile);
    }

    /**
     * Config
     */
    private void config() {
        fullNameTextView = (TextView) findViewById(R.id.full_name_text_view);
        mobileTextView = (TextView) findViewById(R.id.mobile_text_view);
        emailTextView = (TextView) findViewById(R.id.email_profile_text_view);
        homeTextView = (TextView) findViewById(R.id.home_profile_text_view);
        passwordTextView = (TextView) findViewById(R.id.change_password_text_view);

        enableSnoozeSwitch = (Switch) findViewById(R.id.enable_snooze_switch);
    }

    /**
     *
     */
    private void setData() {
        User currentUser = userDb.getCurrentUser();
        if(currentUser != null) {
            Log.e("user", currentUser.toString());
//            fullNameTextView.setText(currentUser.getName());
            mobileTextView.setText(currentUser.getMobile());
            emailTextView.setText(currentUser.getEmail());
        }
    }
}
