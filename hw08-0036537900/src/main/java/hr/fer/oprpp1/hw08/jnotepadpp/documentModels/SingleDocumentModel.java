package hr.fer.oprpp1.hw08.jnotepadpp.documentModels;

import hr.fer.oprpp1.hw08.jnotepadpp.liseners.SingleDocumentListener;

import javax.swing.*;
import java.nio.file.Path;
import java.util.List;

public interface SingleDocumentModel {
    JTextArea getTextComponent();
    Path getFilePath();
    void setFilePath(Path path);
    boolean isModified();
    void setModified(boolean modified);
    void addSingleDocumentListener(SingleDocumentListener l);
    void removeSingleDocumentListener(SingleDocumentListener l);

    List<SingleDocumentListener> getLiseners();
}

