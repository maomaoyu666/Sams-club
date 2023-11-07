package model;

public class Supplier {

    private long supplierId;
    private String supplierName;
    private String supplierContact;
    private long itemId;

    // Default constructor
    public Supplier() {}

    // Parameterized constructor
    public Supplier(long supplierId, String supplierName, String supplierContact, long itemId) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.itemId = itemId;
    }

    // Getters and Setters

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
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

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

}
