package com.example.foodorderapp;

public class CartModel {
    private String productKey;
    private String productName;
    private String productPrice;
    private int productQuantity;

    public CartModel(){};
    public CartModel(String key, String name, String price, int quantity){
        this.productKey = key;
        this.productName = name;
        this.productPrice = price;
        this.productQuantity = quantity;
    }
    public String getKey(){
        return this.productKey;
    }
    public void setKey(String key){
        this.productKey = key;
    }

    public String getName(){
        return this.productName;
    }
    public void setName(String name){
        this.productName = name;
    }

    public String getPrice(){
        return this.productPrice;
    }
    public void setPrice(String price){
        this.productPrice = price;
    }

    public int getQuantity(){
        return this.productQuantity;
    }
    public void setProductQuantity(int quantity){
        this.productQuantity = quantity;
    }
}
