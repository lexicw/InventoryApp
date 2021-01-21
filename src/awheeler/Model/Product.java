package awheeler.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author lexic
 */
public class Product {

    private final IntegerProperty productID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final IntegerProperty min;
    private final IntegerProperty max;
    
    private static ObservableList<Part> parts = FXCollections.observableArrayList();

    public Product() {
        productID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        stock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }

    public IntegerProperty productIDProperty() {
        return productID;
    }
    public StringProperty productNameProperty() {
        return name;
    }
    public DoubleProperty productPriceProperty() {
        return price;
    }
    public IntegerProperty productInvProperty() {
        return stock;
    }
    public int getProductID() {
        return this.productID.get();
    }
    public void setProductID(int productID) {
        this.productID.set(productID);
    }
    public String getProductName() {
        return this.name.get();
    }
    public void setProductName(String name) {
        this.name.set(name);
    }
    public double getProductPrice() {
        return this.price.get();
    }
    public void setProductPrice(double price) {
        this.price.set(price);
    }
    public int getProductInStock() {
        return this.stock.get();
    }
    public void setProductInStock(int inStock) {
        this.stock.set(inStock);
    }
    public int getProductMin() {
        return this.min.get();
    }
    public void setProductMin(int min) {
        this.min.set(min);
    }
    public int getProductMax() {
        return this.max.get();
    }
    public void setProductMax(int max) {
        this.max.set(max);
    }
    public ObservableList getProductParts() {
        return parts;
    }
    public void setProductParts(ObservableList<Part> parts) {
        this.parts = parts;
    }
    //Method that checks Products for validation before adding or modifying
    public static String productValidation(String name, int min, int max, int inv, double price, ObservableList<Part> parts, String message) {
        double sumOfParts = 0.00;
        for (int i = 0; i < parts.size(); i++) {
            sumOfParts = sumOfParts + parts.get(i).getPartPrice();
        }
        if (name.trim().isEmpty()) {
            message = message + (" Name field is blank.");
        }
        if (min < 0) {
            message = message + (" Inventory level must be greater than 0.");
        }
        if (price < 0) {
            message = message + (" Price must be greater than $0.");
        }
        if (inv < min || inv > max) {
            message = message + (" Inventory level must be a value between minimum and maximum values.");
        }
        if (min > max) {
            message = message + (" Inventory level minimum must be less than the maximum.");
        }
        if (parts.size() < 1) {
            message = message + (" Product must contain at least 1 part.");
        }
        if (sumOfParts > price) {
            message = message + (" Product price must be greater than cost of parts.");
        }
        return message;
    }
}
