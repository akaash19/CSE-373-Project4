package datastructures.sorting;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.BaseTest;
import misc.Searcher;
import org.junit.Test;


import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testUnsortedInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        int[] all = {120, 213, 2, 444, 4585, 323, 78, 7, 9, 13, 41,
                       992, 100000, -103, 55, 1, 37, 81, 84, 59};
        int[] high = {992, 4585, 100000};
        for (int i = 0; i < 20; i++) {
            list.add(all[i]);
        }

        IList<Integer> top = Searcher.topKSort(3, list);
        assertEquals(3, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(high[i], top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testRandomInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        List<Integer> random = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int x = 0 + (int) (Math.random() * ((2000 - 0) + 1));
            random.add(x);
            list.add(x);
        }
        Collections.sort(random);
        IList<Integer> randomTop = new DoubleLinkedList<>();

        for (int i = random.size() - 3; i < random.size(); i++) {
            randomTop.add(random.get(i));
        }

        IList<Integer> top = Searcher.topKSort(3, list);
        assertEquals(3, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(randomTop.get(i), top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testAllMoreNoneInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        IList<Integer> all = Searcher.topKSort(10, list);
        assertEquals(10, all.size());
        for (int i = 0; i < all.size(); i++) {
            assertEquals(i, all.get(i));
        }

        IList<Integer> more = Searcher.topKSort(15, list);

        assertEquals(10, more.size());
        for (int i = 0; i < more.size(); i++) {
            assertEquals(i, more.get(i));
        }

        IList<Integer> empty = Searcher.topKSort(0, list);
        assertEquals(true, empty.isEmpty());


        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testAllZeros() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(0);
        }

        IList<Integer> all = Searcher.topKSort(10, list);
        assertEquals(10, all.size());
        for (int i = 0; i < all.size(); i++) {
            assertEquals(0, all.get(i));
        }

        IList<Integer> more = Searcher.topKSort(15, list);

        assertEquals(10, more.size());
        for (int i = 0; i < more.size(); i++) {
            assertEquals(0, more.get(i));
        }

        IList<Integer> empty = Searcher.topKSort(0, list);
        assertEquals(true, empty.isEmpty());


        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
}
