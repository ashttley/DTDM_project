package com.example.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
public class MainActivity extends AppCompatActivity {
    private Button btnLogout, btnBack;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogout = (Button) findViewById(R.id.btn_Logout);
        btnBack = (Button) findViewById(R.id.btn_Back);
        mAuth = FirebaseAuth.getInstance();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}