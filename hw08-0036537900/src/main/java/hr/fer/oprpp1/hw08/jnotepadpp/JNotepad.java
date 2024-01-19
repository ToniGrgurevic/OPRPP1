package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.documentModels.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.documentModels.DefaultSingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.documentModels.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.liseners.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.liseners.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import static java.awt.SystemColor.text;
import static java.nio.file.Files.readAllBytes;

public class JNotepad extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultMultipleDocumentModel model;

    private String copyed;
    public static ImageIcon modifiedIcon;
    public static ImageIcon unmodifiedIcon;

    private JPanel statusBar;

    private JToolBar toolBar;

    private JLabel clock = new JLabel();
    private JLabel lengthStatus = new JLabel();

    private JLabel caretStatus =  new JLabel();


    public JNotepad() {


        setLocation(0, 0);
        setSize(600, 600);

        this.setTitle("(unnamed) - JNotepad++");

        initGUI();
    }

    private void initGUI() {

        model = new DefaultMultipleDocumentModel();


        this.getContentPane().setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);


        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitAction.actionPerformed(new ActionEvent(this, 0, "exiting"));
            }
        });

        try {
            unmodifiedIcon =initilaizeIcon("icons/redDisk.png");
            modifiedIcon= initilaizeIcon("icons/greenDisk.png");
        }catch (IOException e) {
            throw new RuntimeException("Cannot load icons.");
        }

        createActions();
        createMenus();
        createToolbars();

        //createNewDocument(null);
        model.createNewDocument();
        model.createSingleDocLisener();
        createTabbedPane();
        createStatusBar();



    }



    private void createStatusBar() {
        statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        statusBar.add(lengthStatus);
        statusBar.add(Box.createHorizontalGlue()); // Pushes caretStatus to the center
        statusBar.add(caretStatus);
        statusBar.add(Box.createHorizontalGlue()); // Pushes clock to the right
        statusBar.add(clock);


        JPanel bottomPane = new JPanel();
        bottomPane.setLayout(new BorderLayout());

        bottomPane.add(statusBar, BorderLayout.CENTER);
        bottomPane.add(toolBar, BorderLayout.SOUTH);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClock();
            }
        });
        timer.start();


        this.getContentPane().add(bottomPane, BorderLayout.PAGE_END);

        model.getCurrentDocument().getTextComponent().addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                updateStatusBar();
            }
        });
    }

    private void updateClock() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());
        clock.setText( formattedDate);
    }


    private void createToolbars() {



        toolBar = new JToolBar("Alati");
        toolBar.setFloatable(true);

        toolBar.add(new JButton(openDocumentAction));
        toolBar.add(new JButton(Close));
        toolBar.add(new JButton(saveDocumentAction));
        toolBar.add(new JButton(saveAsDocumentAction));
        toolBar.addSeparator();

        toolBar.add(new JButton(deleteSelectedPartAction));

        toolBar.add(new JButton(copySelectedPartAction));
        toolBar.add(new JButton(pasteAction));

        toolBar.addSeparator();
        toolBar.add(new JButton(statisticInfo));

        //this.getContentPane().add(toolBar, BorderLayout.NORTH);
    }

    private ImageIcon initilaizeIcon(String path) throws IOException {

        File file = new File("src/main/resources/hr/fer/oprpp1/hw08/jnotepadpp/"+path);

        // Read the image file into a BufferedImage
        BufferedImage image = ImageIO.read(file);
        Image resizedImage = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        BufferedImage imageWithTransparency = gc.createCompatibleImage(
                20, 20, Transparency.TRANSLUCENT);

        Graphics g = imageWithTransparency.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();


        return new ImageIcon(resizedImage);
    }


    private void createTabbedPane() {

        this.getContentPane().add(model, BorderLayout.CENTER);
        model.setIconAt(0, unmodifiedIcon);

        MultipleDocumentListener lisener = new MultipleDocumentListener() {
                    @Override
                    public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                        if(currentModel.getFilePath() == null) {
                            setTitle("(unnamed) - JNotepad++");
                        } else {
                            setTitle(currentModel.getFilePath().toString() + " - JNotepad++");
                        }

                        int priv = model.getIndexOfDocument(previousModel);
                        int curr = model.getIndexOfDocument(currentModel);


                        java.util.List<SingleDocumentListener> liseners = previousModel.getLiseners();

                        if(curr == -1){
                            liseners.clear();
                            return;
                        }

                        for (SingleDocumentListener lisener : liseners) {
                           currentModel.addSingleDocumentListener(lisener);
                        }
                        liseners.clear();


                        if(priv!=-1) {
                            model.setIconAt(priv,null);
                        }
                        model.setIconAt(curr,  currentModel.isModified() ? modifiedIcon : unmodifiedIcon);
                    }

                    @Override
                    public void documentAdded(SingleDocumentModel singleDocumentModelodel) {

                        if(model.getNumberOfDocuments()==1) {
                            model.setIconAt(0, unmodifiedIcon);
                            model.createSingleDocLisener();

                        }
                        singleDocumentModelodel.getTextComponent().addCaretListener(new CaretListener() {
                            public void caretUpdate(CaretEvent e) {
                                updateStatusBar();
                            }
                        });
                        singleDocumentModelodel.getTextComponent().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                            @Override
                            public void insertUpdate(DocumentEvent e) {
                                updateStatusBar();
                            }

                            @Override
                            public void removeUpdate(DocumentEvent e) {
                                updateStatusBar();
                            }

                            @Override
                            public void changedUpdate(DocumentEvent e) {
                                updateStatusBar();
                            }
                        });

                    }

                    @Override
                    public void documentRemoved(SingleDocumentModel model) {

                    }
        };



        model.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateStatusBar();
            }

        });

        model.addMultipleDocumentListener(lisener);

    }

    private void updateStatusBar( ) {
        try {

            SingleDocumentModel document = model.getDocument(model.getSelectedIndex());
            JTextArea textArea = document.getTextComponent();
            int caretPosition = textArea.getCaretPosition();
            int lineNumber = textArea.getLineOfOffset(caretPosition) + 1;
            int columnNumber = caretPosition - textArea.getLineStartOffset(lineNumber - 1) + 1;
            int selectionLength = Math.abs(textArea.getSelectionEnd() - textArea.getSelectionStart());

            lengthStatus.setText("length: " + document.getTextComponent().getDocument().getLength());

            caretStatus.setText("Ln: " + lineNumber + " Col: " + columnNumber + " Sel: " + selectionLength);



        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }


    private final Action statisticInfo = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            Document doc= model.getCurrentDocument().getTextComponent().getDocument();
            int lenght = doc.getLength();
            JTextArea editor = model.getCurrentDocument().getTextComponent();
            int rows = editor.getLineCount();
            int nonBlankCount = 0;
            try {
                for (char c : doc.getText(0, lenght).toCharArray()) {
                    if (!Character.isWhitespace(c)) {
                        nonBlankCount++;
                    }
                }
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(
                    JNotepad.this,
                    "Your document has "+lenght+" characters, "+rows+" rows and "+nonBlankCount+" non-blank characters.",
                    "Informacija",
                    JOptionPane.INFORMATION_MESSAGE);

        }


    };

    private final Action openNewDocumentAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            model.createNewDocument();
        }
    };

    private final Action openDocumentAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Open file");
            if(fc.showOpenDialog(JNotepad.this)!=JFileChooser.APPROVE_OPTION) {
                return;
            }
            File fileName = fc.getSelectedFile();
            Path filePath = fileName.toPath();
            if(!Files.isReadable(filePath)) {
                JOptionPane.showMessageDialog(
                        JNotepad.this,
                        "Datoteka "+fileName.getAbsolutePath()+" ne postoji!",
                        "Pogreška",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            model.loadDocument(filePath);
        }
    };

    private Action saveDocumentAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;


            @Override
            public void actionPerformed(ActionEvent e) {

                SingleDocumentModel singleDocumentModel = model.getCurrentDocument();
                if(Objects.equals(e.getActionCommand(), "exiting")) singleDocumentModel = model.getDocument(e.getID());


                if(singleDocumentModel.getFilePath()==null) {
                    JFileChooser jfc = new JFileChooser();
                    jfc.setDialogTitle("Save document");
                    if(jfc.showSaveDialog(JNotepad.this)!=JFileChooser.APPROVE_OPTION) {
                        JOptionPane.showMessageDialog(
                                JNotepad.this,
                                "Ništa nije snimljeno.",
                                "Upozorenje",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    singleDocumentModel.setFilePath(jfc.getSelectedFile().toPath());
                }

                model.saveDocument(singleDocumentModel, singleDocumentModel.getFilePath());

            }

    };

    private Action saveAsDocumentAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {


            SingleDocumentModel singleDocumentModel = model.getCurrentDocument();
            if(Objects.equals(e.getActionCommand(), "exiting")) singleDocumentModel = model.getDocument(e.getID());


            if(singleDocumentModel.getFilePath() != null){
                int n = JOptionPane.showConfirmDialog(
                        JNotepad.this,
                        "File već postoji, želite li ga ponovno stvoriti na drugoj lokaciji?",
                        "Spremanje",
                        JOptionPane.YES_NO_OPTION);
                if(n == JOptionPane.NO_OPTION){
                    return;
                }
            }

            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Save document as");
            if(jfc.showSaveDialog(JNotepad.this)!=JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(
                            JNotepad.this,
                            "Ništa nije snimljeno.",
                            "Upozorenje",
                            JOptionPane.WARNING_MESSAGE);
                    return;
            }

            var openedFilePath = jfc.getSelectedFile().toPath();
            singleDocumentModel.setFilePath(openedFilePath);
            model.saveDocument(singleDocumentModel, openedFilePath);
            singleDocumentModel.setModified(false);
            singleDocumentModel.setFilePath(openedFilePath);
        }
    };


    private Action Close = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {


            SingleDocumentModel singleDocumentModel = model.getCurrentDocument();

            if (singleDocumentModel.isModified()) {
                Object[] options = {"Spremi", "Ne spremaj", "Cancel"};
                String message = "File je modificiran, želite li ga spremiti?";
                String tittle = "Spremanje";
                int n = JOptionPane.showOptionDialog(
                        JNotepad.this,
                        message,
                        tittle,
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (n == JOptionPane.CANCEL_OPTION) {
                    return;
                } else if (n == JOptionPane.YES_OPTION) {
                    saveDocumentAction.actionPerformed(new ActionEvent(this, model.getIndexOfDocument(singleDocumentModel), "exiting"));
                }
            }

            model.closeDocument(singleDocumentModel);
            if(!model.iterator().hasNext())
                exitAction.actionPerformed(e);

        }
    };

    private Action deleteSelectedPartAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = model.getCurrentDocument().getTextComponent();
            Document doc = editor.getDocument();
            int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
            if(len==0) return;
            int offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
            try {
                doc.remove(offset, len);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    };

    private Action copySelectedPartAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = model.getCurrentDocument().getTextComponent();
            Document doc = editor.getDocument();
            copyed = editor.getSelectedText();
        }
    };

    private Action pasteAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = model.getCurrentDocument().getTextComponent();
            Document doc = editor.getDocument();
            //copyed = editor.getSelectedText();
            var carter = editor.getCaret();
            try {
                doc.insertString(carter.getDot(), copyed, null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    };


    private Action toggleCaseAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea editor = model.getCurrentDocument().getTextComponent();
            Document doc = editor.getDocument();
            int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
            int offset = 0;
            if(len!=0) {
                offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
            } else {
                len = doc.getLength();
            }
            try {
                String text = doc.getText(offset, len);
                text = changeCase(text);
                doc.remove(offset, len);
                doc.insertString(offset, text, null);
            } catch(BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        private String changeCase(String text) {
            char[] znakovi = text.toCharArray();
            for(int i = 0; i < znakovi.length; i++) {
                char c = znakovi[i];
                if(Character.isLowerCase(c)) {
                    znakovi[i] = Character.toUpperCase(c);
                } else if(Character.isUpperCase(c)) {
                    znakovi[i] = Character.toLowerCase(c);
                }
            }
            return new String(znakovi);
        }
    };

    private Action exitAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {

            for (int i = 0; i < model.getNumberOfDocuments(); i++) {
                var singleDocumentModel =model.getDocument(i);

                if(singleDocumentModel.isModified()) {

                    String path = singleDocumentModel.getFilePath() == null ? "(unnamed)" : singleDocumentModel.getFilePath().toString();
                    String message = "Datoteka  " + path  +" nije snimljena.Želite li je snimiti?";
                    int rezultat = JOptionPane.showConfirmDialog(
                            JNotepad.this,
                            "Datoteka nije snimljena. Želite li je snimiti?",
                            "Upozorenje",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (rezultat == JOptionPane.CANCEL_OPTION) {
                        return;
                    } else if (rezultat == JOptionPane.NO_OPTION) {
                        // model.closeDocument(singleDocumentModel);
                    } else {
                        if (singleDocumentModel.getFilePath() == null)
                            saveAsDocumentAction.actionPerformed(new ActionEvent(this,i,"exiting"));
                        else saveDocumentAction.actionPerformed(new ActionEvent(this,i,"exiting"));
                        // model.closeDocument(singleDocumentModel);

                    }


                }
            }
            dispose();
        }
    };

    private void createActions() {

        openDocumentAction.putValue(
                Action.NAME,
                "Open");
        openDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control O"));
        openDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_O);
        openDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to open existing file from disk.");

        openNewDocumentAction.putValue(
                Action.NAME,
                "Open New");
        openNewDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control N"));
        openNewDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_N);
        openNewDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to open new file that doesn't exist on disk.");



        saveDocumentAction.putValue(
                Action.NAME,
                "Save");
        saveDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control S"));
        saveDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_S);
        saveDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to save current file to disk.");

        saveAsDocumentAction.putValue(
                Action.NAME,
                "Save As");
        saveAsDocumentAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control E"));
        saveAsDocumentAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_E);
        saveAsDocumentAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to save current file to disk.");

        deleteSelectedPartAction.putValue(
                Action.NAME,
                "cut");
        deleteSelectedPartAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("F2"));
        deleteSelectedPartAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_D);
        deleteSelectedPartAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to delete the selected part of text.");

        toggleCaseAction.putValue(
                Action.NAME,
                "Toggle case");
        toggleCaseAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control F3"));
        toggleCaseAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_T);
        toggleCaseAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Used to toggle character case in selected part of text or in entire document.");


        exitAction.putValue(
                Action.NAME,
                "Exit");
        exitAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control X"));
        exitAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_X);
        exitAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Exit application.");


        copySelectedPartAction.putValue(
                Action.NAME,
                "Copy");
        copySelectedPartAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control C"));
        copySelectedPartAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_C);
        copySelectedPartAction.putValue(
                Action.SHORT_DESCRIPTION,
                "Copy selected part of text.");

        pasteAction.putValue(
                Action.NAME,
                "Paste");
        pasteAction.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control V"));
        pasteAction.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_V);
        pasteAction.putValue(
                Action.SHORT_DESCRIPTION,
                "paste selected (copied) part of text.");

        statisticInfo.putValue(
                Action.NAME,
                "Statistic info");
        statisticInfo.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control I"));
        statisticInfo.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_I);
        statisticInfo.putValue(
                Action.SHORT_DESCRIPTION,
                "Shows statistic info about document.");

        Close.putValue(
                Action.NAME,
                "Close");
        Close.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control W"));
        Close.putValue(
                Action.MNEMONIC_KEY,
                KeyEvent.VK_W);
        Close.putValue(
                Action.SHORT_DESCRIPTION,
                "Close current document.");

    }

    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(openDocumentAction));
        fileMenu.add(new JMenuItem(openNewDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(saveDocumentAction));
        fileMenu.add(new JMenuItem(saveAsDocumentAction));
        fileMenu.addSeparator();

        fileMenu.add(new JMenuItem(statisticInfo));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(Close));
        fileMenu.add(new JMenuItem(exitAction));



        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        editMenu.add(new JMenuItem(deleteSelectedPartAction));
        editMenu.add(new JMenuItem(toggleCaseAction));
        editMenu.add(new JMenuItem(copySelectedPartAction));
        editMenu.add(new JMenuItem(pasteAction));


        this.setJMenuBar(menuBar);
    }



    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            LocalizationProvider lp = LocalizationProvider.getInstance();
            @Override
            public void run() {
                new JNotepad().setVisible(true);
            }
        });
    }

}