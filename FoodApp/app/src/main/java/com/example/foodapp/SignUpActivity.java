package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
public class SignUpActivity extends AppCompatActivity {
    private EditText txt_email, txt_pass;
    private Button btn_res;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        txt_email = (EditText) findViewById(R.id.txtEmail);
        txt_pass=(EditText) findViewById(R.id.txtPass);
        btn_res=(Button) findViewById(R.id.btn_Res);
        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resgiter();
            }
        });
    }

    private void Resgiter() {
        String email, pass;
        email = txt_email.getText().toString();
        pass = txt_pass.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vui lòng nhập Email!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Vui lòng nhập mật khẩu!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Đăng nhập thành công!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Đăng nhập không thành công!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

}