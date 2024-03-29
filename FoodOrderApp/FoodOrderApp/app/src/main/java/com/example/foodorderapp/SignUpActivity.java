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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText txt_username, txt_phone, txt_email, txt_pass, txt_repass;
    private TextView txt_login;
    private ToggleButton toggle;
    private Button btn_res;
    private DatabaseReference dt_User;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txt_username = findViewById(R.id.txtUsername);
        txt_phone = findViewById(R.id.txtPhone);
        txt_email = findViewById(R.id.txtEmail);
        txt_pass = findViewById(R.id.txtPass);
        txt_repass = findViewById(R.id.txtRePass);
        txt_login = findViewById(R.id.txtLogin);
        btn_res = findViewById(R.id.btn_Res);
        toggle = findViewById(R.id.passwordToggle);

        dt_User = FirebaseDatabase.getInstance().getReference("User");
        mAuth = FirebaseAuth.getInstance();

        // Ẩn/một mat khaụ
        // Thiết lập ẩn mat khaụ khi app moi khởi chạy
        txt_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txt_repass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Hiển thị mật khẩu
                    txt_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    txt_repass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Ẩn mật khẩu
                    txt_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txt_repass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        btn_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    // Kiểm tra định dạng số điện thoại
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Định dạng số điện thoại: 10 chữ số, bắt đầu bằng số 0
        String phonePattern = "^0\\d{9}$";
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    // Kiểm tra định dạng email
    public static boolean isValidEmail(String email) {
        // Định dạng email đơn giản: [username]@[domain]
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void Register() {
        String username = txt_username.getText().toString().trim();
        String phone = txt_phone.getText().toString().trim();
        String email = txt_email.getText().toString().trim();
        String pass = txt_pass.getText().toString().trim();
        String repass = txt_repass.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên người dùng!", Toast.LENGTH_SHORT).show();
            return;
        } else if (phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        } else if (repass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập lại mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!pass.equals(repass)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isValidPhoneNumber(phone)) {
            Toast.makeText(this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

    // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu chưa
        dt_User.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()) {
                User user = ds.getValue(User.class);
                if (user != null && user.getUsername().equals(username)) {
                    Toast.makeText(SignUpActivity.this, "Tên người dùng đã tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Tạo tài khoản người dùng trong Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Đăng ký thành công
                                // Lấy ID người dùng hiện tại từ Firebase Authentication
                                String userID = mAuth.getCurrentUser().getUid();
                                User newUser = new User( username, phone, email, pass);
                                dt_User.child(userID).setValue(newUser);
                                Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(i);
                            } else {
                                // Đăng ký thất bại
                                Toast.makeText(SignUpActivity.this, "Đăng ký thất bại! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(SignUpActivity.this, "Đã xảy ra lỗi. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
        }
    });
}
}