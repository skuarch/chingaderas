

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author skuarch
 */
public class ButtonsEditor extends ButtonsPanel implements TableCellEditor {
    
    private JButton delete = null;
    private JButton enableDisable = null;
    private JButton edit = null;

    //==========================================================================
    public ButtonsEditor(final JTable table) {
        super();

        //---->
        //DEBUG: view button click -> control key down + edit button(same cell) press -> remain selection color
        MouseListener ml = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                ButtonModel m = ((JButton) e.getSource()).getModel();
                if (m.isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) {
                    setBackground(table.getBackground());
                }
            }
        };
        
        delete = getDeleteButton();
        delete.addMouseListener(ml);        
        enableDisable = getEnableDisableButton();
        enableDisable.addMouseListener(ml);
        edit = getEditButton();
        edit.addMouseListener(ml);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                fireEditingStopped();
            }
        });
    }

    public JButton getDelete() {
        return delete;
    }

    public void setDelete(JButton delete) {
        this.delete = delete;
    }

    public JButton getEdit() {
        return edit;
    }

    public void setEdit(JButton edit) {
        this.edit = edit;
    }

    public JButton getEnableDisable() {
        return enableDisable;
    }

    public void setEnableDisable(JButton enableDisable) {
        this.enableDisable = enableDisable;
    }    
    
    //==========================================================================
    public void addListenerButtonEnable(ActionListener actionListener){
        enableDisable.addActionListener(actionListener);
    }
    
    //==========================================================================
    public void addListenerButtonDelete(ActionListener actionListener){
        delete.addActionListener(actionListener);
    }
    
    //==========================================================================
    public void addListenerButtonEdit(ActionListener actionListener){
        edit.addActionListener(actionListener);
    }

    //==========================================================================
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.setBackground(table.getSelectionBackground());
        return this;
    }

    //==========================================================================
    @Override
    public Object getCellEditorValue() {
        return "";
    }
    //Copid from AbstractCellEditor
    //protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;

    //==========================================================================
    @Override
    public boolean isCellEditable(java.util.EventObject e) {
        return true;
    }

    //==========================================================================
    @Override
    public boolean shouldSelectCell(java.util.EventObject anEvent) {
        return true;
    }

    //==========================================================================
    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    //==========================================================================
    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    //==========================================================================
    @Override
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    //==========================================================================
    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    //==========================================================================
    public CellEditorListener[] getCellEditorListeners() {
        return listenerList.getListeners(CellEditorListener.class);
    }

    //==========================================================================
    protected void fireEditingStopped() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
            }
        }
    }

    //==========================================================================
    protected void fireEditingCanceled() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    }
}
