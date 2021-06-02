import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ShowResultFrame {

    public static void show(ArrayList<Lexer.Token> tokens) {
        String[] Column = {"Line No", "Lexeme", "Return Token", "Lexeme No in Line", "Matchability"};
        JLabel label = new JLabel("Total NO of errors: " + 0);

        DefaultTableModel model = new DefaultTableModel(Column, 0);

        for (Lexer.Token token : tokens)
            model.addRow(new Object[]{token.line, token.lexeme, token.tokentype, token.pos, "Matched"});


        JTable table = new JTable(model) {
            public boolean isCellEditable(int r, int w) {
                return false;
            }
        };


        Font font = new Font("Verdana", Font.PLAIN, 12);
        table.setFont(font);
        table.setRowHeight(30);
        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 500);
        frame.add(new JScrollPane(table));
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLayout(new FlowLayout());
        frame.add(label);
    }
}