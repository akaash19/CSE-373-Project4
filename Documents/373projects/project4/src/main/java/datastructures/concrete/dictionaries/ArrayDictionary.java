package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

//import datastructures.concrete.DoubleLinkedList.Node;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;
    private int capacity;
    private static final int DEFAULT_CAPACITY = 7; // default capacity of dictionary

    public ArrayDictionary() {
        this.pairs = makeArrayOfPairs(DEFAULT_CAPACITY);
        this.capacity = DEFAULT_CAPACITY;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }
    
    // Returns the value for given key that is found in the dictionary
    @Override
    public V get(K key) {
        int index = find(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        } else { // if key exists
            Pair<K, V> entry = getPair(index);
            return entry.value;
        }
    }
    
    // Adds a key and value pair to the dictionary at the end of the dictionary
    // If the key already exists, its value is replaced with the new value
    @Override
    public void put(K key, V value) {
        int index = find(key);
        if (index != -1) { // if key exists
            Pair<K, V> entry = getPair(index);
            entry.value = value; // overwrites key's value
        } else { // adds key-value pair to end
            if (fullCapacity()) {
                doubleCapacity();
            }
            this.pairs[size()] = new Pair<>(key, value);
            this.size++;
        }
    }
    
    // Removes a given key, returning the corresponding value of that deleted value
    // and shifting the subsequent pairs left
    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        } else {
            int index = find(key);
            V value = get(key);
            if (index == size() - 1) { // end case
                this.pairs[index] = null; 
            } else { // shifts subsequent key-value pairs left
                for (int i = index; i < size() - 1; i++) {
                    this.pairs[i] = getPair(i + 1);
                }
            }
            this.size--;
            return value;
        }
    }
    
    // Returns true if a certain key can be found in the dictionary
    @Override
    public boolean containsKey(K key) {
        return find(key) != -1;
    }
    
    // Returns the size of the dictionary
    @Override
    public int size() {
        return this.size;
    }
    
    // Finds the index location of a key (and its value), returning -1 if it cannot be found
    private int find(K key) {
        for (int i = 0; i < size(); i++) {
            Pair<K, V> entry = getPair(i);
            if (key == null || entry.key == null) { // null key/value case
                if (key == entry.key) {
                    return i;
                }
            } else {
                if (key.equals(entry.key)) {
                    return i;
                }
            }
        }
        return -1;
    }

    // Checks if the dictionary is full, returning false if otherwise
    private boolean fullCapacity() {
        return size() == this.capacity;
    }
    
    // Doubles the capacity of the dictionary
    private void doubleCapacity() {
        this.capacity = 2 * this.capacity;
        Pair<K, V>[] doublePairs = makeArrayOfPairs(this.capacity);
        for (int i = 0; i < size(); i++) {
            doublePairs[i] = getPair(i);
        }
        this.pairs = doublePairs;
    }
    
    // Returns the key and value pair at the given index
    private Pair<K, V> getPair(int index) {
        return this.pairs[index];
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        Iterator<KVPair<K, V>> iter = new ArrayDictionaryIterator<>(this.pairs, this.size);
        return iter;
    }
    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int size;
        private int nextIndex;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            this.size = size;
            this.nextIndex = 0;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return this.nextIndex < this.size;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                Pair<K, V> pair = this.pairs[this.nextIndex];
                KVPair<K, V> kvPair = new KVPair<>(pair.key, pair.value);
                this.nextIndex += 1;
                return kvPair;
            }
        }
    }
}
