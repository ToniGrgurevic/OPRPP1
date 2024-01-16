package hr.fer.oprpp1.custom.collections;


import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 *  linked list-backed collection of objects.
 *  Duplicate elements are allowed (each of those element will be held in different list node).
 *  Storage of null references is not allowed.
 *  Size represents the number of currently stored objects in this collections.
 *  First is reference to the first node of the linked list.
 *  Last is reference to the last node of the linked list.
 */
public class LinkedListIndexedCollection implements List{



    private int size;
    private ListNode first;
    private ListNode last;

    private int modificationCount;



/** Constructs an empty list
 * */
    public LinkedListIndexedCollection() {
        this.size = 0;
        this.first = null;
        this.last = null;
        this.modificationCount = 0;
    }

    /** Constructs an  list and copies the elements from the given collection
     * @param other collection from witch elements are copied
     * if other is null throws NullPointerException
     * */
    public LinkedListIndexedCollection(Collection other) {
        this();
        if (other == null) throw new NullPointerException("Given collection can't be null");
        this.addAll(other);
    }



    /**Removes element at specified index from collection. Element that was previously at location index+1
     *after this operation is on location index.Legal indexes are in range [0,size-1].
     * @param index index of the element that is to be removed
     *@throws IndexOutOfBoundsException if index is invalid
     */
    public void remove(int index) {
        if (index < 0 || index > size - 1) throw new IndexOutOfBoundsException("Given index is invalid");
        if (index == 0) {
            first = first.next;
            first.previous = null;

        } else if (index == size - 1) {
            last = last.previous;
            last.next = null;
        } else {
            int middle = size / 2;
            ListNode head;
            if (index <= middle) {
                head = first;
                for (int i = 0; i < index; i++) {
                    head = head.next;
                }
            } else {
                head = last;
                for (int i = size - 1; i > index; i--) {
                    head = head.previous;
                }
            }
            head.previous.next = head.next;
            head.next.previous = head.previous;
        }
        size--;
        modificationCount++;
    }

    /**Searches the collection and returns the index of the first occurrence of the given value or -1
     * if the value is not fund.Null is valid argument.Complexity is O(n) where n is the size of the collection
     * @param value value to be searched
     * @return index of the first occurrence of the given value or -1 if the value is not fund
     */
    public int indexOf(Object value) {
        if(value == null) return -1;
        var head = first;
        int index = 0;
        while (head != null) {
            if (head.value.equals(value)) return index;
            head = head.next;
            index++;
        }
        return -1;
    }


    /**
     * Inserts (does not overwrite) the given value at the given position in linked-list. Elements starting from
     * this position are shifted one position. The legal positions are 0 to size.
     * complexity is O(n/2) where n is the size of the collection
     *
     * @param value    value to be inserted
     * @param position index at witch the given value is inserted
     * @throws NullPointerException      if value is null throws NullPointerException
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public void insert( Object value,int position) {
        if (value == null) throw new NullPointerException("Given value is null");
        if (position < 0 || position > size) throw new IndexOutOfBoundsException("Given index is invalid");

        if (position == size) {
            add(value);

        } else if (position == 0) {
            var newNode = new ListNode(value, null, first);
            first.previous = newNode;
            first = newNode;
            size++;
            modificationCount++;

        } else {
            int middle = size / 2;

            ListNode head;
            if (position <= middle) {
                head = first;
                for (int i = 0; i < position; i++) {
                    head = head.next;
                }
                var newNode = new ListNode(value, head.previous, head);
                head.previous.next = newNode;
                head.previous = newNode;
            }else{
                head = last;
                for (int i = size - 1; i > position; i--) {
                    head = head.previous;
                }
                var newNode = new ListNode(value, head.previous, head);
                head.previous.next = newNode;
                head.previous = newNode;
            }
            size++;
            modificationCount++;
        }
    }

        /**
         * Returns the object that is stored in linked list at position index
         * complexity is O(n/2 ) where n is the size of the collection
         * @param index index of the object to be returned
         * @return object at the given index
         */
        public Object get ( int index){
            if (index < 0 || index > size - 1) throw new IndexOutOfBoundsException("Given index is invalid");

            var head = first;
            int middle = this.size / 2;
            if (index > middle) {
                head = last;
                for (int i = this.size - 1; i > index; i--) {
                    head = head.previous;
                }
            } else {
                for (int i = 0; i < index; i++) {
                    head = head.next;
                }
            }
            return head.value;
        }

    /**
     * @return the number of currently stored objects in this collections.
     */
        @Override
        public int size () {
            return this.size;
        }

        @Override
        public boolean contains (Object value){
            return indexOf(value) != -1;
        }

    /**
     * Removes the first occurrence of the given value from the collection if it exists and returns true.
     * @param value of object to be removed
     * @return true if the object is removed,otherwise false
     */
    @Override
        public boolean remove (Object value){
            if (value == null) return false;
            var head = first;

            while (head != null) {
                if (head.value.equals(value)) {
                    if (head == first) {
                        first = first.next;
                        first.previous = null;
                    } else if (head == last) {
                        last = last.previous;
                        last.next = null;
                    } else {
                        head.previous.next = head.next;
                        head.next.previous = head.previous;
                    }
                    size--;
                    modificationCount++;
                    return true;
                }
                head = head.next;
            }
            return false;

        }

        /**
         * Adds the given object into this collection at the end
         * @param value object to be added
         * @throws NullPointerException if value is null
         */
        @Override
        public void add (Object value){
            if (value == null) throw new NullPointerException("Given value is null witch is not allowed");
            var newNode = new ListNode(value, last, null);

            if (first == null) {
                first = newNode;
                last = newNode;
            }else{
                last.next = newNode;
                last = newNode;
            }

            size++;
            modificationCount++;
        }


        /** Allocates new array with size equals to the size of this collections
         * this method never returns null
         * @return new array with elements of this collection
         */
        @Override
        public Object[] toArray () {
            if (this.size == 0) throw new UnsupportedOperationException("Size of collection is 0");
            var array = new Object[this.size];
            var head = first;
            int i = 0;
            while (head != null) {
                array[i++] = head.value;
                head = head.next;
            }
            return array;

        }




        /**
         *Removes all elements from the collection. Collection “forgets” about current linked list.
         */
        @Override
        public void clear () {
            this.size = 0;
            this.first = null;
            this.last = null;
            modificationCount++;
        }

    /**
     * Class witch  objects are one node of the list LinkedListIndexedCollection.
     * It stores refrences to previous and next node and the value of the node.
     */
    private static class ListNode {


        ListNode previous;
        ListNode next;
        Object value;

        /** Constructs a node
         *
         * @param previous reference to previous node
         * @param next reference to next node
         * @param value value of the node
         */
        public ListNode(Object value,ListNode previous, ListNode next) {
            this.previous = previous;
            this.next = next;
            this.value = value;
        }
    }

    /**
     * Crates new LinkedListElementsGetter for this collection and returns it as
     * ElementsGetter
     * @return new ElementsGetter for this collection
     */
    @Override
    public ElementsGetter createElementsGetter() {
        return new LinkedListElementsGetter(this, this.modificationCount);
    }

    /**
     * Class witch implements ElementsGetter and is used to iterate over the elements of the collection.
     * It stores reference to the current node and the collection.There are no restrictions on the number of
     *      * ElementsGetters that can be created for one collection.
     */
    private static class LinkedListElementsGetter implements ElementsGetter{
        private ListNode current;
        private LinkedListIndexedCollection collection;

        private int savedModificationCount;

        public LinkedListElementsGetter(LinkedListIndexedCollection collection, int ModificationCount) {
            this.collection = collection;
            this.current = collection.first;
            this.savedModificationCount = ModificationCount;
        }

        /**
         * checks if there is next element in the collection
         * @return true if there is next element, false otherwise
         * @throws ConcurrentModificationException if the collection has been modified
         *
         */
        @Override
        public boolean hasNextElement() {
            if(savedModificationCount != collection.modificationCount) throw new ConcurrentModificationException("Collection has been modified");
            return current != null;
        }

        /**
         * Returns the next element in the collection
         * @return next element in the collection
         * @throws ConcurrentModificationException if the collection has been modified
         * @throws NoSuchElementException if there are no more elements
         */
        @Override
        public Object getNextElement() {
            if(savedModificationCount != collection.modificationCount) throw new ConcurrentModificationException("Collection has been modified");
            if(!hasNextElement()) throw new NoSuchElementException("There are no more elements");
            var value = current.value;
            current = current.next;
            return value;
        }
    }
}
