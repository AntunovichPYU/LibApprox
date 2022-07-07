import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HASH_MAP<K,V> {
    Base<K> keys;
    Base<V> values;

    Base<Map.Entry<K, V>> entries;

    HASH_MAP(Base<K> keys, Base<V> values, Base<Map.Entry<K, V>> entries) {
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
    //Post: isEmpty() == base.isEmpty()
    public boolean isEmpty() {
        return size() == 0;
    }

    //Pre: key is in map
    //Post: get == base.get()
    public V get(Object key) {
        int i = keys.search(key, false);
        if (i != -1) return values.get(i);
        else return null;
    }

    //Pre: true
    //Post: ??
    public boolean containsKey(Object key) {
        return keys.search(key, false) != -1;
    }

    //Pre: entry is not in map
    //Post: size = size' + 1
    public V put(K key, V value) {
        if (keys.search(key, false) == -1) {
            keys.add(size(), key);
            values.add(size(), value);
            entries.add(size(), new Entry<>(key, value));
        } else {
            int i = keys.search(key, false);
            values.remove(i);
            values.add(i, value);
            entries.remove(i);
            entries.add(i, new Entry<>(key, value));
        }
        return value;
    }

    //Pre: entries are not in map
    //Post: size = size' + m.size()
    public void	putAll(Map<? extends K, ? extends V> m) {
        keys.addAll(size(), m.keySet());
        values.addAll(size(), m.values());
        entries.addAll(size(), m.entrySet());
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
    public void	clear() {
        keys.removeAll();
        values.removeAll();
        entries.removeAll();
    }

    //Pre: true
    //Post: ??
    public boolean containsValue(Object value) {
        return values.search(value, false) != -1;
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

    //Pre: set.size() == 0
    //Post: set.size() == entries.size()
    public Set<Map.Entry<K,V>> entrySet() {
        return new HashSet<>(entries.collection());
    }

    //Pre: true
    //Post: getOrDefault == returnedValue || getOrDefault == defaultValue
    public V getOrDefault(Object key, V defaultValue) {
        if (keys.search(key, false) != -1) {
            int i = keys.search(key, false);
            return values.get(i);
        } else {
            return defaultValue;
        }
    }

    //Pre: entry is not in map
    //Post: size == size' + 1
    public V putIfAbsent(K key, V value) {
        int i = keys.search(key, false);
        if (i == -1) {
            keys.add(size(), key);
            values.add(size(), value);
            entries.add(size(), new Entry<>(key, value));
            return null;
        } else if (values.get(i) == null) {
            i = keys.search(key, false);
            values.add(i, value);
            entries.remove(i);
            entries.add(i, new Entry<>(key, value));
            return null;
        } else {
            return value;
        }
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
            entries.add(i, new Entry<>(key, value));
            return old;
        } else {
            return null;
        }
    }

    //Pre: key is not mapped
    //Post: size == size' + 1
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        if (keys.search(key, false) != -1) {
            return values.get(keys.search(key, false));
        }
        V v = mappingFunction.apply(key);
        if (v == null) {
            return null;
        } else {
            keys.add(size(), key);
            values.add(size(), v);
            entries.add(size(), new Entry<>(key, v));
            return v;
        }
    }

    //Pre: key is mapped
    //Post: size == size' || size == size' - 1
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        int i = keys.search(key, false);
        if (i != -1) {
            V old = values.get(i);
            V v = remappingFunction.apply(key, old);
            if (v == null) {
                keys.remove(i);
                values.remove(i);
                entries.remove(i);
            } else {
                values.remove(i);
                values.add(i, v);
                entries.remove(i);
                entries.add(size(), new Entry<>(key, v));
                return v;
            }
        }
        return null;
    }

    //Pre: key is mapped
    //Post: size == size' || size == size' - 1
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        int i = keys.search(key, false);
        if (i != -1) {
            V old = values.get(i);
            V v = remappingFunction.apply(key, old);
            if (v == null) {
                keys.remove(i);
                values.remove(i);
                entries.remove(i);
            } else {
                values.remove(i);
                values.add(i, v);
                entries.remove(i);
                entries.add(size(), new Entry<>(key, v));
                return v;
            }
        }
        return null;
    }

    //Pre: key is mapped
    //Post: currentValue == (oldValue, value) -> ? || currentValue == value
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        int i = keys.search(key, false);
        if (i != -1) {
            if (values.get(i) == value) {
                V old = values.get(i);
                V v = remappingFunction.apply(old, value);
                if (v == null) {
                    keys.remove(i);
                    values.remove(i);
                    entries.remove(i);
                } else {
                    values.remove(i);
                    values.add(i, v);
                    entries.remove(i);
                    entries.add(size(), new Entry<>(key, v));
                    return v;
                }
            } else {
                values.remove(i);
                values.add(i, value);
                entries.remove(i);
                entries.add(size(), new Entry<>(key, value));
                return value;
            }
        }

        return null;
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
}
