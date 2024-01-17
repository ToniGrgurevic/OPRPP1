package hr.fer.oprpp1.hw08.jnotepadpp.documentModels;

import hr.fer.oprpp1.hw08.jnotepadpp.JNotepad;
import hr.fer.oprpp1.hw08.jnotepadpp.liseners.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.liseners.SingleDocumentListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel{


    private List<SingleDocumentModel> documents = new ArrayList<>();

    private SingleDocumentModel currentDocument;

    private List<MultipleDocumentListener> listeners = new ArrayList<>();


    {
        model.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int activeIndex = model.getSelectedIndex();
                if(activeIndex == -1) {
                    currentDocument = null;
                    return;
                }
                SingleDocumentModel previusDocument = currentDocument;
                currentDocument = documents.get(activeIndex);
                listeners.forEach(l -> l.currentDocumentChanged(previusDocument, currentDocument));
                String path = currentDocument.getFilePath() == null ? "(unnamed)" : currentDocument.getFilePath().toString();
                System.out.println("trenutni dokument je " + path);

            }

        });


    }


    @Override
    public JComponent getVisualComponent() {
        return this;
    }

    @Override
    public SingleDocumentModel createNewDocument() {



        SingleDocumentModel newDocument = new DefaultSingleDocumentModel(null, "");
        documents.add(newDocument);

        listeners.forEach(l -> l.documentAdded(newDocument));
        listeners.forEach(l -> l.currentDocumentChanged(currentDocument,newDocument));

        this.addTab("(unammed)",new JScrollPane(newDocument.getTextComponent()));
        currentDocument = newDocument;
        this.setSelectedIndex(this.getIndexOfDocument(currentDocument));

        return newDocument;
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        return currentDocument;
    }

    @Override
    public SingleDocumentModel loadDocument(Path path) {

        if(path == null)
            throw new NullPointerException("Path can not be null");


         for (int i = 0; i < documents.size(); i++) {
             if(documents.get(i).getFilePath() == null)
                 continue;
              if (documents.get(i).getFilePath().toAbsolutePath().toString().equals(path.toString())) {
                  SingleDocumentModel previusDocument = currentDocument;
                    currentDocument = documents.get(i);
                    listeners.forEach(l -> l.currentDocumentChanged(previusDocument, currentDocument));
                    this.setSelectedIndex(i);
                    return currentDocument;
              }
         }

        SingleDocumentModel newDocument;

             byte[] okteti;
             try {
                 okteti = Files.readAllBytes(path);
             } catch (Exception ex) {
                 JOptionPane.showMessageDialog(
                         this.getParent(),
                         "Pogreška prilikom čitanja datoteke " + path.getFileName() + ".",
                         "Pogreška",
                         JOptionPane.ERROR_MESSAGE);
                 return null;
             }
             String tekst = new String(okteti, StandardCharsets.UTF_8);
             newDocument = new DefaultSingleDocumentModel(path, tekst);

        documents.add(newDocument);
        listeners.forEach(l -> l.documentAdded(newDocument));
        listeners.forEach(l -> l.currentDocumentChanged(currentDocument,newDocument));
        currentDocument = newDocument;
        this.addTab(path.getFileName().toString(), newDocument.getTextComponent());
        this.setSelectedIndex(documents.size()-1);
        return newDocument;
    }

    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        byte[] podatci = model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8);
        try {
            Files.write(newPath, podatci);
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pogreška prilikom zapisivanja datoteke "+newPath.toFile().getAbsolutePath()+".\nPaĹľnja: nije jasno u kojem je stanju datoteka na disku!",
                    "Pogreška",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(
                this,
                "Datoteka je snimljena.",
                "Informacija",
                JOptionPane.INFORMATION_MESSAGE);

    }

    @Override
    public void closeDocument(SingleDocumentModel model) {


        documents.remove(model);
        listeners.forEach(l -> l.documentRemoved(model));

        if(documents.isEmpty()) {
            currentDocument = null;
            return;
        }else {
            currentDocument = documents.get(0);
            listeners.forEach(l -> l.currentDocumentChanged(model, currentDocument));
        }

        this.removeTabAt(this.getIndexOfDocument(model));

    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.add(l);

    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return documents.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return documents.get(index);
    }

    @Override
    public SingleDocumentModel findForPath(Path path) {
        return null;
    }

    @Override
    public int getIndexOfDocument(SingleDocumentModel doc) {
        return documents.indexOf(doc);
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documents.iterator();
    }
}
