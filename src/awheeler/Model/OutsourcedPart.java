package awheeler.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author lexic
 */
public class OutsourcedPart extends Part {

    private final StringProperty companyName;

    public OutsourcedPart() {
        super();
        companyName = new SimpleStringProperty();
    }
    public String getPartCompanyName() {
        return this.companyName.get();
    }
    public void setPartCompanyName(String companyName) {
        this.companyName.set(companyName);
    }
}