package controller.admin.pages.stores;

import app.utils.HelperMethods;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import model.*;

/**
 * {@inheritDoc}
 */
public class AddStoreController extends StoresController {

    @FXML
    public TextField fieldAddStoreName;
    public TextField fieldAddStoreLocation;
    public TextField fieldAddStoreContactInfo;
    public TextField fieldAddStoreType;
    public TextField fieldAddStoreOpeningDate;

    @FXML
    public ComboBox<User> fieldAddUser;


    @FXML
    private void initialize() {

        fieldAddUser.setItems(FXCollections.observableArrayList(Datasource.getInstance().getAllUsersByManager()));

        fieldAddUser.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User object) {
                return object.getUserName();
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });
    }

    /**
     * This private method handles the add product button functionality.
     * It validates user input fields and adds the values to the database.
     * @since                   1.0.0
     */
    @FXML
    private void btnAddStoreOnAction() {
//        Categories category = fieldAddProductCategoryId.getSelectionModel().getSelectedItem();
//        int cat_id = 0;
//        if (category != null) {
//            cat_id = category.getId();
//        }

 //       assert category != null;
        //if (areStoreInputsValid(fieldAddProductName.getText(), fieldAddProductDescription.getText(), fieldAddProductPrice.getText(), fieldAddProductQuantity.getText(), cat_id)) {

            User user = fieldAddUser.getSelectionModel().getSelectedItem();
            Store store = new Store();
            store.setStoreName(fieldAddStoreName.getText());
            store.setContactEmail(fieldAddStoreContactInfo.getText());
            store.setLocation(fieldAddStoreLocation.getText());
            store.setOpeningDate(fieldAddStoreOpeningDate.getText());
            store.setStoreType(fieldAddStoreType.getText());
            store.setStoreUserId(user.getUserId()+"");

        Task<Boolean> addProductTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().insertNewStore(store);
                }
            };

            addProductTask.setOnSucceeded(e -> {
                if (addProductTask.valueProperty().get()) {
                    HelperMethods.alertBox("Created new Store Success!", null, "Created Success!");
                    System.out.println("Product added!");
                }
            });

            new Thread(addProductTask).start();
        //}
    }
}
