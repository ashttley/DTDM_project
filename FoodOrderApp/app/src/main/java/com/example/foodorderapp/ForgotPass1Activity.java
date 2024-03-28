package com.example.foodorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPass1Activity extends AppCompatActivity {
    private EditText txtEmail;
    private Button btnNext;
    private DatabaseReference dt_User;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass1);
        txtEmail = findViewById(R.id.txtEmail);
        btnNext = findViewById(R.id.btn_Next);

        dt_User = FirebaseDatabase.getInstance().getReference("User");
        mAuth = FirebaseAuth.getInstance();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Next();
            }
        });

    }

    private void Next() {
        String email = txtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Thực hiện xác thực xem email đã tồn tại hay chưa
            dt_User.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            User user = userSnapshot.getValue(User.class);
                            if (user != null) {
                                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Email khôi phục mật khẩu đã được gửi thành công
                                            Intent intent = new Intent(ForgotPass1Activity.this, ForgotPass2Activity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // Gửi email khôi phục mật khẩu không thành công
                                            Toast.makeText(ForgotPass1Activity.this, "Gửi email khôi phục mật khẩu không thành công!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        // Người dùng không tồn tại
                        Toast.makeText(ForgotPass1Activity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                    Toast.makeText(ForgotPass1Activity.this, "Đã xảy ra lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}