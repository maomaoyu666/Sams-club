package controller.manager.pages.orders;

import app.utils.HelperMethods;
import controller.UserSessionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.*;
import org.sqlite.date.DateFormatUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@inheritDoc}
 */
public class AddOrdersController extends OrdersController {

    @FXML
    public TextField fieldAddItemName;
    public TextField fieldAddDescription;
    public TextField fieldAddCategory;
    public TextField fieldAddPrice;
    public TextField fieldAddQuantity;

    public ComboBox<Store> fieldAddStore;
    public ComboBox<Item> fieldAddItem;
    public TextField fieldAddNum;

    @FXML
    private TableView<Order> tableStorePage;

    private List<Order> orders;


    @FXML
    private void initialize() {
        orders = new ArrayList<>();
        fieldAddItem.setItems(FXCollections.observableArrayList(Datasource.getInstance().getAllItems()));
        //fieldAddStore.setItems(FXCollections.observableArrayList(Datasource.getInstance().getAllStores()));

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

/*        fieldAddStore.setConverter(new StringConverter<Store>() {
            @Override
            public String toString(Store object) {
                return object.getStoreName();
            }

            @Override
            public Store fromString(String string) {
                return null;
            }


        });*/
        //fieldAddNum.setText(UUID.randomUUID().toString());
    }

    /**
     * This private method handles the add product button functionality.
     * It validates user input fields and adds the values to the database.
     * @since                   1.0.0
     */
    @FXML
    private void btnAddOrderOnAction() {

        Store store =  Datasource.getInstance().getStoreByStoreUserId();
        if(Objects.isNull(store)){
            HelperMethods.alertBox("The store manager is not associated with a store!", null, "Unassociated store!");
            return;
        }

        Item item = fieldAddItem.getSelectionModel().getSelectedItem();
        Integer num = Integer.valueOf(fieldAddNum.getText());

        Order order = new Order();
        order.setOrderDate(DateFormatUtils.format(new Date(),"MM-dd-yyyy HH:mm:ss"));
        order.setOrderStatus(Order.Status.PENDING.name());
        order.setStoreId(store.getStoreId());
        order.setItemId(item.getItemId());
        order.setQuantity(num);
        order.setUserId(UserSessionController.getUserId());
        order.setUserName(UserSessionController.getUserName());

        // Store Item Inventory threshold verification
        StoreInventory inventory = Datasource.getInstance().queryStoreInventory(store.getStoreId(), item.getItemId());

        if(Objects.isNull(inventory)){
            HelperMethods.alertBox("This Store Item Not yet listed", null, "Not yet listed!");
            return;
        }
        int quantity = inventory.getQuantity();

        // Query Item quantity Num
        ///Item itemsById = Datasource.getInstance().getItemsById(item.getItemId());
       // int quantity = itemsById.getQuantity();

        if(quantity <inventory.getMinimumWarningThreshold() || quantity > inventory.getMaximumWarningThreshold()){
            HelperMethods.alertBox("This store Item Prompt inventory Greater than or less than Specify threshold", null, "Prompt inventory!");
            return;
        }

        // Determine if there is sufficient inventory
        if(quantity<num){
            HelperMethods.alertBox("This Store Item Insufficient inventory", null, "Insufficient inventory!");
            return;
        }


        // Update Store item inventory
        inventory.setQuantity(quantity-num);
        Datasource.getInstance().updateStoreInventory(inventory);

        Task<Boolean> addOrdersTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().createOrder(order);
                }
            };
        AtomicReference<Long> newOrderId = new AtomicReference<>(0l);
        addOrdersTask.setOnSucceeded(e -> {
                if (addOrdersTask.valueProperty().get()) {
                    HelperMethods.alertBox("Created new Order Success!", null, "Created Success!");
                    System.out.println("Order added!");
                    newOrderId.set(Datasource.getInstance().getNewOrderId());
                    orders.forEach(x->{
                        Datasource.getInstance().createSubOrder(new SubOrder(newOrderId.get(),item.getItemId(),Integer.valueOf(x.getStoreId()+""),Integer.valueOf(fieldAddNum.getText())));
                    });
                }
            });

            new Thread(addOrdersTask).start();
        //}
    }

    public void btnAddItemOnAction(ActionEvent actionEvent) {
        Item item = fieldAddItem.getSelectionModel().getSelectedItem();
        Integer num = Integer.valueOf(fieldAddNum.getText());
        Store store =  Datasource.getInstance().getStoreByStoreUserId();

        if(Objects.isNull(store)){
            HelperMethods.alertBox("The store manager is not associated with a store!", null, "Unassociated store!");
            return;
        }

        StoreInventory inventory = Datasource.getInstance().queryStoreInventory(store.getStoreId(), item.getItemId());

        if(Objects.isNull(inventory)){
            HelperMethods.alertBox("This Store Item Not yet listed", null, "Not yet listed!");
            return;
        }

        int quantity = inventory.getQuantity();

        if(quantity <inventory.getMinimumWarningThreshold() || quantity > inventory.getMaximumWarningThreshold()){
            HelperMethods.alertBox("This store Item Prompt inventory Greater than or less than Specify threshold", null, "Prompt inventory!");
            return;
        }

        // Determine if there is sufficient inventory
        if(quantity<num){
            HelperMethods.alertBox("This Store Item Insufficient inventory", null, "Insufficient inventory!");
            return;
        }

        AtomicReference<Boolean> flag = new AtomicReference<>(false);
        orders.forEach(x->{
            if(x.getItemName().equalsIgnoreCase(item.getItemName())) {
                flag.set(true);
            }
        });

        if(flag.get()){
            HelperMethods.alertBox("This item has been added!", null, "Add Reminder!");
            return;
        }



        Order order = new Order();
        order.setStoreId(store.getStoreId());
        order.setStoreName(store.getStoreName());
        order.setItemId(item.getItemId());
        order.setItemName(item.getItemName());
        order.setQuantity(num);
        orders.add(order);

        Task<ObservableList<Order>> getAllItemsTask = new Task<ObservableList<Order>>() {
            @Override
            protected ObservableList<Order> call() {
                return FXCollections.observableArrayList(orders);
            }
        };
        new Thread(getAllItemsTask).start();
        tableStorePage.itemsProperty().bind(getAllItemsTask.valueProperty());
        addActionButtonsToTable();
    }

    @FXML
    private void addActionButtonsToTable() {
        Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                return new TableCell<Order, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.getStyleClass().add("button");
                        deleteButton.getStyleClass().add("xs");
                        deleteButton.getStyleClass().add("danger");
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Order storeData = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Are you sure that you want to delete " + storeData.getItemName() + " ?");
                            alert.setTitle("Delete " + storeData.getItemName() + " ?");
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
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setSpacing(10);
                        buttonsPane.getChildren().add(deleteButton);
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

    }
}
