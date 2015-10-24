/**
 * COSC 2203-01 / Data Structures
 * Assignment 03 / "Huffman Coding"
 * Brian Scott
 *
 * 04 July 2015
 *
 * This class implements a min-heap using an array. This will be used to store
 * Huffman tree nodes containing character frequencies
 */
import java.util.Arrays;
@SuppressWarnings("unchecked")
public class MinHeap<E extends Comparable<E>> {
    private static final int INIT_CAPACITY = 20;
    private E[] heap;
    private int size;

    // Constructor
    MinHeap() {
        heap = (E[]) new Comparable[INIT_CAPACITY];
    }

    // Size accessor
    public int size() {
        return this.size;
    }

    // toString override
    @Override
    public String toString()
    {
        String out = "";
        for(int k = 1; k <= size; k++) out += heap[k]+" ";
        return out;
    }

    // Adds a value to the min-heap.
    public void add(E value) {
        // grow heap if needed
        if (size == heap.length - 1) heap = Arrays.copyOf(heap, heap.length * 2);

        // place element into heap at bottom
        int index = ++size;

        // Shift up
        for(; index > 1 && value.compareTo(heap[index/2]) < 0; index = index/2) heap[index] = heap[index/2];
        
        heap[index] = value;
    }
    
    // Retrieves and removes the top element
    public E extractMin() throws RuntimeException {
        if (size == 0) throw new RuntimeException();
        E min = heap[1];
        heap[1] = heap[size--];
        shiftDown(1);
        return min;
    }

    // Shifts elements down to keep the root
    // in the correct index
    private void shiftDown(int i) {
        E tmp = heap[i];
        int childIndex;

        for(; 2*i <= size; i = childIndex)
        {
            childIndex = 2*i;

            if(childIndex != size && heap[childIndex].compareTo(heap[childIndex + 1]) > 0) childIndex++;

            if(tmp.compareTo(heap[childIndex]) > 0) heap[i] = heap[childIndex];
            else break;
        }
        heap[i] = tmp;
    }
}