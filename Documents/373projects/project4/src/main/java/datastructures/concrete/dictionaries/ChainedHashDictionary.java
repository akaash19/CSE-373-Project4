package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;

    private int numKeys;
    private int hashTableSize;
    // Initial capacity = 11 (i.e. THABIT[3]); ith rehash/resize = indexThabit - 3.
    private int indexThabit; // Initial index = 3
    private static final int[] THABIT = {0, 2, 5, 11, 23, 47, 95, 191, 383, 767,
                                            1535, 3071, 6143, 12287, 24575, 49151,
                                            98303, 196607, 393215, 786431, 1572863,
                                            3145727, 6291455, 12582911, 25165823,
                                            50331647, 100663295, 201326591,
                                            402653183, 805306367, 1610612735};
    // See https://en.wikipedia.org/wiki/Thabit_number & https://oeis.org/A055010
    // more information on Thâbit ibn Kurrah Numbers that follow 3*2^(n - 1) - 1
    // for n>0. Any larger Thabit number exceeds Java's 2^32 integer limit.

    public ChainedHashDictionary() {
        this.indexThabit = 3;
        this.chains = makeArrayOfChains(THABIT[this.indexThabit]);
        this.hashTableSize = THABIT[this.indexThabit];
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        IDictionary<K, V> bucket = getBucket(key);
        if (bucket == null) {
            throw new NoSuchKeyException();
        } else {
            return bucket.get(key);
        }
    }

    @Override
    public void put(K key, V value) {
        if (updateLoadFactor() >= 1) {
           rehash();
        }
        if (!containsKey(key)) {
            this.numKeys++;
        }
        add(key, value);
    }

    private void add(K key, V value) {
        IDictionary<K, V> bucket = getBucket(key);
        if (bucket == null) {
            bucket = new ArrayDictionary<>();
        }
        bucket.put(key, value);
        this.chains[getIndex(key)] = bucket;
    }

    @Override
    public V remove(K key) {
        IDictionary<K, V> bucket = getBucket(key);
        if (bucket == null) {
            throw new NoSuchKeyException();
        } else {
            this.numKeys--;
            return bucket.remove(key);
        }
    }

    @Override
    public boolean containsKey(K key) {
        IDictionary<K, V> bucket = getBucket(key);
        return bucket != null && bucket.containsKey(key);
    }

    private IDictionary<K, V> getBucket(K key) {
        return this.chains[getIndex(key)];
    }

    private int getIndex(K key) {
        int index = 0;
        if (key != null) {
            index = hashFunction(key);
        }
        return index;
    }

    @Override
    public int size() {
        return this.numKeys;
    }

    private int hashFunction(K key) {
        return Math.abs(key.hashCode()) % this.hashTableSize;
    }

    private double updateLoadFactor() {
        return (double) this.numKeys / this.hashTableSize;
    }

    private void rehash() {
        if (this.indexThabit < THABIT.length - 1) {
            this.indexThabit++;
            this.hashTableSize = THABIT[this.indexThabit];
            Iterator<KVPair<K, V>> iter = new ChainedIterator<>(this.chains);
            this.chains = makeArrayOfChains(this.hashTableSize);
            while (iter.hasNext()) {
                KVPair<K, V> pair = iter.next();
                K key = pair.getKey();
                V value = pair.getValue();
                add(key, value);
            }
        }
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
<<<<<<< HEAD
     * 2. Think about what exactly your *invariants* are. Once you've
     *    decided, write them own in a comment somewhere to help you
     *    remember.
     *
     * 3. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
=======
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant*
     *    is something that must *always* be true once the constructor is
     *    done setting up the class AND must *always* be true both before and
     *    after you call any method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
>>>>>>> e9154deef026c8fbe3e89b0544561f18888df89f
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int tableIndex;
        private Iterator<KVPair<K, V>> iterBucket;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.tableIndex = 0;
            if (this.chains[0] == null) {
                this.tableIndex = hasNextIndex();
            }
            if (this.tableIndex == -1) {
                this.iterBucket = null;
            } else {
                this.iterBucket = this.chains[this.tableIndex].iterator();

            }
        }

        @Override
        public boolean hasNext() {
            // Returns true if non-null bucket has next or the table has next
            return this.iterBucket != null && this.iterBucket.hasNext() || hasNextIndex() != -1;
        }

        @Override
        public KVPair<K, V> next() {
            if (this.iterBucket == null || !hasNext()) {
                throw new NoSuchElementException();
            } else if (this.iterBucket.hasNext()) {
                return this.iterBucket.next();
            } else {
                this.tableIndex = hasNextIndex();
                IDictionary<K, V> bucket = this.chains[this.tableIndex];
                this.iterBucket = bucket.iterator();
                return this.iterBucket.next();
            }
        }

        private int hasNextIndex() {
            int tableLength = this.chains.length;
            int index = this.tableIndex + 1;
            int next = -1;
            while (index < tableLength && next == -1) {
                if (this.chains[index] != null) {
                    next = index;
                }
                index++;
            }
            return next;
        }
    }
}
