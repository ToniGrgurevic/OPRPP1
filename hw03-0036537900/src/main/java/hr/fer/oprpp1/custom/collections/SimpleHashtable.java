package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import static java.lang.Math.abs;

public class SimpleHashtable<K,V> implements Iterable<SimpleHashtable.TableEntry<K,V>>{
    private int size;
    private TableEntry<K,V>[] map;

    private int modificationCount;

    public SimpleHashtable() {
        map =(TableEntry<K, V>[]) new TableEntry[16];
        this.size = 0;
        modificationCount = 0;
    }

    public SimpleHashtable(int initialCapacity) {
        if(initialCapacity < 1 ) throw new IllegalArgumentException("initialCapacity can't be smaller than 1");
        int cap = 1;

        while(initialCapacity > cap){
            cap*=2;
        }
        map =(TableEntry<K, V>[]) new TableEntry[cap];
        this.size = 0;
        modificationCount= 0;
    }






    /**
     * If Entry with same key exists,this method updates value of that entry.Otherwise
     * it adds new Entry in map at the end of that slot list
     * @param key key of entry witch value will be updated or added
     * @param value value of new entry
     * @return old value or null
     * @throws NullPointerException if key is null
     */
    public V put(K key, V value){
        if (key == null) throw new  NullPointerException("Key can't be null");

        if(this.size /( this.map.length * 1.0 ) >= 0.75){
            reorganize();
        }

        var head = map[abs(Objects.hash(key) % map.length)];
        var previous  =head;


        while(head != null && !head.getKey().equals(key)){

            if(head.getKey() != previous.getKey()){ //u prvoj iteraciji previous se nece promjeniti
                previous = previous.next;
            }
            head = head.next;
        }
        if(head == null){
            head = new TableEntry<>(key,value);
            if(previous!=null )  //slot nije bio prazan
               previous.next = head;
            size++;
            modificationCount++;
            if(map[Objects.hash(key) % map.length] == null)
                map[Objects.hash(key) % map.length] = head;
            return null;
        }else{
            V oldValue = head.getValue();
            head.setValue(value);
            return oldValue;
        }

    }

    /**
     * Method doubles size of hashtable and reorganizes Entryes.
     */
    private void reorganize() {
        TableEntry<K,V>[] oldArray=(TableEntry<K,V>[])  toArray();
        this.map =(TableEntry<K, V>[]) new TableEntry[map.length * 2];
        size = 0;

        for(var entry : oldArray){
            if(entry!=null) {
                put(entry.getKey(), entry.getValue());

            }
        }
        modificationCount++;
    }

    /**
     * Method searches for Entry with given key and returns its value,or null if its not int in map
     * @param key key of Entry that method is looking for.Can be null
     * @return value of stored Entry with given value or null
     */
    public V get(Object key) {
        if(key == null) return null;
        var head = map[abs(Objects.hash(key) % map.length)];

        while (head != null && !head.getKey().equals(key)) {
            head = head.next;
        }
        if(head == null) return null;
        return head.getValue();
    }

    /**
     * Method returns number of Entrys stored in map
     * @return number of Entrys stored in map
     */
    public int size(){
        return this.size;
    }

    /**
     * Method searches stored Entrys for one with given key
     * @param key key of Entry method is searching for.Can be null
     * @return true if Entry with given key is in map,otherwise false
     */
    public boolean containsKey(Object key){
        if(key == null) return false;
        var head = map[abs(Objects.hash(key) % map.length)];

        while (head != null){
            if(head.getKey() == key)
               return true;
            head  =head.next;
        }
        return false;
    }

    /**
     * Method searches stored Entrys for one with given value
     * @param value value of Entry method is searching for.Can be null
     * @return true if Entry with given value exist in map,otherwise false
     */
    public boolean containsValue(Object value){

        for(var head : map){

            while (head != null){
                if(head.getValue() == value){
                    return true;
                }
                head = head.next;
            }

        }
        return false;
    }

    /**
     * Removes Entry with given key value
     * @param key key of Entry to be removed.Can be null
     * @return vale of removed Entry or null
     */
    public V remove(Object key){
        if(key == null) return null;

        var head = map[abs(Objects.hash(key) % map.length)];
        var previous  =head;


        while(head != null){

            if( head.getKey().equals(key)){
                V oldValue = head.getValue();

                if(map[Objects.hash(key) % map.length] == head){
                    map[Objects.hash(key) % map.length] = head.next;
                }else previous.next = head.next;
                size--;
                modificationCount++;
                return oldValue;
            }

            if(head.getKey() != previous.getKey()){ //u prvoj iteraciji previous se nece promjeniti
                previous = previous.next;
            }
            head = head.next;
        }
        return null;
    }


    /**
     * Checks if hashtable is empty
     * @return true if hashtable is empty
     */
    public boolean isEmpty(){
        return size==0;
    }

    /**
     * Returns String representation of hashtable
     * @return String representation of hashtable
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(var head : map){

            while(head != null){
                sb.append(", ").append(head.getKey()).append("=").append(head.getValue());
                head = head.next;
            }
        }
        sb.append("]");
        String string = sb.toString();
        string = string.replaceFirst(", ", "");
        return string;
    }

    /**
     * Returns array of all elements of hashtable
     * @return array of Entris in hashtable
     */
    public TableEntry<K,V>[] toArray(){

        TableEntry<K,V>[] array =(TableEntry<K, V>[]) new TableEntry[this.size];
        int i = 0;
        for(var head : map){

            while(head != null){
                array[i++] = head;
                head = head.next;
            }
        }
        return array;
    }

    /**
     * Method empty's the hashtable
     */
    public void clear(){
        this.map =(TableEntry<K, V>[]) new TableEntry[map.length];
        this.size = 0;
    }

    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl(this.modificationCount);
    }


    public class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {

        private int modCountSAved;
        private int slot;
        private int index =0;

        TableEntry<K,V> tableEntry;
        public IteratorImpl(int modCountSAved) {
            this.modCountSAved = modCountSAved;
            slot = 0;
            index = 0;
            tableEntry=null;
        }

        @Override
        public boolean hasNext() {
            if(modCountSAved != modificationCount ) throw new ConcurrentModificationException();

            return index < size;
        }

        @Override
        public TableEntry<K, V> next() {
            if(modCountSAved != modificationCount ) throw new ConcurrentModificationException();
            if(index >= size ) throw new NoSuchElementException();

            if(tableEntry==null){
            //find by index
                int tmpIndex= 0;
                for(var head : map){
                    while(head != null){

                        if(tmpIndex ==this.index ){
                            tableEntry = head;
                            index++;
                            return tableEntry;
                        }
                        tmpIndex++;
                        head = head.next;
                    }
                }
            }

            if(tableEntry.next == null){
                slot++;
                while( map[slot] == null) slot++;

                tableEntry = map[slot];
            }else{
                tableEntry = tableEntry.next;
            }

            index++;
            return tableEntry;
        }

        @Override
        public void remove() {
            if(tableEntry == null) throw new IllegalStateException();
            SimpleHashtable.this.remove(tableEntry.getKey());
            tableEntry = null;
            modCountSAved++;
            index--;
        }
    }


    /**
     * Represents Entry in SimpleHashtable map.Key of Entry can't be null and value can
     * @param <K> variable type of key.
     * @param <V> variable type of value
     */
    public static class TableEntry<K, V> {
        /* key of entry.Can't be null */
        private K key;
        /* value on entry.Can be null*/
        private V value;
        /* next element in map slot with same hash */
        TableEntry<K,V> next;

        public TableEntry(K key, V value) {
            this.key = key;
            this.value =value;
        }

        /**
         * Setter for value
         * @param value new value of Entry
         */
        public void setValue(V value) {
            this.value = value;
        }

        /**
         * Getter for key of Entry
         * @return key of entry
         */
        public K getKey() {
            return key;
        }
        /**
         * Getter for value of Entry
         * @return value of entry
         */
        public V getValue() {
            return value;
        }




       /* @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TableEntry)) return false;
            TableEntry<?, ?> that = (TableEntry<?, ?>) o;
            return Objects.equals(key, that.key);
        }
        */


        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }
}
