// Starter code for Project 2: skip lists
// Do not rename the class, or change names/signatures of methods that are declared to be public.


// change to your netid
package lab160730;

import javafx.geometry.Pos;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class SkipList<T extends Comparable<? super T>> {
    private static final int PossibleLevels = 33;
    private Entry head, tail;
    private int size, maxLevel;
    private Entry[] last;
    private Random random;

    static class Entry<E> {
        E element;
        Entry[] next;
        Entry prev;
        int[] span;

        public Entry(E x, int lev) {
            element = x;
            next = new Entry[lev];
            span = new int[lev];
        }

        public E getElement() {
            return element;
        }
    }

    // Constructor
    public SkipList() {
        head = new Entry<>(null, PossibleLevels);
        tail = new Entry<>(null, PossibleLevels);

        size = 0;
        maxLevel = 1;
        last = new Entry[33];
        random = new Random();
    }

    //Helper function
    private void find(T x){
        Entry<T> ptr;

        //Iterate through each level
        for(int i = (maxLevel-1); i >= 0; i--){
            ptr = head.next[i];
            while(ptr.next[0].element != null && ptr.element.compareTo(x) < 0){
                System.out.println("Wahh");
                ptr = ptr.next[i];
            }
            System.out.println(i);
            System.out.println(ptr.element);
            last[i] = ptr;
        }
    }

    //Helper function
    int chooseLevel(){

        int level = 1 +Integer.numberOfTrailingZeros(random.nextInt());
        level = Math.min(level, maxLevel+1);
        if(level > maxLevel)
            maxLevel = level;
        return level;
    }

    // Add x to list. If x already exists, reject it. Returns true if new node is added to list
    public boolean add(T x) {

        //If the list is empty assign x to be the next immediate value of head aka next[0]
        if(isEmpty()){
            head.next[0] = new Entry<>(x, 1);

            //Set the new element's next element to be the tail
            head.next[0].next[0] = tail;

            //Increment size
            size++;
            return true;
        }

        //No duplicates
        if(contains(x)) return false;

        int level = chooseLevel();
        System.out.println(level);
        Entry<T> ent = new Entry<>(x, level);

        for(int i = 0; i < level; i++){
            if(last[i] != null) {
                ent.next[i] = last[i].next[i];
                last[i].next[i] = ent;
            }
        }

        ent.next[0].prev = ent;
        ent.prev = last[0];

        size++;

        return true;
    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {
        return null;
    }

    // Does list contain x?
    public boolean contains(T x) {
        //Check for an empty list
        if(isEmpty()) return false;

        //find(x) last[0] contains the entry before the expected target entry
        find(x);

        //Check if the next entry is the expected target entry
        return (last[0].next[0].element != null) && (last[0].next[0].element.equals(x));
    }

    // Return first element of list
    public T first() {
        return (T)head.next[0].element;
    }

    // Find largest element that is less than or equal to x
    public T floor(T x) {
        return null;
    }

    // Return element at index n of list.  First element is at index 0.
    public T get(int n) {
        if(n < 0 || n > size-1) throw new java.util.NoSuchElementException();
        Entry<T> ptr = head;
        for(int i = 0; i <= n; i++){
            ptr = ptr.next[0];
        }
        return ptr.element;
    }

    // Is the list empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() {
        return null;
    }

    // Return last element of list
    public T last() {
        return null;
    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
        if(!contains(x)) return null;
        Entry<T> ent = last[0].next[0];
        for(int i=0; i < ent.next.length; i++){
            last[i].next[i] = ent.next[i];
        }
        size--;
        return ent.element;
    }

    // Return the number of elements in the list
    public int size() {
        return size;
    }
}