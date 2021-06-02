import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UI extends JFrame implements ActionListener {

    private final JTextArea textArea;
    private final JMenuBar menuBar;
    private final JComboBox<String> fontType;
    private final JComboBox<Integer> fontSize;
    private final JMenu menuFile, menuEdit;
    private final JMenuItem newFile, openFile, saveFile, cut, copy, paste, clearFile, selectAll;
    private final JToolBar mainToolbar;
    private final Action selectAllAction;
    // setup icons - File Menu
    private final ImageIcon newIcon = new ImageIcon("icons/new.png");
    private final ImageIcon openIcon = new ImageIcon("icons/open.png");
    private final ImageIcon saveIcon = new ImageIcon("icons/save.png");
    // setup icons - Edit Menu
    private final ImageIcon clearIcon = new ImageIcon("icons/clear.png");
    private final ImageIcon cutIcon = new ImageIcon("icons/cut.png");
    private final ImageIcon copyIcon = new ImageIcon("icons/copy.png");
    private final ImageIcon pasteIcon = new ImageIcon("icons/paste.png");
    private final ImageIcon selectAllIcon = new ImageIcon("icons/selectall.png");
    private final ImageIcon analyzeIcon = new ImageIcon("icons/run.png");
    // setup icons - Help Menu
    JButton newButton, openButton, saveButton, clearButton, analyzeButton;

    private boolean edit = false;

    public UI() {

        // Set the initial size of the window
        setSize(800, 500);

        // Set the title of the window
        setTitle("Untitled | " + Main.NAME);

        // Set the default close operation (exit when it gets closed)
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // center the frame on the monitor
        setLocationRelativeTo(null);

        // Set a default font for the TextArea
        textArea = new JTextArea("", 0, 0);
        textArea.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        textArea.setTabSize(2);
        textArea.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        textArea.setTabSize(2);


        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setWrapStyleWord(true);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().setLayout(new BorderLayout()); // the BorderLayout bit makes it fill it automatically
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane);
        getContentPane().add(panel);

        // Set the Menus
        menuFile = new JMenu("File");
        menuEdit = new JMenu("Edit");
        //Font Settings menu

        // Set the Items Menu
        newFile = new JMenuItem("New", newIcon);
        openFile = new JMenuItem("Open", openIcon);
        saveFile = new JMenuItem("Save", saveIcon);
        clearFile = new JMenuItem("Clear", clearIcon);

        menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuEdit);


        this.setJMenuBar(menuBar);

        // Set Actions:
        selectAllAction = new SelectAllAction("Select All", clearIcon, "Select all text");

        this.setJMenuBar(menuBar);

        // New File
        newFile.addActionListener(this);
        menuFile.add(newFile);

        // Open File
        openFile.addActionListener(this);
        menuFile.add(openFile);

        // Save File
        saveFile.addActionListener(this);
        menuFile.add(saveFile);


        // Select All Text
        selectAll = new JMenuItem(selectAllAction);
        selectAll.setText("Select All");
        selectAll.setIcon(selectAllIcon);
        selectAll.setToolTipText("Select All");
        menuEdit.add(selectAll);

        // Clear File (Code)
        clearFile.addActionListener(this);
        menuEdit.add(clearFile);

        // Cut Text
        cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        cut.setIcon(cutIcon);
        cut.setToolTipText("Cut");
        menuEdit.add(cut);


        // Copy Text
        copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        copy.setIcon(copyIcon);
        copy.setToolTipText("Copy");
        menuEdit.add(copy);

        // Paste Text
        paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        paste.setIcon(pasteIcon);
        paste.setToolTipText("Paste");
        menuEdit.add(paste);


        mainToolbar = new JToolBar();
        this.add(mainToolbar, BorderLayout.NORTH);
        // used to create space between button groups
        newButton = new JButton(newIcon);
        newButton.setToolTipText("New");
        newButton.addActionListener(this);
        mainToolbar.add(newButton);
        mainToolbar.addSeparator();

        openButton = new JButton(openIcon);
        openButton.setToolTipText("Open");
        openButton.addActionListener(this);
        mainToolbar.add(openButton);
        mainToolbar.addSeparator();

        saveButton = new JButton(saveIcon);
        saveButton.setToolTipText("Save");
        saveButton.addActionListener(this);
        mainToolbar.add(saveButton);
        mainToolbar.addSeparator();

        clearButton = new JButton(clearIcon);
        clearButton.setToolTipText("Clear All");
        clearButton.addActionListener(this);
        mainToolbar.add(clearButton);
        mainToolbar.addSeparator();


        analyzeButton = new JButton(analyzeIcon);
        analyzeButton.setToolTipText("Analyze");
        analyzeButton.addActionListener(this);
        mainToolbar.add(analyzeButton);
        mainToolbar.addSeparator();

        mainToolbar.addSeparator();


        //FONT FAMILY SETTINGS SECTION START
        fontType = new JComboBox<>();

        //GETTING ALL AVAILABLE FONTS
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (String font : fonts) {
            fontType.addItem(font);
        }


        fontType.setMaximumSize(new Dimension(170, 30));
        fontType.setToolTipText("Font Type");
        mainToolbar.add(fontType);
        mainToolbar.addSeparator();

        //Adding Action Listener on fontType JComboBox
        fontType.addActionListener(ev -> {
            //Getting the selected fontType value from ComboBox
            String p = Objects.requireNonNull(fontType.getSelectedItem()).toString();
            //Getting size of the current font or text
            int s = textArea.getFont().getSize();
            textArea.setFont(new Font(p, Font.PLAIN, s));
        });

        //FONT SIZE SETTINGS START
        fontSize = new JComboBox<>();

        for (int i = 5; i <= 100; i++) {
            fontSize.addItem(i);
        }
        fontSize.setMaximumSize(new Dimension(70, 30));
        fontSize.setToolTipText("Font Size");
        mainToolbar.add(fontSize);

        fontSize.addActionListener(ev -> {
            String sizeValue = Objects.requireNonNull(fontSize.getSelectedItem()).toString();
            int sizeOfFont = Integer.parseInt(sizeValue);
            String fontFamily = textArea.getFont().getFamily();

            Font font1 = new Font(fontFamily, Font.PLAIN, sizeOfFont);
            textArea.setFont(font1);
        });
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (edit) {
                Object[] options = {"Save and exit", "No Save and exit", "Cancel"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (n == 0) {// save and exit
                    saveFile();
                    this.dispose();// dispose all resources and close the application
                } else if (n == 1) {// no save and exit
                    this.dispose();// dispose all resources and close the application
                }
            } else {
                System.exit(99);
            }
        }
    }

    // Make the TextArea available to the autocomplete handler
    protected JTextArea getEditor() {
        return textArea;
    }

    public void actionPerformed(ActionEvent e) {
        // If the source was the "new" file option
        if (e.getSource() == newFile || e.getSource() == newButton) {
            if (edit) {
                Object[] options = {"Save", "No Save", "Cancel"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file at first ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 0) {// save
                    saveFile();
                    edit = false;
                } else if (n == 1) {
                    edit = false;
                    textArea.setText("");
                }
            } else {
                textArea.setText("");
            }

        }// If the source was the "analyze" file option
        else if (e.getSource() == analyzeButton) {

            String[] s = textArea.getText().split("\\r?\\n");
            ArrayList<String> arrList = new ArrayList<>(Arrays.asList(s));

            StringBuilder source = new StringBuilder();
            for (String value : arrList)
                source.append(value).append("\n");

            Lexer l = new Lexer(source.toString());
            ArrayList<Lexer.Token> res = l.printTokens();
            ShowResultFrame.show(res);

        }


        // If the source was the "open" option
        else if (e.getSource() == openFile || e.getSource() == openButton) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(".")); //sets current directory
            int response = fileChooser.showOpenDialog(this); //select file to open
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner scanner = null;
                try {
                    scanner = new Scanner(file);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
                }
                StringBuilder source = new StringBuilder();
                while (true) {
                    assert scanner != null;
                    if (!scanner.hasNext()) break;
                    source.append(scanner.nextLine()).append("\n");
                }
                System.out.println(source);
                Lexer l = new Lexer(source.toString());
                ArrayList<Lexer.Token> res = l.printTokens();
                ShowResultFrame.show(res);
            }

        } // If the source of the event was the "save" option
        else if (e.getSource() == saveFile || e.getSource() == saveButton) {
            saveFile();
        }
        // Clear File
        else if (e.getSource() == clearFile || e.getSource() == clearButton) {

            Object[] options = {"Yes", "No"};
            int n = JOptionPane.showOptionDialog(this, "Are you sure to clear the text Area ?", "Question",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0) {// clear
                textArea.setText("");
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(".")); //sets current directory
        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File openFile = fileChooser.getSelectedFile();
                setTitle(openFile.getName() + " | " + Main.NAME);

                BufferedWriter out = new BufferedWriter(new FileWriter(openFile.getPath()));
                out.write(textArea.getText());
                out.close();

                edit = false;
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    class SelectAllAction extends AbstractAction {

        public SelectAllAction(String text, ImageIcon icon, String desc) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        public void actionPerformed(ActionEvent e) {
            textArea.selectAll();
        }
    }

}
