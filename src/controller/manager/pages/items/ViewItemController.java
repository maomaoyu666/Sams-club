package controller.manager.pages.items;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Datasource;
import model.Item;

/**
 * {@inheritDoc}
 */
public class ViewItemController extends ItemsController {

    @FXML
    public GridPane formViewItem;
    public TextField fieldViewItemName;
    public TextField fieldViewDescription;
    public TextField fieldViewCategory;
    public TextField fieldViewPrice;
    public TextField fieldViewQuantity;

    @FXML
    private void initialize() {
//        formViewItem.setItems(FXCollections.observableArrayList(Datasource.getInstance().getAllItems()));
    }

    /**
     * This method gets the data for one product from the database and binds the values to viewing fields.
     * @param ItemId        Item id.
     * @since                   1.0.0
     */
    public void fillViewingItemFields(long ItemId) {
        Task<ObservableList<Item>> fillItemTask = new Task<ObservableList<Item>>() {
            @Override
            protected ObservableList<Item> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getItemsById(ItemId));
            }
        };
        fillItemTask.setOnSucceeded(e -> {
            fieldViewItemName.setText("Viewing: " + fillItemTask.valueProperty().getValue().get(0).getItemName());
            fieldViewDescription.setText(fillItemTask.valueProperty().getValue().get(0).getDescription());
            fieldViewCategory.setText(fillItemTask.valueProperty().getValue().get(0).getCategory());
            fieldViewPrice.setText(fillItemTask.valueProperty().getValue().get(0).getPrice()+"");
            fieldViewQuantity.setText(fillItemTask.valueProperty().getValue().get(0).getQuantity()+"");
        });

        new Thread(fillItemTask).start();
    }
}
