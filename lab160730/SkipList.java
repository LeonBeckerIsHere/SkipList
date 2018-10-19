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

    protected class SkipListIterator<T> implements Iterator<T>{
        Entry cursor, prev;

        public SkipListIterator(){
            cursor = head;
            prev = null;
        }

        public boolean hasNext(){
            return cursor.next[0] != tail;
        }

        public T next(){
            prev = cursor;
            cursor = cursor.next[0];
            return (T)cursor.element;
        }
    }

    // Constructor
    public SkipList() {
        //create dummy head and tail
        head = new Entry<>(null, PossibleLevels);
        tail = new Entry<>(null, PossibleLevels);

        //Highest possible level of head should point to tail
        head.next[32] = tail;

        //Set other initial variables
        size = 0;
        maxLevel = 1;
        last = new Entry[33];
        random = new Random();
    }

    //Helper function
    //Pre: last is either empty or holds values from a previous find
    //Post: last contains the path that leads to where x is or where x would go
    private void find(T x){
        Entry<T> ptr = head;

        //Iterate through each level
        for(int i = (maxLevel-1); i >= 0; i--){
            //Check for null values as not all entries have the same number of levels
            //Then check if the next entry element is less than x, if it then set the ptr equal to the next entry
            while(ptr.next[i] != null && ptr.next[i].element != null && ((T)ptr.next[i].element).compareTo(x) < 0){
                ptr = ptr.next[i];
            }

            //Add ptr to at last[i] to keep track of the path taken to find x
            last[i] = ptr;
        }
    }


    //Helper function
    //Randomly chooses a level to provide a normal distribution of levels across the skip list
    int chooseLevel(){
        //Sleek method provided by Dr. Raghavachari
        int level = 1 +Integer.numberOfTrailingZeros(random.nextInt());

        //ensures that the levels slowly propagate as entries are ended
        level = Math.min(level, maxLevel+1);

        if(level > maxLevel)
            maxLevel = level;

        return level;
    }

    // Add x to list. If x already exists, reject it. Returns true if new node is added to list
    public boolean add(T x) {

        //No duplicates
        if(contains(x)) return false;

        //Randomly choose level of new entry
        int level = chooseLevel();

        //If the list is empty then initialize it properly
        if(isEmpty()){
            head.next[0] = new Entry(x, level);
            head.next[0].prev = head;
            head.next[0].next[0] = tail;
            tail.prev = head.next[0];
            size++;
            return true;
        }

        Entry<T> ent = new Entry<>(x, level);

        for(int i = 0; i < level; i++){
            if(last[i] != null) {
                ent.next[i] = last[i].next[i];
                last[i].next[i] = ent;
                ent.prev = last[i];
            }
        }

        ent.next[0].prev = ent;
        ent.prev = last[0];

        size++;

        return true;
    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {
        find(x);
        if(last[0] != null && last[0].next[0] != null)
            return (T)last[0].next[0].element;
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
        if(contains(x))
            return (T)last[0].next[0].element;
        else if(last[0] != null)
            return (T) last[0].element;

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
        return new SkipListIterator<>();
    }

    // Return last element of list
    public T last() {
        return (T)tail.prev.element;
    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
        if(!contains(x)) return null;
        Entry<T> ent = last[0].next[0];
        for(int i=0; i < ent.next.length; i++){
            last[i].next[i] = ent.next[i];
            if(ent.next[i] != null)
                ent.next[i].prev = last[i];
        }
        size--;
        return ent.element;
    }

    // Return the number of elements in the list
    public int size() {
        return size;
    }
}