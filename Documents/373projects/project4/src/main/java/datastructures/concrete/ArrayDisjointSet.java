package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;


/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    private IDictionary<T, Integer> nodes;
    private int numNodes;
    private static final int DEFAULT_CAPACITY = 11;

    public ArrayDisjointSet() {
        this.nodes = new ChainedHashDictionary<>();
        this.pointers = new int[DEFAULT_CAPACITY];
        this.numNodes = 0;
    }

    @Override
    public void makeSet(T item) {
        if (setContains(item)) {
            throw new IllegalArgumentException();
        } else {
            int rank = 0;
            int nodeIndex = this.numNodes;
            this.numNodes++;
            if (nodeIndex == this.pointers.length) {
                this.pointers = enlarge();
            }
            this.nodes.put(item, nodeIndex);
            this.pointers[nodeIndex] = -1 * rank - 1;
        }
    }

    private boolean setContains(T item) {
        return (this.nodes.containsKey(item));
    }

    private int[] enlarge() {
        int[] enlarged = new int[2 * this.pointers.length];
        for (KVPair<T, Integer> id : this.nodes) {
            enlarged[id.getValue()] = this.pointers[id.getValue()];
        }
        return enlarged;
    }

    @Override
    public int findSet(T item) {
        if (!setContains(item)) {
            throw new IllegalArgumentException();
        } else {
            int index = this.nodes.get(item);
            return findSet(index);
        }
    }

    private int findSet(int index) {
        int set = this.pointers[index];
        if (set < 0) {
            return index;
        } else {
            return findSet(set);
        }
    }

    @Override
    public void union(T item1, T item2) {
        if (!setContains(item1)) {
            throw new IllegalArgumentException("item1 is not contained inside this disjoint set");
        } else if (!setContains(item2)) {
            throw new IllegalArgumentException("item2 is not contained inside this disjoint set");
        }
        int set1 = findSet(item1);
        int set2 = findSet(item2);
        if (set1 == set2) {
            throw new IllegalArgumentException("item1 and item2 are already a part of the same set");
        } else {
            int rank1 = -1 * this.pointers[set1] - 1;
            int rank2 = -1 * this.pointers[set2] - 1;

            if (rank1 < rank2) {
                this.pointers[set1] = set2;
            } else if (rank1 > rank2) {
                this.pointers[set2] = set1;
            } else {
                this.pointers[set1] = set2;
                this.pointers[set2] = -1 * (rank2 + 1) - 1;
            }
        }
    }
}
