package awheeler.View_Controller;

import awheeler.Model.InhousePart;
import awheeler.Model.Inventory;
import static awheeler.Model.Inventory.getPartIDMinus;
import awheeler.Model.OutsourcedPart;
import awheeler.Model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lexic
 */
public class AddpartController implements Initializable {

    private int partID;
    private boolean inHouse = true;
    private String errorMsg = new String();
    
    @FXML private TextField PartIdTxt;
    @FXML private TextField PartNameTxt;
    @FXML private TextField PartInvTxt;
    @FXML private TextField PartPriceTxt;
    @FXML private TextField PartMinTxt;
    @FXML private TextField PartChangeTxt;
    @FXML private TextField PartMaxTxt;
    @FXML private Label changeLabel;
    
    @FXML
    void AddPartBtn(ActionEvent event) throws IOException {
        String partName = PartNameTxt.getText();
        String partInv = PartInvTxt.getText();
        String partPrice = PartPriceTxt.getText();
        String partMin = PartMinTxt.getText();
        String partMax = PartMaxTxt.getText();
        String partChange = PartChangeTxt.getText();

        try {
            errorMsg = Part.partValidation(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), errorMsg);
            if (errorMsg.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Adding Part");
                alert.setHeaderText(errorMsg);
                alert.showAndWait();
                errorMsg = "";
            } else {
                if (inHouse == true) {
                    InhousePart inPart = new InhousePart();
                    inPart.setPartID(partID);
                    inPart.setPartName(partName);
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setPartInStock(Integer.parseInt(partInv));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setPartMachineID(Integer.parseInt(partChange));
                    Inventory.addPart(inPart);
                } else {
                    OutsourcedPart outPart = new OutsourcedPart();
                    outPart.setPartID(partID);
                    outPart.setPartName(partName);
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setPartInStock(Integer.parseInt(partInv));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setPartCompanyName(partChange);
                    Inventory.addPart(outPart);
                }
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Part Added");
                alert.setHeaderText("Part '" + partName + "' has been added to the system." );
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Adding Part");
            alert.setHeaderText("All fields must have valid information.");                  
            alert.showAndWait();
        }
    }

    @FXML
    // Method that is called when the "Cancel" Button is clicked
    void cancelBtn(ActionEvent event) throws IOException {
        
        // Checks to see if all fields are empty. If empty, will let you close the window without a warning.
         if (PartNameTxt.getText().trim().isEmpty() && PartInvTxt.getText().trim().isEmpty() && PartPriceTxt.getText().trim().isEmpty() && PartMinTxt.getText().trim().isEmpty() && PartMaxTxt.getText().trim().isEmpty() && PartChangeTxt.getText().trim().isEmpty()) {
            getPartIDMinus();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.close();
        } else {
        // If any fields have text, the program will warn you before allowing you to close the window
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel? Part will not be saved.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            getPartIDMinus();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.close();
        } else {
        }
      }
        
    }
    
    @FXML
    // Method that is called when the "In-house" Radio Button is selected
    void InHouseRadio(ActionEvent event) {
        inHouse = true;
        changeLabel.setText("Machine ID");
        PartChangeTxt.setPromptText("Machine ID");
    }

    @FXML
    // Method that is called when the "Outsourced" Radio Button is selected
    void OutsourcedRadio(ActionEvent event) {
        inHouse = false;
        changeLabel.setText("Company");
        PartChangeTxt.setPromptText("Company Name");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID = Inventory.getPartIDCount();
        PartIdTxt.setText(partID + "");
    }    
    
}
