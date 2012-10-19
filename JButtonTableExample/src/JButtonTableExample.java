
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @version 1.0 11/09/98
 */
public class JButtonTableExample extends JFrame {

    //==========================================================================
    public JButtonTableExample() {
        super("JButtonTable Example");

        DefaultTableModel dm = new DefaultTableModel();
        dm.setDataVector(new Object[][]{{"button 1", "foo"},
                    {"button 2", "bar"}}, new Object[]{"Button", "String"});

        JTable table = new JTable(dm);
        table.getColumn("Button").setCellRenderer(new ButtonRenderer());
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox());
        buttonEditor.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("mocos");
            }
        });
        
        table.getColumn("Button").setCellEditor(buttonEditor);
        JScrollPane scroll = new JScrollPane(table);
        getContentPane().add(scroll);
        setSize(400, 100);
        setVisible(true);
    }

    //==========================================================================
    public static void main(String[] args) {


        JButtonTableExample frame = new JButtonTableExample();
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
