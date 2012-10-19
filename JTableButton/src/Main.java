
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author skuarch
 */
public class Main extends JFrame {

    //==========================================================================
    public Main() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        JPanel panel1 = new ButtonsPanel();
        

        DefaultTableModel dm = new DefaultTableModel();
        Object[][] objects = new Object[][]{
            {"Group 1", new ButtonsPanel()},
            {"Group 1", new ButtonsPanel()},
            {"Group 1", new ButtonsPanel()},
            {"Group 2", new ButtonsPanel()},
            {"Group 2", new ButtonsPanel()}
        };

        for (Object[] objects1 : objects) {
            for (Object object : objects1) {
                System.out.println(object.getClass());
            }
        }
        
        String[] columns = new String[]{"String", "JButton"};

        dm.setDataVector(objects, columns);
        JTable table = new JTable(dm) {

            public void tableChanged(TableModelEvent e) {
                super.tableChanged(e);
                repaint();
            }
        };

        table.setModel(dm);

        ButtonsPanel bp = (ButtonsPanel) dm.getValueAt(0, 1);
        bp.addActionListenerDelete(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("a huevo puto");
            }
        });
        
        table.getColumn("JButton").setCellRenderer(new ButtonsRenderer());
        table.getColumn("JButton").setCellEditor(new ButtonsEditor(table));


        table.setRowHeight(40);
        add(table);

        setSize(new Dimension(500, 500));
        setVisible(true);
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new Main();

    }
}
