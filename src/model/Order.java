package model;

public class Order {

    public enum Status {
        PENDING("PENDING"),
        APPROVED("APPROVED"),
        DELIVERED("DELIVERED");

        private final String status;

        Status(final String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    private long orderId;
    private String orderStatus;
    private String orderDate;
    private long storeId;
    private long itemId;
    private int quantity;

    private String storeName;

    private String itemName;
    private String category;
    private String supplierName;
    private String supplierContact;
    private long suborderId;
    private String userName;
    private long userId;


    // Default constructor
    public Order() {}


    public Order(long orderId, String orderStatus, String orderDate, long storeId, long itemId) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.storeId = storeId;
        this.itemId = itemId;
    }

    // Parameterized constructor
    public Order(long orderId, String orderStatus, String orderDate, long storeId, long itemId, int quantity) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.storeId = storeId;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public Order(long orderId, String orderStatus, String orderDate, long storeId, long itemId, int quantity, String itemName, String category, String supplierName, String supplierContact, long suborderId, String userName, long userId) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.storeId = storeId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.itemName = itemName;
        this.category = category;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.suborderId = suborderId;
        this.userName = userName;
        this.userId = userId;
    }


    // Getters and Setters

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }
    public long getStoreId() {
        return storeId;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public long getSuborderId() {
        return suborderId;
    }

    public void setSuborderId(long suborderId) {
        this.suborderId = suborderId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}

