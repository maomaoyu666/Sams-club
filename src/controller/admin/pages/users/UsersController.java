package controller.admin.pages.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import model.Datasource;
import model.User;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class UsersController {
    @FXML
    public TextField fieldUsersSearch;
    @FXML
    private StackPane usersContent;
    @FXML
    private TableView<User> tableUsersPage;
    @FXML
    public GridPane formEditUserView;

    public void btnUsersSearchOnAction() {
        Task<ObservableList<User>> searchUsersTask = new Task<ObservableList<User>>() {
            @Override
            protected ObservableList<User> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().searchUsers(fieldUsersSearch.getText()));
            }
        };
        tableUsersPage.itemsProperty().bind(searchUsersTask.valueProperty());
        new Thread(searchUsersTask).start();
    }
    @FXML
    public void listUsers() {

        Task<ObservableList<User>> getAllCustomersTask = new Task<ObservableList<User>>() {
            @Override
            protected ObservableList<User> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().getAllUsers());
            }
        };
        tableUsersPage.itemsProperty().bind(getAllCustomersTask.valueProperty());
        addActionButtonsToTable();
        new Thread(getAllCustomersTask).start();
    }

    @FXML
    private void addActionButtonsToTable() {
        TableColumn colBtnEdit = new TableColumn("Actions");

        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<User, Void>() {

                    private final Button editButton = new Button("Edit");

                    {
                        editButton.getStyleClass().add("button");
                        editButton.getStyleClass().add("xs");
                        editButton.getStyleClass().add("primary");
                        editButton.setOnAction((ActionEvent event) -> {
                            User userData = getTableView().getItems().get(getIndex());
                            btnEditUser(userData.getUserId());
                        });
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setSpacing(10);
                        buttonsPane.getChildren().add(editButton);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonsPane);
                        }
                    }
                };
            }
        };

        colBtnEdit.setCellFactory(cellFactory);

        tableUsersPage.getColumns().add(colBtnEdit);

    }

    @FXML
    private void btnEditUser(int userId) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(Objects.requireNonNull(getClass().getResource("/view/admin/pages/users/edit-user.fxml")).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        usersContent.getChildren().clear();
        usersContent.getChildren().add(root);

        EditUserController controller = fxmlLoader.getController();
        controller.fillEditingUserFields(userId);
    }

}
