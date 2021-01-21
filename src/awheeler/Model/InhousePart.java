package awheeler.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author lexic
 */
public class InhousePart extends Part {

    private final IntegerProperty machineID;

    public InhousePart() {
        super();
        machineID = new SimpleIntegerProperty();
    }
    public int getPartMachineID() {
        return this.machineID.get();
    }
    public void setPartMachineID(int machineID) {
        this.machineID.set(machineID);
    }

}
