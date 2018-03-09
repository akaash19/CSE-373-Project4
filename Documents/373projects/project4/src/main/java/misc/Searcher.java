package misc;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import java.util.Iterator;

public class Searcher {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "biggest".
     *
     * If the input list contains fewer then 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        // Implementation notes:
        //
        // - This static method is a _generic method_. A generic method is similar to
        //   the generic methods we covered in class, except that the generic parameter
        //   is used only within this method.
        //
        //   You can implement a generic method in basically the same way you implement
        //   generic classes: just use the 'T' generic type as if it were a regular type.
        //
        // - You should implement this method by using your ArrayHeap for the sake of
        //   efficiency.

        if (k < 0) {
            throw new IllegalArgumentException();
        }

        IList<T> output = new DoubleLinkedList<>();

        if (k != 0) {
            Iterator<T> iter = input.iterator();
            IPriorityQueue<T> minHeap = new ArrayHeap<>();

            // Sort k (or n if k > n) elements from list. Runtime: k*lg(k)
            int iItem = 1; // i = [1,k]
            while (iter.hasNext() && iItem <= k) {
                T element = iter.next();
                minHeap.insert(element);
                iItem++;
            }

            // With remaining list elements, replace minimum in heap only if list
            // element greater, otherwise discard it. The heap will therefore sort and
            // store the top k (greatest) elements of the list. Runtime: (n-k)*lg(k)
            while (iter.hasNext()) {
                T element = iter.next();
                if (element.compareTo(minHeap.peekMin()) >= 0) {
                    minHeap.removeMin();
                    minHeap.insert(element);
                }
            }

            // Copy the k greatest values, which are in ascending (least to greatest)
            // order to an output list. Runtime: k*lg(k)
            int size = minHeap.size();
            for (int i = 0; i < size; i++) {
                T item = minHeap.removeMin();
                output.add(item);
            }
        }
            return output; // Worst-case runtime: O(n*lg(k))
    }
}
