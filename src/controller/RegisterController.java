package controller;

import app.utils.HelperMethods;
import app.utils.PasswordUtils;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Categories;
import model.Datasource;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterController{

    @FXML
    public TextField usernameField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;

    @FXML
    private ComboBox<Categories> userRoleField;

    Stage dialogStage = new Stage();
    Scene scene;

    @FXML
    private void initialize() {

        List<Categories> categories = new ArrayList<>();
        Categories categoryAdmin = new Categories();
        categoryAdmin.setId(0);
        categoryAdmin.setName("Admin");
        categories.add(categoryAdmin);
        Categories categoryManager = new Categories();
        categoryManager.setId(1);
        categoryManager.setName("Manager");
        categories.add(categoryManager);
        Categories categoryStaff = new Categories();
        categoryStaff.setId(2);
        categoryStaff.setName("Staff");
        categories.add(categoryStaff);
        userRoleField.setItems(FXCollections.observableArrayList(categories));
    }

    /**
     * This method handles the login button action event.
     * It transfers the user screen to the login view.
     * @param actionEvent       Accepts ActionEvent.
     * @throws IOException      If an input or output exception occurred.
     * @since                   1.0.0
     */
    public void handleLoginButtonAction(ActionEvent actionEvent) throws IOException {
        Stage dialogStage;
        Node node = (Node) actionEvent.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login.fxml"))));
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    /**
     * This method handles the register button action event.
     * It gets the user entered data and makes the proper validations.
     * If the entered details are correct, it saves the user data to the database,
     * creates a new UserSessionController instance and transitions the user screen
     * to the appropriate dashboard.
     * @param actionEvent       Accepts ActionEvent.
     * @since                   1.0.0
     */
    public void handleRegisterButtonAction(ActionEvent actionEvent) {
        String validationErrors = "";
        boolean errors = false;
        String userRole = userRoleField.getSelectionModel().getSelectedItem().getName();
        String username = usernameField.getText();
        String email = emailField.getText();
        String providedPassword = passwordField.getText();

        // Validate Full Name
        if (userRole == null || userRole.isEmpty()) {
            validationErrors += "Please enter your user role! \n";
            errors = true;
        } else if (!HelperMethods.validateUserRole(userRole)) {
            validationErrors += "Please enter a valid user role! (Admin, Manager, Staff) \n";
            errors = true;
        }

        // Validate Username
        if (username == null || username.isEmpty()) {
            validationErrors += "Please enter a username! \n";
            errors = true;
        } else if (!HelperMethods.validateUsername(username)) {
            validationErrors += "Please enter a valid Username! \n";
            errors = true;
        } else {
            User userByUsername = model.Datasource.getInstance().getUser(username);
            if (userByUsername != null) {
                validationErrors += "There is already a user registered with this username! \n";
                errors = true;
            }
        }

        // Validate Password
        if (providedPassword == null || providedPassword.isEmpty()) {
            validationErrors += "Please enter the password! \n";
            errors = true;
        } else if (!HelperMethods.validatePassword(providedPassword)){
            validationErrors += "Password must be at least 6 and maximum 16 characters! \n";
            errors = true;
        }

        if (errors) {
            HelperMethods.alertBox(validationErrors, null, "Registration Failed!");
        } else {

            String salt = PasswordUtils.getSalt();
            String securePassword = PasswordUtils.generateSecurePassword(providedPassword, salt);

            Task<Boolean> addUserTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().insertNewUser(new User(0, username, userRole, securePassword, email));
                }
            };

            addUserTask.setOnSucceeded(e -> {
                if (addUserTask.valueProperty().get()) {
                    User user = null;
                    user = Datasource.getInstance().getUser(username);

                    // Method invocation 'getId' may produce 'NullPointerException'
                    assert user != null;

                    UserSessionController.setUserId(user.getUserId());
                    UserSessionController.setUserRole(user.getUserRole());
                    UserSessionController.setUserName(user.getUserName());
                    UserSessionController.setUserContact(user.getUserContact());

                    Node node = (Node) actionEvent.getSource();
                    dialogStage = (Stage) node.getScene().getWindow();
                    dialogStage.close();
                    try {
                        if (user.getUserRole().equals(User.Role.ADMIN.toString())) {
                            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/admin/main.fxml"))));
                        } else if (user.getUserRole().equals(User.Role.MANAGER.toString())) {
                            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/manager/main.fxml"))));
                        } else if (user.getUserRole().equals(User.Role.STAFF.toString())) {
                            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/staff/main.fxml"))));
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    dialogStage.setScene(scene);
                    dialogStage.show();
                }
            });

            new Thread(addUserTask).start();

        }
    }

}
