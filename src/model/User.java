package model;

public class User {

    public enum Role {
        ADMIN("Admin"),
        MANAGER("Manager"),
        STAFF("Staff");

        private final String role;

        Role(final String role) {
            this.role = role;
        }

        @Override
        public String toString() {
            return role;
        }
    }

    protected int userId;
    protected String userName;
    protected String userContact;
    protected String userRole;
    protected String passwordHash;

    public User(int userId, String userName, String userRole, String passwordHash, String userContact) {
        this.userId = userId;
        this.userName = userName;
        this.userContact = userContact;
        this.userRole = userRole;
        this.passwordHash = passwordHash;
    }
    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public String getUserRole() {
        return userRole;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }
}
