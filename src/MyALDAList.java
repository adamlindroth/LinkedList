/*
Adam Lindroth
adli7087
*/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyALDAList<E> implements ALDAList<E> {

    // Inner node class
    private static class Node<E> {
        private E data;
        private Node<E> next;


        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }

        Node(E data) {
            this.data = data;
            next = null;
        }
    }

    private int size;
    private Node<E> first;
    private Node<E> last;

    public MyALDAList() {
        size = 0;
        first = null;
        last = null;
    }

    @Override
    public void add(E element) {
        Node<E> n = new Node<>(element);
        //Case: empty list
        if (size == 0) {
            first = n;

        }

        //Case: populated list
        else {
            getNodeAtIndex(size - 1).next = n;
        }
        last = n;
        size++;
    }



    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Size: " + size + " Index: " + index);
        }
        //Case: Add an element to an empty list
        if (size == 0) {
            add(element);
        }
        //Case: Add first element in a populated list
        else if (index == 0) {
            addFirst(element);
        }
        //Case: Add last element in a populated list
        else if (index == size) {
            add(element);
        }
        //Case: Add element to middle of list
        else if (index < size) {
            addMiddle(index, element);
        }
    }



    @Override
    public E remove(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException("Size: " + size + " Index: " + index);
        }
        E data; //The data to be returned

        //Remove element at index 0
        if (index == 0) {
            data = first.data;  //Save the data stored in first element

            if (size == 1) { //If the list only contains one element, reset both references
                first = null;
                last = null;

            } else {
                first = first.next;     //If list contains multiple elements, set first reference to the node pointed out by next
            }


        } else if (index == size - 1) { //Remove element at last index
            data = last.data;           //Save data of last element
            if (size < 3) {             //If the list only contains 2 elements, last has to be re-referenced to the first element
                last = first;
            } else {
                last = getNodeAtIndex(size - 2); //If list is larger that 2, set last pointer to the element before last
            }

        } else {                        //Remove element in the middle of the list
            data = getNodeAtIndex(index).data;  //Save data of the element to be removed
            Node<E> nodeBefore = getNodeAtIndex(index - 1); // get the element before the one to be removed
            nodeBefore.next = nodeBefore.next.next;           //Set before-elements next-pointer to point to the element after the one to be removed
        }
        size--;
        return data;
    }

    @Override
    public boolean remove(E element) {
        if (!contains(element)) { //Check if the list contains the element to be removed
            return false;

        } else if (size == 1) { //If its the only element stored in the list, reset references to first/last
            first = null;
            last = null;
        } else if (first.data == element) { // If size is greater than 1 and the node is first, set first to point to next element
            first = first.next;
        } else {                            // If size is greater than 1 and node isn't in front
            Node<E> nodeBefore = getNodeAtIndex(indexOf(element) - 1);     //Get the node before the one to be removed
            Node<E> nodeToRemove = nodeBefore.next;                           //Get the node to be removed
            if (last == nodeToRemove) {                                    //If it is the last element,  set last to point out the node before
                last = nodeBefore;
            } else {                                                    // Else set node before to point out the node after the one to be removed as next.
                nodeBefore.next = nodeToRemove.next;
            }

        }
        size--;
        return true;
    }

    @Override
    public E get(int index) {
        return getNodeAtIndex(index).data;
    }

    @Override
    public boolean contains(E element) {
        boolean found = false;
        for (int i = 0; i < size; i++) {
            if (getNodeAtIndex(i).data == element) {
                found = true;
                break;
            }
        }
        return found;
    }

    @Override
    public int indexOf(E element) {
        //Return -1 if list does not contain element
        if (!contains(element)) {
            return -1;
        }

        int index = 0;
        for (int i = 0; i < size; i++) {
            if (getNodeAtIndex(i).data == element) {
                break;
            }
            index++;
        }
        return index;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private boolean okToRemove;
            private Node<E> current = first;
            private Node<E> previous;
            private Node<E> beforePrevious;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                E data = current.data;
                beforePrevious = previous;
                previous = current;
                current = current.next;
                okToRemove = true;
                return data;
            }

            @Override
            public void remove() {
                if (!okToRemove ) { // next() has not been called
                    throw new IllegalStateException();
                }

                else if(current == first.next){ // remove first element
                    first = current;
                }

                else { // remove the element last returned by next
                    beforePrevious.next = current;
                }
                size--;
                okToRemove = false; // Prevent removing until next has been called again
            }
        };
    }

    public String toString() {
        String str = "[";

        if (size > 0) {
            for (int i = 0; i < size - 1; i++) {
                str += getNodeAtIndex(i).data + ", ";
            }
            str += getNodeAtIndex(size - 1).data;
        }

        str += "]";
        return str;
    }

    //Gets the at specific index
    public Node<E> getNodeAtIndex(int index) { // TODO: make private
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Size: " + size + " Index: " + index);
        }
        Node<E> node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    private void addFirst(E element) {
        Node<E> n = new Node<>(element, first);
        first = n;
        size++;
    }

    private void addMiddle(int index, E element) {
        Node<E> n = new Node<>(element);
        Node<E> nodeBefore = getNodeAtIndex(index - 1);
        n.next = nodeBefore.next;
        nodeBefore.next = n;
        size++;
    }
}