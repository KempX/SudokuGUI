import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.*;

public class Field {
    private IntegerProperty fieldValue = new SimpleIntegerProperty();
    private Set<Integer> possibleValues = new HashSet<Integer>();
    private int size;
    private int block;

    public Field(int size) {
        this.size = size;
        setFieldValue(0);
    }

    private void resetPossibleValues(int size) {
        possibleValues.clear();
        for (int i = 1; i < (size + 1); i++){
            possibleValues.add(i);
        }
    }

    public void setFieldValue(int i){
        if (i == 0){
            fieldValue.set(0);
            resetPossibleValues(size);
        } else {
            fieldValue.set(i);
            possibleValues.clear();
            possibleValues.add(i);
        }
    }

    public int getFieldValue(){
        return fieldValue.getValue();
    }

    public IntegerProperty getProperty() {
        return fieldValue;
    }

    public void setBlock(int i){
        block = i;
    }

    public int getBlock(){
        return block;
    }

    public void addPossibleValue(int i){
        possibleValues.add(i);
    }

    public void removePossibleValue(int i){
        possibleValues.remove(i);
    }

    public Set<Integer> getPossibleValues (){
        return possibleValues;
    }
}