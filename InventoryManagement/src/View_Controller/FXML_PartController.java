/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Part;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author David Watkins
 */
public class FXML_PartController implements Initializable {
    
    private FXML_InventoryManagementController mainController;
    private boolean outsourced = true; // Which part type is being used?
    
    // Part Components
    @FXML
    private Label partTitle, mfrLabel;
    @FXML
    private RadioButton inhouseBtn, outsourcedBtn;
    @FXML
    private TextField partIdVal, partNameVal, partQtyVal, partCostVal, partMinVal, partMaxVal, mfrVal;
    @FXML
    private Button savePartBtn, cancelPartBtn;
    
    @FXML
    void addPart(ActionEvent event) {
        if (validatePart()) {
            return; // Invalid or missing input.
        }
        int partID = mainController.getNewPartID();
        String name = partNameVal.getText();
        int inStock = Integer.parseInt(partQtyVal.getText());
        Double price = Double.parseDouble(partCostVal.getText());
        int min = Integer.parseInt(partMinVal.getText());
        int max = Integer.parseInt(partMaxVal.getText());
        String manufacturer = mfrVal.getText();
        
        if (outsourced) {
            Inventory.addPart(new OutsourcedPart(partID, name, inStock, price, min, max, manufacturer));
        } else {
            Inventory.addPart(new InhousePart(partID, name, inStock, price, min, max, Integer.parseInt(manufacturer)));
        }
        mainController.displayParts();
        closeWindow(event);                        
    }
    
    @FXML
    void modifyPart(ActionEvent event) {
        if (validatePart()) {
            return; // Invalid or missing input.
        }
        int partID = Integer.parseInt(partIdVal.getText());
        String name = partNameVal.getText();
        int inStock = Integer.parseInt(partQtyVal.getText());
        Double price = Double.parseDouble(partCostVal.getText());
        int min = Integer.parseInt(partMinVal.getText());
        int max = Integer.parseInt(partMaxVal.getText());
        String manufacturer = mfrVal.getText();
        
        int partIndex = Inventory.getPartIndex(partID);
        
        if (outsourced) {
            OutsourcedPart part = new OutsourcedPart(partID, name, inStock, price, min, max, manufacturer);
            Inventory.updatePart(partIndex, part);
        } else {
            InhousePart part = new InhousePart(partID, name, inStock, price, min, max, Integer.parseInt(manufacturer));
            Inventory.updatePart(partIndex, part);
        }
        mainController.displayParts();
        closeWindow(event);
    }
    
    @FXML
    void changePartType(ActionEvent event) {
        if (event.getSource() == outsourcedBtn) {
            outsourced = true;
            mfrLabel.setText("Company Name");
            mfrVal.setPromptText("Company Name");
            mfrVal.clear();
        } else if (event.getSource() == inhouseBtn) {
            outsourced = false;
            mfrLabel.setText("Machine ID");
            mfrVal.setPromptText("Machine ID");
            mfrVal.clear();
        }
    }
    
    @FXML           
    void cancelWindow(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Part Entry");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to cancel this entry?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            // User canceled dialog.
        }
    }
    
    @FXML           
    void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
   
    public void setMainController(FXML_InventoryManagementController mainController) {
        this.mainController = mainController;
    }
    
    public String checkIfEmpty(String value, String error) {
        if (value.isEmpty()){
            return error;
        }
        return "";
    }
    
    public static String isGreaterThan(String value1, String value2, String error) {
        if (Integer.parseInt(value1) > Integer.parseInt(value2)) {            
            return error;
        }
        return "";
    }
    
    public static boolean isGreaterThan(String value1, String value2) {
        if (Integer.parseInt(value1) > Integer.parseInt(value2)) {            
            return true;
        }
        return false;
    }
    
    public boolean validatePart() {
        String errorMessage = "";
        
        errorMessage += checkIfEmpty(partNameVal.getText(), "\u2022 Name\n");
        errorMessage += checkIfEmpty(partQtyVal.getText(), "\u2022 Inventory Amount\n");
        errorMessage += checkIfEmpty(partCostVal.getText(), "\u2022 Price\n");
        errorMessage += checkIfEmpty(partMinVal.getText(), "\u2022 Minimum Inventory\n");
        errorMessage += checkIfEmpty(partMaxVal.getText(), "\u2022 Maximum Inventory\n");
        
        if (!partQtyVal.getText().isEmpty() && !partMaxVal.getText().isEmpty() && partMinVal.getText().isEmpty()) {
            errorMessage += isGreaterThan(partQtyVal.getText(), partMaxVal.getText(),
                "\u2022 Requested inventory is greater than maximum inventory.");
        } else if (partQtyVal.getText().isEmpty() && !partMaxVal.getText().isEmpty() && !partMinVal.getText().isEmpty()) {
            errorMessage += isGreaterThan(partMinVal.getText(), partMaxVal.getText(),
                "\u2022 Minimum inventory is greater than maximum inventory.");            
        } else if (!partQtyVal.getText().isEmpty() && partMaxVal.getText().isEmpty() && !partMinVal.getText().isEmpty()) {
            errorMessage += isGreaterThan(partMinVal.getText(), partQtyVal.getText(),
                "\u2022 Requested inventory is less than minimum inventory.");            
        } else if (!partQtyVal.getText().isEmpty() && !partMaxVal.getText().isEmpty() && !partMinVal.getText().isEmpty()) {
            if (isGreaterThan(partQtyVal.getText(), partMaxVal.getText())) { 
                errorMessage += "\u2022 Requested inventory is greater than maximum inventory.\n";
            } else if (isGreaterThan(partMinVal.getText(), partQtyVal.getText())) {
                errorMessage += "\u2022 Requested inventory is less than minimum inventory.\n";
            }
            if (isGreaterThan(partMinVal.getText(), partMaxVal.getText())) {
                errorMessage += "\u2022 Minimum inventory must be less than or equal to maximum inventory.";
            }
        }

        if (errorMessage.isEmpty()) {
            return false;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Missing or Invalid Input");
            alert.setContentText("The following values are missing or invalid:\n\n" + errorMessage);
            alert.showAndWait();            
            return true;            
        }
    }

    public void loadPart(Part part) {
        // Change page title and Save button handler
        partTitle.setText("Modify Part");
        savePartBtn.setOnAction(this::modifyPart);
        
        String manufacturer;
        if (part instanceof OutsourcedPart) {
            manufacturer = ((OutsourcedPart) part).getCompanyName();
        } else {
            manufacturer = Integer.toString(((InhousePart) part).getMachineID());
            inhouseBtn.fire();
        }
        DecimalFormat decimal = new DecimalFormat("#0.00");  
        
        partIdVal.setText(Integer.toString(part.getPartID()));
        partNameVal.setText(part.getName());
        partQtyVal.setText(Integer.toString(part.getInStock()));
        partCostVal.setText(decimal.format(part.getPrice()));
        partMinVal.setText(Integer.toString(part.getMin()));
        partMaxVal.setText(Integer.toString(part.getMax()));
        mfrVal.setText(manufacturer);
    }    
      
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partQtyVal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,9}")) {
                partQtyVal.setText(oldValue);
            }
        });
        
        partCostVal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}(\\.\\d{0,2})?")) {
                partCostVal.setText(oldValue);
            }
        });

        partMinVal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,8}")) {
                partMinVal.setText(oldValue);
            }
        });

        partMaxVal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,8}")) {
                partMaxVal.setText(oldValue);
            }
        });
        
        mfrVal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!outsourced && !newValue.matches("\\d{0,8}")) {
                mfrVal.setText(oldValue);
            }
        });
    }   
}
