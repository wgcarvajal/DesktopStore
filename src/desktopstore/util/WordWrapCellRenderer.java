/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore.util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Wilson Carvajal
 */
public class WordWrapCellRenderer extends JTextPane implements TableCellRenderer {
    public WordWrapCellRenderer() {
        
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setSize(table.getColumnModel().getColumn(column).getWidth(), table.getRowHeight());
       
        return this;
    }
    
    
    
}
