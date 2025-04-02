package domainModel;

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
        return name + " "+ price + ", " + stock + "remaining";
    }
}
