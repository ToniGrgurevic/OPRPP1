package hr.fer.oprpp1.hw08.jnotepadpp.documentModels;

import hr.fer.oprpp1.hw08.jnotepadpp.liseners.SingleDocumentListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DefaultSingleDocumentModel implements SingleDocumentModel {


    private JTextArea textArea;

    private Path path;

    private List<SingleDocumentListener> listeners= new ArrayList<>();

    private boolean modified;





    public DefaultSingleDocumentModel(Path path, String text) {
        this.path = path;
        textArea = new JTextArea(text);
        modified = false;

        textArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setModified(true);
            }
        });
    }

    @Override
    public JTextArea getTextComponent() {
        return textArea;
    }

    @Override
    public Path getFilePath() {
        return path;
    }

    @Override
    public void setFilePath(Path path) {
        listeners.forEach(l -> l.documentFilePathUpdated(this));
        this.path = path;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
        listeners.forEach(l -> l.documentModifyStatusUpdated(this));
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }

    @Override
    public List<SingleDocumentListener> getLiseners() {
        return listeners;

    }


}
