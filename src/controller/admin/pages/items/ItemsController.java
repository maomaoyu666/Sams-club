package controller.admin.pages.items;

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
import model.Categories;
import model.Datasource;
import model.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemsController {

    @FXML
    public TextField fieldItemsSearch;
    @FXML
    public GridPane formSearchItemView;
    @FXML
    private StackPane ItemsContent;
    @FXML
    private TableView<Item> tableItemsPage;
    @FXML
    private ComboBox<Categories>  PriceRange;

    /**
     * This method lists all the product to the view table.
     * It starts a new Task, gets all the products from the database then bind the results to the view.
     * @since                   1.0.0
     */
    @FXML
    public void listItems() {

        List<Categories> categories = new ArrayList<>();
        Categories categoryFive = new Categories();
        categoryFive.setId(5);
        categoryFive.setName("-");
        categories.add(categoryFive);
        Categories categoryOne = new Categories();
        categoryOne.setId(1);
        categoryOne.setName("0-10");
        categories.add(categoryOne);
        Categories categoryTwo = new Categories();
        categoryTwo.setId(2);
        categoryTwo.setName("10-50");
        categories.add(categoryTwo);
        Categories categoryThree = new Categories();
        categoryThree.setId(3);
        categoryThree.setName("50-100");
        categories.add(categoryThree);
        Categories categoryFour = new Categories();
        categoryFour.setId(4);
        categoryFour.setName("Over-100");
        categories.add(categoryFour);
        PriceRange.setItems(FXCollections.observableArrayList(categories));

        Task<ObservableList<Item>> getAllItemsTask = new Task<ObservableList<Item>>() {
            @Override
            protected ObservableList<Item> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().getAllItems());
            }
        };

        tableItemsPage.itemsProperty().bind(getAllItemsTask.valueProperty());
        addActionButtonsToTable();
        new Thread(getAllItemsTask).start();

    }

    /**
     * This private method adds the action buttons to the table rows.
     * @since                   1.0.0
     */
    @FXML
    private void addActionButtonsToTable() {
        TableColumn colBtnEdit = new TableColumn("Actions");

        Callback<TableColumn<Item, Void>, TableCell<Item, Void>> cellFactory = new Callback<TableColumn<Item, Void>, TableCell<Item, Void>>() {
            @Override
            public TableCell<Item, Void> call(final TableColumn<Item, Void> param) {
                return new TableCell<Item, Void>() {

                    private final Button viewButton = new Button("View");

                    {
                        viewButton.getStyleClass().add("button");
                        viewButton.getStyleClass().add("xs");
                        viewButton.getStyleClass().add("info");
                        viewButton.setOnAction((ActionEvent event) -> {
                            Item itemData = getTableView().getItems().get(getIndex());
                            btnViewStore(itemData.getItemId());
                        });
                    }

                    private final Button editButton = new Button("Edit");

                    {
                        editButton.getStyleClass().add("button");
                        editButton.getStyleClass().add("xs");
                        editButton.getStyleClass().add("primary");
                        editButton.setOnAction((ActionEvent event) -> {
                            Item itemData = getTableView().getItems().get(getIndex());
                            btnEditProduct(itemData.getItemId());
                        });
                    }

/*                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.getStyleClass().add("button");
                        deleteButton.getStyleClass().add("xs");
                        deleteButton.getStyleClass().add("danger");
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Item itemData = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Are you sure that you want to delete " + itemData.getItemName() + " ?");
                            alert.setTitle("Delete " + itemData.getItemName() + " ?");
                            Optional<ButtonType> deleteConfirmation = alert.showAndWait();

                            if (deleteConfirmation.isPresent() && deleteConfirmation.get() == ButtonType.OK) {
                                System.out.println("Delete Product");
                                System.out.println("store id: " + itemData.getItemId());
                                System.out.println("store name: " + itemData.getItemName());
                                if (Datasource.getInstance().deleteStore(itemData.getItemId())) {
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
                       // buttonsPane.getChildren().add(deleteButton);
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

        tableItemsPage.getColumns().add(colBtnEdit);

    }

    /**
     * This private method handles the products search functionality.
     * It creates a new task, gets the search results from the database and binds them to the view table.
     * @since                   1.0.0
     */
    @FXML
    private void btnItemsSearchOnAction() {
        Categories priceRangSelectedItem = PriceRange.getSelectionModel().getSelectedItem();
        Task<ObservableList<Item>> searchItemsTask = new Task<ObservableList<Item>>() {
            @Override
            protected ObservableList<Item> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getItems(fieldItemsSearch.getText(),Datasource.COLUMN_ITEM_NAME,priceRangSelectedItem.getName()));
            }
        };
        tableItemsPage.itemsProperty().bind(searchItemsTask.valueProperty());

        new Thread(searchItemsTask).start();
    }

    /**
     * This private method loads the add product view page.
     * @since                   1.0.0
     */
    @FXML
    private void btnAddItemsOnClick() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/items/add-items.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        ItemsContent.getChildren().clear();
        ItemsContent.getChildren().add(root);

    }

    /**
     * This private method loads the edit product view page.
     * @param storeId        Product id.
     * @since                   1.0.0
     */
    @FXML
    private void btnEditProduct(long storeId) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/items/edit-items.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        ItemsContent.getChildren().clear();
        ItemsContent.getChildren().add(root);

        EditItemsController controller = fxmlLoader.getController();
        controller.fillEditingStoreFields(storeId);

    }

    @FXML
    private void btnViewStore(long itemId) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/items/view-items.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        ItemsContent.getChildren().clear();
        ItemsContent.getChildren().add(root);

        ViewItemController controller = fxmlLoader.getController();
        controller.fillViewingItemFields(itemId);
    }
}