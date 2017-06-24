package restaurant.model.tmodels;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import restaurant.model.dto.MealDTO;

/**
 *
 * @author igor
 */

public class TModelMeal implements TableModel{
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_TYPE = 2;
    public static final int COLUMN_PRICE = 3;
    public static final int COLUMN_VEGETARIAN = 4;
    public static final int COLUMN_TRANSGENIC = 5;
    public static final int COLUMN_AVAILABLE = 6;
    public static final int COLUMN_SERVES_NUMBER = 7;
    private final String[] titles = {
        "ID", "Name", "Category","Price","Vegetarian",
        "Transgenic", "Available", "Number"};
    private List<MealDTO> list;
    private List<Integer> numServes;

    public TModelMeal(List<MealDTO> list) {
        this.list = list;
        numServes = new ArrayList<>();
        for(int i = 0; i < list.size(); ++i){
            numServes.add(0);
        }
    }
    
    public MealDTO getMeal(int row){
        if(row >= 0 && row < getRowCount()){
            return list.get(row);
        } else {
            throw new IllegalArgumentException("Invalid row index");
        }
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return titles.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return titles[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(list.isEmpty()){
            return Object.class;
        } else {
            return getValueAt(0, columnIndex).getClass();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COLUMN_SERVES_NUMBER;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MealDTO meal = list.get(rowIndex);
        Object result = null;
        switch(columnIndex){
            case COLUMN_ID :
                result = meal.getId();
                break;
            case COLUMN_NAME :
                result = meal.getName();
                break;
            case COLUMN_TYPE :
                result = meal.getType();
                break;
            case COLUMN_PRICE :
                result = meal.getPrice();
                break;
            case COLUMN_VEGETARIAN :
                result = meal.isVegetarian();
                break;
            case COLUMN_TRANSGENIC :
                result = meal.isTransgenic();
                break;
            case COLUMN_AVAILABLE :
                result = meal.isAvailable();
                break;
            case COLUMN_SERVES_NUMBER :
                result = numServes.get(rowIndex);
                break;
            default : 
                throw new IllegalArgumentException("Column index is not valid");
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex == COLUMN_SERVES_NUMBER){
            numServes.set(rowIndex, (Integer)aValue);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }

}

