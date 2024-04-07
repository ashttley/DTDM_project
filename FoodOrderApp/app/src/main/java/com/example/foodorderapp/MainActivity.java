package com.example.foodorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageButton;
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
    private LinearLayout profile,chat, delivery, cart, home;
    private Button btnSearch;
    private EditText searchProduct;
    private DatabaseReference dt_User;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchProduct = findViewById(R.id.txtSearch);
        btnSearch = findViewById(R.id.btn_search);

        hello_name = findViewById(R.id.txtHello);
        cart = findViewById(R.id.layout_cart);
        delivery = findViewById(R.id.layout_delivery);
        chat = findViewById(R.id.layout_chat);
        profile = findViewById(R.id.layout_profile);
        home = findViewById(R.id.layout_home);

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

        // xy ly search
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProduct.setText(null);
                displayProducts();
            }
        });

    }
    private void search() {
        String txtSearch = searchProduct.getText().toString();
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinearLayout productsLayout = findViewById(R.id.list_product);
                productsLayout.removeAllViews();
                Boolean flag = false;
                for (DataSnapshot productsSnapshot : dataSnapshot.getChildren()) {
                    final String productsID = productsSnapshot.getKey();
                    String productsImageUrl = productsSnapshot.child("image").getValue(String.class);
                    String productsName = productsSnapshot.child("name").getValue(String.class);
                    String productsDesciption = productsSnapshot.child("description").getValue(String.class);
                    String productsPrice = productsSnapshot.child("price").getValue(String.class);

                    if (productsName.contains(txtSearch)) {
                        flag = true;
                        // Tạo các thành phần bao gồm hình ảnh và thông tin sản phẩm
                        LinearLayout productContainer = new LinearLayout(MainActivity.this);
                        productContainer.setOrientation(LinearLayout.HORIZONTAL);
                        productsLayout.addView(productContainer);

                        //Tạo layout chứa hình ảnh
                        LinearLayout imageContainer = new LinearLayout(MainActivity.this);
                        int widthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                        int heightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPx, heightPx);
                        imageContainer.setLayoutParams(layoutParams);
                        productContainer.addView(imageContainer);

                        // Hình ảnh sản phẩm
                        ImageView productImage = new ImageView(getApplicationContext());
                        Picasso.get().load(productsImageUrl).into(productImage);

                        int widthImg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                        int heightImg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(widthImg, heightImg);
                        productImage.setLayoutParams(imageParams);

                        imageContainer.addView(productImage);

                        // Tạo LinearLayout chứa phần thông tin
                        LinearLayout productInfoLayout = new LinearLayout(MainActivity.this);
                        productInfoLayout.setOrientation(LinearLayout.VERTICAL);
                        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 470, getResources().getDisplayMetrics());
                        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams layoutParamsInfo = new LinearLayout.LayoutParams(width, height);
                        productInfoLayout.setLayoutParams(layoutParamsInfo);

                        // Tên sản phẩm
                        TextView productNameText = new TextView(MainActivity.this);
                        productNameText.setText("Tên: " + productsName);
                        productNameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                        productNameText.setTextColor(getResources().getColor(R.color.black));

                        LinearLayout.LayoutParams nameTextParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        nameTextParams.setMarginStart(20);
                        productNameText.setLayoutParams(nameTextParams);

                        productInfoLayout.addView(productNameText);

                        // Mô tả sản phẩm
                        TextView productDescriptionText = new TextView(MainActivity.this);
                        productDescriptionText.setText("Mô tả: " + productsDesciption);
                        productDescriptionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                        productDescriptionText.setTextColor(getResources().getColor(R.color.black));

                        LinearLayout.LayoutParams descriptionTextParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        descriptionTextParams.setMarginStart(20);
                        productDescriptionText.setLayoutParams(descriptionTextParams);

                        productInfoLayout.addView(productDescriptionText);

                        // Giá sản phẩm
                        TextView productPriceText = new TextView(MainActivity.this);
                        productPriceText.setText("Giá: " + productsPrice);
                        productPriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                        productPriceText.setTextColor(getResources().getColor(R.color.camdonau));

                        LinearLayout.LayoutParams priceTextParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        priceTextParams.setMarginStart(20);
                        productPriceText.setLayoutParams(priceTextParams);

                        productInfoLayout.addView(productPriceText);

                        // Tạo button "Thêm"
                        ImageButton addButton = new ImageButton(MainActivity.this);
                        addButton.setBackgroundResource(R.drawable.icon_add);

                        int buttonWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                        int buttonHeightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(buttonWidthPx, buttonHeightPx);
                        addButton.setLayoutParams(buttonLayoutParams);

                        LinearLayout.LayoutParams buttonLayoutParamsM = new LinearLayout.LayoutParams(buttonWidthPx, buttonHeightPx);
                        buttonLayoutParamsM.setMarginStart(370);
                        addButton.setLayoutParams(buttonLayoutParamsM);

                        // Xử lý sự kiện khi nhấp vào addButton
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addToCart(productsID,productsImageUrl, productsName, productsPrice);
                            }
                        });
                        productInfoLayout.addView(addButton);

                        // Thêm productInfoLayout vào productContainer
                        productContainer.addView(productInfoLayout);
                    }

                }
                if(flag == false){
                    TextView alert = new TextView(MainActivity.this);
                    alert.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                    alert.setText("Sản phẩm không tồn tại, hãy thử lại!");
                    alert.setTextColor(R.color.gray);
                    productsLayout.addView(alert);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to retrieve products", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayProducts() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinearLayout productsLayout = findViewById(R.id.list_product);
                productsLayout.removeAllViews();

                for (DataSnapshot productsSnapshot : dataSnapshot.getChildren()) {

                    final String productsID = productsSnapshot.getKey();
                    final String productsImageUrl = productsSnapshot.child("image").getValue(String.class);
                    final String productsName = productsSnapshot.child("name").getValue(String.class);
                     String productsDesciption = productsSnapshot.child("description").getValue(String.class);
                    final String productsPrice = productsSnapshot.child("price").getValue(String.class);

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
                    productPriceText.setText("Giá: " + productsPrice + " VND");
                    productPriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    productPriceText.setTextColor(getResources().getColor(R.color.camdonau));

                    LinearLayout.LayoutParams priceTextParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    priceTextParams.setMarginStart(20);
                    productPriceText.setLayoutParams(priceTextParams);

                    productInfoLayout.addView(productPriceText);

                    // tao button add
                    ImageButton addButton = new ImageButton(MainActivity.this);
                    addButton.setBackgroundResource(R.drawable.icon_cart);
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
                            addToCart(productsID,productsImageUrl, productsName, productsPrice);
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
                    final String productsID = productsSnapshot.getKey();
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

                        // su ly xu kien khi nhan vao addbutton
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addToCart(productsID, productsImageUrl, productsName, productsPrice);
                            }
                        });
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
    private void addToCart(final String productsID, final String productsImageUrl, final String productsName, final String productsPrice ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Chọn số lượng");
        EditText quantityEditText = new EditText(MainActivity.this);
        quantityEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(quantityEditText);
        builder.setPositiveButton("Thêm vào giỏ hàng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantityString = quantityEditText.getText().toString();
                String userID = mAuth.getCurrentUser().getUid();
                if (!quantityString.isEmpty()) {
                  DatabaseReference dt_cart = FirebaseDatabase.getInstance().getReference("Cart");
                  dt_cart.child(userID).child(productsID).addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          if(snapshot.exists()){
                              int oldQuantity = snapshot.child("quantity").getValue(Integer.class);
                              int updateQuantity = oldQuantity + Integer.parseInt(quantityString);
                              snapshot.child("quantity").getRef().setValue(updateQuantity);
                              Toast.makeText(MainActivity.this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                          }
                          else{
                              CartModel new_cart = new CartModel(productsName, productsPrice, Integer.parseInt(quantityString),productsImageUrl);
                              DatabaseReference data_newCart = FirebaseDatabase.getInstance().getReference("Cart");
                              data_newCart.child(userID).child(productsID).setValue(new_cart);
                              Toast.makeText(MainActivity.this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                          }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {
                          Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  });
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}