package controller.admin.pages.items;

import controller.admin.pages.stores.StoresController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.Categories;
import model.Datasource;
import model.Item;
import model.Store;

/**
 * {@inheritDoc}
 */
public class EditItemsController extends StoresController {
    @FXML
    public TextField fieldEditItemId;
    public TextField fieldEditItemName;
    public TextField fieldEditDescription;
    public TextField fieldEditCategory;
    public TextField fieldEditPrice;
    public TextField fieldEditQuantity;
    public ComboBox<Categories> fieldEditProductCategoryId;

    public Text viewItemName;
    public Text viewStoreResponse;

    @FXML
    private void initialize() {
        // fieldEditProductCategoryId.setItems(FXCollections.observableArrayList(Datasource.getInstance().getProductCategories(Datasource.ORDER_BY_ASC)));

//        TextFormatter<Double> textFormatterDouble = formatDoubleField();
//        TextFormatter<Integer> textFormatterInt = formatIntField();
//        fieldEditProductPrice.setTextFormatter(textFormatterDouble);
//        fieldEditProductQuantity.setTextFormatter(textFormatterInt);
    }

    /**
     * This private method handles the add product button functionality.
     * It validates user input fields and adds the values to the database.
     * @since                   1.0.0
     */
    @FXML
    private void btnEditStoreOnAction() {
        //Categories category = fieldEditProductCategoryId.getSelectionModel().getSelectedItem();
//        int cat_id = 0;
//        if (category != null) {
//            cat_id = category.getId();
//        }
//
//        assert category != null;
        //if (areStoreInputsValid(fieldEditStoreName.getText(), fieldEditStoreLocation.getText(), fieldEditStoreContactInfo.getText(), fieldEditStoreOpeningDate.getText())) {

            String itemId = fieldEditItemId.getText();
            String itemName = fieldEditItemName.getText();
            String description = fieldEditDescription.getText();
            String category = fieldEditCategory.getText();
            String price = fieldEditPrice.getText();
            String quantity = fieldEditQuantity.getText();



        Task<Boolean> addItemTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().updateItemInfo(new Item(Long.valueOf(itemId), itemName, description, category,Double.valueOf(price), Integer.valueOf(quantity),1l));
                }
            };

        addItemTask.setOnSucceeded(e -> {
                if (addItemTask.valueProperty().get()) {
                    viewStoreResponse.setVisible(true);

                    System.out.println("Item edited!");
                }
            });

            new Thread(addItemTask).start();
       // }
    }

    /**
     * This method gets the data for one product from the database and binds the values to editing fields.
     * @param ItemId        Product id.
     * @since                   1.0.0
     */
    public void fillEditingStoreFields(long ItemId) {
        Task<ObservableList<Item>> fillItemTask = new Task<ObservableList<Item>>() {
            @Override
            protected ObservableList<Item> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getItemsById(ItemId));
            }
        };
        fillItemTask.setOnSucceeded(e -> {
            viewItemName.setText("Editing " + fillItemTask.valueProperty().getValue().get(0).getItemName());
            fieldEditItemId.setText(String.valueOf(fillItemTask.valueProperty().getValue().get(0).getItemId()));
            fieldEditItemName.setText(fillItemTask.valueProperty().getValue().get(0).getItemName());
            fieldEditDescription.setText(String.valueOf(fillItemTask.valueProperty().getValue().get(0).getDescription()));
            fieldEditCategory.setText(fillItemTask.valueProperty().getValue().get(0).getCategory());
            fieldEditPrice.setText(fillItemTask.valueProperty().getValue().get(0).getPrice()+"");
            fieldEditQuantity.setText(fillItemTask.valueProperty().getValue().get(0).getQuantity()+"");
        });

        new Thread(fillItemTask).start();
    }
}
