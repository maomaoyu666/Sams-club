package model;

public class StoreInventory {

    private long storeInventoryId;
    private long storeId;
    private long itemId;
    private int quantity;

    private int minimumWarningThreshold;
    private int maximumWarningThreshold;
    private String auditStatus;

    private String storeName;
    private String itemName;

    // Default constructor
    public StoreInventory() {}


    public StoreInventory(int quantity, int minimumWarningThreshold, int maximumWarningThreshold,long storeInventoryId) {
        this.quantity = quantity;
        this.minimumWarningThreshold = minimumWarningThreshold;
        this.maximumWarningThreshold = maximumWarningThreshold;
        this.storeInventoryId = storeInventoryId;
    }

    // Parameterized constructor

    public StoreInventory(long storeId, long itemId, int quantity, int minimumWarningThreshold, int maximumWarningThreshold) {
        this.storeId = storeId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.minimumWarningThreshold = minimumWarningThreshold;
        this.maximumWarningThreshold = maximumWarningThreshold;
    }

    public StoreInventory(long storeInventoryId,long storeId, long itemId, int quantity, int minimumWarningThreshold, int maximumWarningThreshold, String storeName, String itemName) {
        this.storeInventoryId = storeInventoryId;
        this.storeId = storeId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.minimumWarningThreshold = minimumWarningThreshold;
        this.maximumWarningThreshold = maximumWarningThreshold;
        this.storeName = storeName;
        this.itemName = itemName;
    }

    public StoreInventory(long storeInventoryId,long storeId, long itemId, int quantity, int minimumWarningThreshold, int maximumWarningThreshold, String storeName, String itemName,String auditStatus) {
        this.storeInventoryId = storeInventoryId;
        this.storeId = storeId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.minimumWarningThreshold = minimumWarningThreshold;
        this.maximumWarningThreshold = maximumWarningThreshold;
        this.storeName = storeName;
        this.itemName = itemName;
        this.auditStatus = auditStatus;
    }

    public StoreInventory(Integer storeInventoryId, int quantity) {
        this.storeInventoryId = storeInventoryId;
        this.quantity = quantity;
    }

    // Getters and Setters

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
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

    public int getMinimumWarningThreshold() {
        return minimumWarningThreshold;
    }

    public void setMinimumWarningThreshold(int minimumWarningThreshold) {
        this.minimumWarningThreshold = minimumWarningThreshold;
    }

    public int getMaximumWarningThreshold() {
        return maximumWarningThreshold;
    }

    public void setMaximumWarningThreshold(int maximumWarningThreshold) {
        this.maximumWarningThreshold = maximumWarningThreshold;
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

    public long getStoreInventoryId() {
        return storeInventoryId;
    }

    public void setStoreInventoryId(long storeInventoryId) {
        this.storeInventoryId = storeInventoryId;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
}
