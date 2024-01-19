package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.List;

/**
 * Interface ILocalizationListener represents a localization listener.
 * It is used inform observers on changes of current language.
 */
public interface ILocalizationListener {

        /**
        * Method localizationChanged is called when the current language is changed.
        */
        void localizationChanged();


}
