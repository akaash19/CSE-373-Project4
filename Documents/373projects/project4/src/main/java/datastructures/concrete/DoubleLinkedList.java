package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }
    
    // Adds item at the end of the list
    @Override
    public void add(T item) {
        if (front == null && back == null) { // empty list case
            back = new Node<T>(item);
            front = back;
        } else {
            Node<T> current = back;
            back = new Node<T>(item);
            current.next = back;
            back.prev = current;
        }
        this.size++;
    }
    
    // Removes item that is at the end of the list
    @Override
    public T remove() {
        Node<T> current = back;
        if (front == null && back == null) { // empty list case
            throw new EmptyContainerException();
        } else if (this.size() == 1) { // single value case
            front = null;
            back = null;
        } else { // normal casse
            back = current.prev;
            current.prev.next = null;
            current.prev = null;
        }
        this.size--;
        return current.data;
    }
    
    // Returns the item at the index
    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size()) { // index exceeds size or negative case
            throw new IndexOutOfBoundsException();
        } else if (index > size() / 2) { // back half
            Node<T> current = backTraverse(index);
            return current.data;
        } else { // front half
            Node<T> current = frontTraverse(index);
            return current.data;
        }
    }
    
    // Overwrites the existing item at the index with the item
    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= this.size()) { // index exceeds size or negative case
            throw new IndexOutOfBoundsException();
        } else if ((index == 0 && this.size() == 1) || index == this.size() - 1) { // single or back case
            remove();
            add(item);
        } else if (index == 0) { // front case
            Node<T> current = front;
            front = new Node<T>(item);
            front.next = current.next;
            current.next.prev = front;
            current.next = null;
        } else {
            Node<T> currentF = null;
            Node<T> currentB = null;
            if (index > size() / 2) { // back half
                currentB = backTraverse(index + 1);
                currentF = currentB.prev.prev;
            } else { // front half
                currentF = frontTraverse(index - 1);
                currentB = currentF.next.next;
            }
            // removes existing item and replaces with new item
            currentB.prev.next = null;
            currentF.next.prev = null;
            currentB.prev = new Node<T>(item);
            currentF.next = currentB.prev;
            currentF.next.next = currentB;
            currentB.prev.prev = currentF;
        }
    }
    
    // Adds the item at the specified index
    @Override
    public void insert(int index, T item) {
        // index exceeds one more than the size or is negative case
        if (index < 0 || index >= this.size() + 1) {
            throw new IndexOutOfBoundsException();
        } else if (index == this.size()) { // back case
            add(item);
        } else if (index == 0) { // front case
            Node<T> current = front;
            front = new Node<T>(item);
            front.next = current;
            current.prev = front;
            this.size++;
        } else {
            Node<T> currentB = null;
            if (index > size() / 2) { // back half
                currentB = backTraverse(index);
            } else { // front half
                currentB = frontTraverse(index);
            }
            Node<T> currentF = currentB.prev;
            currentF.next = new Node<T>(item);
            currentB.prev = currentF.next;
            currentF.next.next = currentB;
            currentB.prev.prev = currentF;
            this.size++;
        }
    }
    
    // Returns the item at the index and deletes it from the list
    @Override
    public T delete(int index) {
        if (index < 0 || index >= this.size()) { // index exceeds size or negative case
            throw new IndexOutOfBoundsException();
        } else if ((index == 0 && this.size() == 1) || index == this.size() - 1) { // single or back case
            return remove();
        } else if (index == 0) { // front case
            Node<T> current = front;
            front = front.next;
            front.prev = null;
            current.next = null;
            this.size--;
            return current.data;
        } else {
            Node<T> current = null;
            if (index > size() / 2) { // back half
                current = backTraverse(index);
            } else { // front half
                current = frontTraverse(index);
            }
            current.prev.next = current.next;
            current.next.prev = current.prev;
            this.size--;
            return current.data;
        }
    }
    
    // Returns the index of the item in the list
    @Override
    public int indexOf(T item) {
        Iterator<T> iter = iterator();
        for (int i = 0; i < this.size(); i++) {
            T val = iter.next();
            if (item == null || val == null) { // null item case
                if (item == val) {
                    return i;
                }
            } else { // if a non-null item
                if (item.equals(val)) {
                    return i;
                }
            } 
        }
        return -1; // if the item is not found
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean contains(T other) {
        return indexOf(other) != -1;
    }
    
    // Traverses (searches) starting from front to given index
    private Node<T> frontTraverse(int index) {
        Node<T> current = front;
        for (int i = 0; i < index; i++) {
            current = current.next; 
        }
        return current;
    }
    
    // Traverses (searches) starting from back to given index
    private Node<T> backTraverse(int index) {
        Node<T> current = back;
        for (int i = size() - 1; i > index; i--) {
            current = current.prev; 
        }
        return current;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                T result = current.data;
                current = current.next;
                return result;
            }
        }
    }
}
