package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

import java.util.NoSuchElementException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.
    private static final int DEFAULT_CAPACITY = 21;
    private int heapSize;

    public ArrayHeap() {
        this.heap = makeArrayOfT(DEFAULT_CAPACITY);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        if (size() == 0) {
            throw new EmptyContainerException();
        } else {
            T last = this.heap[this.heapSize - 1];
            T min = this.heap[0];
            this.heap[0] = last;
            this.heapSize--;
            percolateDown(0);
            return min;
        }
    }

    public void remove(T item) {
        int index = find(item);
        this.heap[index] = this.heap[this.heapSize - 1];
        this.heapSize--;

        if (index == 0 || less(this.heap[findParent(index)], this.heap[index])) {
            percolateDown(index);
        } else {
            percolateUp(index);
        }
    }

    private int find(T item) {
        for (int i = 0; i < this.heapSize; i++) {
            if (this.heap[i].equals(item)) {
                return i;
            }
        }
        throw new NoSuchElementException("given item does not exist");
    }

    @Override
    public T peekMin() {
        if (size() == 0) {
            throw new EmptyContainerException();
        } else {
            return this.heap[0];
        }
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        } else {
            int endIndex = this.heapSize;
            if (endIndex == this.heap.length) {
                doubleCapacity();
            }
            this.heap[endIndex] = item;
            percolateUp(endIndex);
            this.heapSize++;
        }
    }

    private void percolateUp(int light) {
        int heavy = findParent(light);
        while (light > 0 && less(this.heap[light], this.heap[heavy])) {
            swap(heavy, light);
            light = heavy;
            heavy = findParent(light);
        }
    }

    private void percolateDown(int heavy) {
        int light = findChild(heavy);
        while (light > 0 && less(this.heap[light], this.heap[heavy])) {
            swap(heavy, light);
            heavy = light;
            light = findChild(heavy);
        }
    }

    private void swap(int parentIndex, int childIndex) {
        T parent = this.heap[parentIndex];
        T child = this.heap[childIndex];
        this.heap[parentIndex] = child;
        this.heap[childIndex] = parent;
    }

    @Override
    public int size() {
        return this.heapSize;
    }

    private void doubleCapacity() {
        T[] oldArray = this.heap;
        this.heap = makeArrayOfT(2 * this.heapSize);
        for (int i = 0; i < this.heapSize; i++) {
            this.heap[i] = oldArray[i];
        }
    }

    private int findParent(int childIndex) {
        return (childIndex - 1) / NUM_CHILDREN;
    }

    private int findChild(int parentIndex) {
        // 1o, 2o, 3o, 4o -> far-left, centre-left, centre-right, far-right
        int firstChild = NUM_CHILDREN * parentIndex + 1;
        if (firstChild >= this.heapSize) {
            return 0;
        } else {
            int lastChild = firstChild + NUM_CHILDREN - 1;
            if (lastChild >= this.heapSize) {
                lastChild = this.heapSize - 1;
            }
            int child = lastChild;
            for (int i = lastChild - 1; i >= firstChild; i--) {
                if (less(this.heap[i], this.heap[child])) {
                    child = i;
                }
            }
            return child;
        }
    }

    // Comparison helper method
    private boolean less(T a, T b) {
        return a.compareTo(b) < 0;
    }
}
