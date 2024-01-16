package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Razred koji predstavlja model liste prim brojeva.
 */
public class PrimListModel implements ListModel<Integer> {


    /**
     * Lista prim brojeva.
     */
    private List<Integer> list = new ArrayList<>();

    private List<ListDataListener> liseners = new ArrayList<>();


    /**
     * Dodaje promatrača.
     * @param l promatrač.
     */
    @Override
    public void addListDataListener(ListDataListener l) {
        liseners.add(l);
    }


    /**
     * Uklanja promatrača.
     * @param l promatrač.
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
        liseners.remove(l);
    }



    public PrimListModel() {
        list.add(1);
    }

    /**
     * Vraća broj elemenata u listi.
     * @return broj elemenata u listi.
     */
    public int getSize() {
        return list.size();
    }


    /**
     * Vraća element liste na zadanom indeksu.
     *
     * @param index indeks elementa.
     * @return element liste na zadanom indeksu.
     */
    @Override
    public Integer getElementAt(int index) {
        return list.get(index);
    }


    /**
     * Dodaje sljedeći prim broj u listu.
     */
    public void next() {
        int last = list.get(list.size()-1);
        int next = last+1;
        while(true) {
            if(isPrime(next)) {
                list.add(next);

                ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, list.size(), list.size());
                for(ListDataListener l : liseners) {
                    l.intervalAdded(event);
                }

                break;
            }
            next++;
        }
    }

    /**
     * Provjerava je li broj prim broj.
     * @param next broj koji se provjerava.
     * @return true ako je prim broj, false inače.
     */
    private boolean isPrime(int next) {
        for(int i = 2; i <= next/2; i++) {
            if(next % i == 0) {
                return false;
            }
        }
        return true;
    }
}
