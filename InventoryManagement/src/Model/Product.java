/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author David Watkins
 */
public class Product {
    protected ObservableList<Part> associatedParts = FXCollections.observableArrayList();;
    protected IntegerProperty productID;
    protected StringProperty name;
    protected IntegerProperty inStock;
    protected DoubleProperty price;
    protected IntegerProperty min;
    protected IntegerProperty max;
    
    public Product() {        
    }
    
    public Product(int productID, String name, int inStock, Double price, int min, int max){
        this.productID = new SimpleIntegerProperty(productID);
        this.name = new SimpleStringProperty(name);
        this.inStock = new SimpleIntegerProperty(inStock);
        this.price = new SimpleDoubleProperty(price);
        this.min = new SimpleIntegerProperty(min);
        this.max = new SimpleIntegerProperty(max);
    }

    public final void setName(String name) {
        this.name.set(name);
    }

    public final String getName() {
        return this.name.get();
    }

    public final void setPrice(Double price) {
        this.price.set(price);
    }

    public final Double getPrice() {
        return this.price.get();
    }

    public final void setInStock(int inStock) {
        this.inStock.set(inStock);
    }

    public final int getInStock() {
        return this.inStock.get();
    }

    public final void setMin(int min) {
        this.min.set(min);
    }

    public final int getMin() {
        return this.min.get();
    }

    public final void setMax(int max) {
        this.max.set(max);
    }

    public final int getMax() {
        return this.max.get();
    }

    public final void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    public final boolean removeAssociatedPart(int partID) {
        for(int x = 0; x < associatedParts.size(); x++) {
            if(associatedParts.get(x).getPartID() == partID) {
                associatedParts.remove(x);
                return true;
            }
        }
        return false;
    }
   
    public final Part lookupAssociatedPart(int partID) {
        for(int x = 0; x < associatedParts.size(); x++) {
            if(associatedParts.get(x).getPartID() == partID) {
                return associatedParts.get(x);
            }
        }
        return null;
    }
    
    public final ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;        
    }
    
    public final void setProductID(int productID) {
        this.productID.set(productID);
    }

    public final int getProductID() {
        return this.productID.get();
    }    
}