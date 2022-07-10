import java.util.*;

public class TREE_MAP<K,V> {
    Base<K> keys;
    Base<V> values;

    Base<Map.Entry<K,V>> entries;

    TREE_MAP(Base<K> keys, Base<V> values, Base<Map.Entry<K,V>> entries) {
        this.keys = keys;
        this.values = values;
        this.entries = entries;
    }

    static class Entry<K,V> implements Map.Entry<K,V> {

        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }
    }

    //Pre: true
    //Post: size() == base.size()
    public int size() {
        return keys.size();
    }

    //Pre: true
    //Post: containsKey != null
    public boolean containsKey(Object key) {
        return keys.search(key, false) != -1;
    }

    //Pre: true
    //Post: containsValue != null
    public boolean containsValue(Object value) {
        return values.search(value, false) != -1;
    }

    //Pre: true
    //Post: get == base.get()
    public V get(Object key) {
        int i = keys.search(key, false);
        if (i != -1) return values.get(i);
        else return null;
    }

    //Pre: true
    //Post: firstKey() == keys.get(0)
    public K firstKey() {
        return keys.get(0);
    }

    //Pre: true
    //Post: lastKey() == keys.get(size() - 1)
    public K lastKey() {
        return keys.get(size() - 1);
    }

    //Pre: m != null
    //Post: size = size' + m.size()
    public void putAll(Map<? extends K, ? extends V> m) {
        keys.addAll(size(), m.keySet());
        values.addAll(size(), m.values());
        entries.addAll(size(), m.entrySet());
    }

    //Pre: true
    //Post: size = size' + 1
    public V put(K key, V value) {
        if (keys.search(key, false) == -1) {
            keys.add(size(), key);
            keys.sort(false);
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

    //Pre: true
    //Post: size = 0
    public void clear() {
        keys.removeAll();
        values.removeAll();
        entries.removeAll();
    }

    //Pre: true
    //Post: clone == this
    public Object clone() {
        Base<K> k = new Base<>();
        Base<V> v = new Base<>();
        Base<Map.Entry<K,V>> e = new Base<>();
        k.addAll(0, keys.collection());
        v.addAll(0, values.collection());
        e.addAll(0, entries.collection());
        return new HASH_MAP<>(k, v, e);
    }

    //Pre: true
    //Post: firstEntry == entries.get(0)
    public Map.Entry<K,V> firstEntry() {
        if (size() == 0)
            return null;
        else
            return entries.get(0);
    }

    //Pre: true
    //Post: lastEntry == entries.get(size() - 1)
    public Map.Entry<K,V> lastEntry() {
        if (size() == 0)
            return null;
        else
            return entries.get(size() - 1);
    }

    //Pre: true
    //Post: size == size' - 1 && pollFirstEntry() == firstEntry()
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

    //Pre: true
    //Post: size == size' - 1 && pollLastEntry() == lastEntry()
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

    //Pre: true
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

    //Pre: true
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

    //Pre: true
    //Post: floorEntry.getKey <= key
    public Map.Entry<K,V> floorEntry(K key) {
        K n = keys.getNearest(key, true);
        if (n == null)
            return null;
        int i = keys.search(n, false);

        return entries.get(i);
    }

    //Pre: true
    //Post: floorKey <= key
    public K floorKey(K key) {
        return keys.getNearest(key, true);
    }

    //Pre: true
    //Post: ceilingEntry.getKey <= key
    public Map.Entry<K,V> ceilingEntry(K key) {
        K n = keys.getNearest(key, false);
        if (n == null)
            return null;
        int i = keys.search(n, false);

        return entries.get(i);
    }

    //Pre: true
    //Post: ceilingKey >= key
    public K ceilingKey(K key) {
        return keys.getNearest(key, false);
    }

    //Pre: true
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

    //Pre: true
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

    //Pre: true
    //Post: set.size() == keys.size()
    public Set<K> keySet() {
        return new HashSet<>(keys.collection());
    }

    //Pre: true
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

    //Pre: true
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

    //Pre: true
    //Post: descendingMap == reverse oldMap
    public TREE_MAP<K,V> descendingMap() {
        Base<K> dk = keys;
        Base<V> dv = values;
        Base<Map.Entry<K,V>> de = entries;
        dk.sort(true);
        dv.sort(true);
        de.sort(true);

        return new TREE_MAP<>(dk, dv, de);
    }

    //Pre: fromKey >= 0; toKey < size()
    //Post: subMap.size() <= map.size()
    public TREE_MAP<K,V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        Base<K> dk = keys.subList(fromKey, fromInclusive, toKey, toInclusive);
        int iFrom = keys.search(fromKey, false);
        int iTo = keys.search(toKey, false);
        Base<V> dv = values.subList(values.get(iFrom), fromInclusive, values.get(iTo), toInclusive);
        Base<Map.Entry<K,V>> de = entries.subList(entries.get(iFrom), fromInclusive, entries.get(iTo), toInclusive);

        return new TREE_MAP<>(dk, dv, de);
    }

    //Pre: toKey < size()
    //Post: headMap.size() <= map.size()
    public TREE_MAP<K,V> headMap(K toKey, boolean inclusive) {
        Base<K> dk = keys.subList(keys.get(0), true, toKey, inclusive);
        int iTo = keys.search(toKey, false);
        Base<V> dv = values.subList(values.get(0), true, values.get(iTo), inclusive);
        Base<Map.Entry<K,V>> de = entries.subList(entries.get(0), true, entries.get(iTo), inclusive);

        return new TREE_MAP<>(dk, dv, de);
    }

    //Pre: fromKey >= 0
    //Post: tailMap.size() <= map.size()
    public TREE_MAP<K,V> tailMap(K fromKey, boolean inclusive) {
        Base<K> dk = keys.subList(fromKey, inclusive, keys.get(size() - 1), true);
        int iFrom = keys.search(fromKey, false);
        Base<V> dv = values.subList(values.get(iFrom), inclusive, values.get(size() - 1), true);
        Base<Map.Entry<K,V>> de = entries.subList(entries.get(iFrom), inclusive, entries.get(size() - 1), true);

        return new TREE_MAP<>(dk, dv, de);
    }

    //Pre: fromKey >= 0; toKey < size()
    //Post: subMap.size() <= map.size()
    public TREE_MAP<K,V> subMap(K fromKey, K toKey) {
        Base<K> dk = keys.subList(fromKey, true, toKey, false);
        int iFrom = keys.search(fromKey, false);
        int iTo = keys.search(toKey, false);
        Base<V> dv = values.subList(values.get(iFrom), true, values.get(iTo), false);
        Base<Map.Entry<K,V>> de = entries.subList(entries.get(iFrom), true, entries.get(iTo), false);

        return new TREE_MAP<>(dk, dv, de);
    }

    //Pre: toKey < size()
    //Post: headMap.size() <= map.size()
    public TREE_MAP<K,V> headMap(K toKey) {
        Base<K> dk = keys.subList(keys.get(0), true, toKey, false);
        int iTo = keys.search(toKey, false);
        Base<V> dv = values.subList(values.get(0), true, values.get(iTo), false);
        Base<Map.Entry<K,V>> de = entries.subList(entries.get(0), true, entries.get(iTo), false);

        return new TREE_MAP<>(dk, dv, de);
    }

    //Pre: fromKey >= 0
    //Post: tailMap.size() <= map.size()
    public TREE_MAP<K,V> tailMap(K fromKey) {
        Base<K> dk = keys.subList(fromKey, true, keys.get(size() - 1), true);
        int iFrom = keys.search(fromKey, false);
        Base<V> dv = values.subList(values.get(iFrom), true, values.get(size() - 1), true);
        Base<Map.Entry<K,V>> de = entries.subList(entries.get(iFrom), true, entries.get(size() - 1), true);

        return new TREE_MAP<>(dk, dv, de);
    }
}
