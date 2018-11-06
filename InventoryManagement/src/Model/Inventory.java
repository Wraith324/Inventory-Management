/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author David Watkins
 */
public class Inventory {
    private static ObservableList<Product> products = FXCollections.observableArrayList();
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    
    public static ObservableList<Product> getAllProducts() {
        return products;
    }

    public static void addProduct(Product product) {
        products.add(product);    
    }
    
    public static boolean removeProduct(int productID) {
        for(int x = 0; x < products.size(); x++) {
            if(products.get(x).getProductID() == productID) {
                products.remove(x);
                return true;
            }
        }
        return false;
    }
    public static Product lookupProduct(int productID) {
        for(int x = 0; x < products.size(); x++) {
            if(products.get(x).getProductID() == productID) {
                return products.get(x);
            }
        }
        return null;        
    }
    
    // Find out the index of the product
    public static int getProductIndex(int productID) {
        for(int x = 0; x < products.size(); x++) {
            if(products.get(x).getProductID() == productID) {
                return x;
            }
        }
        return -1;        
    }

    public static void updateProduct(int index, Product product) {
        products.set(index, product);
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static void addPart(Part part) {
        allParts.add(part);
    }

    public static boolean deletePart(int partID) {
        for(int x = 0; x < allParts.size(); x++) {
            if(allParts.get(x).getPartID() == partID) {
                allParts.remove(x);
                return true;
            }
        }
        return false;   
    }

    public static Part lookupPart(int partID) {
        for(int x = 0; x < allParts.size(); x++) {
            if(allParts.get(x).getPartID() == partID) {
                return allParts.get(x);
            }
        }
        return null;         
    }
    
    // Find out the index of the part
    public static int getPartIndex(int partID) {
        for(int x = 0; x < allParts.size(); x++) {
            if(allParts.get(x).getPartID() == partID) {
                return x;
            }
        }
        return -1;        
    }

    public static void updatePart(int index, Part part) {
        allParts.set(index, part);
    }
}
