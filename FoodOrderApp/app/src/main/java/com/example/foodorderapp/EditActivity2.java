package com.example.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditActivity2 extends AppCompatActivity {

    private EditText edtuser, edtphone, edtpass;

    private FirebaseAuth auth;
    private DatabaseReference dtuser;

    private Button btnsave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit2);

        edtuser = findViewById(R.id.edit_username);
        edtphone = findViewById(R.id.edit_phone);
        edtpass = findViewById(R.id.edit_pass);


        btnsave= findViewById(R.id.btn_save);
        auth = FirebaseAuth.getInstance();

        display();
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataAndReturn();
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
                    User user = dataSnapshot.getValue(User.class);
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
                Toast.makeText(EditActivity2.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditActivity2.this, "Failed to update user data", Toast.LENGTH_SHORT).show();
                } else {
                    // Dữ liệu đã được cập nhật thành công
                    // Tạo Intent để chuyển dữ liệu về ProfileActivity hoặc MainActivity
                    Intent intent = new Intent(EditActivity2.this, ProfileActivity.class);
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