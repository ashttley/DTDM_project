package com.example.foodorderapp;

public class CartModel {
    private String productKey;
    private String productName;
    private Double productPrice;
    private int productQuantity;

    public CartModel(){};
    public CartModel(String key, String name, double price,  int quantity){
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
    public void setName(String name){
        this.productName = name;
    }
    public String getName(){
        return this.productName;
    }
    public void setPrice(double price){
        this.productPrice = price;
    }
    public double getPrice(){
        return this.productPrice;
    }
    public int getQuantity(){
        return this.productQuantity;
    }
    public void setProductQuantity(int quantity){
        this.productQuantity = quantity;
    }
}
