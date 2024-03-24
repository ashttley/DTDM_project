 package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText nameInput, emailInput;
    private TextView userView;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase reference
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        userView = findViewById(R.id.userView);
        Button addButton = findViewById(R.id.addButton);
        Button readButton = findViewById(R.id.readButton);
        Button updateButton = findViewById(R.id.updateButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        addButton.setOnClickListener(v -> addUser());
        readButton.setOnClickListener(v -> readUsers());
        updateButton.setOnClickListener(v -> updateUser());
        deleteButton.setOnClickListener(v -> deleteUser());
    }

    private void addUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();

        if (!name.isEmpty() || !email.isEmpty()) {
            String id = databaseUsers.push().getKey();
            User user = new User(id, name, email);
            assert id != null;
            databaseUsers.child(id).setValue(user);
        }
    }

    private void readUsers() {
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder userBuilder = new StringBuilder();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    userBuilder.append(user.getName()).append(" - ").append(user.getEmail()).append("\n");
                }
                userView.setText(userBuilder.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateUser() {
        // Similar to addUser, but use databaseUsers.child(id).setValue(user) with an existing user ID
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();

        if (!name.isEmpty() && !email.isEmpty()) {
            String id = databaseUsers.push().getKey(); // Lấy ID của người dùng cần cập nhật
            User updatedUser = new User(id, name, email); // Tạo một đối tượng User mới với thông tin cập nhật

            // Cập nhật dữ liệu trong Firebase Realtime Database
            databaseUsers.child(id).setValue(updatedUser);
        }
    }

    private void deleteUser() {
        // Use databaseUsers.child(id).removeValue() with an existing user ID
        String id = "-NsTdLSIuOOlVbn0GUbh";// Lấy ID của người dùng cần xóa (có thể được lấy từ người dùng hoặc dữ liệu khác)
        if (id != null) {
            // Xóa người dùng khỏi Firebase Realtime Database
            databaseUsers.child(id).removeValue();
        }
    }
}