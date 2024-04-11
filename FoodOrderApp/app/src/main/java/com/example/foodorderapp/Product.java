package com.example.foodorderapp;

public class Product {
    public Product() {
    }

    public Product(String categories, String description, String image, String name, String object, int price, int quantity) {
        this.categories = categories;
        this.description = description;
        this.image = image;
        this.name = name;
        this.object = object;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }




    private String categories;
    private String description;
    private String image;
    private String name;
    private String object;
    private int price;
    private int quantity;
}
