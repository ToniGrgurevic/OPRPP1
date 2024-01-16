package hr.fer.oprpp1.hw08.jnotepadpp.documentModels;

import hr.fer.oprpp1.hw08.jnotepadpp.liseners.MultipleDocumentListener;

import javax.swing.*;
import java.nio.file.Path;

public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
    /**
     * returns the graphical component which is responsible for displaying the entire
     * MultipleDocumentModel‘s user interface. This component will be added into the hierarchy of graphical
     * components which are displayed in window.
     * @return  If the MultipleDocumentModel is graphical component itself, it will simply implement the method as: return this;
     */
    JComponent getVisualComponent();
    SingleDocumentModel createNewDocument();
    SingleDocumentModel getCurrentDocument();

    /**
     * Loads document from given path.
     * @param path Path to document.Can't be null.
     * @return Loaded document.
     */
    SingleDocumentModel loadDocument(Path path);

    /**
     * Saves document to given path.
     * @param model Document to save.Can't be null.
     * @param newPath Path to save document to. newPath can be null ->  if null, document should be saved using path associated from
     * document, otherwise, new path should be used and after saving is completed, document’s path should be
     * updated to newPath
     */
    void saveDocument(SingleDocumentModel model, Path newPath);

    /**
     * Closes given document.removes specified document (does not check modification status or ask any questions).
     * @param model Document to close.Can't be null.
     */
    void closeDocument(SingleDocumentModel model);
    void addMultipleDocumentListener(MultipleDocumentListener l);
    void removeMultipleDocumentListener(MultipleDocumentListener l);
    int getNumberOfDocuments();
    SingleDocumentModel getDocument(int index);

    /**
     * given path must not be null.
     * @param path Path to document.
     * @return SingleDocumentModel
     */
    SingleDocumentModel findForPath(Path path); //null, if no such model exists
    int getIndexOfDocument(SingleDocumentModel doc); //-1 if not present
}
