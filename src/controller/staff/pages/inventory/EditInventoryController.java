package controller.staff.pages.inventory;

import app.utils.HelperMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Datasource;
import model.Item;
import model.StoreInventory;

/**
 * {@inheritDoc}
 */
public class EditInventoryController extends InventoryController {

    @FXML
    public TextField fieldEditMinimumWarningThreshold;
    public TextField fieldEditMaximumWarningThreshold;
    public TextField fieldEditQuantity;
    public TextField fieldEditInventoryId;

    public TextField fieldEditStore;
    public TextField fieldEditItem;

    @FXML
    private void initialize() {
        // fieldEditProductCategoryId.setItems(FXCollections.observableArrayList(Datasource.getInstance().getProductCategories(Datasource.ORDER_BY_ASC)));

//        TextFormatter<Double> textFormatterDouble = formatDoubleField();
//        TextFormatter<Integer> textFormatterInt = formatIntField();
//        fieldEditProductPrice.setTextFormatter(textFormatterDouble);
//        fieldEditProductQuantity.setTextFormatter(textFormatterInt);
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

        //String minimumWarningThreshold = fieldEditMinimumWarningThreshold.getText();
        //String maximumWarningThreshold = fieldEditMaximumWarningThreshold.getText();
        String quantity = fieldEditQuantity.getText();
        String inventoryId = fieldEditInventoryId.getText();

        // Check product inventory to see if there is sufficient inventory
        StoreInventory storeInventoryById = Datasource.getInstance().getStoreInventoryById(Long.valueOf(inventoryId));
        Item itemQuery = Datasource.getInstance().getItemsById(storeInventoryById.getItemId());

        int oldQuantity = storeInventoryById.getQuantity();
        Integer newQuantity = Integer.valueOf(quantity);
        int itemQueryQuantity = itemQuery.getQuantity();

        if(itemQueryQuantity>=newQuantity){
            // Sub
            if(newQuantity>oldQuantity){
                itemQuery.setQuantity(itemQueryQuantity-(newQuantity-oldQuantity));
            }else if(newQuantity<oldQuantity){
                // ADD
                itemQuery.setQuantity(itemQueryQuantity+(oldQuantity-newQuantity));
            }

        }else{
            HelperMethods.alertBox("The modified inventory cannot be greater than the product inventory!", null, "Inventory Prompt!");
            return;
        }

        Task<Boolean> addInventoryTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().updateStoreInventoryThree(new StoreInventory(
                            Integer.valueOf(inventoryId),(Integer.valueOf(quantity))));
                }
            };

        addInventoryTask.setOnSucceeded(e -> {
                if (addInventoryTask.valueProperty().get()) {
                    Datasource.getInstance().updateItemInfo(itemQuery);
                    HelperMethods.alertBox("Edit Store Inventory Success!", null, "Inventory edited!");
                    System.out.println("Inventory edited!");
                }
            });

            new Thread(addInventoryTask).start();
       // }
    }

    /**
     * This method gets the data for one product from the database and binds the values to editing fields.
     * @param inventoryId
     * @since                   1.0.0
     */
    public void fillEditingInventoryFields(long inventoryId) {
        Task<ObservableList<StoreInventory>> fillInventoryTask = new Task<ObservableList<StoreInventory>>() {
            @Override
            protected ObservableList<StoreInventory> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getStoreInventoryById(inventoryId));
            }
        };
        fillInventoryTask.setOnSucceeded(e -> {
            fieldEditItem.setText(fillInventoryTask.valueProperty().getValue().get(0).getItemName());
            fieldEditQuantity.setText(fillInventoryTask.valueProperty().getValue().get(0).getQuantity()+"");
            fieldEditInventoryId.setText(fillInventoryTask.valueProperty().getValue().get(0).getStoreInventoryId()+"");
        });

        new Thread(fillInventoryTask).start();
    }
}
