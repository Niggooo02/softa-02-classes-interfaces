package ohm.softa.a02;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Peter Kurfer
 * Created on 10/6/17.
 */
public class SimpleListImpl implements SimpleList, Iterable<Object> {
    private SimpleListElement head;

    @Override
    public void add(Object o) {
        if (head == null) {
            head = new SimpleListElement(o);
            return;
        }

        SimpleListElement element = head;

        while (element.next != null) {
            element = element.next;
        }

        element.next = new SimpleListElement(o);
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

        for (Object listItem : this) {
            if (filter.include(listItem)) {
                filteredList.add(listItem);
            }
        }

        return filteredList;
    }

    @Override
    public Iterator<Object> iterator() {
        return new SimpleIteratorImpl();
    }

    class SimpleIteratorImpl implements Iterator<Object> {
        private SimpleListElement currentElement = head;
        private boolean isFirst = true;

        @Override
        public boolean hasNext() {
            return currentElement.next != null;
        }

        @Override
        public Object next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            if (isFirst) {
                isFirst = false;
                return currentElement.element;
            }

            currentElement = currentElement.next;
            return currentElement.element;
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
