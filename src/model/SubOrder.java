package model;

public class SubOrder {

    private long suborderId;
    private long orderId;
    private long storeId;
    private long itemId;
    private int quantity;

    private String storeName;

    private String itemName;
    private String category;
    private String supplierName;
    private String supplierContact;

    // Default constructor
    public SubOrder() {}

    // Parameterized constructor
    public SubOrder(long suborderId, long orderId, long itemId, int quantity) {
        this.suborderId = suborderId;
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public SubOrder(long orderId, long itemId, int storeId,int quantity) {
        this.suborderId = suborderId;
        this.orderId = orderId;
        this.itemId = itemId;
        this.storeId = storeId;
        this.quantity = quantity;
    }

    public SubOrder(String storeName, String itemName, String category, String supplierName, String supplierContact,int quantity) {
        this.storeName = storeName;
        this.itemName = itemName;
        this.category = category;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.quantity = quantity;
    }

    // Getters and Setters

    public long getSuborderId() {
        return suborderId;
    }

    public void setSuborderId(long suborderId) {
        this.suborderId = suborderId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }
}

