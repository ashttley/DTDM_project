package com.example.foodorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
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


public class CartActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private LinearLayout home, delivery, chat, profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        home = findViewById(R.id.layout_home);
        delivery = findViewById(R.id.layout_delivery);
        chat = findViewById(R.id.layout_chat);
        profile = findViewById(R.id.layout_profile);

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
        String userID = mAuth.getCurrentUser().getUid();
        DatabaseReference dt_cart = FirebaseDatabase.getInstance().getReference("Cart");
        dt_cart.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LinearLayout layout_cart  =  findViewById(R.id.layout_productInCart);
                layout_cart.removeAllViews();

                for(DataSnapshot cartSnapshot : snapshot.getChildren()){
                    String productsName = cartSnapshot.child("name").getValue(String.class);
                    String productsImageUrl = cartSnapshot.child("image").getValue(String.class);
                    String productsPrice = cartSnapshot.child("price").getValue(String.class);
                    int productsQuantity = cartSnapshot.child("quantity").getValue(Integer.class);

                    // tao mot container tong
                    LinearLayout cartContainer = new LinearLayout(CartActivity.this);
                    cartContainer.setOrientation(LinearLayout.HORIZONTAL);

                    LinearLayout.LayoutParams cartParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    cartParams.setMargins(23, 0, 0, 30);
                    cartContainer.setLayoutParams(cartParams);
                    layout_cart.addView(cartContainer);


                    // tao checkbox
                    CheckBox check = new CheckBox(CartActivity.this);
                    int widthCheckbox = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                    int heightCheckbox= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams CheckboxParams = new LinearLayout.LayoutParams(widthCheckbox, heightCheckbox);
                    check.setLayoutParams(CheckboxParams);
                    cartContainer.addView(check);


                    // push anh
                    ImageView productsImage = new ImageView(getApplicationContext());
                    int widthImg= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
                    int heightImg= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
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

                    LinearLayout.LayoutParams inforParams  = new LinearLayout.LayoutParams(
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
                    pricePro.setText("Giá: " + productsPrice + " VND");
                    pricePro.setTextSize(17);
                    inforContainer.addView(pricePro);

                   //container chua button + quantity
                    LinearLayout quantityContainer = new LinearLayout(CartActivity.this);
                    quantityContainer.setOrientation(LinearLayout.HORIZONTAL);
                    int widthSLContainer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    int heightSLContainer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams slContainerParams = new LinearLayout.LayoutParams(widthSLContainer, heightSLContainer);
                    quantityContainer.setLayoutParams(slContainerParams);

                    LinearLayout.LayoutParams quantityParams  = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    quantityParams.setMarginStart(400);
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
                    editQuantityParams.setMargins(20, 0, 20, 20); // marginBottomValue là giá trị của marginBottom
                    quantityPro.setLayoutParams(editQuantityParams);

                    quantityContainer.addView(quantityPro);

                     // button add
                    ImageButton add = new ImageButton(CartActivity.this);
                    add.setBackgroundResource(R.drawable.icon_add);
                    int widthAdd= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                    int heightAdd= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams addParams = new LinearLayout.LayoutParams(widthAdd, heightAdd);
                    add.setLayoutParams(addParams);
                    quantityContainer.addView(add);

                    inforContainer.addView(quantityContainer);
                    cartContainer.addView(inforContainer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Loi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayUserNameCart(){
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference dt_user = FirebaseDatabase.getInstance().getReference("User");
        dt_user.child(userId).addValueEventListener(new ValueEventListener() {
            LinearLayout layout_usercart  =  findViewById(R.id.layout_usernamecart);
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue(String.class);
                TextView userCart = new TextView(CartActivity.this);
                userCart.setText(username + "'s cart");
                userCart.setTextSize(30);
                userCart.setTextColor(getResources().getColor(R.color.camdonau));

                layout_usercart.addView(userCart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Loi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}