package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider  extends AbstractLocalizationProvider{

    private static LocalizationProvider instance = new LocalizationProvider();

    private String language;

    private ResourceBundle bundle;

    private LocalizationProvider() {
        language = "en";
        Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.prijevodi", locale);

    }

    /**
     * Returns the instance of the localization provider.Only one instance of the localization provider can exist.
     * @return instance of the localization provider
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    @Override
    public String getCurrentLanguage() {
        return language;
    }

    /**
     * Sets the current language to the given language.
     * @param language language
     */
    public void setLanguage(String language) {
        this.language = language;
        Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.prijevodi", locale);
        fire();
    }
}
