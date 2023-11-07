package controller.admin.pages.orders;

import controller.admin.pages.orders.OrdersController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Datasource;
import model.Order;
import model.SubOrder;

/**
 * {@inheritDoc}
 */
public class ViewOrderController extends OrdersController {

    @FXML
    public GridPane formViewOrder;
    public TextField fieldViewOrderNo;
    public TextField fieldViewItemName;
    public TextField fieldViewCategory;
    public TextField fieldViewSupplierName;
    public TextField fieldViewSupplierContact;
    public TextField fieldViewQuantity;
    public TextField fieldViewOrderDate;
    public TextField fieldViewOrderStatus;
    public TextField fieldViewUserName;

    @FXML
    private TableView<SubOrder> tableStorePage;

    @FXML
    private void initialize() {
//        formViewOrder.setOrders(FXCollections.observableArrayList(Datasource.getInstance().getAllOrders()));
    }

    /**
     * This method gets the data for one product from the database and binds the values to viewing fields.
     * @param OrderId        Order id.
     * @since                   1.0.0
     */
    public void fillViewingOrderFields(long OrderId) {
        Task<ObservableList<Order>> fillOrderTask = new Task<ObservableList<Order>>() {
            @Override
            protected ObservableList<Order> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getOrdersById(OrderId));
            }
        };
        fillOrderTask.setOnSucceeded(e -> {
            fieldViewOrderNo.setText(fillOrderTask.valueProperty().getValue().get(0).getOrderId()+"");
           // fieldViewItemName.setText(fillOrderTask.valueProperty().getValue().get(0).getItemName());
          //  fieldViewCategory.setText(fillOrderTask.valueProperty().getValue().get(0).getCategory());
          //  fieldViewSupplierName.setText(fillOrderTask.valueProperty().getValue().get(0).getSupplierName()+"");
           // fieldViewQuantity.setText(fillOrderTask.valueProperty().getValue().get(0).getQuantity()+"");
          //  fieldViewSupplierContact.setText(fillOrderTask.valueProperty().getValue().get(0).getSupplierContact()+"");
            fieldViewOrderDate.setText(fillOrderTask.valueProperty().getValue().get(0).getOrderDate()+"");
            fieldViewOrderStatus.setText(fillOrderTask.valueProperty().getValue().get(0).getOrderStatus()+"");
          //  fieldViewUserName.setText(fillOrderTask.valueProperty().getValue().get(0).getUserName()+"");
        });

        new Thread(fillOrderTask).start();

        Task<ObservableList<SubOrder>> getAllItemsTask = new Task<ObservableList<SubOrder>>() {
            @Override
            protected ObservableList<SubOrder> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().getSubOrder(OrderId));
            }
        };
        new Thread(getAllItemsTask).start();
        tableStorePage.itemsProperty().bind(getAllItemsTask.valueProperty());
    }
}
