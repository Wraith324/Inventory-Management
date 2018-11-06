/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author David Watkins
 */
public class InhousePart extends Part {
    protected IntegerProperty machineID = new SimpleIntegerProperty();
    
    public InhousePart() {        
    }
    
    public InhousePart(int partID, String name, int quantity, Double price, int min, int max, int machineID) {
        super(partID, name, quantity, price, min, max);
        this.machineID = new SimpleIntegerProperty(machineID);
    }
    
    public InhousePart(int partID, String name, int quantity, Double price) {
        super(partID, name, quantity, price, 0, 0);
        this.machineID = new SimpleIntegerProperty(0);
    }
    
    public final void setMachineID(int id) {
        this.machineID.set(id);
    }
    public final int getMachineID() {
        return this.machineID.get();
    }
}
