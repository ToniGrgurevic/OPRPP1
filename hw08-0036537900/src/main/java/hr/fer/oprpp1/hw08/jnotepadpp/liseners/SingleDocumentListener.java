package hr.fer.oprpp1.hw08.jnotepadpp.liseners;

import hr.fer.oprpp1.hw08.jnotepadpp.documentModels.SingleDocumentModel;

public interface SingleDocumentListener {
    void documentModifyStatusUpdated(SingleDocumentModel model);
    void documentFilePathUpdated(SingleDocumentModel model);
}
