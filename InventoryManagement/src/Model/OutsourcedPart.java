/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author David Watkins
 */
public class OutsourcedPart extends Part {
    private StringProperty companyName;
    
    public OutsourcedPart() {
    }
    
    public OutsourcedPart(int partID, String name, int quantity, Double price, int min, int max, String companyName) {
        super(partID, name, quantity, price, min, max);
        this.companyName = new SimpleStringProperty(companyName);
    }
    
    public OutsourcedPart(int partID, String name, int quantity, Double price) {
        super(partID, name, quantity, price, 0, 0);
        this.companyName = new SimpleStringProperty("");
    }
    
    public final String getCompanyName() {
        return this.companyName.get();
    }

    public final void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }
}
