package io.cosentino.booksource.models;

import java.net.URL;
import java.util.List;

public class Product {
    private String author;
    private String category;
    private String title;
    private String description;
    private Boolean available;
    private double price;
    private String productId;
    private String photoUrl;

    private List<Review> reviews;



    public Product(String author, String category, String title, String description, double price, Boolean available, String productId, String photoUrl) {
        this.author = author;
        this.category = category;
        this.title = title;
        this.description = description;
        this.available = available;
        this.price = price;
        this.productId = productId;
        this.photoUrl = photoUrl;
    }

    public Product(Product other){
        this.author = other.author;
        this.category = other.category;
        this.title = other.title;
        this.description = other.description;
        this.available = other.available;
        this.price = other.price;
        this.productId = other.productId;
        this.photoUrl = other.photoUrl;
        this.reviews = other.reviews;
    }

    public Product() {

    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Product{" +
                "author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", price=" + price +
                ", productId='" + productId + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", reviews=" + reviews +
                '}';
    }
}
