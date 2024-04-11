package com.example.foodorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassword2Activity extends AppCompatActivity {
    private String userID;
    private DatabaseReference dt_user;
    private EditText txtNewPass, txtReNewpass;
    private Button btnLogin;
    private ToggleButton toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);

        txtNewPass = findViewById(R.id.txtNewPass);
        txtReNewpass = findViewById(R.id.txtReNewPass);
        btnLogin = findViewById(R.id.btn_Save);
        toggle = findViewById(R.id.passwordToggle);

        // Thiết lập ẩn mat khaụ khi app moi khởi chạy
        txtNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtReNewpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Hiển thị mật khẩu
                    txtNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    txtReNewpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Ẩn mật khẩu
                    txtNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtReNewpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
            Log.d("UserID", "Received userID: " + userID);
            // Tiếp tục xử lý thông tin
        } else {
            // Intent không tồn tại
            Toast.makeText(this, "Không có Intent!", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc Activity hiện tại (nếu muốn)
        }



    }
    private void login(){
        String newpass = txtNewPass.getText().toString();
        String renewpass = txtReNewpass.getText().toString();
        if(newpass.isEmpty()){
            Toast.makeText(ForgotPassword2Activity.this, "Vui lòng nhập mật khẩu mới!",Toast.LENGTH_SHORT).show();
        }
        else if(renewpass.isEmpty()){
            Toast.makeText(ForgotPassword2Activity.this, "Vui lòng xác nhận lại mật khẩu mới!",Toast.LENGTH_SHORT).show();
        }
        else if (!newpass.equals(renewpass)) {
            Toast.makeText(ForgotPassword2Activity.this, "Mật khẩu không trùng khớp!",Toast.LENGTH_SHORT).show();
        }
        else{
            dt_user = FirebaseDatabase.getInstance().getReference("User");
            dt_user.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        snapshot.child("pass").getRef().setValue(newpass);
                        Toast.makeText(ForgotPassword2Activity.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPassword2Activity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ForgotPassword2Activity.this, "Snapshot không tồn tại!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ForgotPassword2Activity.this, "Error: " + error.toException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}