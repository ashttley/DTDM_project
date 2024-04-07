package com.example.foodorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CartActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnDathang;
    private LinearLayout home, delivery, profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        home = findViewById(R.id.layout_home);
        delivery = findViewById(R.id.layout_delivery);
        profile = findViewById(R.id.layout_profile);
        btnDathang = findViewById(R.id.btn_datHang);

        mAuth = FirebaseAuth.getInstance();
        //hien thi gio hang cua user
        displayUserNameCart();
        //hien thi san pham trong gio hang cua user
        displayProductsInCart();

        //chuyen huong home
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //chuyen huong den profile
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //chuyen huong delivery
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, DeliveryActivity.class);
                startActivity(intent);
            }
        });


    }


    private void displayProductsInCart(){
        // item thanh toan
        TextView txt_tongTien = findViewById(R.id.txt_tongtien);
        int tongTien = 0;
        String userID = mAuth.getCurrentUser().getUid();
        DatabaseReference dt_cart = FirebaseDatabase.getInstance().getReference("Cart");
        dt_cart.child(userID);
        dt_cart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LinearLayout layout_cart = findViewById(R.id.layout_productInCart);
                layout_cart.removeAllViews();
                // HashMap ánh xạ giữa CheckBox và giá tiền
                HashMap<CheckBox, Integer> productPrices = new HashMap<>();
//                // haspmap anh xa giua checkbox va
//                HashMap<CheckBox, Integer> productPrices = new HashMap<>();
                for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                    String productsName = cartSnapshot.child("name").getValue(String.class);
                    String productsImageUrl = cartSnapshot.child("image").getValue(String.class);
                    String productsPrice = cartSnapshot.child("price").getValue(String.class);
                    final int productsQuantity = cartSnapshot.child("quantity").getValue(Integer.class);

                    // tao mot container tong
                    LinearLayout cartContainer = new LinearLayout(CartActivity.this);
                    cartContainer.setOrientation(LinearLayout.HORIZONTAL);

                    LinearLayout.LayoutParams cartParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    cartParams.setMargins(17, 0, 0, 30);
                    cartContainer.setLayoutParams(cartParams);
                    layout_cart.addView(cartContainer);


                    // tao checkbox
                    CheckBox check = new CheckBox(CartActivity.this);
                    int widthCheckbox = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                    int heightCheckbox = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams CheckboxParams = new LinearLayout.LayoutParams(widthCheckbox, heightCheckbox);
                    check.setLayoutParams(CheckboxParams);
                    cartContainer.addView(check);


                    // push anh
                    ImageView productsImage = new ImageView(getApplicationContext());
                    int widthImg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    int heightImg = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams productsImageParams = new LinearLayout.LayoutParams(widthImg, heightImg);
                    productsImage.setLayoutParams(productsImageParams);
                    Picasso.get().load(productsImageUrl).into(productsImage);
                    cartContainer.addView(productsImage);

                    //tao container chua infor
                    LinearLayout inforContainer = new LinearLayout(CartActivity.this);
                    inforContainer.setOrientation(LinearLayout.VERTICAL);
                    int widthIfContainer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 470, getResources().getDisplayMetrics());
                    int heightIfContainer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams layoutParamsInfor = new LinearLayout.LayoutParams(widthIfContainer, heightIfContainer);
                    inforContainer.setLayoutParams(layoutParamsInfor);

                    LinearLayout.LayoutParams inforParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    inforParams.setMarginStart(20);
                    inforContainer.setLayoutParams(inforParams);


                    //push name
                    TextView namePro = new TextView(CartActivity.this);
                    namePro.setText("Tên: " + productsName);
                    namePro.setTextSize(17);
                    inforContainer.addView(namePro);

                    //push price
                    TextView pricePro = new TextView(CartActivity.this);
                    pricePro.setText("Giá: " + productsPrice + " VNĐ");
                    pricePro.setTextSize(17);
                    inforContainer.addView(pricePro);

                    //container chua button + quantity
                    LinearLayout quantityContainer = new LinearLayout(CartActivity.this);
                    quantityContainer.setOrientation(LinearLayout.HORIZONTAL);
                    int widthSLContainer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    int heightSLContainer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams slContainerParams = new LinearLayout.LayoutParams(widthSLContainer, heightSLContainer);
                    quantityContainer.setLayoutParams(slContainerParams);

                    LinearLayout.LayoutParams quantityParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    quantityParams.setMarginStart(250);
                    quantityContainer.setLayoutParams(quantityParams);


                    //button -
                    ImageButton reduce = new ImageButton(CartActivity.this);
                    reduce.setBackgroundResource(R.drawable.reduce);
                    int widthReduce = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                    int heightReduce = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams reduceParams = new LinearLayout.LayoutParams(widthReduce, heightReduce);
                    reduce.setLayoutParams(reduceParams);
                    quantityContainer.addView(reduce);

                    //push quantity
                    TextView quantityPro = new TextView(CartActivity.this);
                    quantityPro.setText(String.valueOf(productsQuantity));
                    quantityPro.setTextSize(17);
                    LinearLayout.LayoutParams editQuantityParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    editQuantityParams.setMargins(20, 0, 20, 20);
                    quantityPro.setLayoutParams(editQuantityParams);

                    quantityContainer.addView(quantityPro);

                    // button add
                    ImageButton add = new ImageButton(CartActivity.this);
                    add.setBackgroundResource(R.drawable.icon_add);
                    int widthAdd = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                    int heightAdd = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams addParams = new LinearLayout.LayoutParams(widthAdd, heightAdd);
                    add.setLayoutParams(addParams);
                    quantityContainer.addView(add);

                    inforContainer.addView(quantityContainer);
                    cartContainer.addView(inforContainer);


                    //xu ly su kien button
                    reduce.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (productsQuantity > 0) {
                                int updateQuantity = productsQuantity - 1;
                                quantityPro.setText(String.valueOf(updateQuantity));
                                cartSnapshot.child("quantity").getRef().setValue(updateQuantity);
                            }
                            if (productsQuantity == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Xác nhận xóa sản phẩm")
                                        .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?")
                                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                cartSnapshot.getRef().removeValue();
                                            }
                                        })
                                        .setNegativeButton("Hủy", null)
                                        .show();
                            }
                        }
                    });
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (productsQuantity > 0) {
                                int updateQuantity = productsQuantity + 1;
                                quantityPro.setText(String.valueOf(updateQuantity));
                                cartSnapshot.child("quantity").getRef().setValue(updateQuantity);
                            }
                        }
                    });

                    // Cập nhật ánh xạ giá tiền
                    productPrices.put(check, Integer.parseInt(productsPrice) * productsQuantity);
                    // Xử lý sự kiện checkbox
                    check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int selectedCount = 0;
                            int totalAmount = 0;

                            for (Map.Entry<CheckBox, Integer> entry : productPrices.entrySet()) {
                                CheckBox cb = entry.getKey();
                                int price = entry.getValue();

                                if (cb.isChecked()) {
                                    selectedCount++;
                                    totalAmount += price;
                                }
                            }

                            txt_tongTien.setText(String.format("%sVNĐ", String.valueOf(totalAmount)));
                        }
                    });

                    btnDathang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Tạo một danh sách để lưu trữ sản phẩm được chọn
                            List<ProductModel> selectedProducts = new ArrayList<>();

                            // Tính tổng tiền
                            int totalAmount = 0;

                            for (Map.Entry<CheckBox, Integer> entry : productPrices.entrySet()) {
                                CheckBox cb = entry.getKey();
                                int price = entry.getValue();

                                if (cb.isChecked()) {
                                    // Thêm sản phẩm vào danh sách sản phẩm được chọn
                                    selectedProducts.add(getProductFromCheckBox(cb));

                                    // Cập nhật tổng tiền
                                    totalAmount += price;
                                }
                            }

                            // Tạo Intent để chuyển đến hoạt động phieuDatHangActivity
                            Intent intent = new Intent(CartActivity.this, phieuDatHangActivity.class);

                            // Đính kèm danh sách sản phẩm được chọn và tổng tiền vào Intent
                            intent.putExtra("selectedProducts", (ArrayList<ProductModel>) selectedProducts);
                            intent.putExtra("totalAmount", totalAmount);

                            // Chuyển đến hoạt động phieuDatHangActivity
                            startActivity(intent);
                        }
                    });


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Loi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}