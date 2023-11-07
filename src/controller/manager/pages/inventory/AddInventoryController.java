package controller.manager.pages.inventory;

import app.utils.HelperMethods;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import model.Datasource;
import model.Item;
import model.Store;
import model.StoreInventory;

import java.util.Objects;

/**
 * {@inheritDoc}
 */
public class AddInventoryController extends InventoryController {

    @FXML
    public TextField fieldAddMinimumWarningThreshold;
    public TextField fieldAddMaximumWarningThreshold;
    public TextField fieldAddQuantity;

    public ComboBox<Store> fieldAddStore;
    public ComboBox<Item> fieldAddItem;


    @FXML
    private void initialize() {/*
        fieldAddProductCategoryId.setItems(FXCollections.observableArrayList(Datasource.getInstance().getProductCategories(Datasource.ORDER_BY_ASC)));

        TextFormatter<Double> textFormatterDouble = formatDoubleField();
        TextFormatter<Integer> textFormatterInt = formatIntField();
        fieldAddProductPrice.setTextFormatter(textFormatterDouble);
        fieldAddProductQuantity.setTextFormatter(textFormatterInt);*/
        fieldAddItem.setItems(FXCollections.observableArrayList(Datasource.getInstance().getAllItems()));

        fieldAddItem.setConverter(new StringConverter<Item>() {
            @Override
            public String toString(Item object) {
                return object.getItemName();
            }

            @Override
            public Item fromString(String string) {
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
    private void btnAddInventoryOnAction() {
//        Categories category = fieldAddProductCategoryId.getSelectionModel().getSelectedItem();
//        int cat_id = 0;
//        if (category != null) {
//            cat_id = category.getId();
//        }

 //       assert category != null;
        //if (areStoreInputsValid(fieldAddProductName.getText(), fieldAddProductDescription.getText(), fieldAddProductPrice.getText(), fieldAddProductQuantity.getText(), cat_id)) {

        Integer quantity = Integer.valueOf(fieldAddQuantity.getText());
        Store store =  Datasource.getInstance().getStoreByStoreUserId();
        if(Objects.isNull(store)){
            HelperMethods.alertBox("The store manager is not associated with a store!", null, "Unassociated store!");
            return;
        }

        Item item = fieldAddItem.getSelectionModel().getSelectedItem();
        Integer max = Integer.valueOf(fieldAddMaximumWarningThreshold.getText());
        Integer min = Integer.valueOf(fieldAddMinimumWarningThreshold.getText());

        if(item.getQuantity()<quantity){
            HelperMethods.alertBox("The inventory of this product is insufficient!", null, "Insufficient inventory!");
            return;
        }

        if(Datasource.getInstance().assertForStoreAndItem(store.getStoreId(),item.getItemId())){
            HelperMethods.alertBox("This store's product inventory already exists!", null, "Already exists!");
            return;
        }


        if(max<min){
            HelperMethods.alertBox("The maximum threshold cannot be less than the minimum threshold!", null, "Threshold Prompt!");
            return;
        }

        StoreInventory inventory = new StoreInventory();
        inventory.setStoreId(store.getStoreId());
        inventory.setItemId(item.getItemId());
        inventory.setMinimumWarningThreshold(min);
        inventory.setMaximumWarningThreshold(max);
        inventory.setQuantity(quantity);
        inventory.setAuditStatus("Approved");


        Task<Boolean> addItemsTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().createInventory(inventory);
                }
            };

        addItemsTask.setOnSucceeded(e -> {
                if (addItemsTask.valueProperty().get()) {
                    item.setQuantity(item.getQuantity()-quantity);
                    Datasource.getInstance().updateItemInfo(item);
                    HelperMethods.alertBox("Created new Store Inventory Success!", null, "Created Success!");
                    System.out.println("Inventory added!");
                }
            });

            new Thread(addItemsTask).start();
        //}
    }
}
