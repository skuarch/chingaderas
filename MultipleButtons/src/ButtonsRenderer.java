
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author skuarch
 */
public class ButtonsRenderer extends ButtonsPanel implements TableCellRenderer {

    //==========================================================================
    public ButtonsRenderer() {
        super();
        setName("Table.cellRenderer");
    }

    //==========================================================================
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this;
    }
}
