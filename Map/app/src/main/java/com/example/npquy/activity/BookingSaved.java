package com.example.npquy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BookingSaved extends AppCompatActivity {

    private Button closeBook;
    private Button bookingDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_saved);
        closeBook = (Button) findViewById(R.id.close_book);

        closeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });

        bookingDetail = (Button) findViewById(R.id.booking_detail);
        bookingDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingDetailIntent = new Intent(BookingSaved.this, BookingDetailActivity.class);
                startActivity(bookingDetailIntent);
            }
        });
    }
}
