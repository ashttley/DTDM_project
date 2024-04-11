package com.example.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.util.HashMap;

public class EditActivity extends AppCompatActivity {

    private EditText edtuser, edtphone, edtpass;

    private FirebaseAuth auth;
    private DatabaseReference dtuser;

    private Button btnsave,btnsend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edtuser = findViewById(R.id.edit_username);
        edtphone = findViewById(R.id.edit_phone);
        edtpass = findViewById(R.id.edit_pass);
        btnsend = findViewById(R.id.btn_send);


        btnsave= findViewById(R.id.btn_save);
        auth = FirebaseAuth.getInstance();

        Intent intent= getIntent();
        String email= intent.getStringExtra("email");

        display();

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveDataAndReturn();
            }
        });
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail(email);

            }
        });
    }
    private void sendPasswordResetEmail(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditActivity.this, "Một email đã được gửi đến để đặt lại mật khẩu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void display() {
        String userID = auth.getCurrentUser().getUid();
        dtuser = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference userRef = dtuser.child(userID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    if (user != null) {
                        String username = user.getUsername();
                        edtuser.setText(username);
                        String phone = user.getPhone();
                        edtphone.setText(phone);
                        String pass = user.getPass();
                        edtpass.setText(pass);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần thiết
                Toast.makeText(EditActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void saveDataAndReturn() {
        String userID = auth.getCurrentUser().getUid();
        dtuser = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference userRef = dtuser.child(userID);

        String updatedUsername = edtuser.getText().toString();
        String updatedPhone = edtphone.getText().toString();
        String updatedPass = edtpass.getText().toString();

        // Tạo một HashMap để lưu trữ dữ liệu đã sửa đổi
        HashMap<String, Object> updatedUserData = new HashMap<>();
        updatedUserData.put("username", updatedUsername);
        updatedUserData.put("phone", updatedPhone);
        updatedUserData.put("pass", updatedPass);

        // Cập nhật dữ liệu lên Firebase
        userRef.updateChildren(updatedUserData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    // Xử lý lỗi nếu cần thiết
                    Toast.makeText(EditActivity.this, "Failed to update user data", Toast.LENGTH_SHORT).show();
                } else {
                    // Dữ liệu đã được cập nhật thành công
                    // Tạo Intent để chuyển dữ liệu về ProfileActivity hoặc MainActivity
                    Intent intent = new Intent(EditActivity.this, ProfileActivity.class);
                    intent.putExtra("username", updatedUsername);
                    intent.putExtra("phone", updatedPhone);
                    intent.putExtra("pass", updatedPass);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}