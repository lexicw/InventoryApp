package awheeler.View_Controller;

import awheeler.Model.InhousePart;
import awheeler.Model.Inventory;
import static awheeler.Model.Inventory.getPartInventory;
import static awheeler.Model.Inventory.getProductInventory;
import static awheeler.Model.Inventory.removePart;
import static awheeler.Model.Inventory.removeProduct;
import static awheeler.Model.Inventory.deletePartValidation;
import awheeler.Model.OutsourcedPart;
import awheeler.Model.Part;
import awheeler.Model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author lexic
 */
public class MainController implements Initializable {   
    
    @FXML private TextField PartsSearchField;
    @FXML private TextField ProductsSearchField;
    @FXML private TableView<Part> PartsTableView;
    @FXML private TableColumn<Part, Integer> PartIDCol;
    @FXML private TableColumn<Part, String> PartNameCol;
    @FXML private TableColumn<Part, Integer> PartInvCol;
    @FXML private TableColumn<Part, Double> PartPriceCol;
    @FXML private TableView<Product> ProductsTableView;
    @FXML private TableColumn<Product, Integer> ProductIDCol;
    @FXML private TableColumn<Product, String> ProductNameCol;
    @FXML private TableColumn<Product, Integer> ProductInvCol;
    @FXML private TableColumn<Product, Double> ProductPriceCol;
    
    public ObservableList<Part> tempPart = FXCollections.observableArrayList();
    public ObservableList<Product> tempProduct = FXCollections.observableArrayList();
    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;
   
    // Parts Methods
    
    // Method that is called when the search Button on the Parts side is clicked or enter is pressed in the search bar
    public void searchPartBtn(ActionEvent event) throws IOException {
        String searchPart = PartsSearchField.getText().toLowerCase();
        if (searchPart.equals("")){
                PartsTableView.setItems(getPartInventory());
        } else{
            boolean found = false;
            try{
                int itemNumber = Integer.parseInt(searchPart);
                Part x = Inventory.searchPart(itemNumber);
                if(x != null){
                    found = true;
                    tempPart.clear();
                    tempPart.add(x);
                    PartsTableView.setItems(tempPart);
                } 
                if (found == false){
                PartsTableView.setItems(getPartInventory());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Part Not Found");
                alert.setHeaderText("Your search returned no results. Please try again.");
                alert.showAndWait();
            }
        }
        catch(NumberFormatException e){
            for(Part p: Inventory.getPartInventory()){
                if(p.getPartName().toLowerCase().equals(searchPart)){
                    found = true;
                    tempPart.clear();
                    tempPart.add(p);
                    PartsTableView.setItems(tempPart);
                }
            }
            if (found == false){
            PartsTableView.setItems(getPartInventory());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Part Not Found");
            alert.setHeaderText("Your search returned no results. Please try again.");
            alert.showAndWait();
            }
        }
        }
    }
    
    // Method that is called when "Add Part" Button is clicked
    public void addPartBtn(ActionEvent event) throws IOException 
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addpart.fxml"));
        Scene addPartScene = new Scene(fxmlLoader.load(), 450, 450);
        Stage stage = new Stage();
        stage.setTitle("Add Part");
        stage.setScene(addPartScene);
        stage.show();
    }
    
    // Method that is called when "Modify Part" Button is clicked
        public void modifyPartBtn(ActionEvent event) throws IOException 
    { 
        modifyPart = PartsTableView.getSelectionModel().getSelectedItem();
        if (modifyPart == null) {
            // When a part is not selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Modify Part Error");
            alert.setHeaderText("You must select a part to modify.");
            alert.showAndWait();
        } 
        else {
            // When a part is selected
        modifyPartIndex = getPartInventory().indexOf(modifyPart);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("modifypart.fxml"));
        Scene modifyPartScene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Modify Part");
        stage.setScene(modifyPartScene);
        stage.show();
        }
    }
        // Method that is called when "Delete Part" Button is clicked
        public void deletePartBtn(ActionEvent event) throws IOException {
        Part part = PartsTableView.getSelectionModel().getSelectedItem();
        if (part == null) {
            // When there is no part selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Error");
            alert.setHeaderText("You must select a part to delete.");
            alert.showAndWait();
        } 
        else {
            // When Part is Selected
           if (deletePartValidation(part)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Part Error");
            alert.setHeaderText("This part is used in a product.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Delete Part");
            alert.setHeaderText("Are you sure you want to delete '" + part.getPartName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Part Deleted");
                alert.setHeaderText("Part '" + part.getPartName() + "' has been removed from the system." );
                alert.showAndWait();
                removePart(part);
                updatePartTable();
            } else {
            }
            }
        }
    }    
        
     // Product Methods
    
    // Method that is called when the Search button on the product side is clicked
    public void searchProductBtn(ActionEvent event) throws IOException {
        String searchProduct = ProductsSearchField.getText().toLowerCase();
        if (searchProduct.equals("")){
                ProductsTableView.setItems(getProductInventory());
        } else{
            boolean found = false;
            try{
                int searchTerm = Integer.parseInt(searchProduct);
                Product x = Inventory.searchProduct(searchTerm);
                if(x != null){
                    found = true;
                    tempProduct.clear();
                    tempProduct.add(x);
                    ProductsTableView.setItems(tempProduct);
                } 
                if (found == false){
                ProductsTableView.setItems(getProductInventory());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Product Not Found");
                alert.setHeaderText("Your search returned no results. Please try again.");
                alert.showAndWait();
            }
        }
        catch(NumberFormatException e){
            for(Product x: Inventory.getProductInventory()){
                if(x.getProductName().toLowerCase().equals(searchProduct)){
                    found = true;
                    tempProduct.clear();
                    tempProduct.add(x);
                    ProductsTableView.setItems(tempProduct);
                }
            }
            if (found == false){
            ProductsTableView.setItems(getProductInventory());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Product Not Found");
            alert.setHeaderText("Your search returned no results. Please try again.");
            alert.showAndWait();
            }
        }
        }
    }    
    // Method that is called when "Add Product" Button is clicked
        public void addProductBtn(ActionEvent event) throws IOException 
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addproduct.fxml"));
        Scene addProductScene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Add Product");
        stage.setScene(addProductScene);
        stage.show();
    }

    // Method that is called when "Modify Product" Button is clicked
        public void modifyProductBtn(ActionEvent event) throws IOException 
    {
        modifyProduct = ProductsTableView.getSelectionModel().getSelectedItem();
        if (modifyProduct == null) {
            // When there is no Product selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Modify Product Error");
            alert.setHeaderText("You must select a product to modify.");
            alert.showAndWait();
        } 
        else {
        modifyProductIndex = getProductInventory().indexOf(modifyProduct);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("modifyproduct.fxml"));
        Scene addProductScene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Modify Product");
        stage.setScene(addProductScene);
        stage.show();
        }
    }
        // Method that is called when "Delete Product" button is clicked
        public void deleteProductBtn(ActionEvent event) throws IOException {
        Product product = ProductsTableView.getSelectionModel().getSelectedItem();
        if (product == null) {
            // When there is no product selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Error");
            alert.setHeaderText("You must select a product to delete.");
            alert.showAndWait();
        } 
        else {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Are you sure you want to delete '" + product.getProductName() + "'?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Product Deleted");
            alert.setHeaderText("Product '" + product.getProductName() + "' has been removed from the system." );
            alert.showAndWait();
            removeProduct(product);
            updateProductTable();
        } else {
        }
        }
    }
        
    // Method that is called when the "Exit" button is clicked
    public void exitBtn(ActionEvent event) 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit System");
        alert.setHeaderText("Are you sure you wish to exit? Any changes will not be saved.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
           System.exit(0);
        } 
        else {
        }
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        PartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        PartInvCol.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        PartPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        ProductIDCol.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        ProductNameCol.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        ProductInvCol.setCellValueFactory(cellData -> cellData.getValue().productInvProperty().asObject());
        ProductPriceCol.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        dummyPartsDatabase();
        dummyProductsDatabase();
        updatePartTable();
        updateProductTable();
    }
    public void updatePartTable() {
        PartsTableView.setItems(getPartInventory());
    }

    public void updateProductTable() {
        ProductsTableView.setItems(getProductInventory());
    }
    
    // Method that creates a "dummy" database to show what existing parts would be like in the system
    void dummyPartsDatabase() {
        int partID = Inventory.getPartIDCount();
        InhousePart dummyPartA = new InhousePart();
        dummyPartA.setPartID(partID);
        dummyPartA.setPartName("Lock");
        dummyPartA.setPartPrice(7.99);
        dummyPartA.setPartInStock(42);
        dummyPartA.setPartMin(0);
        dummyPartA.setPartMax(100);
        dummyPartA.setPartMachineID(2323);
        Inventory.addPart(dummyPartA);
        
        partID = Inventory.getPartIDCount();
        InhousePart dummyPartB = new InhousePart();
        dummyPartB.setPartID(partID);
        dummyPartB.setPartName("Awning");
        dummyPartB.setPartPrice(39.99);
        dummyPartB.setPartInStock(34);
        dummyPartB.setPartMin(0);
        dummyPartB.setPartMax(100);
        dummyPartB.setPartMachineID(5656);
        Inventory.addPart(dummyPartB);
        
        partID = Inventory.getPartIDCount();
        OutsourcedPart dummyPartC = new OutsourcedPart();
        dummyPartC.setPartID(partID);
        dummyPartC.setPartName("Mattress");
        dummyPartC.setPartPrice(29.99);
        dummyPartC.setPartInStock(22);
        dummyPartC.setPartMin(0);
        dummyPartC.setPartMax(100);
        dummyPartC.setPartCompanyName("RV Bedding inc.");
        Inventory.addPart(dummyPartC);
    }
    
    // Method that creates a "dummy" database to show what existing products would be like in the system
    void dummyProductsDatabase() {
        int productID = Inventory.getProductIDCount();
        Product newProduct = new Product();
        newProduct.setProductID(productID);
        newProduct.setProductName("Travel Trailer RV");
        newProduct.setProductPrice(11000);
        newProduct.setProductInStock(4);
        newProduct.setProductMin(0);
        newProduct.setProductMax(100);
        //newProduct.setProductParts(currentParts);
        Inventory.addProduct(newProduct);
        }
                
    public static int partModifyIndex() {
        return modifyPartIndex;
    }

    public static int productModifyIndex() {
        return modifyProductIndex;
    }

    public void setMainApp(Awheeler mainApp) {
        updatePartTable();
        updateProductTable();
    }
        
}