package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.List;

abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    List<ILocalizationListener> listeners = new java.util.ArrayList<>();


    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }



    /**
     * Notifies all listeners that the current language is changed.
     */
    protected void fire() {
        for (ILocalizationListener listener : listeners) {
            listener.localizationChanged();
        }
    }
}