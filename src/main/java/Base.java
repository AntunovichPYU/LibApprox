import java.util.ArrayList;
import java.util.Collection;

public class Base<E> {
    private ArrayList<E> list;
    private int size;

    public ArrayList<E> collection() {
        return list;
    } //костыль, чтобы представить базис как коллекцию

    Base() {}

    /**
     * base functions
     *
     */
    public void add(int index, Object element) {} //добавляет элемент в список по индексу, сдвигает все последующие элементы

    public void remove(int index) {} //удаляет элемент по индексу, сдвигает все элементы

    public int search(Object element, Boolean reverse) {return 1;} //возвращает индекс элемента, если его нет в списке, возвращает -1!!

    public E get(int index) {return list.get(index);} //возвращает элемент по индексу

    public int size() {return size;} //возвращает размер списка

    public void sort(boolean reverse) {} //сортирует все элементы по возрастанию, при reverse = true элементы сортируются по убыванию

    public boolean addAll(int index, Collection<?> c) {return true;} //добавляет все элементы из указанной коллекции
                                                                            //при успешном добавлении возвращает true, иначе false

    public Base<E> removeAll() {return this;} //удаляет все элементы в списке, возвращает удаленные элементы

    public Base<E> subList(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {return new Base<>();}
    //возвращает новый сет, который состоит из элементов, находящихся между fromElement и toElement

    public E getNearest(E e, boolean lower) {return e;}
    //Возвращает элемент строго меньше(больше (в зависимости от параметра lower)) данного (если его нет), либо возвращает этот элемент,
    //возвращает null, если список пустой

}
