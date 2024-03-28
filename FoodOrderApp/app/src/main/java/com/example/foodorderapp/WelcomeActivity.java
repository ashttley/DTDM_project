package com.example.foodorderapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class WelcomeActivity extends AppCompatActivity {
    private Button btn_login, btn_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_res = findViewById(R.id.btn_Res);
        btn_login=findViewById(R.id.btn_Login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp();
            }
        });

    }
    private void Login() {
        Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(i);
    }
    //NHAN VAO BTN SIGNUP CHUYEN SANG TRANG SIGN UP
    private void SignUp() {
        Intent i = new Intent(WelcomeActivity.this, SignUpActivity.class);
        startActivity(i);
    }

}