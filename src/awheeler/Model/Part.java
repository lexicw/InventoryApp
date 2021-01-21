package awheeler.Model;

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
public abstract class Part {

    private final IntegerProperty partID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final IntegerProperty min;
    private final IntegerProperty max;

    public Part() {
        partID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        stock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }
    public IntegerProperty partIDProperty() {
        return partID;
    }
    public StringProperty partNameProperty() {
        return name;
    }
    public DoubleProperty partPriceProperty() {
        return price;
    }

    public IntegerProperty partInvProperty() {
        return stock;
    }
    
    // Getters and Setters
    public int getPartID() {
        return this.partID.get();
    }
    public void setPartID(int partID) {
        this.partID.set(partID);
    }
    public String getPartName() {
        return this.name.get();
    }
    public void setPartName(String name) {
        this.name.set(name);
    }
    public double getPartPrice() {
        return this.price.get();
    }
    public void setPartPrice(double price) {
        this.price.set(price);
    }
    public int getPartInStock() {
        return this.stock.get();
    }
    public void setPartInStock(int inStock) {
        this.stock.set(inStock);
    }
    public int getPartMin() {
        return this.min.get();
    }
    public void setPartMin(int min) {
        this.min.set(min);
    }
    public int getPartMax() {
        return this.max.get();
    }
    public void setPartMax(int max) {
        this.max.set(max);
    }

    //Method that checks Parts for validation before adding or modifying
    public static String partValidation(String name, int min, int max, int inv, double price, String errorMsg) {
        if (name.trim().isEmpty()) {
            errorMsg = errorMsg + ("Name field is blank.");
        }
        if (inv < 1) {
            errorMsg = errorMsg + ("Inventory level must be greater than 0.");
        }
        if (price < 1) {
            errorMsg = errorMsg + ("Price must be greater than $0");
        }
        if (inv < min || inv > max) {
            errorMsg = errorMsg + ("Inventory level must be a value between minimum and maximum values.");
        }
        if (min > max) {
            errorMsg = errorMsg + ("Inventory level minimum must be less than the maximum.");
        }
        return errorMsg;
    }
}