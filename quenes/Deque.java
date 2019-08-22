/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first = null, last = null;

    private int sizevalue = 0;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    public Deque() {
    }                          // construct an empty deque

    public boolean isEmpty() {
        return first == null || last == null;
    }               // is the deque empty?

    public int size() {
        return sizevalue;
    }                       // return the number of items on the deque

    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("null argument");
        }
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.previous = null;
        if (isEmpty()) {
            last = first;
        }
        else {
            oldfirst.previous = first;
        }
        sizevalue++;

    }         // add the item to the front

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null argument");
        }
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) {
            last.previous = null;
            first = last;
        }
        else {
            oldlast.next = last;
            last.previous = oldlast;
        }
        sizevalue++;

    }        // add the item to the end

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty");
        }
        Item item = first.item;
        first = first.next;
        if (isEmpty()) {
            last = null;
        }
        else {
            first.previous = null;
        }
        sizevalue--;
        return item;
    }           // remove and return the item from the front

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty");
        }
        Item item = last.item;
        last = last.previous;
        if (isEmpty()) {
            first = null;
        }
        else {
            last.next = null;
        }
        sizevalue--;
        return item;
    }             // remove and return the item from the end

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }       // return an iterator over items in order from front to end

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();

        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
    } // unit testing (optional)
}
