package com.github.mkopylec.charon.forwarding;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RetryingState.getRetryAttempts;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RetryingState.getRetryerIndex;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RetryingState.isNextRetryAttempt;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RetryingState.nextRetryAttemptUsed;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RetryingState.setRetryerIndex;

public class RetryAwareList<E> extends ArrayList<E> {

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Copy of {@link ArrayList.Itr}
     */
    private class Itr implements Iterator<E> {

        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        // prevent creating a synthetic constructor
        Itr() {
        }

        @Override
        public boolean hasNext() {
            // TODO Refactor, simplify?
            if (getRetryAttempts() > 1 && isNextRetryAttempt() && getRetryerIndex() != null) {
                cursor = getRetryerIndex() + 1;
                nextRetryAttemptUsed();
            }
            return cursor != RetryAwareList.this.size();
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (getRetryAttempts() == 1) {
                setRetryerIndex(cursor - 1);
            }
            if (getRetryAttempts() > 1 && isNextRetryAttempt() && getRetryerIndex() != null) {
                cursor = getRetryerIndex() + 1;
                nextRetryAttemptUsed();
            }
            checkForComodification();
            int i = cursor;
            if (i >= RetryAwareList.this.size())
                throw new NoSuchElementException();
            Object[] elementData = RetryAwareList.this.toArray();
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                RetryAwareList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int size = RetryAwareList.this.size();
            int i = cursor;
            if (i < size) {
                final Object[] es = RetryAwareList.this.toArray();
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < size && modCount == expectedModCount; i++) {
                    if (getRetryAttempts() > 1) { // TODO Must behave as logic above
                        i--;
                    }
                    action.accept((E) es[i]);
                }
                // update once at end to reduce heap write traffic
                cursor = i;
                lastRet = i - 1;
                checkForComodification();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
}
