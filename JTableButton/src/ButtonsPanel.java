
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author skuarch
 */
public class ButtonsPanel extends JPanel {

    //public final java.util.List<JButton> buttons = java.util.Arrays.asList(new JButton("delete"), new JButton("edit"));
    private JButton deleteButton = new JButton("delete");
    private JButton enableDisableButton = new JButton("enable");
    private JButton editButton = new JButton("edit");

    //==========================================================================
    public ButtonsPanel() {
        super();
        setOpaque(true);
        add(deleteButton);
        add(enableDisableButton);
        add(editButton);
    }

    //==========================================================================
    public void addActionListenerDelete(ActionListener actionListener) {
        deleteButton.addActionListener(actionListener);
    }

    //==========================================================================
    public void addActionListenerEnable(ActionListener actionListener) {
        enableDisableButton.addActionListener(actionListener);
    }

    //==========================================================================
    public void addActionListenerEdit(ActionListener actionListener) {
        editButton.addActionListener(actionListener);
    }

    //==========================================================================
    public JButton getDeleteButton() {
        return deleteButton;
    }

    //==========================================================================
    public void setDeleteButton(JButton deleteButton) {
        this.deleteButton = deleteButton;
    }

    //==========================================================================
    public JButton getEnableDisableButton() {
        return enableDisableButton;
    }

    //==========================================================================
    public void setEnableDisableButton(JButton enableDisableButton) {
        this.enableDisableButton = enableDisableButton;
    }

    //==========================================================================
    public JButton getEditButton() {
        return editButton;
    }

    //==========================================================================
    public void setEditButton(JButton editButton) {
        this.editButton = editButton;
    }
}
