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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class ForgotPass2Activity extends AppCompatActivity {
    private EditText txtpass;
    private ToggleButton toggle;
    private Button btnLogin;
    private DatabaseReference dt_User;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass2);

        txtpass = (EditText) findViewById(R.id.txtNewPass);
        btnLogin = (Button) findViewById(R.id.btn_Login);
        toggle = findViewById(R.id.passwordToggle);
// thiet lap tat mat khau khi app moi khoi chay
        txtpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Hiển thị mật khẩu
                    txtpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Ẩn mật khẩu
                    txtpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RePass();
            }
        });
    }

    private void RePass() {
        String newpass = txtpass.getText().toString();

        // Kiểm tra xem newpass có giá trị hợp lệ hay không
        if (newpass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu mới!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy ID người dùng hiện tại
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Cập nhật mật khẩu mới trong Realtime Database
        dt_User = FirebaseDatabase.getInstance().getReference("User").child(userId);
        dt_User.child("password").setValue(newpass)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Cập nhật mật khẩu mới thành công trong Realtime Database
                        Toast.makeText(ForgotPass2Activity.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPass2Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi nếu cập nhật thất bại
                        Toast.makeText(ForgotPass2Activity.this, "Cập nhật mật khẩu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}