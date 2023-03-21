package ohm.softa.a02;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Peter Kurfer
 * Created on 10/6/17.
 */
public class SimpleListImpl implements SimpleList, Iterable<SimpleListImpl.SimpleListElement> {
    private SimpleListElement head;

    @Override
    public void add(Object o) {
        if (head == null) {
            head = new SimpleListElement(o);
            return;
        }

        if (head.next == null) {
            head.next = new SimpleListElement(o);
        }

        for (SimpleListElement element : this) {
            if (element.next == null) {
                element.next = new SimpleListElement(o);
                return;
            }
        }

        throw new IllegalStateException();
    }

    @Override
    public int size() {
        if (head == null) {
            return 0;
        }

        AtomicInteger count = new AtomicInteger();

        this.forEach(elem -> count.getAndIncrement());

        return count.get();
    }

    @Override
    public SimpleList filter(SimpleFilter filter) {
        SimpleList filteredList = new SimpleListImpl();

        for (SimpleListElement element : this) {
            if (filter.include(element)) {
                filteredList.add(element);
            }
        }

        return filteredList;
    }

    @Override
    public Iterator<SimpleListElement> iterator() {
        return new SimpleIteratorImpl();
    }

    class SimpleIteratorImpl implements Iterator<SimpleListElement> {
        private SimpleListElement currentElement = head;

        @Override
        public boolean hasNext() {
            return currentElement.next != null;
        }

        @Override
        public SimpleListElement next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            currentElement = currentElement.next;

            return currentElement;
        }
    }

    public static class SimpleListElement {
        private Object element;
        private SimpleListElement next;

        private SimpleListElement(Object element) {
            this.element = element;
        }
    }
}
