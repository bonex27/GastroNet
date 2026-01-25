package domainModel;

import java.util.Objects;

public class Product {

    private int id;
    private String name;
    private String description;
    private Category category;
    private double price;
    private int stock;

    public Product(int id, String name, String description, double price, int stock, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public  Product(Product p) {
        this.id = p.id;
        this.name = p.name;
        this.description = p.description;
        this.category = p.category;
        this.price = p.price;
        this.stock = p.stock;
    }
    //Getter

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    //Setters
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return name + " "+ price + "€ [" + stock + " available] (" + category.getDescription() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id  ;
    }

}
