package controller.admin.pages.items;

import app.utils.HelperMethods;
import controller.admin.pages.stores.StoresController;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Datasource;
import model.Item;
import model.Store;

/**
 * {@inheritDoc}
 */
public class AddItemsController extends ItemsController {

    @FXML
    public TextField fieldAddItemName;
    public TextField fieldAddDescription;
    public TextField fieldAddCategory;
    public TextField fieldAddPrice;
    public TextField fieldAddQuantity;


    @FXML
    private void initialize() {/*
        fieldAddProductCategoryId.setItems(FXCollections.observableArrayList(Datasource.getInstance().getProductCategories(Datasource.ORDER_BY_ASC)));

        TextFormatter<Double> textFormatterDouble = formatDoubleField();
        TextFormatter<Integer> textFormatterInt = formatIntField();
        fieldAddProductPrice.setTextFormatter(textFormatterDouble);
        fieldAddProductQuantity.setTextFormatter(textFormatterInt);*/
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

            Item item = new Item();
        item.setItemName(fieldAddItemName.getText());
        item.setDescription(fieldAddDescription.getText());
        item.setCategory(fieldAddCategory.getText());
        item.setPrice(Double.valueOf(fieldAddPrice.getText()));
        item.setQuantity(Integer.valueOf(fieldAddQuantity.getText()));

        Task<Boolean> addItemsTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().insertNewItem(item);
                }
            };

        addItemsTask.setOnSucceeded(e -> {
                if (addItemsTask.valueProperty().get()) {
                    HelperMethods.alertBox("A new item added!", null, "Update");
                    System.out.println("Item added!");
                }
            });

            new Thread(addItemsTask).start();
        //}
    }
}
