package Products;


import Commons.staticLookupCommons;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.Main;
import screensframework.ControlledScreen;
import screensframework.ScreensController;


public class productsController implements ControlledScreen
{
    private ScreensController myController;

    @FXML
    JFXTreeTableView<product> table;

    @FXML
    JFXTextField searchField;

    private static ObservableList<product> productList = FXCollections.observableArrayList();

    //Change this string to override what the table
    //will display a null value as
    private static String nullCharacter = "--";

    @Override
    public void setScreenParent(ScreensController screenPage)
    {
        myController = screenPage;
    }

    @Override
    public void update() {

    }

    public void initialize()
    {
        initTable();
        initSearchField();
    }


    //Here we initialize the table injected by the FXML
    //This is similar to the rest of the tables we create
    //in other pages in the sense that it is initialized
    //with data from the database. However this method also
    //create an action listener to detect double clicks so
    //we can create a popup with relevant data about the source
    private void initTable()
    {

        Commons.UIComponents.initProductTable(table,productList,nullCharacter);

        //This is the action listener responsible for displaying
        //an attribute menu on double click
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ProductDetails(getSelectedProduct(),table.getSelectionModel().getSelectedItem().getValue().productCategory.getValue(),
                        table.getSelectionModel().getSelectedItem().getValue().partNumber.getValue());
            }
        });


        //Add context menus to each row so we can pull the specific product instance
        //into the context menu. This adds the edit and remove product option to the table.
        //NOTE: We should pass a reference to the tale to the editProductUI and
        //move this there
        table.setRowFactory(tableView -> {
            final TreeTableRow<product> row = new TreeTableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem editProduct = new MenuItem("     Edit Product    ");
            final MenuItem removeProduct = new MenuItem("     Remove Product    ");
            editProduct.setOnAction(event -> editProductUI.createUI(row.getItem()));
            removeProduct.setOnAction(event -> removeProductUI.createUI(row.getItem(),productList));
            contextMenu.getItems().addAll(editProduct,removeProduct);
            // Set context menu on row, but use a binding to make it only show for non-empty rows:
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );
            return row ;
        });


    }



    //This method is called within the action listener for displaying the attribute menu
    private void ProductDetails(product product, String product_category,String partNumber)
    {
        getProductDetails.getProperUI(product,product_category,partNumber);
    }

    private void initSearchField()
    {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> table.setPredicate(ProductTreeItem ->
                staticLookupCommons.checkNull(ProductTreeItem.getValue().partNumber.getValue()).toUpperCase().contains(newValue.toUpperCase()) ||
                        staticLookupCommons.checkNull(ProductTreeItem.getValue().productName.getValue()).toUpperCase().contains(newValue.toUpperCase()) ||
                        staticLookupCommons.checkNull(ProductTreeItem.getValue().UOM.getValue()).toUpperCase().contains(newValue.toUpperCase()) ||
                        staticLookupCommons.checkNull(ProductTreeItem.getValue().UPC.getValue()).toUpperCase().contains(newValue.toUpperCase()) ||
                        staticLookupCommons.checkNull(ProductTreeItem.getValue().activeStatus.getValue()).toUpperCase().contains(newValue.toUpperCase()) ||
                        staticLookupCommons.checkNull(ProductTreeItem.getValue().productCategory.getValue()).toUpperCase().contains(newValue.toUpperCase())));

    }


    public void addProduct()
    {
        addProductUI.createUI(productList);
    }

    private product getSelectedProduct()
    {
        if(table.getSelectionModel().getSelectedItem() == null)
        {
            return null;
        }

        return table.getSelectionModel().getSelectedItem().getValue();
    }

    public void editProduct()
    {
        product selected = getSelectedProduct();

        if(selected != null)
        {
            editProductUI.createUI(selected);
        }
        else
        {
            Alert test = new Alert(Alert.AlertType.INFORMATION);
            test.setTitle("");
            test.setHeaderText("Please select a product to edit");
            test.show();
        }
    }

    public void removeProduct()
    {
        product selected = getSelectedProduct();

        if(selected != null)
        {
            removeProductUI.createUI(selected,productList);
        }
        else
        {
            Alert test = new Alert(Alert.AlertType.INFORMATION);
            test.setTitle("");
            test.setHeaderText("Please select a product to remove");
            test.show();
        }
    }


    public void buildProduct()
    {
        myController.setScreen(Main.screen11ID);
    }


    public void goBack()
    {
        myController.setScreen(Main.screen1ID);
    }
}
