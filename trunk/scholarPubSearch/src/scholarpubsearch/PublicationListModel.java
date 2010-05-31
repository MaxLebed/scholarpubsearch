/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scholarpubsearch;

import java.util.ArrayList;
import messages.Publication.AuthorList;
import messages.Publications;
import messages.Publication;
import java.util.List;
import javax.swing.table.AbstractTableModel;

//Table model for fiew list of publications with check boxes
public class PublicationListModel extends AbstractTableModel {
    private List<Publication> pubs;
    private String[] columnNames = {"","Title",
        "Authors", "Published", "Journal"};
    private Object[][] data = new Object [][] {
        {"","","","",""}};
    public static final Object[] longValues =
        {Boolean.FALSE, "Vary long title Vary long title",
         "Author1 and Author 2", "0000", "Joutnal title" };

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
        if(data[row][col]!=null)
            return data[row][col];
        return "";
    }

    @Override
    public Class getColumnClass(int c) {

        if(c < columnNames.length && data.length > 0) {
            Object value = getValueAt(0, c);
            if(value != null)
                return getValueAt(0, c).getClass();
        }
        return null;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if(value != null)
            data[row][col] = value;
        else
            data[row][col] = "";
        fireTableCellUpdated(row, col);
    }

    public void showPublications(Publications p) {
        pubs = p.getPublication();
        data = new Object [pubs.size()][5];
        fireTableStructureChanged();
        for(int i = 0; i < pubs.size(); i++) {
            Publication pub = pubs.get(i);
            setValueAt(Boolean.FALSE,i,0);
            setValueAt(pub.getTitle(),i,1);
            if(pub.getAuthorList()!=null)
                setValueAt(getAuthorString(pub.getAuthorList()),i,2);
            else
                setValueAt("",i,2);
            if(pub.getYear()!=null)
                setValueAt(pub.getYear().toString(),i,3);
            else
                setValueAt("",i,3);
            if(pub.getJournal()!=null)
                setValueAt(pub.getJournal().getName(),i,4);
            else
                setValueAt("",i,4);
            
        }
        fireTableDataChanged();
    }

    public Publication getPublication(int i) {
        if(pubs == null) {
            return null;
        }
        return pubs.get(i);
    }

    public List<Publication> getPublications() {
        if(pubs == null) {
            pubs = new ArrayList<Publication>();
        }
        return pubs;
    }

    public static Object getAuthorString(AuthorList authorList) {
        String result = "";
        if(authorList != null && authorList.getAuthor().size() > 0) {
            String firstAuthor = authorList.getAuthor().get(0).getName();
            if(firstAuthor != null)
                result += authorList.getAuthor().get(0).getName();
            for(int i = 1; i < authorList.getAuthor().size(); i++) {
                String author =  authorList.getAuthor().get(i).getName();
                if(author != null)
                    result +=" and " + author;
            }
        }
        return result;
    }

    public boolean noSelectedPubs() {
        for(int i = 0; i< data.length; i++) {
            if (data[i][0] == Boolean.TRUE)
                return false;
        }
        return true;
    }
}

