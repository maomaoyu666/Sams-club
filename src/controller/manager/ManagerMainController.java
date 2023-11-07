package controller.manager;

import controller.UserSessionController;
import controller.manager.pages.HomeController;
import controller.manager.pages.items.ItemsController;
import controller.manager.pages.orders.OrdersController;
import controller.admin.pages.users.UsersController;
import controller.manager.pages.inventory.InventoryController;
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

public class ManagerMainController implements Initializable {
    @FXML
    public Button btnStores;
    @FXML
    public Button btnItems;
    @FXML
    public Button btnInventory;
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

        //FXMLLoader fxmlLoader = loadFxmlPage("/view/manager/pages/home/home.fxml");
        //HomeController homeController = fxmlLoader.getController();
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
        if(null!=root){
            dashContent.getChildren().add(root);
        }

        return fxmlLoader;
    }

    public void btnHomeOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/manager/pages/home/home.fxml");
        HomeController homeController = fxmlLoader.getController();
    }

    public void btnItemsOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/manager/pages/items/items.fxml");
        ItemsController controller = fxmlLoader.getController();
        controller.listItems();
    }

    public void btnOrdersOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/manager/pages/orders/orders.fxml");
        OrdersController controller = fxmlLoader.getController();
        controller.listOrders();
    }

    public void btnInventoryOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/manager/pages/inventory/inventory.fxml");
        InventoryController controller = fxmlLoader.getController();
        controller.listStoreInventory();
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
