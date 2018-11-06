/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import Model.InhousePart;
import Model.OutsourcedPart;
import Model.Part;
import Model.Product;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
//zimport searchtableview.*

/**
 * FXML Controller class
 *
 * @author David Watkins
 */
public class FXML_InventoryManagementController implements Initializable {
    
    private static FXML_PartController partController;
    private static FXML_ProductController productController;
    private static AtomicInteger newPartID = new AtomicInteger();
    private static AtomicInteger newProdID = new AtomicInteger();

    @FXML
    private Button searchPartsBtn, addPartBtn, modPartBtn, delPartBtn, searchProdBtn, addProdBtn, modProdBtn, delProdBtn, exitProgramBtn;   
    @FXML
    private TextField searchPartsVal, searchProdVal;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableView<Product> prodTable;
    @FXML
    private TableColumn<Part, Integer> partID, partQty;
    @FXML
    private TableColumn<Product, Integer> prodID, prodQty;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Product, String> prodName;
    @FXML
    private TableColumn<Part, Double> partCost;
    @FXML
    private TableColumn<Product, Double> prodCost;
    
    @FXML
    void addPart(ActionEvent event) throws Exception {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/FXML_AddPart.fxml"));
        Parent root = loader.load();
        
        partController = (FXML_PartController)loader.getController();
        partController.setMainController(this);        
        stage.setScene(new Scene(root));
        stage.setTitle("Add Part");
        stage.setResizable(false);
        
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(addPartBtn.getScene().getWindow());
        stage.showAndWait();        
    }
    
    @FXML
    void modifyPart(ActionEvent event) throws Exception {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if (partsTable.getItems().size() < 1) {
            return; // Parts table is empty.
        } else if (selectedPart == null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Part Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a part to modify from the parts table.");
            alert.showAndWait();
            return; // No part selected.
        } 
        
        // Open Modify Part window.
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/FXML_AddPart.fxml"));
        Parent root = loader.load();
        
        partController = (FXML_PartController)loader.getController();
        partController.setMainController(this);        
        stage.setScene(new Scene(root));
        stage.setTitle("Modify Part");
        stage.setResizable(false);
        
        partController.loadPart(selectedPart); 
            
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(addPartBtn.getScene().getWindow());
        stage.showAndWait();
    }
    
    @FXML
    void deletePart(ActionEvent event) {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if (partsTable.getItems().size() < 1) {
            // Parts table is empty.
        } else if (selectedPart == null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Part Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a part to delete from the parts table.");
            alert.showAndWait();            
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Part");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this part?\n\n"
                    + "Part ID:  " + selectedPart.getPartID() + "\n"
                    + "Part Name:  " + selectedPart.getName());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Inventory.deletePart(selectedPart.getPartID());
                displayParts();
            } else {
                // User chose CANCEL or closed the dialog
            }
        }
    }

    @FXML
    void addProduct(ActionEvent event) throws Exception {
        // You can ONLY create a product if parts are available.
        if (partsTable.getItems().size() < 1) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Parts Available");
            alert.setHeaderText(null);
            alert.setContentText("No parts available to associate to a product.\n"
                + "Please add one or more parts before creating a product.");
            alert.showAndWait();
            return; // No parts to create a product with.
        }
        
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/FXML_AddProduct.fxml"));
        Parent root = loader.load();
        
        productController = (FXML_ProductController)loader.getController();
        productController.setMainController(this);
        stage.setScene(new Scene(root));
        stage.setTitle("Add Product");
        stage.setResizable(false);
        
        productController.displayParts();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(addProdBtn.getScene().getWindow());
        stage.showAndWait();        
    }
    
    
    @FXML
    void modifyProduct(ActionEvent event) throws Exception {
        Product selectedProd = prodTable.getSelectionModel().getSelectedItem();
        if (prodTable.getItems().size() < 1) {
            return; // Products table is empty.
        } else if (selectedProd == null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Product Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to modify from the products table.");
            alert.showAndWait();
            return;
        }
        
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/FXML_AddProduct.fxml"));
        Parent root = loader.load();
        
        productController = (FXML_ProductController)loader.getController();
        productController.setMainController(this);
        stage.setScene(new Scene(root));
        stage.setTitle("Modify Product");
        stage.setResizable(false);
            
        productController.loadProduct(selectedProd);
        productController.displayParts();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(addProdBtn.getScene().getWindow());
        stage.showAndWait();
    }
    
    @FXML
    void deleteProduct(ActionEvent event) {
        Product selectedProd = prodTable.getSelectionModel().getSelectedItem();

        if (prodTable.getItems().size() < 1) {
            // Parts table is empty.
        } else if (selectedProd == null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Product Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to delete from the products table.");
            alert.showAndWait();            
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Product");
            alert.setHeaderText(null);
            alert.setContentText("This product is associated with one or more parts.\n"
                    + "Are you sure you want to delete this product?\n\n"
                    + "Product ID:  " + selectedProd.getProductID() + "\n"
                    + "Product Name:  " + selectedProd.getName());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Inventory.removeProduct(selectedProd.getProductID());
                displayProducts();
            } else {
                // User chose CANCEL or closed the dialog
            }
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
        sortedParts.comparatorProperty().bind(partsTable.comparatorProperty());
        partsTable.setItems(sortedParts);       
    }
    
    @FXML
    void searchProducts(ActionEvent event) {
        FilteredList<Product> filteredProducts = new FilteredList<>(Inventory.getAllProducts());
        filteredProducts.setPredicate(product -> {
            if (searchProdVal.getText() == null || searchProdVal.getText().isEmpty()) {
                return true;
            }
            String lowerCaseFilter = searchProdVal.getText().toLowerCase();
                
            if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches Name
            }
            else if (Integer.toString(product.getProductID()).contains(lowerCaseFilter)) {
                return true; // Filter matches Part ID
            }
            else if (Integer.toString(product.getInStock()).contains(lowerCaseFilter)) {
                return true; // Filter matches In-Stock amount
            }
            else if (Double.toString(product.getPrice()).contains(lowerCaseFilter)) {
                return true; // Filter matches Price
            }
            return false; // No matches found
        });
        
        SortedList<Product> sortedProducts = new SortedList<>(filteredProducts);
        sortedProducts.comparatorProperty().bind(prodTable.comparatorProperty());
        prodTable.setItems(sortedProducts);       
    }

    @FXML
    void exitProgram(ActionEvent event) {
        Platform.exit();
    }
    
    public int getNewPartID() {
        return newPartID.incrementAndGet();
    }
    
    public int getNewProdID() {
        return newProdID.incrementAndGet();
    }
    
    public void displayParts() {
        partsTable.setItems(Inventory.getAllParts());        
    }
    
    public void displayProducts() {
        // Refreshes parts table when a part is modified
        prodTable.setItems(Inventory.getAllProducts());        
    }
    
    public void refreshParts() {
        // Refreshes parts table when a part is modified
        partsTable.refresh();        
    }
    public void refreshProducts() {
        // Refreshes parts table when a part is modified
        prodTable.refresh();        
    }
      
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
       
        partID.setCellValueFactory(new PropertyValueFactory<>("partID"));
        prodID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partQty.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        prodQty.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        partCost.setCellFactory(tableColumn -> new TableCell<Part, Double>() {
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
        prodCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        prodCost.setCellFactory(tableColumn -> new TableCell<Product, Double>() {
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
        
        searchPartsVal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                displayParts();
            }
        });
        
        searchProdVal.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                displayProducts();
            }
        });
        
        // Test Data
        Inventory.addPart(new OutsourcedPart(newPartID.incrementAndGet(), "Gear", 10, 4.00, 5, 20, "Company A"));
        Inventory.addPart(new OutsourcedPart(newPartID.incrementAndGet(), "Spring", 5, 2.50, 1, 10, "Company B"));
        Inventory.addPart(new InhousePart(newPartID.incrementAndGet(), "Frame", 2, 15.00, 1, 3, 12345));
        Inventory.addPart(new OutsourcedPart(newPartID.incrementAndGet(), "Wheel", 20, 5.00, 1, 50, "Company C"));
        Inventory.addPart(new InhousePart(newPartID.incrementAndGet(), "Sprocket", 20, 2.00, 1, 100, 78910));
        Inventory.addPart(new OutsourcedPart(newPartID.incrementAndGet(), "Hinge", 20, 3.00, 1, 30, "Company D"));
        displayParts();
    }   
}