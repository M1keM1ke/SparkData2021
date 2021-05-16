package ru.mike.unsafe_sparkdata.collection;

import lombok.Data;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@Data
public class LazySparkList<T> implements List<T> {
    private List content;

    private long ownerId;

    private Class<?> modelClass;

    private String foreignKeyName;

    private String pathToSource;

    public boolean initialized(){
        return content != null;
    }

    public int size() {
        return this.content.size();
    }

    public boolean isEmpty() {
        return this.content.isEmpty();
    }

    public boolean contains(Object o) {
        return this.content.contains(o);
    }

    public Iterator iterator() {
        return this.content.iterator();
    }

    public Object[] toArray() {
        return this.content.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return (T[]) this.content.toArray(a);
    }

    public boolean add(Object e) {
        return this.content.add(e);
    }

    public boolean remove(Object o) {
        return this.content.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return this.content.containsAll(c);
    }

    public boolean addAll(Collection c) {
        return this.content.addAll(c);
    }

    public boolean addAll(int index, Collection c) {
        return this.content.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return this.content.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return this.content.retainAll(c);
    }

    public void replaceAll(UnaryOperator operator) {
        this.content.replaceAll(operator);
    }

    public void sort(Comparator c) {
        this.content.sort(c);
    }

    public void clear() {
        this.content.clear();
    }

    public T get(int index) {
        return (T) this.content.get(index);
    }

    public Object set(int index, Object element) {
        return this.content.set(index, element);
    }

    public void add(int index, Object element) {
        this.content.add(index, element);
    }

    public T remove(int index) {
        return (T) this.content.remove(index);
    }

    public int indexOf(Object o) {
        return this.content.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return this.content.lastIndexOf(o);
    }

    public ListIterator listIterator() {
        return this.content.listIterator();
    }

    public ListIterator listIterator(int index) {
        return this.content.listIterator(index);
    }

    public List subList(int fromIndex, int toIndex) {
        return this.content.subList(fromIndex, toIndex);
    }

    public Spliterator spliterator() {
        return this.content.spliterator();
    }

    public <T> T[] toArray(IntFunction<T[]> generator) {
        return (T[]) this.content.toArray(generator);
    }

    public boolean removeIf(Predicate filter) {
        return this.content.removeIf(filter);
    }

    public Stream stream() {
        return this.content.stream();
    }

    public Stream parallelStream() {
        return this.content.parallelStream();
    }

    public void forEach(Consumer action) {
        this.content.forEach(action);
    }
}