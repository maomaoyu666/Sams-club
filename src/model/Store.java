package model;

public class Store {

    public enum Type {
        RETAIL("retail"),
        WAREHOUSE("warehouse");

        private final String type;

        Type(final String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    private long storeId;
    private String storeName;
    private String location;
    private String contactEmail;
    private String storeType;
    private String openingDate;

    // Store Manager
    private String storeUserId;

    public Store() {}


    public Store(long storeId, String storeName, String location, String contactEmail, String storeType, String openingDate) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.location = location;
        this.contactEmail = contactEmail;
        this.storeType = storeType;
        this.openingDate = openingDate;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public String getStoreUserId() {
        return storeUserId;
    }

    public void setStoreUserId(String storeUserId) {
        this.storeUserId = storeUserId;
    }
}
