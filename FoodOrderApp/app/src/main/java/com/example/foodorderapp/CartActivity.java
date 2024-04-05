package com.example.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class CartActivity extends AppCompatActivity {

    private LinearLayout home, delivery, chat, profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        home = findViewById(R.id.layout_home);
        delivery = findViewById(R.id.layout_delivery);
        chat = findViewById(R.id.layout_chat);
        profile = findViewById(R.id.layout_profile);


        //chuyen huong home
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //chuyen huong chat
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        //chuyen huong den profile
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //chuyen huong delivery
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, DeliveryActivity.class);
                startActivity(intent);
            }
        });
    }
}