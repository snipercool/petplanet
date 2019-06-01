package be.example.petplanet.petplanet.Activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductsClass {
    // Array
    private List<Object> products = new ArrayList<>();

    // Default constructor

    public ProductsClass() {
    }

    // Constructor

    public ProductsClass(List<Object> products) {
        this.products = products;
    }

    // Getters

    public List<Object> getProducts() {
        return products;
    }

    // Setters

    public void setProducts(List<Object> products) {
        this.products = products;
    }
}
