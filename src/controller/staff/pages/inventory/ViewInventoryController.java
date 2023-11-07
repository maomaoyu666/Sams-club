package controller.staff.pages.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Datasource;
import model.StoreInventory;

/**
 * {@inheritDoc}
 */
public class ViewInventoryController extends InventoryController {

    @FXML
    public GridPane formViewInventory;
    public TextField fieldViewStore;
    public TextField fieldViewItem;
    public TextField fieldViewMinimumWarningThreshold;
    public TextField fieldViewMaximumWarningThreshold;
    public TextField fieldViewQuantity;

    @FXML
    private void initialize() {
//        formViewInventory.setItems(FXCollections.observableArrayList(Datasource.getInstance().getAllItems()));
    }

    /**
     * This method gets the data for one product from the database and binds the values to viewing fields.
     * @param inventoryId        inventoryId id.
     * @since                   1.0.0
     */
    public void fillViewingInventoryFields(long inventoryId) {
        Task<ObservableList<StoreInventory>> fillInventoryTask = new Task<ObservableList<StoreInventory>>() {
            @Override
            protected ObservableList<StoreInventory> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getStoreInventoryById(inventoryId));
            }
        };
        fillInventoryTask.setOnSucceeded(e -> {
            fieldViewItem.setText(fillInventoryTask.valueProperty().getValue().get(0).getItemName());
            fieldViewMinimumWarningThreshold.setText(fillInventoryTask.valueProperty().getValue().get(0).getMinimumWarningThreshold()+"");
            fieldViewMaximumWarningThreshold.setText(fillInventoryTask.valueProperty().getValue().get(0).getMaximumWarningThreshold()+"");
            fieldViewQuantity.setText(fillInventoryTask.valueProperty().getValue().get(0).getQuantity()+"");
        });

        new Thread(fillInventoryTask).start();
    }
}
