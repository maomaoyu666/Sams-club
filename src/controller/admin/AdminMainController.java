package controller.admin;

import controller.UserSessionController;
import controller.admin.pages.HomeController;
import controller.admin.pages.inventory.InventoryController;
import controller.admin.pages.items.ItemsController;
import controller.admin.pages.orders.OrdersController;
import controller.admin.pages.stores.StoresController;
import controller.admin.pages.users.UsersController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminMainController implements Initializable {

    @FXML
    public Button btnStores;
    @FXML
    public Button btnItems;
    @FXML
    public Button btnInventory;
    @FXML
    public Button btnUsers;
    @FXML
    public Button btnOrders;
    @FXML
    public Button lblLogOut;
    @FXML
    public AnchorPane dashHead;
    @FXML
    private StackPane dashContent;
    @FXML
    private Label lblUsrName;
    @FXML
    private Label lblUsrRole;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUsrName.setText(UserSessionController.getUserName());
        lblUsrRole.setText(UserSessionController.getUserRole());

        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/home/home.fxml");
        HomeController homeController = fxmlLoader.getController();
    }

    private FXMLLoader loadFxmlPage(String viewPath) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource(viewPath).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane root = fxmlLoader.getRoot();
        dashContent.getChildren().clear();
        dashContent.getChildren().add(root);

        return fxmlLoader;
    }

    public void btnHomeOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/home/home.fxml");
        HomeController homeController = fxmlLoader.getController();
    }

    public void btnUsersOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/users/users.fxml");
        UsersController controller = fxmlLoader.getController();
        controller.listUsers();
    }

    public void btnStoresOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/stores/stores.fxml");
        StoresController orders = fxmlLoader.getController();
        orders.listStores();
    }

    public void btnItemsOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/items/items.fxml");
        ItemsController controller = fxmlLoader.getController();
        controller.listItems();
    }

    public void btnInventoryOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/inventory/inventory.fxml");
        InventoryController controller = fxmlLoader.getController();
        controller.listStoreInventory();
    }

    public void btnOrdersOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/orders/orders.fxml");
        OrdersController controller = fxmlLoader.getController();
        controller.listOrders();
    }

    public void btnLogOutOnClick(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure that you want to log out?");
        alert.setTitle("Log Out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            UserSessionController.cleanUserSession();
            Stage dialogStage;
            Node node = (Node) actionEvent.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/login.fxml"))));
            dialogStage.setFullScreen(false);
            dialogStage.setScene(scene);
            dialogStage.show();
        }
    }
}
