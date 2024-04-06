package com.example.foodorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class CartActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference dt_cart;
    private LinearLayout home, delivery, chat, profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        home = findViewById(R.id.layout_home);
        delivery = findViewById(R.id.layout_delivery);
        chat = findViewById(R.id.layout_chat);
        profile = findViewById(R.id.layout_profile);

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
        //chuyen huong chat
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ChatActivity.class);
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
        String userId = mAuth.getCurrentUser().getUid();
        dt_cart = FirebaseDatabase.getInstance().getReference("Cart");
        dt_cart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LinearLayout layout_cart = new LinearLayout(CartActivity.this);
                layout_cart.findViewById(R.id.layout_productInCart);
                layout_cart.removeAllViews();
                for(DataSnapshot cartSnapshot : snapshot.getChildren()){
                    String productsKey = cartSnapshot.getKey();
                    String productsName = cartSnapshot.child(productsKey).child("name").getValue(String.class);
                    String productsImageUrl = cartSnapshot.child(productsKey).child("image").getValue(String.class);
                    String productsPrice = cartSnapshot.child(productsKey).child("price").getValue(String.class);
                    int productsQuantity = cartSnapshot.child(productsKey).child("quantity").getValue(Integer.class);

                    // tao mot container chua infor, check, img
                    LinearLayout cartCotainer = new LinearLayout(CartActivity.this);
                    cartCotainer.setOrientation(LinearLayout.HORIZONTAL);
                    layout_cart.addView(cartCotainer);

                    // tao checkbox
                    CheckBox check = new CheckBox(CartActivity.this);
                    int widthCheckbox = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                    int heightCheckbox= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams CheckboxParams = new LinearLayout.LayoutParams(widthCheckbox, heightCheckbox);
                    check.setLayoutParams(CheckboxParams);
                    cartCotainer.addView(check);

                    // tao container bao anh
                    LinearLayout imgContainer = new LinearLayout(CartActivity.this);
                    int widthImgContainer= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    int heightImgContainer= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams ImgContainerParams = new LinearLayout.LayoutParams(widthImgContainer, heightImgContainer);
                    imgContainer.setLayoutParams(ImgContainerParams);
                    cartCotainer.addView(imgContainer);

                    // push anh
                    ImageView productsImage = new ImageView(getApplicationContext());
                    int widthImg= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    int heightImg= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams productsImageParams = new LinearLayout.LayoutParams(widthImg, heightImg);
                    productsImage.setLayoutParams(productsImageParams);
                    Picasso.get().load(productsImageUrl).into(productsImage);
                    imgContainer.addView(productsImage);

                    //tao container chua infro
                    LinearLayout inforContainer = new LinearLayout(CartActivity.this);
                    inforContainer.setOrientation(LinearLayout.VERTICAL);
                    cartCotainer.addView(inforContainer);

                    //push infor
                    TextView namePro = new TextView(CartActivity.this);
                    namePro.setText(productsName);
                    inforContainer.addView(namePro);

                    //push price
                    TextView pricePro = new TextView(CartActivity.this);
                    pricePro.setText(productsPrice);
                    inforContainer.addView(pricePro);

                    //push quantity
                    TextView quantityPro = new TextView(CartActivity.this);
                    quantityPro.setText(productsQuantity);
                    inforContainer.addView(quantityPro);
                    //y tuong: cho tru edittext sl cong
                    //moi khi nhan deu cap nhat lai so luong tren database
                    //khi nhan thanh toan hien thi ra hoa don, nhan dong y la se xoa danh sach san pham
                    // trong gio hang nguoi dung hien tai
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}