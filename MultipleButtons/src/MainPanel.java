import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class MainPanel extends JPanel {

    //==========================================================================
    public MainPanel() {
        super(new BorderLayout());
        add(new JScrollPane(makeTable()));
        setBorder(BorderFactory.createTitledBorder("Multiple Buttons in a Table Cell"));
        setPreferredSize(new Dimension(320, 240));
    }

    //==========================================================================
    private JTable makeTable() {
        String[] columnNames = {"String", "Button"};
        Object[][] data = {
            {"AAA", ""}, {"CCC", ""}, {"BBB", ""}, {"ZZZ", ""}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {

            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        final JTable table = new JTable(model);
        table.setRowHeight(36);
        table.setAutoCreateRowSorter(true);
        //table.addMouseListener(new CellButtonsMouseListener());
        //ButtonsEditorRenderer er = new ButtonsEditorRenderer(table);
        TableColumn column = table.getColumnModel().getColumn(1);
        column.setCellRenderer(new ButtonsRenderer());
        column.setCellEditor(new ButtonsEditor(table));
        return table;
    }

    //==========================================================================
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    //==========================================================================
    public static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("MultipleButtonsInTableCell");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


// class CellButtonsMouseListener extends MouseAdapter{
//     @Override public void mouseReleased(MouseEvent e) {
//         JTable t = (JTable)e.getComponent();
//         Point pt = e.getPoint();
//         int row  = t.rowAtPoint(pt);
//         int col  = t.columnAtPoint(pt);
//         if(t.convertRowIndexToModel(row)>=0 && t.convertColumnIndexToModel(col)==1) {
//             TableCellEditor ce = t.getCellEditor(row, col);
//             ce.stopCellEditing();
//             Component c = ce.getTableCellEditorComponent(t, null, true, row, col);
//             Point p = SwingUtilities.convertPoint(t, pt, c);
//             Component b = SwingUtilities.getDeepestComponentAt(c, p.x, p.y);
//             if(b instanceof JButton) ((JButton)b).doClick();
//         }
//     }
// }

