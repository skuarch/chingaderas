
import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * @version 1.0 11/09/98
 */
public class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    

    //==========================================================================
    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
    }

    //==========================================================================
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    //==========================================================================
    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // 
            // 
            JOptionPane.showMessageDialog(button, label + ": Ouch!");
            // System.out.println(label + ": Ouch!");
        }
        isPushed = false;
        return label;
    }

    //==========================================================================
    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    //==========================================================================
    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
    
    //==========================================================================
    public void addActionListener(ActionListener actionListener){        
        button.addActionListener(actionListener);
        fireEditingStopped();
    }
}