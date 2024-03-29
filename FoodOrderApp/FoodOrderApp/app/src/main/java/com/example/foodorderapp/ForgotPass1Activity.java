package com.example.foodorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPass1Activity extends AppCompatActivity {
    private EditText txtEmail,txtRepass,txtRepass1;
    private Button btnNext;

    private ToggleButton toggle;
    private DatabaseReference dt_User;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass1);
        txtEmail = findViewById(R.id.txtEmail);
        txtRepass = findViewById(R.id.txtRePass);
        txtRepass1 = findViewById(R.id.txtRePass1);
        btnNext = findViewById(R.id.btn_Next);
        toggle = findViewById(R.id.passwordToggle);

        dt_User = FirebaseDatabase.getInstance().getReference("User");

        // Thiết lập ẩn mat khaụ khi app moi khởi chạy
        txtRepass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtRepass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Hiển thị mật khẩu
                    txtRepass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    txtRepass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Ẩn mật khẩu
                    txtRepass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtRepass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Next();
            }
        });

    }

    private void Next() {
        String email = txtEmail.getText().toString().trim();
        String repass = txtRepass.getText().toString().trim();
        String repass1 = txtRepass1.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            return;
        } else if (repass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        } else if (repass1.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập lại mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!repass.equals(repass1)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Thực hiện xác thực xem email đã tồn tại hay chưa
            dt_User.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Lấy tham chiếu đến nút người dùng cần cập nhật thông tin mật khẩu
                        dt_User = dataSnapshot.getChildren().iterator().next().getRef();
                        dt_User.child("pass").setValue(repass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Cập nhật mật khẩu thành công
                                    Toast.makeText(ForgotPass1Activity.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ForgotPass1Activity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Xử lý lỗi nếu cập nhật mật khẩu thất bại
                                    Toast.makeText(ForgotPass1Activity.this, "Cập nhật mật khẩu thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        // Người dùng không tồn tại
                        Toast.makeText(ForgotPass1Activity.this, "Tài khoản không tồn tại! Vui lòng đăng ký tài khoản.", Toast.LENGTH_SHORT).show();
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