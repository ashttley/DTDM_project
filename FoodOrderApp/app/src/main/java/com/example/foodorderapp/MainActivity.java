package com.example.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {
    private TextView hello_name;
    private Button logout;
    private DatabaseReference dt_User;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hello_name = findViewById(R.id.txtHello);
//        img_avt = findViewById(R.id.imgAvt);
//        logout = findViewById(R.id.btn_Logout);

        mAuth = FirebaseAuth.getInstance();

        //hien thi cac loai san pham len button
        displayCategories();

        // hien thi username
        displayUsername();

        // hien thi products
        displayProducts();

//        sign out
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Logout();
//            }
//        });

    }
    private void displayProducts(){
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinearLayout productsLayout = findViewById(R.id.list_product);
                productsLayout.removeAllViews();

                for (DataSnapshot productsSnapshot : dataSnapshot.getChildren()) {
                    // Lấy thông tin về sản phẩm từ dataSnapshot
                    String productsImgUrl = productsSnapshot.child("image").getValue(String.class);
                    String productsName = productsSnapshot.child("name").getValue(String.class);
                    String productsDesciption = productsSnapshot.child("description").getValue(String.class);
                    String productsPrice = productsSnapshot.child("price").getValue(String.class);

                    // Tạo các thành phần giao diện người dùng
                    LinearLayout productContainer = new LinearLayout(MainActivity.this);
                    productContainer.setOrientation(LinearLayout.HORIZONTAL);
                    productsLayout.addView(productContainer);

                    ImageView productImage = new ImageView(MainActivity.this);
                    // Sử dụng thư viện hình ảnh như Picasso hoặc Glide để tải và hiển thị hình ảnh từ productsImgUrl
                    Picasso.get().load(productsImgUrl).into(productImage);
                    // Cấu hình kích thước và thuộc tính khác của ImageView

                    LinearLayout productInfoLayout = new LinearLayout(MainActivity.this);
                    productInfoLayout.setOrientation(LinearLayout.VERTICAL);

                    TextView productNameText = new TextView(MainActivity.this);
                    productNameText.setText(productsName);

                    TextView productDescriptionText = new TextView(MainActivity.this);
                    productDescriptionText.setText(productsDesciption);

                    TextView productPriceText = new TextView(MainActivity.this);
                    productPriceText.setText(productsPrice);

                    // Thêm các thành phần vào productContainer và productInfoLayout
                    productContainer.addView(productImage);
                    productContainer.addView(productInfoLayout);
                    productInfoLayout.addView(productNameText);
                    productInfoLayout.addView(productDescriptionText);
                    productInfoLayout.addView(productPriceText);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to retrieve categories", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayCategories() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("categories");
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinearLayout categoryLayout = findViewById(R.id.list_categories);
                categoryLayout.removeAllViews(); // Xóa các button cũ trước khi tạo mới

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("name").getValue(String.class);

                    Button categoryButton = new Button(MainActivity.this);
                    categoryButton.setText(categoryName);
                    categoryButton.setAllCaps(false);
                    categoryButton.setBackgroundResource(R.drawable.vien_button);
                    categoryButton.setTextColor(getResources().getColor(R.color.black)); // Áp dụng màu textColor
                    categoryButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17); // Áp dụng kích thước textSize
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(0, 0, 20, 0); // Đặt khoảng cách 20dp bên phải

                    categoryButton.setLayoutParams(layoutParams);
                    categoryLayout.addView(categoryButton); // Thêm button vào LinearLayout
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần thiết
                Toast.makeText(MainActivity.this, "Failed to retrieve categories", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void Logout(){
        mAuth.signOut();
        Toast.makeText(MainActivity.this, "Successful Logout!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    private void displayUsername(){
        String userID = mAuth.getCurrentUser().getUid();
        dt_User = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference userRef = dt_User.child(userID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String username = user.getUsername();
                        hello_name.setText("Hi, " + username);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần thiết
                Toast.makeText(MainActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}