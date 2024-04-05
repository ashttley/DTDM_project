package com.example.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    private LinearLayout profile;
    private LinearLayout chat;
    private LinearLayout delivery;
    private LinearLayout cart;
    private DatabaseReference dt_User;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ZohoSalesIQ.init(this.getApplication(),"lcP7eiKnDBYigBZ2doqIP0Yj3%2FB1xhb%2FujjltoC%2F6wjsygyfTP8Js1CURnoJy3vN","VXYedrQX8SnlbJmrcouAR0asbHNvGug1XohqYv84VA7ctZiRRP%2FPaDzA4Yu2ifTQvoFucCLzbok8xCYlypigqIueJvKDP3bOibeL2qmH0oHzXv1h38F4sZ7VMaQEImgP");
//        ZohoSalesIQ.showLauncher(true);

        hello_name = findViewById(R.id.txtHello);
        cart = findViewById(R.id.layout_cart);
        delivery = findViewById(R.id.layout_delivery);
        chat = findViewById(R.id.layout_chat);
        profile = findViewById(R.id.layout_profile);

        mAuth = FirebaseAuth.getInstance();


        //chuyen huong den gio hang
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        //chuyen huong den delivery
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DeliveryActivity.class);
                startActivity(intent);
            }
        });
        //chuyen huong den chat
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
        //chuyen huong den profile
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


        //hien thi cac loai san pham len button
        displayCategories();

        // hien thi username
        displayUsername();

        // hien thi products
        displayProducts();

    }

    private void displayProducts() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinearLayout productsLayout = findViewById(R.id.list_product);
                productsLayout.removeAllViews();

                for (DataSnapshot productsSnapshot : dataSnapshot.getChildren()) {
                    String productsImageUrl = productsSnapshot.child("image").getValue(String.class);
                    String productsName = productsSnapshot.child("name").getValue(String.class);
                    String productsDesciption = productsSnapshot.child("description").getValue(String.class);
                    String productsPrice = productsSnapshot.child("price").getValue(String.class);

                    // Tạo các thành phần bao anh + infor san pham
                    LinearLayout productContainer = new LinearLayout(MainActivity.this);
                    productContainer.setOrientation(LinearLayout.HORIZONTAL);
                    productsLayout.addView(productContainer);

                    //Tao layout bao anh
                    LinearLayout imageContainer = new LinearLayout(MainActivity.this);
                    int widthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    int heightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPx, heightPx);
                    imageContainer.setLayoutParams(layoutParams);
                    productContainer.addView(imageContainer);

                    // anh san pham
                    ImageView productImage = new ImageView(getApplicationContext());
                    Picasso.get().load(productsImageUrl).into(productImage);

                    int widthImg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    int heightImg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams ImageParams = new LinearLayout.LayoutParams(widthImg, heightImg);
                    productImage.setLayoutParams(ImageParams);

                    imageContainer.addView(productImage);

                    // Tạo linearlayout bao phần chữ
                    LinearLayout productInfoLayout = new LinearLayout(MainActivity.this);
                    productInfoLayout.setOrientation(LinearLayout.VERTICAL);
                    int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 470, getResources().getDisplayMetrics());
                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams layoutParamsInfor = new LinearLayout.LayoutParams(width, height);
                    productInfoLayout.setLayoutParams(layoutParamsInfor);

                    // ten san pham
                    TextView productNameText = new TextView(MainActivity.this);
                    productNameText.setText("Tên: " + productsName);
                    productNameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    productNameText.setTextColor(getResources().getColor(R.color.black));

                    LinearLayout.LayoutParams nameTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    nameTextParams.setMarginStart(20);
                    productNameText.setLayoutParams(nameTextParams);

                    productInfoLayout.addView(productNameText);

                    // mo ta san pham
                     TextView productDescriptionText = new TextView(MainActivity.this);
                     productDescriptionText.setText("Mô tả: " +  productsDesciption);
                     productDescriptionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                     productDescriptionText.setTextColor(getResources().getColor(R.color.black));

                    LinearLayout.LayoutParams descriptionTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    descriptionTextParams.setMarginStart(20);
                    productDescriptionText.setLayoutParams(descriptionTextParams);

                     productInfoLayout.addView(productDescriptionText);

                    // gia san pham
                    TextView productPriceText = new TextView(MainActivity.this);
                    productPriceText.setText("Giá: " + productsPrice);
                    productPriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    productPriceText.setTextColor(getResources().getColor(R.color.camdonau));

                    LinearLayout.LayoutParams priceTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    priceTextParams.setMarginStart(20);
                    productPriceText.setLayoutParams(priceTextParams);

                    productInfoLayout.addView(productPriceText);

                    // tao button add
                    ImageButton addButton = new ImageButton(MainActivity.this);
                    addButton.setBackgroundResource(R.drawable.icon_add);


                    int buttonWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                    int buttonHeightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(buttonWidthPx, buttonHeightPx);
                    addButton.setLayoutParams(buttonLayoutParams);

                    LinearLayout.LayoutParams buttonLayoutParamsM = new LinearLayout.LayoutParams(buttonWidthPx, buttonHeightPx);
                    buttonLayoutParamsM.setMarginStart(370);
                    addButton.setLayoutParams(buttonLayoutParamsM);

                    // su ly xu kien khi nhan vao addbutton
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addToCart();
                        }
                    });
                    productInfoLayout.addView(addButton);


                    // Thêm productInfoLayout vào productContainer
                    productContainer.addView(productInfoLayout);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayCategories() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("categories");
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinearLayout categoryLayout = findViewById(R.id.list_categories);
                categoryLayout.removeAllViews();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("name").getValue(String.class);
                    final String categoryID = categorySnapshot.child("id").getValue(String.class);

                    Button categoryButton = new Button(MainActivity.this);
                    categoryButton.setText(categoryName);
                    categoryButton.setAllCaps(false);
                    categoryButton.setBackgroundResource(R.drawable.vien_button);
                    categoryButton.setTextColor(getResources().getColor(R.color.black));
                    categoryButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(0, 0, 20, 0);
                    categoryButton.setLayoutParams(layoutParams);
                    categoryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            displayProductsByCategory(categoryID);
                        }
                    });
                    categoryLayout.addView(categoryButton);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to retrieve categories", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayProductsByCategory(final String categoryID){
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinearLayout productsLayout = findViewById(R.id.list_product);
                productsLayout.removeAllViews();

                for (DataSnapshot productsSnapshot : dataSnapshot.getChildren()) {
                    String productsImageUrl = productsSnapshot.child("image").getValue(String.class);
                    String productsName = productsSnapshot.child("name").getValue(String.class);
                    String productsDesciption = productsSnapshot.child("description").getValue(String.class);
                    String productsPrice = productsSnapshot.child("price").getValue(String.class);
                    String productsCategoriesID = productsSnapshot.child("categories").getValue(String.class);

                    if(productsCategoriesID.equals(categoryID)){
                        // Tạo các thành phần bao anh + infor san pham
                        LinearLayout productContainer = new LinearLayout(MainActivity.this);
                        productContainer.setOrientation(LinearLayout.HORIZONTAL);
                        productsLayout.addView(productContainer);

                        //Tao layout bao anh
                        LinearLayout imageContainer = new LinearLayout(MainActivity.this);
                        int widthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                        int heightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPx, heightPx);
                        imageContainer.setLayoutParams(layoutParams);
                        productContainer.addView(imageContainer);

                        // anh san pham
                        ImageView productImage = new ImageView(getApplicationContext());
                        Picasso.get().load(productsImageUrl).into(productImage);

                        int widthImg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                        int heightImg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams ImageParams = new LinearLayout.LayoutParams(widthImg, heightImg);
                        productImage.setLayoutParams(ImageParams);

                        imageContainer.addView(productImage);

                        // Tạo linearlayout bao phần chữ
                        LinearLayout productInfoLayout = new LinearLayout(MainActivity.this);
                        productInfoLayout.setOrientation(LinearLayout.VERTICAL);
                        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 470, getResources().getDisplayMetrics());
                        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams layoutParamsInfor = new LinearLayout.LayoutParams(width, height);
                        productInfoLayout.setLayoutParams(layoutParamsInfor);

                        // ten san pham
                        TextView productNameText = new TextView(MainActivity.this);
                        productNameText.setText("Tên: " + productsName);
                        productNameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                        productNameText.setTextColor(getResources().getColor(R.color.black));

                        LinearLayout.LayoutParams nameTextParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        nameTextParams.setMarginStart(20);
                        productNameText.setLayoutParams(nameTextParams);

                        productInfoLayout.addView(productNameText);

                        // mo ta san pham
                        TextView productDescriptionText = new TextView(MainActivity.this);
                        productDescriptionText.setText("Mô tả: " +  productsDesciption);
                        productDescriptionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                        productDescriptionText.setTextColor(getResources().getColor(R.color.black));

                        LinearLayout.LayoutParams descriptionTextParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        descriptionTextParams.setMarginStart(20);
                        productDescriptionText.setLayoutParams(descriptionTextParams);

                        productInfoLayout.addView(productDescriptionText);

                        // gia san pham
                        TextView productPriceText = new TextView(MainActivity.this);
                        productPriceText.setText("Giá: " + productsPrice);
                        productPriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                        productPriceText.setTextColor(getResources().getColor(R.color.camdonau));

                        LinearLayout.LayoutParams priceTextParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        priceTextParams.setMarginStart(20);
                        productPriceText.setLayoutParams(priceTextParams);

                        productInfoLayout.addView(productPriceText);

                        // tao button add
                        ImageButton addButton = new ImageButton(MainActivity.this);
                        addButton.setBackgroundResource(R.drawable.icon_add);


                        int buttonWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                        int buttonHeightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(buttonWidthPx, buttonHeightPx);
                        addButton.setLayoutParams(buttonLayoutParams);

                        LinearLayout.LayoutParams buttonLayoutParamsM = new LinearLayout.LayoutParams(buttonWidthPx, buttonHeightPx);
                        buttonLayoutParamsM.setMarginStart(370);
                        addButton.setLayoutParams(buttonLayoutParamsM);

                        productInfoLayout.addView(addButton);


                        // Thêm productInfoLayout vào productContainer
                        productContainer.addView(productInfoLayout);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayUsername(){
        String userID = mAuth.getCurrentUser().getUid();
        dt_User = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference userRef = dt_User.child(userID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
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
    private void addToCart(){

    }
}