package datastructures.sorting;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import misc.BaseTest;
import misc.Searcher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=10*SECOND)
    public void testSimpleLargeUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10000000; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(9999995 + i, top.get(i));
        }
    }

    @Test(timeout=10*SECOND)
    public void testLargeAscendingHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 10000000; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < heap.size(); i++) {
            assertEquals(i, heap.removeMin());
        }
    }

    @Test(timeout=10*SECOND)
    public void testLargeDescendingHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 10000000; i > 0; i--) {
            heap.insert(i);
        }
        for (int i = 1; i < heap.size(); i++) {
            assertEquals(i, heap.removeMin());
        }
    }

    @Test(timeout=10*SECOND)
    public void testLargeRandomInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        List<Integer> random = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            int x = 0 + (int) (Math.random() * ((10000000 - 0) + 1));
            random.add(x);
            list.add(x);
        }
        Collections.sort(random);
        IList<Integer> randomTop = new DoubleLinkedList<>();

        for (int i = random.size() - 100; i < random.size(); i++) {
            randomTop.add(random.get(i));
        }

        IList<Integer> top = Searcher.topKSort(100, list);
        assertEquals(100, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(randomTop.get(i), top.get(i));
        }
    }
}
