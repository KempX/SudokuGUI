import java.util.*;

public class Field {
    private int fieldValue;
    private Set<Integer> possibleValues = new HashSet<Integer>();
    private int block;

    public Field(int size) {
        possibleValues.clear();
        for (int i = 1; i < (size + 1); i++){
            possibleValues.add(i);
        }
    }

    public void setFieldValue(int i){
        fieldValue = i;
        possibleValues.clear();
        possibleValues.add(i);
    }

    public int getFieldValue(){
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

    public boolean hasPossibleValue(int i){
        return possibleValues.contains(i);
    }
}