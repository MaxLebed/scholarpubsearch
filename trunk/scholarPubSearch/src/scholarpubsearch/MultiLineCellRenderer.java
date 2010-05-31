package scholarpubsearch;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;

public class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

    public MultiLineCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setFont(table.getFont());
        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            if (table.isCellEditable(row, column)) {
                setForeground(UIManager.getColor("Table.focusCellForeground"));
                setBackground(UIManager.getColor("Table.focusCellBackground"));
            }
        } else {
            setBorder(new EmptyBorder(1, 2, 1, 2));
        }
        setText((value == null) ? "" : value.toString());

        Component comp = table.getDefaultRenderer(table.getColumnClass(column)).
                getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        int prefWidth = comp.getPreferredSize().width;
        int columnWidth = table.getColumnModel().getColumn(column).getWidth();
        int currentHeight = table.getRowHeight(row);

        int height = 16 + (prefWidth / columnWidth) * 16;

        table.setRowHeight(row, Math.max(height, currentHeight));
        return this;
    }
}
