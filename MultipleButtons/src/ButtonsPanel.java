
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author skuarch
 */
public class ButtonsPanel extends JPanel {

    public final java.util.List<JButton> buttons = java.util.Arrays.asList(new JButton("view"), new JButton("edit"));

    //==========================================================================
    public ButtonsPanel() {
        super();
        setOpaque(true);
        for (JButton b : buttons) {
            b.setFocusable(false);
            b.setRolloverEnabled(false);
            add(b);
        }
    }
}
