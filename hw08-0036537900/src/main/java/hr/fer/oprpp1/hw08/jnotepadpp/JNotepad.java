package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.documentModels.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.documentModels.DefaultSingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.documentModels.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.liseners.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.liseners.SingleDocumentListener;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import static java.awt.SystemColor.text;

public class JNotepad extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultMultipleDocumentModel model;

    private String copyed;

    public JNotepad() {

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
                for (int i = 0; i < model.getNumberOfDocuments(); i++) {
                    var singleDocumentModel =model.getDocument(i);

                    if(!singleDocumentModel.isModified()) {

                        int rezultat = JOptionPane.showConfirmDialog(
                                JNotepad.this,
                                "Datoteka nije snimljena. Želite li je snimiti?",
                                "Upozorenje",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                        if (rezultat == JOptionPane.CANCEL_OPTION) {
                            return;
                        } else if (rezultat == JOptionPane.NO_OPTION) {
                            model.closeDocument(singleDocumentModel);
                        } else {
                            if (singleDocumentModel.getFilePath() == null)
                                saveAsDocumentAction.actionPerformed(new ActionEvent(this,i,"exiting"));
                            else saveDocumentAction.actionPerformed(new ActionEvent(this,i,"exiting"));
                            model.closeDocument(singleDocumentModel);
                        }

                    }
                }
                dispose();
            }
        });

        createActions();
        createMenus();
        createToolbars();
        createTabbedPane();

        //createNewDocument(null);
        model.createNewDocument();



    }


    private void createTabbedPane() {

        this.getContentPane().add(model, BorderLayout.CENTER);


        MultipleDocumentListener lisener = new MultipleDocumentListener() {
                    @Override
                    public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                        if(currentModel.getFilePath() == null) {
                            setTitle("(unnamed) - JNotepad++");
                        } else {
                            setTitle(currentModel.getFilePath().toString() + " - JNotepad++");
                        }
                    }

                    @Override
                    public void documentAdded(SingleDocumentModel model) {

                    }

                    @Override
                    public void documentRemoved(SingleDocumentModel model) {

                    }
        };

        model.addMultipleDocumentListener(lisener);


        model.getCurrentDocument().addSingleDocumentListener(new SingleDocumentListener() {

            public void documentModifyStatusUpdated(SingleDocumentModel docModel) {
                model.setIconAt(model.getIndexOfDocument(docModel), docModel.isModified() ? JNotepad.modifiedIcon : JNotepad.unmodifiedIcon);
            }
            public void documentFilePathUpdated(SingleDocumentModel docModel) {
                model.setTitleAt(model.getIndexOfDocument(docModel), docModel.getFilePath() == null ? "(unnamed)" : docModel.getFilePath().getFileName().toString());
                model.setToolTipTextAt(model.getIndexOfDocument(docModel), docModel.getFilePath() == null ? "(unnamed)" : docModel.getFilePath().toString());
            }
        });
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

    private Action deleteSelectedPartAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
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
            Document doc = editor.getDocument();
            copyed = editor.getSelectedText();
        }
    };

    private Action pasteAction = new AbstractAction() {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
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
            System.exit(0);
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




    }

    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(openDocumentAction));
        fileMenu.add(new JMenuItem(saveDocumentAction));
        fileMenu.add(new JMenuItem(saveAsDocumentAction));
        fileMenu.addSeparator();

        fileMenu.add(new JMenuItem(statisticInfo));
        fileMenu.add(new JMenuItem(exitAction));


        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        editMenu.add(new JMenuItem(deleteSelectedPartAction));
        editMenu.add(new JMenuItem(toggleCaseAction));
        editMenu.add(new JMenuItem(copySelectedPartAction));
        editMenu.add(new JMenuItem(pasteAction));


        this.setJMenuBar(menuBar);
    }

    private void createToolbars() {
        JToolBar toolBar = new JToolBar("Alati");
        toolBar.setFloatable(true);

        toolBar.add(new JButton(openDocumentAction));
        toolBar.add(new JButton(saveDocumentAction));
        toolBar.addSeparator();

        toolBar.add(new JButton(deleteSelectedPartAction));
        toolBar.add(new JButton(toggleCaseAction));
        toolBar.addSeparator();

        toolBar.add(new JButton(copySelectedPartAction));
        toolBar.add(new JButton(pasteAction));

        toolBar.addSeparator();
        toolBar.add(new JButton(statisticInfo));

        this.getContentPane().add(toolBar, BorderLayout.PAGE_END);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new JNotepad().setVisible(true);
            }
        });
    }

}