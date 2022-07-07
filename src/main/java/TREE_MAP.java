import java.util.*;

public class TREE_MAP<K,V> {
    Base<K> keys;
    Base<V> values;

    Base<Map.Entry<K,V>> entries;

    static class Entry<K,V> implements Map.Entry<K,V> {

        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public V getValue() {
            return null;
        }

        @Override
        public V setValue(V value) {
            return null;
        }
    }

    //Pre: true
    //Post: size() == base.size()
    public int size() {
        return keys.size();
    }

    //Pre: true
    //Post: ??
    public boolean containsKey(Object key) {
        return keys.search(key, false) != -1;
    }

    //Pre: true
    //Post: ??
    public boolean containsValue(Object value) {
        return values.search(value, false) != -1;
    }

    //Pre: key is in map
    //Post: get == base.get()
    public V get(Object key) {
        int i = keys.search(key, false);
        if (i != -1) return values.get(i);
        else return null;
    }

    //Pre: size != 0
    //Post: firstKey() == keys.get(0)
    public K firstKey() {
        return keys.get(0);
    }

    //Pre: size != 0
    //Post: lastKey() == keys.get(size() - 1)
    public K lastKey() {
        return keys.get(size() - 1);
    }

    //Pre: entries are not in map
    //Post: size = size' + m.size()
    public void putAll(Map<? extends K, ? extends V> m) {
        keys.addAll(size(), m.keySet());
        values.addAll(size(), m.values());
        entries.addAll(size(), m.entrySet());
    }

    //Pre: entry is not in map
    //Post: size = size' + 1
    public V put(K key, V value) {
        if (keys.search(key, false) == -1) {
            keys.add(size(), key);
            keys.sort();
            int i = keys.search(key, false);
            values.add(i, value);
            entries.add(i, new Entry<>(key, value));
        } else {
            int i = keys.search(key, false);
            values.remove(i);
            values.add(i, value);
            entries.remove(i);
            entries.add(i, new Entry<>(key, value));
        }
        return value;
    }

    //Pre: entry is in map
    //Post: size = size' - 1
    public V remove(Object key) {
        int i = keys.search(key, false);
        keys.remove(i);
        V value = values.get(i);
        values.remove(i);
        entries.remove(i);
        return value;
    }

    //Pre: size != 0
    //Post: size = 0
    public void clear() {
        keys.removeAll();
        values.removeAll();
        entries.removeAll();
    }

    //Pre: clone.isEmpty()
    //Post: clone.size() == this.size()
    public Object clone() {
        Base<K> k = new Base<>();
        Base<V> v = new Base<>();
        Base<Map.Entry<K,V>> e = new Base<>();
        k.addAll(0, keys.collection());
        v.addAll(0, values.collection());
        e.addAll(0, entries.collection());
        return new HASH_MAP<>(k, v, e);
    }

    //Pre: size != 0
    //Post: firstEntry == entries.get(0)
    public Map.Entry<K,V> firstEntry() {
        if (size() == 0)
            return null;
        else
            return entries.get(0);
    }

    //Pre: size != 0
    //Post: lastEntry == entries.get(size() - 1)
    public Map.Entry<K,V> lastEntry() {
        if (size() == 0)
            return null;
        else
            return entries.get(size() - 1);
    }

    //Pre: size != 0
    //Post: size == size' - 1 || pollFirstEntry() == firstEntry()
    public Map.Entry<K,V> pollFirstEntry() {
        if (size() == 0) {
            return null;
        } else {
            Map.Entry<K, V> e = entries.get(0);
            keys.remove(0);
            values.remove(0);
            entries.remove(0);
            return e;
        }
    }

    //Pre: size != 0
    //Post: size == size' - 1 || pollLastEntry() == lastEntry()
    public Map.Entry<K,V> pollLastEntry() {
        if (size() == 0) {
            return null;
        } else {
            Map.Entry<K, V> e = entries.get(size() - 1);
            keys.remove(size() - 1);
            values.remove(size() - 1);
            return e;
        }
    }

    //Pre: size != 0
    //Post: lowerEntry.getKey < key
    public Map.Entry<K,V> lowerEntry(K key) {
        K n = keys.getNearest(key, true);
        if (n == null)
            return null;
        int i = keys.search(n, false);
        if (n == key) {
            if (i == 0)
                return null;
            return entries.get(i - 1);
        }

        return entries.get(i);
    }

    //Pre: size != 0
    //Post: lowerKey < key
    public K lowerKey(K key) {
        K n = keys.getNearest(key, true);
        if (n == null)
            return null;
        int i = keys.search(n, false);
        if (n == key) {
            if (i == 0)
                return null;
            return keys.get(i - 1);
        }
        return n;
    }

    //Pre: size != 0
    //Post: floorEntry.getKey <= key
    public Map.Entry<K,V> floorEntry(K key) {
        K n = keys.getNearest(key, true);
        if (n == null)
            return null;
        int i = keys.search(n, false);

        return entries.get(i);
    }

    //Pre: size != 0
    //Post: floorKey <= key
    public K floorKey(K key) {
        return keys.getNearest(key, true);
    }

    //Pre: size != 0
    //Post: ceilingEntry.getKey <= key
    public Map.Entry<K,V> ceilingEntry(K key) {
        K n = keys.getNearest(key, false);
        if (n == null)
            return null;
        int i = keys.search(n, false);

        return entries.get(i);
    }

    //Pre: size != 0
    //Post: ceilingKey >= key
    public K ceilingKey(K key) {
        return keys.getNearest(key, false);
    }

    //Pre: size != 0
    //Post: higherEntry.getKey > key
    public Map.Entry<K,V> higherEntry(K key) {
        K n = keys.getNearest(key, false);
        if (n == null)
            return null;
        int i = keys.search(n, false);
        if (n == key) {
            if (i == size() - 1)
                return null;
            return entries.get(i + 1);
        }

        return entries.get(i);
    }

    //Pre: size != 0
    //Post: higherKey > key
    public K higherKey(K key) {
        K n = keys.getNearest(key, false);
        if (n == null)
            return null;
        int i = keys.search(n, false);
        if (n == key) {
            if (i == size() - 1)
                return null;
            return keys.get(i + 1);
        }
        return n;
    }

    //Pre: set.size() == 0
    //Post: set.size() == keys.size()
    public Set<K> keySet() {
        return new HashSet<>(keys.collection());
    }

    //Pre: collection.size() == 0
    //Post: collection.size() == values.size()
    public Collection<V> values() {
        return values.collection();
    }

    //Pre: entry is in map
    //Post: currentValue == newValue
    public boolean replace(K key, V oldValue, V newValue) {
        int i = keys.search(key, false);
        if (i != -1 && values.get(i) == oldValue) {
            values.remove(i);
            values.add(i, newValue);
            entries.remove(i);
            entries.add(i, new Entry<>(key, newValue));
            return true;
        } else {
            return false;
        }
    }

    //Pre: entry is in map
    //Post: currentValue == value
    public V replace(K key, V value) {
        int i = keys.search(key, false);
        if (i != -1) {
            V old = values.get(i);
            values.remove(i);
            values.add(i, value);
            entries.remove(i);
            entries.add(i, value);
            return old;
        } else {
            return null;
        }
    }

    //Нужно создать новый map и переместить все ключи, значения туда. Как это сделать без циклов и перебора листов с ключами и значениями?
    //Можно ли заменить возвращаемое значение с NavigableMap или SortedMap на тот же TREEMAP?
    // По сути, в джавовском TreeMap этот метод тоже возвращает TreeMap в своей реализации
    public NavigableMap<K,V> descendingMap() {
        return null;
    }

    public NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return null;
    }

    public NavigableMap<K,V> headMap(K toKey, boolean inclusive) {
        return null;
    }

    public NavigableMap<K,V> tailMap(K fromKey, boolean inclusive) {
        return null;
    }

    public SortedMap<K,V> subMap(K fromKey, K toKey) {
        return null;
    }

    public SortedMap<K,V> headMap(K toKey) {
        return null;
    }

    public SortedMap<K,V> tailMap(K fromKey) {
        return null;
    }
}
