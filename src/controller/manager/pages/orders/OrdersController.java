package controller.manager.pages.orders;

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
import model.Datasource;
import model.Order;

import java.io.IOException;

public class OrdersController {

    @FXML
    public TextField btnOrdersSearch;
    @FXML
    public GridPane formSearchStoreView;
    @FXML
    private StackPane OrdersContent;
    @FXML
    private TableView<Order> tableOrdersPage;

    /**
     * This method lists all the product to the view table.
     * It starts a new Task, gets all the products from the database then bind the results to the view.
     * @since                   1.0.0
     */
    @FXML
    public void listOrders() {

        Task<ObservableList<Order>> getAllOrdersTask = new Task<ObservableList<Order>>() {
            @Override
            protected ObservableList<Order> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().getAllOrders());
            }
        };

        tableOrdersPage.itemsProperty().bind(getAllOrdersTask.valueProperty());
        addActionButtonsToTable();
        new Thread(getAllOrdersTask).start();

    }

    /**
     * This private method adds the action buttons to the table rows.
     * @since                   1.0.0
     */
    @FXML
    private void addActionButtonsToTable() {
        TableColumn colBtnEdit = new TableColumn("Actions");

        Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                return new TableCell<Order, Void>() {

                    private final Button viewButton = new Button("View");

                    {
                        viewButton.getStyleClass().add("button");
                        viewButton.getStyleClass().add("xs");
                        viewButton.getStyleClass().add("info");
                        viewButton.setOnAction((ActionEvent event) -> {
                            Order orderData = getTableView().getItems().get(getIndex());
                            btnViewOrder(orderData.getOrderId());
                        });
                    }

/*                    private final Button editButton = new Button("Edit");

                    {
                        editButton.getStyleClass().add("button");
                        editButton.getStyleClass().add("xs");
                        editButton.getStyleClass().add("primary");
                        editButton.setOnAction((ActionEvent event) -> {
                            Store storeData = getTableView().getItems().get(getIndex());
                            btnEditProduct(storeData.getStoreId());
                        });
                    }*/

/*                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.getStyleClass().add("button");
                        deleteButton.getStyleClass().add("xs");
                        deleteButton.getStyleClass().add("danger");
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Store storeData = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Are you sure that you want to delete " + storeData.getStoreName() + " ?");
                            alert.setTitle("Delete " + storeData.getStoreName() + " ?");
                            Optional<ButtonType> deleteConfirmation = alert.showAndWait();

                            if (deleteConfirmation.isPresent() && deleteConfirmation.get() == ButtonType.OK) {
                                System.out.println("Delete Product");
                                System.out.println("store id: " + storeData.getStoreId());
                                System.out.println("store name: " + storeData.getStoreName());
                                if (Datasource.getInstance().deleteStore(storeData.getStoreId())) {
                                    getTableView().getItems().remove(getIndex());
                                }
                            }
                        });
                    }*/

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setSpacing(10);
                        buttonsPane.getChildren().add(viewButton);
                        //buttonsPane.getChildren().add(editButton);
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

        tableOrdersPage.getColumns().add(colBtnEdit);

    }

    /**
     * This private method handles the products search functionality.
     * It creates a new task, gets the search results from the database and binds them to the view table.
     * @since                   1.0.0
     */
    @FXML
    private void btnOrdersSearchOnAction() {
        Task<ObservableList<Order>> searchOrdersTask = new Task<ObservableList<Order>>() {
            @Override
            protected ObservableList<Order> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getOrders(btnOrdersSearch.getText()));
            }
        };
        tableOrdersPage.itemsProperty().bind(searchOrdersTask.valueProperty());

        new Thread(searchOrdersTask).start();
    }

    /**
     * This private method loads the add product view page.
     * @since                   1.0.0
     */
    @FXML
    private void btnAddStoreOnClick() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/manager/pages/orders/add-orders.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        OrdersContent.getChildren().clear();
        OrdersContent.getChildren().add(root);

    }

    /**
     * This private method loads the edit product view page.
     * @param storeId        Product id.
     * @since                   1.0.0
     */
    @FXML
    private void btnEditProduct(long storeId) {
/*        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/stores/edit-store.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        OrdersContent.getChildren().clear();
        OrdersContent.getChildren().add(root);

        EditStoreController controller = fxmlLoader.getController();
        controller.fillEditingStoreFields(storeId);*/

    }

    @FXML
    private void btnViewOrder(long orderId) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/manager/pages/orders/view-orders.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        OrdersContent.getChildren().clear();
        OrdersContent.getChildren().add(root);

        ViewOrderController controller = fxmlLoader.getController();
        controller.fillViewingOrderFields(orderId);
    }

    /**
     * This private method loads the add Order view page.
     * @since                   1.0.0
     */
    @FXML
    private void btnAddOrdersOnClick() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/manager/pages/orders/add-orders.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        OrdersContent.getChildren().clear();
        OrdersContent.getChildren().add(root);

    }
}