package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/*Class that stores elements in an array.Extends class Collection.
*Duplicate elements are allowed.Storage of null references is not
 *allowed.
 */
public class ArrayIndexedCollection implements List {
    /** number of objects in elements array*/
    private int size;
    /** array of objects*/
    private Object[] elements;

    private int modificationCount = 0;

/** Creats new instance of this class wich array is capable of storing 16 elements.Curently empty */
    public ArrayIndexedCollection() {
        elements = new Object[16];
        this.size=0;
    }

    /** Creats new instance of this class wich array is capable of storing initialCapacity elements.Curently empty
     * @param initialCapacity number of elements that array is capable of holding(for now) */
    public ArrayIndexedCollection(int initialCapacity) {
        if (initialCapacity < 1) throw new IllegalArgumentException("Initial capacity must be greater than 1");
        elements = new Object[initialCapacity];
        this.size=0;
    }

    /**
     * Creates new instance of this class and copies elements from other collection into this newly constructed collection.
     * @param other collection whose elements are copied into this newly constructed collection
     * @param initialCapacity number of elements that array is capable of holding, as long it is bigger than the size of the other collection
     */
    public ArrayIndexedCollection(Collection other, int initialCapacity) {
        this(Math.max(other.size(), initialCapacity));
        if (other == null) throw new NullPointerException("Given collection can't be null");
        this.addAll(other);
    }

    /**
     * Creates new instance of this class and copies elements from other collection into this newly constructed collection.
     * @param other collection whose elements are copied into this newly constructed collection
     */
    public ArrayIndexedCollection(Collection other) {
        this(Math.max(other.size(), 16));
        if (other == null) throw new NullPointerException("Given collection can't be null");
        this.addAll(other);
    }


    /**
     * Adds the given object into this collection.
     * Average complexity is O(n) where n is the size of the collection
     * @param value object to be added
     * @throws NullPointerException if value is null
     */
    @Override
    public void add(Object value) {

        if (value == null) throw new NullPointerException("Given value is null witch is not allowed");
        if (size == elements.length) {
            Object[] newElements = new Object[size * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
        elements[size++] = value;
        modificationCount++;

    }

    /**
     * Returns the object that is stored  at position index.Average complexity is O(1)
     * @param index position of the object to be returned
     * @return object at position index
     * @throws IndexOutOfBoundsException if index is less than 0 or greater than size-1
     */
    public Object get(int index) {
        if (index < 0 || index > size - 1) throw new IndexOutOfBoundsException("Given index is out of bounds");
        return elements[index];
    }

    /**
     * Removes all elements from this collection.
     */
    @Override
    public void clear() {
        for(var object : elements){
            object = null;
        }
        size = 0;
        modificationCount++;
    }

    /**
     * Inserts (does not overwrite) the given value at the given position in array.Values stored at
     * position index and greater are shifted one position to the right.
     * Average complexity is O(n) where n is the size of the collection
     * @param value object to be inserted
     * @param position position at which the object is to be inserted
     * @throws NullPointerException if value is null
     * @throws IndexOutOfBoundsException if given index is not in range [0,size] */
    public void insert(Object value, int position){
        if (value == null) throw new NullPointerException("Given value is null witch is not allowed");
        if (position < 0 || position > size ) throw new IndexOutOfBoundsException("Given index is out of bounds");

        if(position == size){
            add(value);
            return;
        }

        if (size == elements.length) {
            Object[] newElements = new Object[size * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }

        for(int i = size; i > position; i--){
            elements[i] = elements[i-1];
        }
        elements[position] = value;
        size++;
        modificationCount++;
    }


    /**
     * Rreturns the index of the first occurrence of the given value or -1 if the value is not found.
     * @param value object whose index is to be returned
     * @return index of first object equal to value or -1
     */
    public int indexOf(Object value){
        if (value == null) return -1;
        for(int i = 0; i < size; i++){
            if(elements[i].equals(value)) return i;
        }
        return -1;
    }

    /**Removes element at specified index from collection. Element that was previously at location index+1
     *after this operation is on location index(same for other objects at positions higer than index).
     * Legal indexes are in range [0,size-1].
     * @param index index of the element that is to be removed
     *@throws IndexOutOfBoundsException if index is invalid */
    public void  remove(int  index) {
        if (index < 0 || index > size - 1) throw new IndexOutOfBoundsException("Given index is out of bounds");
        System.arraycopy(this.elements, index + 1, this.elements, index, size - 1 - index);
        size--;
        modificationCount++;
    }

    /**
     * Removes the first occurrence of the given value from the collection if it exists and returns true.
     * @param value of object to be removed
     * @return true if the object is removed,otherwise false
     */
    @Override
    public boolean remove(Object value) {
        if (value == null) return false;
        int index = indexOf(value);
        if(index == -1) return false;
        remove(index);

        return true;
    }

    /**
     * returns the number of currently stored objects in this collection.
     * @return this.size
     */
    @Override
    public int size() {
        return size;
    }


    /**
     * Checks if the collection contains given value, as determined by equals method.
     * @param value of the object to be checked
     * @return true only if the collection contains given value, otherwise false */
    @Override
    public boolean contains(Object value) {
        int index = indexOf(value);

        if(index == -1) return false;
        else return true;
    }

    /** Allocates new array with size equals to the size of this collections
     * this method never returns null
     * @return new array with elements of this collection
     */
    @Override
    public Object[] toArray() {
        if(size == 0) throw new UnsupportedOperationException("Size of collection is 0");
        Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }




    /**
     * Crates new ElementsGetter for this collection and returns it.
     * @return new ElementsGetter for this collection
     */
    @Override
    public ElementsGetter createElementsGetter() {
        return new ArrayElementsGetter(this,this.modificationCount);
    }

    /**
     * Class that implements ElementsGetter for ArrayIndexedCollection and is used
     * to iterate through the collection.There are no restrictions on the number of
     * ElementsGetters that can be created for one collection.
     */
    private static class ArrayElementsGetter implements ElementsGetter{

        private int index;
        private ArrayIndexedCollection collection;

        private int savedModificationCount;

        public ArrayElementsGetter(ArrayIndexedCollection collection, int modificationCount) {
            this.collection = collection;
            this.index = 0;
            this.savedModificationCount = modificationCount;
        }

        /**
         * checks if there is next element in the collection
         * @return true if there is next element, false otherwise
         * @throws ConcurrentModificationException if the collection has been modified
         */
        @Override
        public boolean hasNextElement() {
            if(this.savedModificationCount != collection.modificationCount) throw new ConcurrentModificationException("Collection has been modified");
            return index < collection.size;
        }


        /**
         * Returns the next element in the collection
         * @return next element in the collection
         * @throws NoSuchElementException if there are no more elements
         * @throws ConcurrentModificationException if the collection has been modified
         */
        @Override
        public Object getNextElement() {
            if(this.savedModificationCount != collection.modificationCount) throw new ConcurrentModificationException("Collection has been modified");
            if(!hasNextElement()) throw new NoSuchElementException("There are no more elements");
            return collection.get(index++);
        }
    }


}



