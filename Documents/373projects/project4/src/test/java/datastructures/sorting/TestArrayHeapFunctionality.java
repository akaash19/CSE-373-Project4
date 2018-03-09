
package datastructures.sorting;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.BaseTest;
import misc.exceptions.EmptyContainerException;

import static org.junit.Assert.fail;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout = SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }

    @Test(timeout = SECOND)
    public void testSizeHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(5);
        heap.insert(20);
        heap.insert(10);
        heap.insert(6);
        heap.insert(7);
        heap.insert(3);
        heap.insert(1);
        heap.insert(2);
        heap.insert(7);
        heap.insert(8);
        heap.insert(11);
        heap.insert(3);
        int[] array = {1, 2, 3, 3, 5, 6, 7, 7, 8, 10, 11, 20};
        assertEquals(array.length, heap.size());
        heap.removeMin();
        assertEquals(array.length - 1, heap.size());
    }

    @Test(timeout = SECOND)
    public void testBasicInsertHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(10);
        heap.insert(100);
        heap.insert(1000);
        assertEquals(10, heap.removeMin());
        assertEquals(100, heap.removeMin());
        assertEquals(1000, heap.removeMin());
    }

    @Test(timeout = SECOND)
    public void testInsertHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(5);
        heap.insert(20);
        heap.insert(10);
        heap.insert(6);
        heap.insert(7);
        heap.insert(3);
        heap.insert(1);
        heap.insert(2);
        heap.insert(7);
        heap.insert(8);
        heap.insert(11);
        heap.insert(3);
        int[] array = {1, 2, 3, 3, 5, 6, 7, 7, 8, 10, 11, 20};
        // representation as tree: {1, 2, 3, 6, 7, 20, 5, 3, 7, 10, 8, 11};
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testRemoveMinHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(5);
        heap.insert(20);
        heap.insert(10);
        heap.insert(6);
        heap.insert(7);
        heap.insert(3);
        heap.insert(1);
        heap.insert(2);
        heap.insert(7);
        heap.insert(8);
        heap.insert(11);
        heap.insert(3);
        heap.removeMin();
        int[] array = {2, 3, 3, 5, 6, 7, 7, 8, 10, 11, 20};
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testPeakMinHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(5);
        heap.insert(20);
        heap.insert(10);
        heap.insert(6);
        heap.insert(7);
        heap.insert(3);
        heap.insert(1);
        heap.insert(2);
        heap.insert(7);
        heap.insert(8);
        heap.insert(11);
        heap.insert(3);
        int[] array = {1, 2, 3, 6, 7, 20, 5, 3, 7, 10, 8, 11};
        assertEquals(array[0], heap.peekMin());
    }

    @Test(timeout = SECOND)
    public void testAscendingInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(16);
        heap.insert(15);
        heap.insert(14);
        heap.insert(13);
        heap.insert(12);
        heap.insert(11);
        heap.insert(10);
        heap.insert(9);
        heap.insert(8);
        heap.insert(7);
        heap.insert(6);
        heap.insert(5);
        heap.insert(4);
        heap.insert(3);
        heap.insert(2);
        heap.insert(1);

        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testDescendingInsertDuplicates() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        heap.insert(4);
        heap.insert(5);
        heap.insert(6);
        heap.insert(7);
        heap.insert(8);
        heap.insert(9);
        heap.insert(10);
        heap.insert(11);
        heap.insert(12);
        heap.insert(13);
        heap.insert(14);
        heap.insert(15);
        heap.insert(16);
        heap.insert(0);
        heap.insert(2);
        heap.insert(3);
        heap.insert(1);
        int[] array = {0, 1, 1, 2, 2, 3, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testStringInsert() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("a");
        heap.insert("b");
        heap.insert("h");
        heap.insert("z");
        heap.insert("y");
        heap.insert("t");
        heap.insert("c");
        heap.insert("a");

        String[] array = {"a", "a", "h", "z", "y", "t", "c", "b"};
        assertEquals(array[0], heap.peekMin());
    }

    @Test(timeout = SECOND)
    public void testStringRemoveMinInsert() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("a");
        heap.insert("b");
        heap.insert("h");
        heap.insert("z");
        heap.insert("y");
        heap.insert("t");
        heap.insert("c");
        heap.insert("a");

        String[] array = {"a", "a", "b", "c", "h", "t", "y", "z"};
        assertEquals(array[0], heap.peekMin());
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testMixStringIntInsert() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("a");
        heap.insert("b");
        heap.insert("h");
        heap.insert("z");
        heap.insert("y");
        heap.insert("t");
        heap.insert("c");
        heap.insert("a");

        String[] array = {"a", "a", "b", "c", "h", "t", "y", "z"};
        assertEquals(array[0], heap.peekMin());
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testEmptyStringInsert() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("a");
        heap.insert("b");
        heap.insert("h");
        heap.insert(" ");
        heap.insert(" ");
        heap.insert("h");
        heap.insert(" ");
        heap.insert(" ");

        String[] array = {" ", " ", " ", " ", "a", "b", "h", "h"};
        assertEquals(array[0], heap.peekMin());
    }

    @Test(timeout = SECOND)
    public void testIntStringInsert() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("a2");
        heap.insert("1b");
        heap.insert("h");
        heap.insert("3y");
        heap.insert("b7");
        heap.insert(" ");
        heap.insert("");

        String[] array = {"", " ", "1b", "3y", "a2", "b7", "h"};
        assertEquals(array[0], heap.peekMin());
    }

    @Test(timeout = SECOND)
    public void testRandomHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        List<Integer> random = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int x = 0 + (int) (Math.random() * ((2000 - 0) + 1));
            random.add(x);
            heap.insert(x);
        }
        Collections.sort(random);

        assertEquals(20, heap.size());
        for (int i = 0; i < heap.size(); i++) {
            assertEquals(random.get(i), heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testStringInsert2() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("aidan");
        heap.insert("meejin");
        heap.insert("michael");
        heap.insert("andrew");
        heap.insert("sara");
        heap.insert("xavier");
        heap.insert("zebra");
        heap.insert("oscar");

        String[] array = {"aidan", "andrew", "meejin", "michael", "oscar", "sara", "xavier", "zebra"};
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], heap.removeMin());
        }
    }
    
    @Test(timeout = SECOND)
    public void testRemoveMinEmptyArray() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException err) { 
            // Do nothing
            }
    }
    
    @Test(timeout = SECOND)
    public void testPeekMinEmptyArray() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException err) { 
              // Do nothing
            }
    }
    
    @Test(timeout = SECOND)
    public void testInsertNull() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException err) { 
            // Do nothing
            }
    }
    
    @Test(timeout = SECOND)
    public void testAssertFalse() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertFalse(heap.isEmpty());
    }

    @Test(timeout = SECOND)
    public void testRemove() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("a");
        heap.insert("b");
        heap.insert("h");
        heap.insert("z");
        heap.insert("y");
        heap.insert("t");
        heap.insert("c");

        heap.remove("h");
        String[] array1 = {"a", "b", "c", "t", "y", "z"};
        for (int i = 0; i < array1.length; i++) {
            assertEquals(array1[i], heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testRemoveEdgeCase() {
        IPriorityQueue<String> heap1 = this.makeInstance();
        heap1.insert("a");
        heap1.insert("b");
        heap1.insert("h");
        heap1.insert("z");
        heap1.insert("y");
        heap1.insert("t");
        heap1.insert("c");

        heap1.remove("a");
        String[] array1 = {"b", "c", "h", "t", "y", "z"};
        for (int i = 0; i < array1.length; i++) {
            assertEquals(array1[i], heap1.removeMin());
        }

        IPriorityQueue<String> heap2 = this.makeInstance();
        heap2.insert("a");
        heap2.insert("b");
        heap2.insert("h");
        heap2.insert("z");
        heap2.insert("y");
        heap2.insert("t");
        heap2.insert("c");

        heap2.remove("z");
        String[] array2 = {"a", "b", "c", "h", "t", "y"};
        for (int i = 0; i < array2.length; i++) {
            assertEquals(array2[i], heap2.removeMin());
        }
    }
}
