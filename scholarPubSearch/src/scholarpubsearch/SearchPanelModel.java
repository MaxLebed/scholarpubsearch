/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scholarpubsearch;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Aleksandr Dzyuba
 */
public class SearchPanelModel extends AbstractTableModel{

    private String[] columnNames = {"Field", "Value"};
    private Object[][] data = new Object[][]{
        {"Title", ""},
        {"Abstract", ""},
        {"Author", ""},
    };
    
    public static Object[] longValues =
        {"With fulltext link",
         "A vary long publication title A vary long publication title"};

    @Override
    public int getRowCount() {
         return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
        if(allFieldsFilled()) {
            Object [] [] newData = new Object [data.length+1] [columnNames.length];
            for(int i = 0; i < data.length; i++) {
                newData [i] = data [i];
            }
            newData [data.length] = new Object [] {"Title", ""};
            data = newData;
            fireTableRowsUpdated(data.length, data.length);
        }
    }
        
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
         return true;
    }

    private boolean allFieldsFilled() {
        boolean allFieldsFilled = true;
        for (int i = 0; i < data.length; i++) {
            if(data[i] [1].equals("")) {
                allFieldsFilled = false;
            }
        }
        return allFieldsFilled;
    }

}
