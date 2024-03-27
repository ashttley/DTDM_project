package com.example.foodapp;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText txt_username,txt_phone,txt_email, txt_pass, txt_repass;
    private ToggleButton toggle;
    private Button btn_res;
    private DatabaseReference dt_User;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        txt_username =(EditText) findViewById(R.id.txtUsername) ;
        txt_phone =(EditText) findViewById(R.id.txtPhone) ;
        txt_email = (EditText) findViewById(R.id.txtEmail);
        txt_pass = (EditText) findViewById(R.id.txtPass);
        txt_repass = (EditText) findViewById(R.id.txtRePass);
        btn_res = (Button) findViewById(R.id.btn_Res);
        toggle = findViewById(R.id.passwordToggle);

        dt_User = FirebaseDatabase.getInstance().getReference("User");

        // tat an mat khau
        // thiet lap tat mat khau khi app moi khoi chay
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
    }
    // kiem tra dinh dang dien thoai
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Định dạng số điện thoại: 10 chữ số, bắt đầu bằng số 0
        String phonePattern = "^0\\d{9}$";
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    // kiem tra dinh dang email
    public static boolean isValidEmail(String email) {
        // Định dạng email đơn giản: [username]@[domain]
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void Register() {
        String username = txt_username.getText().toString();
        String phone = txt_phone.getText().toString();
        String email = txt_email.getText().toString();
        String pass = txt_pass.getText().toString();
        String repass = txt_repass.getText().toString();
        if(username.isEmpty()){
            Toast.makeText(this,"Vui lòng nhập tên người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(phone.isEmpty()){
            Toast.makeText(this,"Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(isValidPhoneNumber(phone) != true){
            Toast.makeText(this,"Vui lòng nhập đúng định dạng số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(email.isEmpty()){
            Toast.makeText(this,"Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(isValidEmail(email) != true){
            Toast.makeText(this,"Vui lòng nhập đúng định dạng email!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(pass.isEmpty()){
            Toast.makeText(this,"Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(repass.isEmpty()){
            Toast.makeText(this,"Vui lòng nhập lại mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(pass.equals(repass)==false){
            Toast.makeText(this,"Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            // kiem tra xem email da ton tai chua
            // để truy vấn cơ sở dữ liệu và tìm kiếm các nút có giá trị
            // email tương ứng với email người dùng đã nhập
            dt_User.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        //email da ton tai trong csdl
                        Toast.makeText(SignUpActivity.this, "Email đã tồn tại! Vui lòng sử dụng một email khác.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        // email khong ton tai
                        String id = dt_User.push().getKey();
                        User user = new User(username, phone, email, pass);
                        assert id != null;
                        dt_User.child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

}

