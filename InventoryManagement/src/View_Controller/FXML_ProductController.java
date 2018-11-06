/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Part;
import Model.Product;
import Model.Inventory;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author David Watkins
 */
public class FXML_ProductController implements Initializable {
    
    private FXML_InventoryManagementController mainController;
    protected ObservableList<Part> localParts = FXCollections.observableArrayList();
    private NumberFormat currency;
    private Double totalCost;
    
    // Add/Modify Product Components
    @FXML
    private Label prodTitle;
    @FXML
    private TextField prodIdVal, prodNameVal, prodQtyVal, prodCostVal, prodMinVal, prodMaxVal, searchPartsVal;
    @FXML
    private Button searchPartsBtn, addPartBtn, deletePartBtn, saveProdBtn, cancelBtn; 
    @FXML
    private TableView<Part> partsTable1, partsTable2;
    @FXML
    private TableColumn<Part, Integer> partIdTable1, partIdTable2, partQtyTable1, partQtyTable2;
    @FXML
    private TableColumn<Part, String> partNameTable1, partNameTable2;
    @FXML
    private TableColumn<Part, Double> partCostTable1, partCostTable2;

    @FXML
    void addPart(ActionEvent event) {
        Part selectedPart = partsTable1.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Part Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a part to add from the upper parts list.");
            alert.showAndWait();
            return; // No part selected.
        }
       
        // Check to see if part has already been added
        Part part = lookupPart(selectedPart.getPartID());
        if (part == null) {  // Part does not exist
            localParts.add(selectedPart);
            // Display newly added part in second parts table
            partsTable2.setItems(localParts);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Part Aleady Added");
            alert.setHeaderText(null);
            alert.setContentText("The selected part has already been added to your product. Please make another selection.\n\n"
                    + "Part ID: " + selectedPart.getPartID() + "\n"
                    + "Part Name: " + selectedPart.getName());
            alert.showAndWait();
        }
    }

    @FXML
    void removePart(ActionEvent event) {
        Part selectedPart = partsTable2.getSelectionModel().getSelectedItem();
        
        if (partsTable2.getItems().size() < 1) {
            // Parts table is empty.
        } else if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Part Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a part to delete from the lower parts list.");
            alert.showAndWait();
        } else {
            removePart(selectedPart.getPartID());
        }
    }    

    @FXML
    void addProduct(ActionEvent event) {
        if (validateProduct()) {
            return;  // Invalid or missing input.
        }
        int productID = mainController.getNewProdID();
        String name = prodNameVal.getText();
        int inStock = Integer.parseInt(inputDefault(prodQtyVal.getText()));
        Double price = Double.parseDouble(inputDefault(prodCostVal.getText()));
        int min = Integer.parseInt(inputDefault(prodMinVal.getText()));
        int max = Integer.parseInt(inputDefault(prodMaxVal.getText()));
        
        Product product = new Product(productID, name, inStock, price, min, max);
        Inventory.addProduct(product);
        
        //totalCost = Bindings.
        
        // Add parts to product.
        for (int x = 0; x < localParts.size(); x++) {
            product.addAssociatedPart(Inventory.lookupPart(localParts.get(x).getPartID()));            
        }        
        mainController.displayProducts();
        closeWindow(event);
    }
    
    @FXML
    void modifyProduct(ActionEvent event) {
        if (validateProduct()) {
            return;  // Invalid or missing input.
        }
        int productID = Integer.parseInt(prodIdVal.getText());
        String name = prodNameVal.getText();
        int inStock = Integer.parseInt(inputDefault(prodQtyVal.getText()));
        Double price = Double.parseDouble(inputDefault(prodCostVal.getText()));
        int min = Integer.parseInt(inputDefault(prodMinVal.getText()));
        int max = Integer.parseInt(inputDefault(prodMaxVal.getText()));
        
        int index = Inventory.getProductIndex(productID);

        Product product = new Product(productID, name, inStock, price, min, max);
        Inventory.updateProduct(index, product);
        
        // Add parts to product.
        for (int x = 0; x < localParts.size(); x++) {
            product.addAssociatedPart(Inventory.lookupPart(localParts.get(x).getPartID()));            
        }   
        mainController.displayProducts();
        closeWindow(event);   
    }
    
    @FXML 
    void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();        
    }
    
    @FXML           
    void cancelWindow(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Product Entry");
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
    void searchParts(ActionEvent event) {
        FilteredList<Part> filteredParts = new FilteredList<>(Inventory.getAllParts());
        filteredParts.setPredicate(part -> {
            if (searchPartsVal.getText() == null || searchPartsVal.getText().isEmpty()) {
                return true;
            }
            String lowerCaseFilter = searchPartsVal.getText().toLowerCase();
                
            if (part.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches Name
            }
            else if (Integer.toString(part.getPartID()).contains(lowerCaseFilter)) {
                return true; // Filter matches Part ID
            }
            else if (Integer.toString(part.getInStock()).contains(lowerCaseFilter)) {
                return true; // Filter matches In-Stock amount
            }
            else if (Double.toString(part.getPrice()).contains(lowerCaseFilter)) {
                return true; // Filter matches Price
            }
            return false; // No matches found
        });
        
        SortedList<Part> sortedParts = new SortedList<>(filteredParts);
        sortedParts.comparatorProperty().bind(partsTable1.comparatorProperty());
        partsTable1.setItems(sortedParts);       
    }
    
    public void setMainController(FXML_InventoryManagementController mainController) {
        this.mainController = mainController;
    }
    
    public void displayParts() {
        partsTable1.setItems(Inventory.getAllParts());        
    }
    
    public Part lookupPart(int partID) {
        for(int x = 0; x < localParts.size(); x++) {
            if(localParts.get(x).getPartID() == partID) {
                return localParts.get(x);
            }
        }
        return null;
    }
    
    public boolean removePart(int partID) {
        for(int x = 0; x < localParts.size(); x++) {
            if(localParts.get(x).getPartID() == partID) {
                localParts.remove(x);
                return true;
            }
        }
        return false;
    }
    
    public String inputDefault(String input) {
        if (input.isEmpty()) {
            return "0";
        } else {
            return input;
        }
    }
    
    public String outputDefault(String input) {
        if (input.equals("0")) {
            return "";
        } else {
            return input;
        }
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
    
    public boolean validateProduct() {
        String errorMessage = "";
        
        errorMessage += checkIfEmpty(prodNameVal.getText(), "\u2022 Name\n");
        errorMessage += checkIfEmpty(prodQtyVal.getText(), "\u2022 Inventory Amount\n");
        errorMessage += checkIfEmpty(prodCostVal.getText(), "\u2022 Price\n");
        
        //errorMessage += checkIfEmpty(prodMinVal.getText(), "\u2022 Minimum Inventory\n"); // Rubic does not indicate that Min value is required.
        //errorMessage += checkIfEmpty(prodMaxVal.getText(), "\u2022 Maximum Inventory\n"); // Rubic does not indicate that Max value is required. 
        if (localParts.size() < 1) {
            errorMessage += "\u2022 NO Associated Parts\n";
        } else if (!prodCostVal.getText().isEmpty()) {
            if (Double.parseDouble(prodCostVal.getText()) < totalCost) {
                errorMessage += "\u2022 The product price is less than " + currency.format(totalCost) + ", the sum of the parts.\n"; 
            }
        }
        
        
        if (!prodQtyVal.getText().isEmpty() && !prodMaxVal.getText().isEmpty() && prodMinVal.getText().isEmpty()) {
            errorMessage += isGreaterThan(prodQtyVal.getText(), prodMaxVal.getText(),
                "\u2022 Requested inventory is greater than maximum inventory.");
        } else if (prodQtyVal.getText().isEmpty() && !prodMaxVal.getText().isEmpty() && !prodMinVal.getText().isEmpty()) {
            errorMessage += isGreaterThan(prodMinVal.getText(), prodMaxVal.getText(),
                "\u2022 Minimum inventory is greater than maximum inventory.");            
        } else if (!prodQtyVal.getText().isEmpty() && prodMaxVal.getText().isEmpty() && !prodMinVal.getText().isEmpty()) {
            errorMessage += isGreaterThan(prodMinVal.getText(), prodQtyVal.getText(),
                "\u2022 Requested inventory is less than minimum inventory.");            
        } else if (!prodQtyVal.getText().isEmpty() && !prodMaxVal.getText().isEmpty() && !prodMinVal.getText().isEmpty()) {
            if (isGreaterThan(prodQtyVal.getText(), prodMaxVal.getText())) { 
                errorMessage += "\u2022 Requested inventory is greater than maximum inventory.\n";
            } else if (isGreaterThan(prodMinVal.getText(), prodQtyVal.getText())) {
                errorMessage += "\u2022 Requested inventory is less than minimum inventory.\n";
            }
            if (isGreaterThan(prodMinVal.getText(), prodMaxVal.getText())) {
                errorMessage += "\u2022 Minimum inventory must be less than or equal to maximum inventory.\n";
            }
        }
      
        if (errorMessage.isEmpty()) {
            return false;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Missing or Invalid Input");
            alert.setContentText("The following values are missing or invalid:\n\n" + errorMessage);
            alert.showAndWait();            
            return true;            
        }
    }
    
    public void loadProduct(Product product) {
        // Change page title and Save button handler
        prodTitle.setText("Modify Product");
        saveProdBtn.setOnAction(this::modifyProduct);
        
        DecimalFormat decimal = new DecimalFormat("#0.00"); 

        prodIdVal.setText(Integer.toString(product.getProductID()));
        prodNameVal.setText(product.getName());
        prodQtyVal.setText(Integer.toString(product.getInStock()));
        prodCostVal.setText(decimal.format(product.getPrice()));
        prodMinVal.setText(outputDefault(Integer.toString(product.getMin())));
        prodMaxVal.setText(outputDefault(Integer.toString(product.getMax())));
        
        ObservableList<Part> tempList = FXCollections.observableArrayList();
        tempList = product.getAllAssociatedParts();
               
        for (int x = 0; x < tempList.size(); x++) {
            localParts.add(Inventory.lookupPart(tempList.get(x).getPartID()));            
        }
        partsTable2.setItems(localParts);
    }  
      
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currency = NumberFormat.getCurrencyInstance();
        
        partIdTable1.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partIdTable2.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partNameTable1.setCellValueFactory(new PropertyValueFactory<>("name"));
        partNameTable2.setCellValueFactory(new PropertyValueFactory<>("name"));
        partQtyTable1.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        partQtyTable2.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        partCostTable1.setCellValueFactory(new PropertyValueFactory<>("price"));
        partCostTable1.setCellFactory(tableColumn -> new TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currency.format(price));
                }
            }
        });
        partCostTable2.setCellValueFactory(new PropertyValueFactory<>("price"));
        partCostTable2.setCellFactory(tableColumn -> new TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currency.format(price));
                }
            }
        });
        
        prodQtyVal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,9}")) {
                prodQtyVal.setText(oldValue);
            }
        });
        
        prodQtyVal.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && prodQtyVal.getText().isEmpty()) {
                prodQtyVal.setText("0");
            }            
        });
        
        prodCostVal.textProperty().addListener((observable, oldValue, newValue) -> {
            //if (!(newValue.matches("\\d{0,10}\.\\d{0,2}") && newValue.length() < 10)) {
            if (!newValue.matches("\\d{0,7}(\\.\\d{0,2})?")) {
                prodCostVal.setText(oldValue);
            }
        });

        prodMinVal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,8}")) {
                prodMinVal.setText(oldValue);
            }
        });

        prodMaxVal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,8}")) {
                prodMaxVal.setText(oldValue);
            }
        });
        
        localParts.addListener((InvalidationListener) observable -> {
            totalCost = 0.0;
            localParts.forEach((localPart) -> {
                totalCost += localPart.getPrice();
            });           
        });
        
        searchPartsVal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                displayParts();
            }
        });
    }   
}
