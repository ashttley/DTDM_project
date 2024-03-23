package com.example.foodapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
public class MainActivity extends AppCompatActivity {
    private Button btn_login, btn_res;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
    //NHAN VAO BTN SIGNUP CHUYEN SANG TRANG SIGN UP
    private void SignUp() {
        Intent i = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(i);
    }

}