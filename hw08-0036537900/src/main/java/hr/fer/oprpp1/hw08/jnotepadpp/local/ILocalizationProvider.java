package hr.fer.oprpp1.hw08.jnotepadpp.local;


/**
 * Interface ILocalizationProvider represents a localization provider.
 * It is used to get the current language and to get the string for the given key.
 */
public interface ILocalizationProvider {

    /**
     * Adds a localization listener.
     * @param listener localization listener
     */
    void addLocalizationListener(ILocalizationListener listener);


    /**
     * Removes a localization listener.
     * @param listener localization listener
     */
    void removeLocalizationListener(ILocalizationListener listener);

    /**
     * Returns the string for the given key in selected language.
     * @param key key
     * @return string for the given key
     */
    String getString(String key);

    /**
     * Returns the current language.
     * @return current language
     */
    String getCurrentLanguage();

}
