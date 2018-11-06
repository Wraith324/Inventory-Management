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

/**
 *
 * @author David Watkins
 */
public abstract class Part {
    private IntegerProperty partID;
    private StringProperty name;
    private IntegerProperty inStock;
    private DoubleProperty price;
    private IntegerProperty min;
    private IntegerProperty max;
    
    public Part() {        
    }
    
    public Part(int partID, String name, int quantity, Double price, int min, int max) {
        this.partID = new SimpleIntegerProperty(partID);
        this.name = new SimpleStringProperty(name);
        this.inStock = new SimpleIntegerProperty(quantity);
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

    public final void setPartID(int partID) {
        this.partID.set(partID);
    }

    public final int getPartID() {
        return this.partID.get();
    }
}