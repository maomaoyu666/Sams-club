package controller;


/**
 * This class acts as a user session.
 * It stores logged-in user details.
 * It is constructed with the Singleton Design Pattern.
 * This pattern involves a single class which is responsible to create an object while making sure that only single
 * object gets created. This class provides a way to access its only object which can be accessed directly without
 * need to instantiate the object of the class.
 *
 */
public class UserSessionController {

    private static int userId;
    private static String userName;
    private static String userContact;
    private static String userRole;

    /**
     * Create an object of UserSessionController
     */
    private static final UserSessionController instance = new UserSessionController();

    /**
     * Make the constructor private so that this class cannot be instantiated
     */
    private UserSessionController() { }

    /**
     * Get the only object available
     * @return      UserSessionController instance.
     */
    public static UserSessionController getInstance() {
        return instance;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserContact() {
        return userContact;
    }

    public static String getUserRole() {
        return userRole;
    }

    public static void setUserName(String userName) {
        UserSessionController.userName = userName;
    }

    public static void setUserContact(String userContact) {
        UserSessionController.userContact = userContact;
    }

    public static void setUserRole(String userRole) {
        UserSessionController.userRole = userRole;
    }

    public static void setUserId(int userId) {
        UserSessionController.userId = userId;
    }

    public static void cleanUserSession() {
        userId = 0;
        userName = null;
        userContact = null;
        userRole = null;
    }

}
