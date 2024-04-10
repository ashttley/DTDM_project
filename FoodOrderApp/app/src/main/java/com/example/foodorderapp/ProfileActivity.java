package com.example.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout home, cart, delivery;

    private EditText edtuser, edtphone, edtpass, edtemail;

    private FirebaseAuth auth;
    private DatabaseReference dtuser;

    private Button btnlogout, btnedit, btndelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edtuser = findViewById(R.id.edit_username);
        edtphone = findViewById(R.id.edit_phone);
        edtpass = findViewById(R.id.edit_pass);
        edtemail = findViewById(R.id.edit_email);

        btnlogout = findViewById(R.id.btn_logout);
        btnedit = findViewById(R.id.btn_edit);
        btndelete = findViewById(R.id.btn_delete);


        auth = FirebaseAuth.getInstance();


        cart = findViewById(R.id.layout_cart);
        delivery = findViewById(R.id.layout_delivery);
        home = findViewById(R.id.layout_home);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, DeliveryActivity.class);
                startActivity(intent);
            }
        });

        display();

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(ProfileActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ProfileActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();

            }
        });
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            String updatedUsername = intent.getStringExtra("username");
            String updatedPhone = intent.getStringExtra("phone");
            String updatedPass = intent.getStringExtra("pass");


            // Cập nhật giao diện với dữ liệu đã sửa đổi
            edtuser.setText(updatedUsername);
            edtphone.setText(updatedPhone);
            edtpass.setText(updatedPass);

        }

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
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
                        edtuser.setEnabled(false);
                        edtuser.setFocusable(false);

                        String phone = user.getPhone();
                        edtphone.setText(phone);
                        edtphone.setEnabled(false);
                        edtphone.setFocusable(false);

                        String pass = user.getPass();
                        edtpass.setText(pass);
                        edtpass.setEnabled(false);
                        edtpass.setFocusable(false);

                        String email = user.getEmail();
                        edtemail.setText(email);
                        edtemail.setEnabled(false);
                        edtemail.setFocusable(false);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần thiết
                Toast.makeText(ProfileActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });


    }

//    private void signout() {
//        Intent mainActivity = new Intent(ProfileActivity.this, LoginActivity.class);
//        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainActivity);
//        finish();
//    }

    private void edit() {
        Intent editactivity= new Intent(ProfileActivity.this,EditActivity.class);
        startActivity(editactivity);
        finish();


    }
    private void delete() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa?")
                .setMessage("Bạn có chắc chắn muốn xóa?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Xóa tài khoản thành công
                                                String userID = user.getUid();
                                                dtuser = FirebaseDatabase.getInstance().getReference("User");
                                                DatabaseReference userRef = dtuser.child(userID);
                                                userRef.removeValue();
                                                // Tạo Intent để chuyển dữ liệu về RegisterActivity
                                                Intent intent = new Intent(ProfileActivity.this, RegisterActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // Xóa tài khoản thất bại
                                                Toast.makeText(ProfileActivity.this, "Failed to delete user account", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}