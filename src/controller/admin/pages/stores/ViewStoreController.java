package controller.admin.pages.stores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.Categories;
import model.Datasource;
import model.Store;

/**
 * {@inheritDoc}
 */
public class ViewStoreController extends StoresController {

    @FXML
    public GridPane formViewStore;
    public TextField fieldViewStoreName;
    public TextField fieldViewStoreLocation;
    public TextField fieldViewStoreType;
    public TextField fieldViewStoreContactInfo;
    public TextField fieldViewStoreOpeningDate;

    @FXML
    private void initialize() {
//        formViewStore.setItems(FXCollections.observableArrayList(Datasource.getInstance().getAllStores()));
    }

    /**
     * This method gets the data for one product from the database and binds the values to viewing fields.
     * @param storeId        Store id.
     * @since                   1.0.0
     */
    public void fillViewingStoreFields(long storeId) {
        Task<ObservableList<Store>> fillStoreTask = new Task<ObservableList<Store>>() {
            @Override
            protected ObservableList<Store> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getStoreById(storeId));
            }
        };
        fillStoreTask.setOnSucceeded(e -> {
            fieldViewStoreName.setText(fillStoreTask.valueProperty().getValue().get(0).getStoreName());
            fieldViewStoreLocation.setText(fillStoreTask.valueProperty().getValue().get(0).getLocation());
            fieldViewStoreContactInfo.setText(fillStoreTask.valueProperty().getValue().get(0).getContactEmail());
            fieldViewStoreOpeningDate.setText(fillStoreTask.valueProperty().getValue().get(0).getOpeningDate());
            fieldViewStoreType.setText(fillStoreTask.valueProperty().getValue().get(0).getStoreType());
        });

        new Thread(fillStoreTask).start();
    }
}
