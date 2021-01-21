package awheeler.View_Controller;

import awheeler.Model.InhousePart;
import awheeler.Model.Inventory;
import static awheeler.Model.Inventory.getPartInventory;
import awheeler.Model.OutsourcedPart;
import awheeler.Model.Part;
import static awheeler.View_Controller.MainController.partModifyIndex;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lexic
 */
public class ModifypartController implements Initializable {

    private int partID;
    private boolean inHouse = true;
    int partIndex = partModifyIndex();
    private String exceptionMessage = new String();
    
    @FXML private TextField PartIdTxt;
    @FXML private TextField PartNameTxt;
    @FXML private TextField PartInvTxt;
    @FXML private TextField PartPriceTxt;
    @FXML private TextField PartMinTxt;
    @FXML private TextField PartMaxTxt;
    @FXML private TextField PartChangeTxt;
    @FXML private Label changeLabel;
    @FXML private RadioButton InHouseRadio;
    @FXML private RadioButton OutsourcedRadio;
    
    @FXML
    // Method that is called when the "Save" Button is clicked
    void modifyPartSaveBtn(ActionEvent event) throws IOException {
        String partName = PartNameTxt.getText();
        String partInv = PartInvTxt.getText();
        String partPrice = PartPriceTxt.getText();
        String partMin = PartMinTxt.getText();
        String partMax = PartMaxTxt.getText();
        String partChange = PartChangeTxt.getText();
        try {
            exceptionMessage = Part.partValidation(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Modifying Part");
                alert.setHeaderText(exceptionMessage);
                alert.showAndWait();
            } else {
                if (inHouse == true) {
                    InhousePart inhousePart = new InhousePart();
                    inhousePart.setPartID(partID);
                    inhousePart.setPartName(partName);
                    inhousePart.setPartPrice(Double.parseDouble(partPrice));
                    inhousePart.setPartInStock(Integer.parseInt(partInv));
                    inhousePart.setPartMin(Integer.parseInt(partMin));
                    inhousePart.setPartMax(Integer.parseInt(partMax));
                    inhousePart.setPartMachineID(Integer.parseInt(partChange));
                    Inventory.updatePart(partIndex, inhousePart);
                } else {
                    OutsourcedPart outsourcedPart = new OutsourcedPart();
                    outsourcedPart.setPartID(partID);
                    outsourcedPart.setPartName(partName);
                    outsourcedPart.setPartPrice(Double.parseDouble(partPrice));
                    outsourcedPart.setPartInStock(Integer.parseInt(partInv));
                    outsourcedPart.setPartMin(Integer.parseInt(partMin));
                    outsourcedPart.setPartMax(Integer.parseInt(partMax));
                    outsourcedPart.setPartCompanyName(partChange);
                    Inventory.updatePart(partIndex, outsourcedPart);;
                }
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Part Modified");
            alert.setHeaderText("Part '" + partName + "' has been modified succesfully." );
            alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Modifying Part");
            alert.setHeaderText("All fields must have valid information.");
            alert.showAndWait();
        }
    }
    
    // Method that is called when the "Cancel" Button is clicked
    public void cancelBtn(ActionEvent event) throws IOException 
    {
        // Will warn you before allowing you to close the window.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel? Changes will not be saved.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.close();
        } else {
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
        Part part = getPartInventory().get(partIndex);
        partID = getPartInventory().get(partIndex).getPartID();
        PartIdTxt.setText(partID + "");
        PartNameTxt.setText(part.getPartName());
        PartInvTxt.setText(Integer.toString(part.getPartInStock()));
        PartPriceTxt.setText(Double.toString(part.getPartPrice()));
        PartMinTxt.setText(Integer.toString(part.getPartMin()));
        PartMaxTxt.setText(Integer.toString(part.getPartMax()));
        if (part instanceof InhousePart) {
            PartChangeTxt.setText(Integer.toString(((InhousePart) getPartInventory().get(partIndex)).getPartMachineID()));
            inHouse = true;
            changeLabel.setText("Machine ID");
            InHouseRadio.setSelected(true);

        } else {
            PartChangeTxt.setText(((OutsourcedPart) getPartInventory().get(partIndex)).getPartCompanyName());
            inHouse = false;
            changeLabel.setText("Company");
            OutsourcedRadio.setSelected(true);
        }
    }    
    
}
