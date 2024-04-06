package com.example.foodorderapp;

public class CartModel {
    private String productName;
    private String productImage;
    private String  productPrice;
    private int productQuantity;

    public CartModel(){};
    public CartModel( String name, String price,  int quantity, String image){
        this.productImage = image;
        this.productName = name;
        this.productPrice = price;
        this.productQuantity = quantity;
    }
    public void setImage(String image){
        this.productImage = image;
    }
    public String getImage(){
        return this.productImage;
    }
    public void setName(String name){
        this.productName = name;
    }
    public String getName(){
        return this.productName;
    }
    public void setPrice(String price){
        this.productPrice = price;
    }
    public String getPrice(){
        return this.productPrice;
    }
    public int getQuantity(){
        return this.productQuantity;
    }
    public void setProductQuantity(int quantity){
        this.productQuantity = quantity;
    }
}
