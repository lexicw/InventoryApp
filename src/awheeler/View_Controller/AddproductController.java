package awheeler.View_Controller;

import awheeler.Model.Inventory;
import static awheeler.Model.Inventory.getPartInventory;
import static awheeler.Model.Inventory.getProductIDMinus;
import awheeler.Model.Part;
import awheeler.Model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
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
public class AddproductController implements Initializable {
     
    public ObservableList<Part> tempPart = FXCollections.observableArrayList();
    
    private String errorMsg = new String();
    private int productID;

    @FXML private TextField ProductIdTxt;
    @FXML private TextField ProductNameTxt;
    @FXML private TextField ProductPriceTxt;
    @FXML private TextField ProductInvTxt;
    @FXML private TextField ProductMinTxt;
    @FXML private TextField ProductMaxTxt;
    @FXML private TextField DeletePartSearchTxt;
    @FXML private TextField AddPartSearchTxt;
    @FXML private TableView<Part> AddTable;
    @FXML private TableColumn<Part, Integer> PartIDCol;
    @FXML private TableColumn<Part, String> PartNameCol;
    @FXML private TableColumn<Part, Integer> InvCol;
    @FXML private TableColumn<Part, Double> PriceCol;
    @FXML private TableView<Part> DeleteTable;
    @FXML private TableColumn<Part, Integer> CurrentPartIDCol;
    @FXML private TableColumn<Part, String> CurrentPartNameCol;
    @FXML private TableColumn<Part, Integer> CurrentInvCol;
    @FXML private TableColumn<Part, Double> CurrentPriceCol;
    
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();

    @FXML
    void searchPartAddBtn(ActionEvent event) {
        String searchPart = AddPartSearchTxt.getText().toLowerCase();
        if (searchPart.equals("")){
                AddTable.setItems(getPartInventory());
        } else{
            boolean found = false;
            try{
                int itemNumber = Integer.parseInt(searchPart);
                Part x = Inventory.searchPart(itemNumber);
                if(x != null){
                    found = true;
                    tempPart.clear();
                    tempPart.add(x);
                    AddTable.setItems(tempPart);
                } 
                if (found == false){
                AddTable.setItems(getPartInventory());
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
                    AddTable.setItems(tempPart);
                }
            }
            if (found == false){
            AddTable.setItems(getPartInventory());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Part Not Found");
            alert.setHeaderText("Your search returned no results. Please try again.");
            alert.showAndWait();
            }
        }
        }
    }


    @FXML
    void addPartBtn(ActionEvent event) {
        Part part = AddTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            // When there is no Part selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Add Part Error");
            alert.setHeaderText("You must select a Part to add.");
            alert.showAndWait();
        } 
        else {
        currentParts.add(part);
        updateCurrentPartTable();
        }
    }

    @FXML
    void searchPartDeleteBtn(ActionEvent event) {
        String searchPart = DeletePartSearchTxt.getText().toLowerCase();
        if (searchPart.equals("")){
                DeleteTable.setItems(getPartInventory());
        } else{
            boolean found = false;
            try{
                int itemNumber = Integer.parseInt(searchPart);
                Part x = Inventory.searchPart(itemNumber);
                if(x != null){
                    found = true;
                    tempPart.clear();
                    tempPart.add(x);
                    DeleteTable.setItems(tempPart);
                } 
                if (found == false){
                DeleteTable.setItems(getPartInventory());
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
                    DeleteTable.setItems(tempPart);
                }
            }
            if (found == false){
            DeleteTable.setItems(getPartInventory());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Part Not Found");
            alert.setHeaderText("Your search returned no results. Please try again.");
            alert.showAndWait();
            }
        }
        }
    }

    @FXML
    void deletePartBtn(ActionEvent event) {
        Part part = DeleteTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            // When there is no Part selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Part Error");
            alert.setHeaderText("You must select a Part to delete.");
            alert.showAndWait();
        } 
        else {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete part '" + part.getPartName() + "' from this Product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
        currentParts.remove(part);
        } else {
        }
      }
    }

    @FXML
    void addProductBtn(ActionEvent event) throws IOException {

        String productName = ProductNameTxt.getText();
        String productInv = ProductInvTxt.getText();
        String productPrice = ProductPriceTxt.getText();
        String productMin = ProductMinTxt.getText();
        String productMax = ProductMaxTxt.getText();

        try {

            errorMsg = Product.productValidation(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv),
                    Double.parseDouble(productPrice), currentParts, errorMsg);

            if (errorMsg.length() > 0) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Adding Product");
                alert.setHeaderText(errorMsg);
                alert.showAndWait();
                errorMsg = "";
            } else {
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.addProduct(newProduct);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Added");
        alert.setHeaderText("Product '" + productName + "' has been added to the system." );
        alert.showAndWait();
            }
        } catch (NumberFormatException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Adding Product");
            alert.setHeaderText("All fields must have valid information.");
            alert.showAndWait();
        }
    }
    // Method that is called when the "Cancel" Button is clicked
    public void cancelBtn(ActionEvent event) throws IOException 
    {
        // Checks to see if all fields are empty. If empty, will let you close the window without a warning.
        if (ProductNameTxt.getText().trim().isEmpty() && ProductInvTxt.getText().trim().isEmpty() && ProductPriceTxt.getText().trim().isEmpty() && ProductMinTxt.getText().trim().isEmpty() && ProductMaxTxt.getText().trim().isEmpty()) {
            getProductIDMinus();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.close();
        } else {
        // Will warn you before allowing you to close the window.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel? Changes will not be saved.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
        getProductIDMinus();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
        } 
        else {
        }
      }
    }
    
    public void updatePartTable() {
        AddTable.setItems(getPartInventory());
    }

    public void updateCurrentPartTable() {
        DeleteTable.setItems(currentParts);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        PartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        InvCol.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        PriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());

        CurrentPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        CurrentPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        CurrentInvCol.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        CurrentPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());

        updatePartTable();
        updateCurrentPartTable();

        productID = Inventory.getProductIDCount();
        ProductIdTxt.setText(productID + "");
    }
}