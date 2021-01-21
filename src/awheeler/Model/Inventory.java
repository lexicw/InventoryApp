package awheeler.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author lexic
 */
public class Inventory {

    private static int partIDCount = 0;
    private static int productIDCount = 0;
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    // Parts Inventory
    public static ObservableList<Part> getPartInventory() {
        return allParts;
    }
    public static void addPart(Part part) {
        allParts.add(part);
    }
    public static void removePart(Part part) {
        allParts.remove(part);
    }
    public static void updatePart(int index, Part part) {
        allParts.set(index, part);
    }
    public static int getPartIDCount() {
        partIDCount++;
        return partIDCount;
    }
    public static int getPartIDMinus() {
        partIDCount--;
        return partIDCount;
    }
    
    // Products Inventory
    public static ObservableList<Product> getProductInventory() {
        return allProducts;
    }
    public static void addProduct(Product product) {
        allProducts.add(product);
    }
    public static void removeProduct(Product product) {
        allProducts.remove(product);
    }
    public static int getProductIDCount() {
        productIDCount++;
        return productIDCount;
    }
    public static int getProductIDMinus() {
        productIDCount--;
        return productIDCount;
    }
    public static boolean deletePartValidation(Part part) {
        boolean isFound = false;
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getProductParts().contains(part)) {
                isFound = true;
            }
        }
        return isFound;
    }
    public static Part searchPart(int itemNumber) {
        for(Part x: getPartInventory()){
            if(x.getPartID() == itemNumber){
                return x;                
            }
       }
       return null;
    }
    
    public static Product searchProduct(int searchTerm) {
        for(Product x: getProductInventory()){
            if(x.getProductID() == searchTerm){
                return x;                
            }
       }
       return null;
    }
    public static void updateProduct(int index, Product product) {
        allProducts.set(index, product);
    }
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}