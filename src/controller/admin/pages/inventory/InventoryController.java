package controller.admin.pages.inventory;

import controller.admin.pages.items.EditItemsController;
import controller.admin.pages.items.ViewItemController;
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
import javafx.util.StringConverter;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryController {

    @FXML
    public TextField fieldInventorySearch;
    @FXML
    public GridPane formSearchInventoryView;
    @FXML
    private StackPane InventoryContent;
    @FXML
    private TableView<StoreInventory> tableInventoryPage;
    @FXML
    private ComboBox<Store>  selectStore;

    /**
     * This method lists all the product to the view table.
     * It starts a new Task, gets all the products from the database then bind the results to the view.
     * @since                   1.0.0
     */
    @FXML
    public void listStoreInventory() {

        List<Store> allStores = new ArrayList<>();
        Store store = new Store();
        store.setStoreName("-");
        store.setStoreId(0l);
        allStores.add(store);
        allStores.addAll(Datasource.getInstance().getAllStores());
        selectStore.setItems(FXCollections.observableArrayList(allStores));

        selectStore.setConverter(new StringConverter<Store>() {
            @Override
            public String toString(Store object) {
                return object.getStoreName();
            }

            @Override
            public Store fromString(String string) {
                return null;
            }
        });

        Task<ObservableList<StoreInventory>> getAllStoreInventoryTask = new Task<ObservableList<StoreInventory>>() {
            @Override
            protected ObservableList<StoreInventory> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().getAllStoreInventory());
            }
        };

        tableInventoryPage.itemsProperty().bind(getAllStoreInventoryTask.valueProperty());
        addActionButtonsToTable();
        new Thread(getAllStoreInventoryTask).start();

    }

    /**
     * This private method adds the action buttons to the table rows.
     * @since                   1.0.0
     */
    @FXML
    private void addActionButtonsToTable() {
        TableColumn colBtnEdit = new TableColumn("Actions");

        Callback<TableColumn<StoreInventory, Void>, TableCell<StoreInventory, Void>> cellFactory = new Callback<TableColumn<StoreInventory, Void>, TableCell<StoreInventory, Void>>() {
            @Override
            public TableCell<StoreInventory, Void> call(final TableColumn<StoreInventory, Void> param) {
                return new TableCell<StoreInventory, Void>() {

                    private final Button viewButton = new Button("View");

                    {
                        viewButton.getStyleClass().add("button");
                        viewButton.getStyleClass().add("xs");
                        viewButton.getStyleClass().add("info");
                        viewButton.setOnAction((ActionEvent event) -> {
                            StoreInventory inventory = getTableView().getItems().get(getIndex());
                            btnViewStore(inventory.getStoreInventoryId());
                        });
                    }

                    private final Button editButton = new Button("Edit");

                    {
                        editButton.getStyleClass().add("button");
                        editButton.getStyleClass().add("xs");
                        editButton.getStyleClass().add("primary");
                        editButton.setOnAction((ActionEvent event) -> {
                            StoreInventory inventory = getTableView().getItems().get(getIndex());
                            btnEditProduct(inventory.getStoreInventoryId());
                        });
                    }

/*                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.getStyleClass().add("button");
                        deleteButton.getStyleClass().add("xs");
                        deleteButton.getStyleClass().add("danger");
                        deleteButton.setOnAction((ActionEvent event) -> {
                            StoreInventory inventory = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Are you sure that you want to delete " + inventory.getItemName() + " ?");
                            alert.setTitle("Delete " + inventory.getItemName() + " ?");
                            Optional<ButtonType> deleteConfirmation = alert.showAndWait();

                            if (deleteConfirmation.isPresent() && deleteConfirmation.get() == ButtonType.OK) {
                                System.out.println("Delete Product");
                                System.out.println("store id: " + inventory.getItemId());
                                System.out.println("store name: " + inventory.getItemName());
                                if (Datasource.getInstance().deleteStore(inventory.getItemId())) {
                                    getTableView().getItems().remove(getIndex());
                                }
                            }
                        });
                    }*/

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setSpacing(10);
                        buttonsPane.getChildren().add(viewButton);
                        buttonsPane.getChildren().add(editButton);
                        //buttonsPane.getChildren().add(deleteButton);
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

        tableInventoryPage.getColumns().add(colBtnEdit);

    }

    /**
     * This private method handles the products search functionality.
     * It creates a new task, gets the search results from the database and binds them to the view table.
     * @since                   1.0.0
     */
    @FXML
    private void btnInventorySearchOnAction() {
        Store store = selectStore.getSelectionModel().getSelectedItem();
        Task<ObservableList<StoreInventory>> searchStoreInventoryTask = new Task<ObservableList<StoreInventory>>() {
            @Override
            protected ObservableList<StoreInventory> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getAllStoreInventoryForAdmin(fieldInventorySearch.getText(),store.getStoreId()+""));
            }
        };
        tableInventoryPage.itemsProperty().bind(searchStoreInventoryTask.valueProperty());

        new Thread(searchStoreInventoryTask).start();
    }

    /**
     * This private method loads the add product view page.
     * @since                   1.0.0
     */
    @FXML
    private void btnAddInventoryOnClick() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/inventory/add-inventory.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        InventoryContent.getChildren().clear();
        InventoryContent.getChildren().add(root);

    }

    /**
     * This private method loads the edit product view page.
     * @param inventoryId        Product id.
     * @since                   1.0.0
     */
    @FXML
    private void btnEditProduct(long inventoryId) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/inventory/edit-inventory.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        InventoryContent.getChildren().clear();
        InventoryContent.getChildren().add(root);

        EditInventoryController controller = fxmlLoader.getController();
        controller.fillEditingInventoryFields(inventoryId);

    }

    @FXML
    private void btnViewStore(long inventoryId) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/inventory/view-inventory.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        InventoryContent.getChildren().clear();
        InventoryContent.getChildren().add(root);

        ViewInventoryController controller = fxmlLoader.getController();
        controller.fillViewingInventoryFields(inventoryId);
    }
}