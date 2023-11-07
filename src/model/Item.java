package model;

public class Item {

    private Long itemId;
    private String itemName;
    private String description;
    private String category;
    private double price;
    private int quantity;
    private Long supplierId;

    // Default constructor
    public Item() {}

    // Parameterized constructor

    public Item(Long itemId, String itemName, String description, String category, double price, int quantity, Long supplierId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.supplierId = supplierId;
    }


    // Getters and Setters


    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", supplierId=" + supplierId +
                '}';
    }
}
