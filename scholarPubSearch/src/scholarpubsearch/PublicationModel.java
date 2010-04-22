package scholarpubsearch;

import messages.Publication;
import javax.swing.table.AbstractTableModel;

public class PublicationModel extends AbstractTableModel {

    private String[] columnNames = {"Field", "Content"};
    private Object[][] data = new Object[][]{
        {"Title", ""},
        {"Id", ""},
        {"Authors", ""},
        {"Published", ""},
        {"Journal", ""},
        {"Volume", ""},
        {"Number", ""},
        {"Pages", ""},
        {"Abstract", ""},
        {"Category", ""},
        {"PagesCount", ""},
        {"Fulltext link", ""}};

    public static final Object[] longValues =
        {"Fulltext link",
         "A vary long publication title A vary long publication title"};

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        Object value = data[row][col];
        if(value != null)
            return value;
        return "";
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    public void showPublication(Publication p) {
        if (p != null) {
            data[0][1] = p.getTitle();
            data[1][1] = p.getId();
            data[2][1] = PublicationListModel.getAuthorString(p.getAuthorList());
            data[3][1] = p.getYear().toString();
            if(p.getJournal()!=null) {
            data[4][1] = p.getJournal().getName();
            data[5][1] = p.getJournal().getVolume();
            data[6][1] = p.getJournal().getNumber();
            data[7][1] = p.getJournal().getPages();
            }
            data[8][1] = p.getSummary();
            data[9][1] = p.getSubjectArea();
            data[10][1] = p.getPagesCount();
            data[11][1] = p.getFulltext();
            this.fireTableDataChanged();
        }
        else {
            data[0][1] = "";
            data[1][1] = "";
            data[2][1] = "";
            data[3][1] = "";
            data[4][1] = "";
            data[5][1] = "";
            data[6][1] = "";
            data[7][1] = "";
            data[8][1] = "";
            data[9][1] = "";
            data[10][1] = "";
            data[11][1] = "";
            this.fireTableDataChanged();
        }
    }
}
