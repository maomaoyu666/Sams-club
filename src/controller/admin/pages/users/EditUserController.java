package controller.admin.pages.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Categories;
import model.Datasource;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class EditUserController extends UsersController {

    @FXML
    public Text editUserResponse;
    public TextField fieldEditUserName;
    public TextField fieldEditUserContact;
    public ComboBox<Categories> fieldEditUserRole;
    public TextField fieldEditUserId;
    public Text viewUserName;

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
        fieldEditUserRole.setItems(FXCollections.observableArrayList(categories));
    }

    @FXML
    private void btnEditUserOnAction() {
        Categories category = fieldEditUserRole.getSelectionModel().getSelectedItem();

        assert category != null;
        int userId = Integer.parseInt(fieldEditUserId.getText());
        User oldUser = Datasource.getInstance().getUserById(userId);
        String userName = fieldEditUserName.getText();
        String userContact = fieldEditUserContact.getText();
        String userRole = category.getName();

        Task<Boolean> editUserTask = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                return Datasource.getInstance().updateUserInfo(new User(userId, userName, userRole, oldUser.getPasswordHash(), userContact));
            }
        };

        editUserTask.setOnSucceeded(e -> {
            if (editUserTask.valueProperty().get()) {
                editUserResponse.setVisible(true);
            }
        });

        new Thread(editUserTask).start();
    }

    public void fillEditingUserFields(int userId) {
        Task<ObservableList<User>> fillUserTask = new Task<ObservableList<User>>() {
            @Override
            protected ObservableList<User> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getUserById(userId));
            }
        };
        fillUserTask.setOnSucceeded(e -> {
            viewUserName.setText("Editing user info: ");
            fieldEditUserId.setText(String.valueOf(fillUserTask.valueProperty().getValue().get(0).getUserId()));
            fieldEditUserName.setText(fillUserTask.valueProperty().getValue().get(0).getUserName());
            fieldEditUserContact.setText(fillUserTask.valueProperty().getValue().get(0).getUserContact());

            Categories category = new Categories();
            switch (fillUserTask.valueProperty().getValue().get(0).getUserRole()) {
                case "Admin":
                    category.setId(0);
                case "Manager":
                    category.setId(1);
                case "Staff":
                    category.setId(2);
                default:
                    category.setId(2);
            }
            category.setName(fillUserTask.valueProperty().getValue().get(0).getUserRole());
            fieldEditUserRole.getSelectionModel().select(category);
        });

        new Thread(fillUserTask).start();
    }
}
