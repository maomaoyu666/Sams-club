package controller.admin.pages.stores;

import app.utils.HelperMethods;
import controller.admin.AdminMainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.Categories;
import model.Datasource;
import model.Store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class EditStoreController extends StoresController {
    @FXML
    public GridPane formEditStoreView;
    public Text getViewStoreResponse;
    public TextField fieldEditStoreId;
    public TextField fieldEditStoreName;
    public TextField fieldEditStoreLocation;
    public TextField fieldEditStoreContactInfo;
    public ComboBox<Categories> fieldEditStoreType;
    public TextField fieldEditStoreOpeningDate;
    public ComboBox<Categories> fieldEditProductCategoryId;

    public Text viewStoreName;
    public Text viewStoreResponse;

    @FXML
    private void initialize() {
        // fieldEditProductCategoryId.setItems(FXCollections.observableArrayList(Datasource.getInstance().getProductCategories(Datasource.ORDER_BY_ASC)));

//        TextFormatter<Double> textFormatterDouble = formatDoubleField();
//        TextFormatter<Integer> textFormatterInt = formatIntField();
//        fieldEditProductPrice.setTextFormatter(textFormatterDouble);
//        fieldEditProductQuantity.setTextFormatter(textFormatterInt);
        List<Categories> categories = new ArrayList<>();
        Categories categoryAdmin = new Categories();
        categoryAdmin.setId(0);
        categoryAdmin.setName("retail");
        categories.add(categoryAdmin);
        Categories categoryManager = new Categories();
        categoryManager.setId(1);
        categoryManager.setName("warehouse");
        categories.add(categoryManager);
        Categories categoryStaff = new Categories();
        categoryStaff.setId(2);
        categoryStaff.setName("others");
        categories.add(categoryStaff);
        fieldEditStoreType.setItems(FXCollections.observableArrayList(categories));
    }

    /**
     * This private method handles the add product button functionality.
     * It validates user input fields and adds the values to the database.
     * @since                   1.0.0
     */
    @FXML
    private void btnEditStoreOnAction() {
        //Categories category = fieldEditProductCategoryId.getSelectionModel().getSelectedItem();
//        int cat_id = 0;
//        if (category != null) {
//            cat_id = category.getId();
//        }
//
//        assert category != null;
        //if (areStoreInputsValid(fieldEditStoreName.getText(), fieldEditStoreLocation.getText(), fieldEditStoreContactInfo.getText(), fieldEditStoreOpeningDate.getText())) {


            Categories category = fieldEditStoreType.getSelectionModel().getSelectedItem();
            String storeName = fieldEditStoreName.getText();
            String storeLocation = fieldEditStoreLocation.getText();
            String storeContact = fieldEditStoreContactInfo.getText();
            String openingDate = fieldEditStoreOpeningDate.getText();
            String storeId = fieldEditStoreId.getText();
            String storeType = category.getName();

        if(Datasource.getInstance().updateStoreInfo(new Store(Long.valueOf(storeId), storeName, storeLocation, storeContact,storeType, openingDate))){
            viewStoreResponse.setVisible(true);
            HelperMethods.alertBox("Update Store Successful!", null, "Notification");
            System.out.println("Store updated!");
        }

//        Task<Boolean> addProductTask = new Task<Boolean>() {
//                @Override
//                protected Boolean call() {
//                    return Datasource.getInstance().updateStoreInfo(new Store(Long.valueOf(storeId), storeName, storeLocation, storeContact,storeType, openingDate));
//                }
//            };
//
//            addProductTask.setOnSucceeded(e -> {
//                if (addProductTask.valueProperty().get()) {
//                    viewStoreResponse.setVisible(true);
//                    HelperMethods.alertBox("Update Store Success!", null, "Update Success!");
//                    System.out.println("Store edited!");
//                }
//            });
       // }
    }

    /**
     * This method gets the data for one product from the database and binds the values to editing fields.
     * @param storeId        Product id.
     * @since                   1.0.0
     */
    public void fillEditingStoreFields(long storeId) {
        Task<ObservableList<Store>> fillStoreTask = new Task<ObservableList<Store>>() {
            @Override
            protected ObservableList<Store> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getStoreById(storeId));
            }
        };
        fillStoreTask.setOnSucceeded(e -> {
            viewStoreName.setText("Editing " + fillStoreTask.valueProperty().getValue().get(0).getStoreName());
            fieldEditStoreId.setText(String.valueOf(fillStoreTask.valueProperty().getValue().get(0).getStoreId()));
            fieldEditStoreName.setText(String.valueOf(fillStoreTask.valueProperty().getValue().get(0).getStoreName()));
            fieldEditStoreLocation.setText(String.valueOf(fillStoreTask.valueProperty().getValue().get(0).getLocation()));
            fieldEditStoreContactInfo.setText(fillStoreTask.valueProperty().getValue().get(0).getContactEmail());
            fieldEditStoreOpeningDate.setText(String.valueOf(fillStoreTask.valueProperty().getValue().get(0).getOpeningDate()));

            Categories category = new Categories();
            switch (fillStoreTask.valueProperty().getValue().get(0).getStoreType()) {
                case "retail":
                    category.setId(0);
                case "warehouse":
                    category.setId(1);
                case "others":
                    category.setId(2);
                default:
                    category.setId(2);
            }
            category.setName(fillStoreTask.valueProperty().getValue().get(0).getStoreType());
            fieldEditStoreType.getSelectionModel().select(category);
        });

        new Thread(fillStoreTask).start();
    }
}
