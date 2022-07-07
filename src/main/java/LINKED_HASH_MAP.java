import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LINKED_HASH_MAP<K,V> extends HASH_MAP<K,V> {
    LINKED_HASH_MAP(Base<K> keys, Base<V> values, Base<Map.Entry<K, V>> entries) {
        super(keys, values, entries);
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

    //Pre: size != 0
    //Post: size = 0
    public void	clear() {
        keys.removeAll();
        values.removeAll();
        entries.removeAll();
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
}
