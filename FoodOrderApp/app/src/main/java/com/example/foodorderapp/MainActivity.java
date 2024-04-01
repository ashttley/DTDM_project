package com.example.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.zoho.commons.Fonts;
import com.zoho.commons.InitConfig;
import com.zoho.livechat.android.listeners.InitListener;
import com.zoho.salesiqembed.ZohoSalesIQ;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       ZohoSalesIQ.init(this.getApplication(),"lcP7eiKnDBYigBZ2doqIP0Yj3%2FB1xhb%2FujjltoC%2F6wjsygyfTP8Js1CURnoJy3vN","VXYedrQX8SnlbJmrcouAR0asbHNvGug1XohqYv84VA7ctZiRRP%2FPaDzA4Yu2ifTQvoFucCLzbok8xCYlypigqIueJvKDP3bOibeL2qmH0oHzXv1h38F4sZ7VMaQEImgP");
       ZohoSalesIQ.showLauncher(true);


    }
}