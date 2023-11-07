package model;

import controller.UserSessionController;
import org.sqlite.SQLiteJDBCLoader;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class Datasource {

    public static final String DB_NAME = "store_management.sqlite";
    //public static final String CONNECTION_STRING = "jdbc:sqlite:D:\\TechBridge2023\\Sam's_store_management_project\\StoreManagementSystem\\src\\app\\db\\" + DB_NAME;

    public static final String ORDER_BY_ASC = "ORDER BY ASC";

    // User table
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_ROLE = "user_role";
    public static final String COLUMN_USER_CONTACT = "user_contact";
    public static final String COLUMN_USER_PASSWORD = "password_hash";

    // Store table
    public static final String TABLE_STORE = "store";
    public static final String COLUMN_STORE_ID = "store_id";
    public static final String COLUMN_STORE_NAME = "store_name";
    public static final String COLUMN_STORE_LOCATION = "location";
    public static final String COLUMN_STORE_CONTACT_EMAIL = "contact_email";
    public static final String COLUMN_STORE_TYPE = "store_type";
    public static final String COLUMN_STORE_USER_ID = "store_user_id";
    public static final String COLUMN_STORE_OPENING_DATE = "opening_date";

    // Item table
    public static final String TABLE_ITEM = "item";
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_DESCRIPTION = "description";
    public static final String COLUMN_ITEM_CATEGORY = "category";
    public static final String COLUMN_ITEM_PRICE = "price";
    public static final String COLUMN_ITEM_QUANTITY = "quantity";
    public static final String COLUMN_ITEM_SUPPLIERID = "supplier_id";
    public static final String COLUMN_ITEM_USERID = "user_id";
    public static final String COLUMN_ITEM_USERNAME = "user_name";

    // StoreInventory table
    public static final String TABLE_STORE_INVENTORY = "store_inventory";
    public static final String COLUMN_STORE_INVENTORY_ID = "store_inventory_id";
    public static final String COLUMN_STORE_INVENTORY_STORE_ID = "store_id";
    public static final String COLUMN_STORE_INVENTORY_ITEM_ID = "item_id";
    public static final String COLUMN_STORE_INVENTORY_QUANTITY = "quantity";
    public static final String COLUMN_STORE_INVENTORY_MINIMUM_WARNING_THRESHOLD = "minimum_warning_threshold";
    public static final String COLUMN_STORE_INVENTORY_MAXIMUM_WARNING_THRESHOLD = "maximum_warning_threshold";
    public static final String COLUMN_STORE_INVENTORY_AUDIT_STATUS = "audit_status";



    // Supplier table
    public static final String TABLE_SUPPLIER = "supplier";
    public static final String COLUMN_SUPPLIER_ID = "supplier_id";
    public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
    public static final String COLUMN_SUPPLIER_CONTACT = "supplier_contact";
    public static final String COLUMN_SUPPLIER_ITEM_ID = "item_id";

    // Order table
    public static final String TABLE_ORDER = "order";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_STATUS = "order_status";
    public static final String COLUMN_ORDER_DATE = "order_date";

    // SubOrder table
    public static final String TABLE_SUBORDER = "suborder";
    public static final String COLUMN_SUBORDER_ID = "suborder_id";
    public static final String COLUMN_SUBORDER_ORDER_ID = "order_id";
    public static final String COLUMN_SUBORDER_ITEM_ID = "item_id";
    public static final String COLUMN_SUBORDER_QUANTITY = "quantity";


//
//
//    public static final int ORDER_BY_NONE = 1;
//    public static final int ORDER_BY_ASC = 2;
//    public static final int ORDER_BY_DESC = 3;

    private Connection conn;

    private static final Datasource instance = new Datasource();

    private Datasource() {
    }

    public static Datasource getInstance() {
        return instance;
    }

    //public static final String CONNECTION_STRING = "jdbc:sqlite::resource:"+Datasource.getInstance().getClass().getResource("/resources/"+DB_NAME).toString();

    public boolean open() {
        try {
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("jdbc-config.properties");
                BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
                Properties properties = new Properties();
            try {
                properties.load(bf);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            properties.list(System.out);
                System.out.println("==============================================");
                String property = properties.getProperty("jdbc.url");
            System.out.println("property = " +  property);

            conn = DriverManager.getConnection(property);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    private ResultSet executeQuery(StringBuilder query) throws SQLException {
        Statement statement = conn.createStatement();
        return statement.executeQuery(query.toString());
    }

    private StringBuilder queryTable(String tableName) {
        return new StringBuilder("SELECT * FROM " + tableName);
    }

    public int getUserId(){
        return UserSessionController.getUserId();
    }

    public String getUserRole(){
        return UserSessionController.getUserRole();
    }

    // User
    public User getUser(String userName) {
        StringBuilder query = queryTable(TABLE_USER);
        query.append(" WHERE " + COLUMN_USER_NAME + " LIKE ").append("'").append(userName).append("'");
        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            return new User(
                    resultSet.getInt(COLUMN_USER_ID),
                    resultSet.getString(COLUMN_USER_NAME),
                    resultSet.getString(COLUMN_USER_ROLE),
                    resultSet.getString(COLUMN_USER_PASSWORD),
                    resultSet.getString(COLUMN_USER_CONTACT)
            );
        } catch (SQLException ignored) {
            return null;
        }
    }


    public User getUserById(int userId) {
        StringBuilder query = queryTable(TABLE_USER);
        query.append(" WHERE " + COLUMN_USER_ID + " = ").append(userId);
        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            return new User(
                    resultSet.getInt(COLUMN_USER_ID),
                    resultSet.getString(COLUMN_USER_NAME),
                    resultSet.getString(COLUMN_USER_ROLE),
                    resultSet.getString(COLUMN_USER_PASSWORD),
                    resultSet.getString(COLUMN_USER_CONTACT)
            );
        } catch (SQLException ignored) {
            return null;
        }
    }

    public List<User> searchUsers(String userName) {
        StringBuilder query = queryTable(TABLE_USER);
        query.append(" WHERE " + COLUMN_USER_NAME + " LIKE ").append("'%").append(userName).append("%'");
        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            List<User> allUsers = new ArrayList<>();
            do {
                allUsers.add(new User(
                                resultSet.getInt(COLUMN_USER_ID),
                                resultSet.getString(COLUMN_USER_NAME),
                                resultSet.getString(COLUMN_USER_ROLE),
                                resultSet.getString(COLUMN_USER_PASSWORD),
                                resultSet.getString(COLUMN_USER_CONTACT)
                        )
                );
            } while (resultSet.next());
            return allUsers;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public List<User> getAllUsers() {
        StringBuilder query = queryTable(TABLE_USER);
        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            List<User> allUsers = new ArrayList<>();
            do {
                allUsers.add(new User(
                        resultSet.getInt(COLUMN_USER_ID),
                        resultSet.getString(COLUMN_USER_NAME),
                        resultSet.getString(COLUMN_USER_ROLE),
                        resultSet.getString(COLUMN_USER_PASSWORD),
                        resultSet.getString(COLUMN_USER_CONTACT)
                        )
                );
            } while (resultSet.next());
            return allUsers;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public List<User> getAllUsersByManager() {
        StringBuilder query = queryTable(TABLE_USER);
        query.append(" WHERE user_role='Manager'");
        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            List<User> allUsers = new ArrayList<>();
            do {
                allUsers.add(new User(
                                resultSet.getInt(COLUMN_USER_ID),
                                resultSet.getString(COLUMN_USER_NAME),
                                resultSet.getString(COLUMN_USER_ROLE),
                                resultSet.getString(COLUMN_USER_PASSWORD),
                                resultSet.getString(COLUMN_USER_CONTACT)
                        )
                );
            } while (resultSet.next());
            return allUsers;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public boolean insertNewUser(User user) {
        if (getUser(user.getUserName()) != null) {
            return false;
        }

        String sql = "INSERT INTO " + TABLE_USER + " (" + COLUMN_USER_NAME + ", " + COLUMN_USER_ROLE + ", " + COLUMN_USER_PASSWORD + ", " + COLUMN_USER_CONTACT + ") VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getUserRole());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getUserContact());

            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public boolean updateUserInfo(User user) {
        if (getUser(user.getUserName()) == null) {
            return false;
        }

        String sql = "UPDATE " + TABLE_USER + " SET " + COLUMN_USER_NAME+ " = ?, " +
                COLUMN_USER_CONTACT + " = ?, " + COLUMN_USER_ROLE + " = ?, " + COLUMN_USER_PASSWORD + " = ?" + " WHERE " + COLUMN_USER_ID + " = " + user.getUserId();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getUserContact());
            statement.setString(3, user.getUserRole());
            statement.setString(4, user.getPasswordHash());

            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    // Item
    public List<Item> getItems(String keyword, String column) {
        StringBuilder query = queryTable(TABLE_ITEM);
        query.append(" WHERE ").append(column).append(" LIKE ").append("'%").append(keyword).append("%'");

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            List<Item> items = new ArrayList<>();
            do {
                items.add(new Item(
                        resultSet.getLong(COLUMN_ITEM_ID),
                        resultSet.getString(COLUMN_ITEM_NAME),
                        resultSet.getString(COLUMN_ITEM_DESCRIPTION),
                        resultSet.getString(COLUMN_ITEM_CATEGORY),
                        resultSet.getDouble(COLUMN_ITEM_PRICE),
                        resultSet.getInt(COLUMN_ITEM_QUANTITY),
                        resultSet.getLong(COLUMN_ITEM_SUPPLIERID)
                ));
            } while (resultSet.next());
            return items;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public List<Item> getItems(String keyword, String column,String priceRang) {
        StringBuilder query = queryTable(TABLE_ITEM);
        query.append(" WHERE ").append(column).append(" LIKE ").append("'%").append(keyword).append("%'");

        if(!Objects.isNull(priceRang) && !"-".equals(priceRang)){
            String[] priceRangArray = priceRang.split("-");
            String priceRangOneNum = priceRangArray[0];
            String priceRangTwoNum = priceRangArray[1];
            if("Over".equals(priceRangOneNum)){
                query.append(" AND price > 100");
            }else{
                query.append(" AND price BETWEEN "+priceRangOneNum+"  AND   "+priceRangTwoNum+"");
            }
        }


        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            List<Item> items = new ArrayList<>();
            do {
                items.add(new Item(
                        resultSet.getLong(COLUMN_ITEM_ID),
                        resultSet.getString(COLUMN_ITEM_NAME),
                        resultSet.getString(COLUMN_ITEM_DESCRIPTION),
                        resultSet.getString(COLUMN_ITEM_CATEGORY),
                        resultSet.getDouble(COLUMN_ITEM_PRICE),
                        resultSet.getInt(COLUMN_ITEM_QUANTITY),
                        resultSet.getLong(COLUMN_ITEM_SUPPLIERID)
                ));
            } while (resultSet.next());
            return items;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public List<Item> getAllItems() {
        StringBuilder query = queryTable(TABLE_ITEM);

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            List<Item> items = new ArrayList<>();
            do {
                items.add(new Item(
                        resultSet.getLong(COLUMN_ITEM_ID),
                        resultSet.getString(COLUMN_ITEM_NAME),
                        resultSet.getString(COLUMN_ITEM_DESCRIPTION),
                        resultSet.getString(COLUMN_ITEM_CATEGORY),
                        resultSet.getDouble(COLUMN_ITEM_PRICE),
                        resultSet.getInt(COLUMN_ITEM_QUANTITY),
                        resultSet.getLong(COLUMN_ITEM_SUPPLIERID)
                ));
            } while (resultSet.next());
            return items;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public List<Item> getItemsByPriceRange(double lower, double upper) {
        StringBuilder query = queryTable(TABLE_ITEM);
        query.append(" WHERE ").append(COLUMN_ITEM_PRICE).append(" BETWEEN ").append(lower).append(" AND ").append(upper);

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            List<Item> items = new ArrayList<>();
            do {
                items.add(new Item(
                        resultSet.getLong(COLUMN_ITEM_ID),
                        resultSet.getString(COLUMN_ITEM_NAME),
                        resultSet.getString(COLUMN_ITEM_DESCRIPTION),
                        resultSet.getString(COLUMN_ITEM_CATEGORY),
                        resultSet.getDouble(COLUMN_ITEM_PRICE),
                        resultSet.getInt(COLUMN_ITEM_QUANTITY),
                        resultSet.getLong(COLUMN_ITEM_SUPPLIERID)
                ));
            } while (resultSet.next());
            return items;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public boolean insertNewItem(Item item) {
        if (getItems(item.getItemName(), COLUMN_ITEM_NAME) != null) {
            return false;
        }

        String sql = "INSERT INTO " + TABLE_ITEM + " (" + COLUMN_ITEM_NAME + ", " + COLUMN_ITEM_DESCRIPTION + ", " + COLUMN_ITEM_CATEGORY + ", " + COLUMN_ITEM_PRICE + ", " + COLUMN_ITEM_QUANTITY + ") VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getDescription());
            statement.setString(3, item.getCategory());
            statement.setDouble(4, item.getPrice());
            statement.setInt(5, item.getQuantity());

            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public Item getItemsById(long itemId) {
        StringBuilder query = queryTable(TABLE_ITEM);
        query.append(" WHERE " + COLUMN_ITEM_ID + " = ").append(itemId);

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            return new Item(
                    resultSet.getLong(COLUMN_ITEM_ID),
                    resultSet.getString(COLUMN_ITEM_NAME),
                    resultSet.getString(COLUMN_ITEM_DESCRIPTION),
                    resultSet.getString(COLUMN_ITEM_CATEGORY),
                    resultSet.getDouble(COLUMN_ITEM_PRICE),
                    resultSet.getInt(COLUMN_ITEM_QUANTITY),
                    resultSet.getLong(COLUMN_ITEM_SUPPLIERID)
            );
        } catch (SQLException ignored) {
            return null;
        }
    }

    public boolean updateItemInfo(Item item) {
        if (getItems(item.getItemName(), COLUMN_ITEM_NAME) == null) {
            return false;
        }

        String sql = "UPDATE " + TABLE_ITEM + " SET " + COLUMN_ITEM_NAME + " = ?, " +
                COLUMN_ITEM_DESCRIPTION + " = ?, " + COLUMN_ITEM_CATEGORY + " = ?, " + COLUMN_ITEM_PRICE + " = ?, " +
                COLUMN_ITEM_QUANTITY + " = ? " + " WHERE " + COLUMN_ITEM_ID + " = " + item.getItemId();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, item.getItemName());
            statement.setString(2, item.getDescription());
            statement.setString(3, item.getCategory());
            statement.setDouble(4, item.getPrice());
            statement.setInt(5, item.getQuantity());

            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    // Store
    public List<Store> getStores(String keyword) {
        StringBuilder query = queryTable(TABLE_STORE);
        query.append(" WHERE (" + COLUMN_STORE_NAME + " LIKE '%")
                .append(keyword)
                .append("%' OR ")
                .append(COLUMN_STORE_TYPE)
                .append(" LIKE '%")
                .append(keyword)
                .append("%' OR ")
                .append(COLUMN_STORE_LOCATION)
                .append(" LIKE '%")
                .append(keyword)
                .append("%' OR ")
                .append(COLUMN_STORE_OPENING_DATE)
                .append(" LIKE '%")
                .append(keyword)
                .append("%')");

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            List<Store> stores = new ArrayList<>();
            do {
                stores.add(new Store(
                        resultSet.getLong(COLUMN_STORE_ID),
                        resultSet.getString(COLUMN_STORE_NAME),
                        resultSet.getString(COLUMN_STORE_LOCATION),
                        resultSet.getString(COLUMN_STORE_CONTACT_EMAIL),
                        resultSet.getString(COLUMN_STORE_TYPE),
                        resultSet.getString(COLUMN_STORE_OPENING_DATE)
                ));
            } while (resultSet.next());
            return stores;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public Store getStoreByName(String keyword) {
        StringBuilder query = queryTable(TABLE_STORE);
        query.append(" WHERE " + COLUMN_STORE_NAME + " LIKE '").append(keyword).append("'");

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            return new Store(
                    resultSet.getLong(COLUMN_STORE_ID),
                    resultSet.getString(COLUMN_STORE_NAME),
                    resultSet.getString(COLUMN_STORE_LOCATION),
                    resultSet.getString(COLUMN_STORE_CONTACT_EMAIL),
                    resultSet.getString(COLUMN_STORE_TYPE),
                    resultSet.getString(COLUMN_STORE_OPENING_DATE)
            );
        } catch (SQLException ignored) {
            return null;
        }
    }

    public Store getStoreById(long storeId) {
        StringBuilder query = queryTable(TABLE_STORE);
        query.append(" WHERE " + COLUMN_STORE_ID + " = ").append(storeId);

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            return new Store(
                    resultSet.getLong(COLUMN_STORE_ID),
                    resultSet.getString(COLUMN_STORE_NAME),
                    resultSet.getString(COLUMN_STORE_LOCATION),
                    resultSet.getString(COLUMN_STORE_CONTACT_EMAIL),
                    resultSet.getString(COLUMN_STORE_TYPE),
                    resultSet.getString(COLUMN_STORE_OPENING_DATE)
            );
        } catch (SQLException ignored) {
            return null;
        }
    }

    public Store getStoreByStoreUserId() {
        StringBuilder query = queryTable(TABLE_STORE);
        query.append(" WHERE " + COLUMN_STORE_USER_ID + " = ").append(getUserId());

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            return new Store(
                    resultSet.getLong(COLUMN_STORE_ID),
                    resultSet.getString(COLUMN_STORE_NAME),
                    resultSet.getString(COLUMN_STORE_LOCATION),
                    resultSet.getString(COLUMN_STORE_CONTACT_EMAIL),
                    resultSet.getString(COLUMN_STORE_TYPE),
                    resultSet.getString(COLUMN_STORE_OPENING_DATE)
            );
        } catch (SQLException ignored) {
            return null;
        }
    }

    public List<Store> getAllStores() {
        StringBuilder query = queryTable(TABLE_STORE);

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            List<Store> stores = new ArrayList<>();
            do {
                stores.add(new Store(
                        resultSet.getLong(COLUMN_STORE_ID),
                        resultSet.getString(COLUMN_STORE_NAME),
                        resultSet.getString(COLUMN_STORE_LOCATION),
                        resultSet.getString(COLUMN_STORE_CONTACT_EMAIL),
                        resultSet.getString(COLUMN_STORE_TYPE),
                        resultSet.getString(COLUMN_STORE_OPENING_DATE)
                ));
            } while (resultSet.next());
            return stores;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public boolean insertNewStore(Store store) {
        if (getStoreByName(store.getStoreName()) != null) {
            return false;
        }

        String sql = "INSERT INTO " + TABLE_STORE + " (" + COLUMN_STORE_NAME + ", " + COLUMN_STORE_LOCATION + ", "
                + COLUMN_STORE_CONTACT_EMAIL + ", " + COLUMN_STORE_TYPE + ", " + COLUMN_STORE_OPENING_DATE +", "+COLUMN_STORE_USER_ID + ") VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, store.getStoreName());
            statement.setString(2, store.getLocation());
            statement.setString(3, store.getContactEmail());
            statement.setString(4, store.getStoreType());
            statement.setString(5, store.getOpeningDate());
            statement.setString(6, store.getStoreUserId());

            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public List<Categories> getProductCategories(String order){
        return null;
    }

    public boolean updateStoreInfo(Store store) {
        if (getStoreById(store.getStoreId()) == null) {
            return false;
        }

        String sql = "UPDATE " + TABLE_STORE + " SET " + COLUMN_STORE_NAME + " = ?, " +
                COLUMN_STORE_LOCATION + " = ?, " + COLUMN_STORE_CONTACT_EMAIL + " = ?, " + COLUMN_STORE_TYPE + " = ?, " +
                COLUMN_STORE_OPENING_DATE + " = ?" + " WHERE " + COLUMN_STORE_ID + " = " + store.getStoreId();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, store.getStoreName());
            statement.setString(2, store.getLocation());
            statement.setString(3, store.getContactEmail());
            statement.setString(4, store.getStoreType());
            statement.setString(5, store.getOpeningDate());

            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    public boolean deleteStore(long storeId) {
        if (getStoreById(storeId) == null) {
            return false;
        }
        String sql = "DELETE FROM " + TABLE_STORE + " WHERE " + COLUMN_STORE_ID + " = " + storeId;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    // Suppliers
    public Supplier getSupplier(long itemId) {
        StringBuilder query = queryTable(TABLE_SUPPLIER);
        query.append(" WHERE ").append(itemId).append(" = ").append(itemId);

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            return new Supplier(
                    resultSet.getLong(COLUMN_SUBORDER_ID),
                    resultSet.getString(COLUMN_SUPPLIER_NAME),
                    resultSet.getString(COLUMN_SUPPLIER_CONTACT),
                    resultSet.getInt(COLUMN_SUPPLIER_ITEM_ID)
            );
        } catch (SQLException ignored) {
            return null;
        }
    }

    public boolean insertNewSupplier(Supplier supplier) {
        if (getSupplier(supplier.getItemId()) != null) {
            return false;
        }

        String sql = "INSERT INTO " + TABLE_SUPPLIER + " (" + COLUMN_SUPPLIER_NAME + ", " + COLUMN_SUPPLIER_CONTACT + ", " + COLUMN_SUPPLIER_ITEM_ID + ") VALUES (?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, supplier.getSupplierName());
            statement.setString(2, supplier.getSupplierContact());
            statement.setLong(3, supplier.getItemId());

            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public List<Order> getAllOrders() {
        StringBuilder query = queryTable("'"+TABLE_ORDER+"'");

        if(User.Role.MANAGER.toString().equals(getUserRole())){
            query.append(" WHERE user_id ="+getUserId());
        }

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            List<Order> orders = new ArrayList<>();
            do {
                orders.add(new Order(
                        resultSet.getLong(COLUMN_ORDER_ID),
                        resultSet.getString(COLUMN_ORDER_STATUS),
                        resultSet.getString(COLUMN_ORDER_DATE),
                        resultSet.getLong(COLUMN_STORE_ID),
                        resultSet.getLong(COLUMN_ITEM_ID)

                ));
            } while (resultSet.next());
            return orders;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public Order getOrderInfo(long orderId) {
        StringBuilder query = queryTable("`" + TABLE_ORDER + "`" + "INNER JOIN" + TABLE_SUBORDER);
        query.append(" WHERE ").append(COLUMN_ORDER_ID).append(" = ").append(orderId);

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            return new Order(
                    resultSet.getLong(COLUMN_ORDER_ID),
                    resultSet.getString(COLUMN_ORDER_STATUS),
                    resultSet.getString(COLUMN_ORDER_DATE),
                    resultSet.getLong(COLUMN_STORE_ID),
                    resultSet.getLong(COLUMN_ITEM_ID),
                    resultSet.getInt(COLUMN_ITEM_QUANTITY)
            );
        } catch (SQLException ignored) {
            return null;
        }
    }
    public Boolean createOrder(Order order) {
        if (getOrderInfo(order.getOrderId()) != null) {
            return null;
        }
        String sql = " INSERT INTO '" + TABLE_ORDER + "' (" + COLUMN_ORDER_STATUS + ", " + COLUMN_ORDER_DATE + ", "
                + COLUMN_STORE_ID  + ", " + COLUMN_ITEM_ID +  ", " + COLUMN_ITEM_USERID +  ", " + COLUMN_ITEM_USERNAME + ") VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, order.getOrderStatus());
            statement.setString(2, order.getOrderDate());
            statement.setString(3, order.getStoreId()+"");
            statement.setString(4, order.getItemId()+"");
            statement.setString(5, order.getUserId()+"");
            statement.setString(6, order.getUserName()+"");

            statement.executeUpdate();
            ResultSet resultSet = executeQuery(new StringBuilder("SELECT last_insert_rowid();"));
            if (resultSet == null || !resultSet.next()) {
                return false;
            }
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public Long getNewOrderId() {
        String sql = "SELECT MAX("+COLUMN_ORDER_ID+") "+COLUMN_ORDER_ID+" FROM 'order'";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            long orderId = 0l;
            while (resultSet.next()){
               orderId = resultSet.getLong(COLUMN_ORDER_ID);
            }
            return orderId;
        } catch (SQLException ignored) {
            return 0l;
        }
    }

    public List<Order> getOrders(String keyword) {
        StringBuilder query = queryTable("'"+TABLE_ORDER+"'").append(" WHERE 1=1");

        if(User.Role.MANAGER.toString().equals(getUserRole())){
            query.append(" AND user_id ="+getUserId());
        }

        if(!"".equals(keyword)){
            query.append(" AND  ( order_id LIKE '%")
                    .append(keyword)
                    .append("%' OR ")
                    .append(" order_status")
                    .append(" LIKE '%")
                    .append(keyword)
                    .append("%')");
        }


        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            List<Order> orders = new ArrayList<>();
            do {
                orders.add(new Order(
                        resultSet.getLong(COLUMN_ORDER_ID),
                        resultSet.getString(COLUMN_ORDER_STATUS),
                        resultSet.getString(COLUMN_ORDER_DATE),
                        resultSet.getLong(COLUMN_STORE_ID),
                        resultSet.getLong(COLUMN_ITEM_ID)
                ));
            } while (resultSet.next());
            return orders;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public Order getOrdersById(long orderId) {
        StringBuilder query = queryTable("'"+TABLE_ORDER+"'");
        query.append(" WHERE " + COLUMN_ORDER_ID + " = ").append(orderId);
        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            return (new Order(
                    resultSet.getLong(COLUMN_ORDER_ID),
                    resultSet.getString(COLUMN_ORDER_STATUS),
                    resultSet.getString(COLUMN_ORDER_DATE),
                    resultSet.getLong(COLUMN_STORE_ID),
                    resultSet.getLong(COLUMN_ITEM_ID)
            ));
        } catch (SQLException ignored) {
            return null;
        }
    }


    public boolean updateOrderStatus(Order order) {
        if (getOrderInfo(order.getOrderId()) == null) {
            return false;
        }

        String sql = "UPDATE " + TABLE_ORDER + " SET " + COLUMN_ORDER_STATUS + " = ?"
                + " WHERE " + COLUMN_ORDER_ID + " = " + order.getOrderId();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, order.getOrderStatus());

            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public boolean createSubOrder(SubOrder suborder) {
        String sql = "INSERT INTO " + TABLE_SUBORDER + " (" + COLUMN_ORDER_ID + ", " + COLUMN_ITEM_ID + ", " + COLUMN_STORE_ID + ", " + COLUMN_ITEM_QUANTITY + ") VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, suborder.getOrderId());
            statement.setLong(2, suborder.getItemId());
            statement.setLong(3, suborder.getStoreId());
            statement.setInt(4, suborder.getQuantity());

            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    // Inventory
    public List<StoreInventory> getInventoryByStoreId(long storeId) {
        StringBuilder sql = new StringBuilder("SELECT\n" +
                "\ts.store_id,\n" +
                "\ts.item_id,\n" +
                "\ts.quantity,\n" +
                "\ts.minimum_warning_threshold,\n" +
                "\ts.maximum_warning_threshold,\n" +
                "\te.store_name,\n" +
                "\tm.item_name \n" +
                "FROM\n" +
                "\tstore_inventory s\n" +
                "\tINNER JOIN store e ON s.store_id = e.store_id\n" +
                "\tINNER JOIN item m ON s.item_id = m.item_id");
        sql.append(" WHERE s.").append(COLUMN_STORE_ID).append(" = ").append(storeId);
        try {
            ResultSet resultSet = executeQuery(sql);
            List<StoreInventory> storeInventory = new ArrayList<>();
            do {
                storeInventory.add(new StoreInventory(
                        resultSet.getLong(COLUMN_STORE_INVENTORY_ID),
                        resultSet.getLong(COLUMN_STORE_INVENTORY_STORE_ID),
                        resultSet.getLong(COLUMN_STORE_INVENTORY_ITEM_ID),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_QUANTITY),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_MINIMUM_WARNING_THRESHOLD),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_MAXIMUM_WARNING_THRESHOLD),
                        resultSet.getString(COLUMN_STORE_NAME),
                        resultSet.getString(COLUMN_ITEM_NAME)
                ));
            } while (resultSet.next());
            return storeInventory;
        } catch (SQLException ignored) {
            return null;
        }

    }

    public List<StoreInventory> getAllStoreInventory() {
        StringBuilder sql = new StringBuilder("SELECT\n" +
                "\ts."+COLUMN_STORE_INVENTORY_ID+",\n" +
                "\ts.store_id,\n" +
                "\ts.item_id,\n" +
                "\ts.quantity,\n" +
                "\ts.minimum_warning_threshold,\n" +
                "\ts.maximum_warning_threshold,\n" +
                "\ts.audit_status,\n"+
                "\te.store_name,\n" +
                "\tm.item_name \n" +
                "FROM\n" +
                "\tstore_inventory s\n" +
                "\tINNER JOIN store e ON s.store_id = e.store_id\n" +
                "\tINNER JOIN item m ON s.item_id = m.item_id");

        if(User.Role.MANAGER.toString().equals(getUserRole())){
            sql.append(" WHERE e.store_user_id ="+getUserId());
        }
        try {
            ResultSet resultSet = executeQuery(sql);
            List<StoreInventory> storeInventory = new ArrayList<>();
            while (resultSet.next()){
                storeInventory.add(new StoreInventory(
                        resultSet.getLong(COLUMN_STORE_INVENTORY_ID),
                        resultSet.getLong(COLUMN_STORE_INVENTORY_STORE_ID),
                        resultSet.getLong(COLUMN_STORE_INVENTORY_ITEM_ID),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_QUANTITY),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_MINIMUM_WARNING_THRESHOLD),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_MAXIMUM_WARNING_THRESHOLD),
                        resultSet.getString(COLUMN_STORE_NAME),
                        resultSet.getString(COLUMN_ITEM_NAME),
                        resultSet.getString(COLUMN_STORE_INVENTORY_AUDIT_STATUS)
                ));
            }
            return storeInventory;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public List<StoreInventory> getAllStoreInventory(String keyword) {

        StringBuilder sql = new StringBuilder("SELECT\n" +
                "\ts."+COLUMN_STORE_INVENTORY_ID+",\n" +
                "\ts.store_id,\n" +
                "\ts.item_id,\n" +
                "\ts.quantity,\n" +
                "\ts.minimum_warning_threshold,\n" +
                "\ts.maximum_warning_threshold,\n" +
                "\ts.audit_status,\n"+
                "\te.store_name,\n" +
                "\tm.item_name \n" +
                "FROM\n" +
                "\tstore_inventory s\n" +
                "\tINNER JOIN store e ON s.store_id = e.store_id\n" +
                "\tINNER JOIN item m ON s.item_id = m.item_id WHERE");

        if(User.Role.MANAGER.toString().equals(getUserRole())){
            sql.append(" e.store_user_id ="+getUserId()+" AND");
        }

        sql.append(" ( e." + COLUMN_STORE_NAME + " LIKE '%").append(keyword).append("%'");
        sql.append(" OR m."+ COLUMN_ITEM_NAME +" LIKE '%").append(keyword).append("%')");
        try {
            ResultSet resultSet = executeQuery(sql);
            List<StoreInventory> storeInventory = new ArrayList<>();
            while (resultSet.next()){
                storeInventory.add(new StoreInventory(
                        resultSet.getLong(COLUMN_STORE_INVENTORY_ID),
                        resultSet.getLong(COLUMN_STORE_INVENTORY_STORE_ID),
                        resultSet.getLong(COLUMN_STORE_INVENTORY_ITEM_ID),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_QUANTITY),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_MINIMUM_WARNING_THRESHOLD),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_MAXIMUM_WARNING_THRESHOLD),
                        resultSet.getString(COLUMN_STORE_NAME),
                        resultSet.getString(COLUMN_ITEM_NAME),
                        resultSet.getString(COLUMN_STORE_INVENTORY_AUDIT_STATUS)
                ));
            }
            return storeInventory;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public List<StoreInventory> getAllStoreInventoryForAdmin(String keyword,String storeId) {

        StringBuilder sql = new StringBuilder("SELECT\n" +
                "\ts."+COLUMN_STORE_INVENTORY_ID+",\n" +
                "\ts.store_id,\n" +
                "\ts.item_id,\n" +
                "\ts.quantity,\n" +
                "\ts.minimum_warning_threshold,\n" +
                "\ts.maximum_warning_threshold,\n" +
                "\ts.audit_status,\n"+
                "\te.store_name,\n" +
                "\tm.item_name \n" +
                "FROM\n" +
                "\tstore_inventory s\n" +
                "\tINNER JOIN store e ON s.store_id = e.store_id\n" +
                "\tINNER JOIN item m ON s.item_id = m.item_id WHERE");

        if(User.Role.MANAGER.toString().equals(getUserRole())){
            sql.append(" e.store_user_id ="+getUserId()+" AND");
        }

        if(!"".equals(storeId) && !"0".equals(storeId)){
            sql.append(" s.store_id ="+storeId+" AND");
        }

        sql.append(" ( e." + COLUMN_STORE_NAME + " LIKE '%").append(keyword).append("%'");
        sql.append(" OR m."+ COLUMN_ITEM_NAME +" LIKE '%").append(keyword).append("%')");
        try {
            ResultSet resultSet = executeQuery(sql);
            List<StoreInventory> storeInventory = new ArrayList<>();
            while (resultSet.next()){
                storeInventory.add(new StoreInventory(
                        resultSet.getLong(COLUMN_STORE_INVENTORY_ID),
                        resultSet.getLong(COLUMN_STORE_INVENTORY_STORE_ID),
                        resultSet.getLong(COLUMN_STORE_INVENTORY_ITEM_ID),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_QUANTITY),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_MINIMUM_WARNING_THRESHOLD),
                        resultSet.getInt(COLUMN_STORE_INVENTORY_MAXIMUM_WARNING_THRESHOLD),
                        resultSet.getString(COLUMN_STORE_NAME),
                        resultSet.getString(COLUMN_ITEM_NAME),
                        resultSet.getString(COLUMN_STORE_INVENTORY_AUDIT_STATUS)
                ));
            }
            return storeInventory;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public boolean createInventory(StoreInventory inventory) {
        String sql = "INSERT INTO " + TABLE_STORE_INVENTORY + " (" + COLUMN_STORE_INVENTORY_STORE_ID + ", " + COLUMN_STORE_INVENTORY_ITEM_ID
                + ", " + COLUMN_STORE_INVENTORY_QUANTITY + ", " + COLUMN_STORE_INVENTORY_MINIMUM_WARNING_THRESHOLD + ", " + COLUMN_STORE_INVENTORY_MAXIMUM_WARNING_THRESHOLD +
                ", " + COLUMN_STORE_INVENTORY_AUDIT_STATUS + "" +") VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, inventory.getStoreId());
            statement.setLong(2, inventory.getItemId());
            statement.setInt(3, inventory.getQuantity());
            statement.setInt(4, inventory.getMinimumWarningThreshold());
            statement.setInt(5, inventory.getMaximumWarningThreshold());
            statement.setString(6, inventory.getAuditStatus());

            statement.executeUpdate();
            ResultSet resultSet = executeQuery(new StringBuilder("SELECT last_insert_rowid();"));
            if (resultSet == null || !resultSet.next()) {
                return false;
            }
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public boolean auditInventory(long storeInventoryId) {
        String sql = "UPDATE " + TABLE_STORE_INVENTORY + " SET " + COLUMN_STORE_INVENTORY_AUDIT_STATUS + " = 'Approved'"
                + " WHERE " + COLUMN_STORE_INVENTORY_ID + " = " + storeInventoryId;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public boolean updateInventory(Order order) {
        if (getOrderInfo(order.getOrderId()) == null) {
            return false;
        }

        String sql = "UPDATE " + TABLE_ORDER + " SET " + COLUMN_ORDER_STATUS + " = ?"
                + " WHERE " + COLUMN_ORDER_ID + " = " + order.getOrderId();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, order.getOrderStatus());

            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    public boolean assertForStoreAndItem(long storeId, Long itemId) {
      StringBuilder sql = new StringBuilder("SELECT\n" +
              "\tcount( 1 ) as count \n" +
              "FROM\n" +
              "\t"+TABLE_STORE_INVENTORY+" \n" +
              "WHERE\n" +
              "\t"+COLUMN_STORE_ID+" ="+storeId+" \n" +
              "\tAND "+COLUMN_ITEM_ID+" ="+itemId+"");
        try {
            ResultSet query = executeQuery(sql);
            while (query.next()){
                if(0<query.getInt("count")){
                    return true;
                }
            }
            return false;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public StoreInventory getStoreInventoryById(long inventoryId) {
        StringBuilder query = new StringBuilder("SELECT\n" +
                "\ts."+COLUMN_STORE_INVENTORY_ID+",\n" +
                "\ts.store_id,\n" +
                "\ts.item_id,\n" +
                "\ts.quantity,\n" +
                "\ts.minimum_warning_threshold,\n" +
                "\ts.maximum_warning_threshold,\n" +
                "\te.store_name,\n" +
                "\tm.item_name \n" +
                "FROM\n" +
                "\tstore_inventory s\n" +
                "\tINNER JOIN store e ON s.store_id = e.store_id\n" +
                "\tINNER JOIN item m ON s.item_id = m.item_id");
        query.append(" WHERE s." + COLUMN_STORE_INVENTORY_ID + " \n="+inventoryId);
        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            return (new StoreInventory(
                    resultSet.getLong(COLUMN_STORE_INVENTORY_ID),
                    resultSet.getLong(COLUMN_STORE_INVENTORY_STORE_ID),
                    resultSet.getLong(COLUMN_STORE_INVENTORY_ITEM_ID),
                    resultSet.getInt(COLUMN_STORE_INVENTORY_QUANTITY),
                    resultSet.getInt(COLUMN_STORE_INVENTORY_MINIMUM_WARNING_THRESHOLD),
                    resultSet.getInt(COLUMN_STORE_INVENTORY_MAXIMUM_WARNING_THRESHOLD),
                    resultSet.getString(COLUMN_STORE_NAME),
                    resultSet.getString(COLUMN_ITEM_NAME)
            ));
        } catch (SQLException ignored) {
            return null;
        }
    }


    public StoreInventory queryStoreInventory(long storeId, Long itemId) {
        StringBuilder query = new StringBuilder("SELECT\n" +
                "\ts."+COLUMN_STORE_INVENTORY_ID+",\n" +
                "\ts.store_id,\n" +
                "\ts.item_id,\n" +
                "\ts.quantity,\n" +
                "\ts.minimum_warning_threshold,\n" +
                "\ts.maximum_warning_threshold,\n" +
                "\te.store_name,\n" +
                "\tm.item_name \n" +
                "FROM\n" +
                "\tstore_inventory s\n" +
                "\tINNER JOIN store e ON s.store_id = e.store_id\n" +
                "\tINNER JOIN item m ON s.item_id = m.item_id");
        query.append(" WHERE s." + COLUMN_STORE_ID + " \n="+storeId);
        query.append("\n AND s." +COLUMN_ITEM_ID +" \n="+itemId);
        query.append("\n AND s." +COLUMN_STORE_INVENTORY_AUDIT_STATUS +" \n='Approved'");
        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }
            return (new StoreInventory(
                    resultSet.getLong(COLUMN_STORE_INVENTORY_ID),
                    resultSet.getLong(COLUMN_STORE_INVENTORY_STORE_ID),
                    resultSet.getLong(COLUMN_STORE_INVENTORY_ITEM_ID),
                    resultSet.getInt(COLUMN_STORE_INVENTORY_QUANTITY),
                    resultSet.getInt(COLUMN_STORE_INVENTORY_MINIMUM_WARNING_THRESHOLD),
                    resultSet.getInt(COLUMN_STORE_INVENTORY_MAXIMUM_WARNING_THRESHOLD),
                    resultSet.getString(COLUMN_STORE_NAME),
                    resultSet.getString(COLUMN_ITEM_NAME)
            ));
        } catch (SQLException ignored) {
            return null;
        }
    }

    public boolean updateStoreInventory(StoreInventory inventory) {

        String sql = "UPDATE " + TABLE_STORE_INVENTORY + " SET " + COLUMN_STORE_INVENTORY_QUANTITY + " = ?"
                + " WHERE " + COLUMN_STORE_INVENTORY_ID + " = " + inventory.getStoreInventoryId();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, inventory.getQuantity()+"");
            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public boolean updateStoreInventoryTwo(StoreInventory inventory) {

        String sql = "UPDATE " + TABLE_STORE_INVENTORY + " SET " + COLUMN_STORE_INVENTORY_QUANTITY + " = ?,"
                +COLUMN_STORE_INVENTORY_MINIMUM_WARNING_THRESHOLD + " = ?,"+COLUMN_STORE_INVENTORY_MAXIMUM_WARNING_THRESHOLD + " = ? ,"
                +COLUMN_STORE_INVENTORY_AUDIT_STATUS + " = ?"
                + " WHERE " + COLUMN_STORE_INVENTORY_ID + " = " + inventory.getStoreInventoryId();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, inventory.getQuantity()+"");
            statement.setString(2, inventory.getMinimumWarningThreshold()+"");
            statement.setString(3, inventory.getMaximumWarningThreshold()+"");
            statement.setString(4,"Approved");
            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public boolean updateStoreInventoryThree(StoreInventory inventory) {

        String sql = "UPDATE " + TABLE_STORE_INVENTORY + " SET " + COLUMN_STORE_INVENTORY_QUANTITY + " = ?"
                + " WHERE " + COLUMN_STORE_INVENTORY_ID + " = " + inventory.getStoreInventoryId();

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, inventory.getQuantity()+"");
            statement.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    public List<SubOrder> getSubOrder(long orderId) {
        StringBuilder query = new StringBuilder(
                "SELECT\n" +
                "\te.store_name,\n" +
                "\ti.item_name,\n" +
                "\tr.quantity,\n" +
                "\ti.category,\n" +
                "\ts.supplier_name,\n" +
                "\ts.supplier_contact \n" +
                "FROM\n" +
                "\tsuborder r\n" +
                "\tLEFT JOIN store e ON e.store_id = r.store_id\n" +
                "\tLEFT JOIN ITEM I ON r.ITEM_ID = I.ITEM_ID\n" +
                "\tLEFT JOIN SUPPLIER S ON i.SUPPLIER_ID = S.SUPPLIER_ID \n" +
                "WHERE\n" +
                "\tr.order_id = " +orderId);

        try {
            ResultSet resultSet = executeQuery(query);
            if (resultSet == null || !resultSet.next()) {
                return null;
            }


            List<SubOrder> subOrders = new ArrayList<>();
            do{
                subOrders.add( new SubOrder(
                        resultSet.getString(COLUMN_STORE_NAME),
                        resultSet.getString(COLUMN_ITEM_NAME),
                        resultSet.getString(COLUMN_ITEM_CATEGORY),
                        resultSet.getString(COLUMN_SUPPLIER_NAME),
                        resultSet.getString(COLUMN_SUPPLIER_CONTACT),
                        resultSet.getInt(COLUMN_SUBORDER_QUANTITY)
                ));
            }
            while (resultSet.next());
            return subOrders;
        } catch (SQLException ignored) {
            return null;
        }
    }
}
