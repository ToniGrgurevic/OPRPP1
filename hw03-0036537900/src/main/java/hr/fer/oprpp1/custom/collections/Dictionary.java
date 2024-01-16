package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * Class acts as adapter around ArrayIndexedCollection and gives user implementation of map(dictionary)
 * @param <K> represents variable type of Key in Entry.
 * @param <V> represents variable type of value in Entry.
 */
public class Dictionary<K,V> {

    /* Storage Entrys(keys and its values). */
    private ArrayIndexedCollection<Entry<K,V>> mapa;

    /* creates empty dictionary */
    public Dictionary() {
        this.mapa = new ArrayIndexedCollection<>();
    }

    /**Checks if dictionary is empty
    * @return true is empty,otherwise false
    *  */
    public boolean isEmpty(){
        return mapa.isEmpty();
    }


    /**
     * Method  number of Enrys in dictionary
     * @return size of dictionary
     */
    public int size(){
        return mapa.size();
    }

    /**
     * Clears the dictionary
     */
    public void clear(){
         mapa.clear();
    }

    /**
     * Creates Entry form key and value given and puts it in storage
     * If its alredy saved Entry with given key,it will be replaced.
     * @param key key of Entry to be added
     * @param value value of Entry to be added
     * @return value that was removed form dictionary or null if no Entry was removed
     * @throws IllegalArgumentException if given key is null
     */
    public V put(K key, V value){
        Entry<K,V> zapis = new Entry<>(key, value);
        V oldValue = null;
        int index;
        if(  ( index = mapa.indexOf(zapis) )  >= 0){
            var old = mapa.get(index);
            oldValue = old.value;
            mapa.remove(zapis);
        }
        mapa.add(zapis);
        return oldValue;
    }

    /**
     * Returns value of Entry in dictionary with given key,if exist
     * @param key key witch value method will look for
     * @return value saved with key,or null if key is not in dictionary
     */
    V get(Object key){
        if(key == null) throw new IllegalArgumentException("Key of Entry can't be null");

        Entry<K,V> tmp = new Entry<>((K) key,null);
        int index = mapa.indexOf(tmp);
        return index==-1 ? null : mapa.get(index).value;
    }

    /**
     * If Entry with given key exist in dict,removes it and returns value of Entry
     * @param key of Entry that will be removed
     * @return value of removed Entry or null
     */
    V remove(K key){
        int index = mapa.indexOf(new Entry<>(key,null));
        if(index >= 0){
            V value = mapa.get(index).value;
            mapa.remove(index);
            return value;
        }else return null;

    }






    /**
     * Represents on unique Entry in dictionary
     * @param <K> key of Entry,can't be null
     * @param <V> value of Entry,can be null
     */
    private static class Entry<K,V>{
        private final K key;
        private final V value;

        public Entry(K key, V value) {
            if(key == null){
                throw new IllegalArgumentException("Key can't be null");
            }

            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Entry)) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.key);
        }


    }
}
