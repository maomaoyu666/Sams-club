package controller.admin.pages.inventory;

import app.utils.HelperMethods;
import controller.admin.pages.stores.StoresController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.*;

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

    }

    /**
     * This private method handles the add product button functionality.
     * It validates user input fields and adds the values to the database.
     * @since                   1.0.0
     */
    @FXML
    private void btnEditStoreOnAction() {

        String minimumWarningThreshold = fieldEditMinimumWarningThreshold.getText();
        String maximumWarningThreshold = fieldEditMaximumWarningThreshold.getText();
        String quantity = fieldEditQuantity.getText();
        String inventoryId = fieldEditInventoryId.getText();

        Task<Boolean> addInventoryTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().updateStoreInventoryTwo(new StoreInventory
                            (Integer.valueOf(quantity),Integer.valueOf(minimumWarningThreshold),Integer.valueOf(maximumWarningThreshold),Long.valueOf(inventoryId)));
                }
            };

        addInventoryTask.setOnSucceeded(e -> {
                if (addInventoryTask.valueProperty().get()) {
                  //  viewInventoryResponse.setVisible(true);
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
            fieldEditStore.setText(fillInventoryTask.valueProperty().getValue().get(0).getStoreName());
            fieldEditItem.setText(fillInventoryTask.valueProperty().getValue().get(0).getItemName());
            fieldEditMinimumWarningThreshold.setText(fillInventoryTask.valueProperty().getValue().get(0).getMinimumWarningThreshold()+"");
            fieldEditMaximumWarningThreshold.setText(fillInventoryTask.valueProperty().getValue().get(0).getMaximumWarningThreshold()+"");
            fieldEditQuantity.setText(fillInventoryTask.valueProperty().getValue().get(0).getQuantity()+"");
            fieldEditInventoryId.setText(fillInventoryTask.valueProperty().getValue().get(0).getStoreInventoryId()+"");
        });

        new Thread(fillInventoryTask).start();
    }
}
