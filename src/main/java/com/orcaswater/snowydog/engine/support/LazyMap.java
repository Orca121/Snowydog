package com.orcaswater.snowydog.engine.support;

import java.util.*;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.support
 * @className: LazyMap
 * @author: Orca121
 * @description: Lazy Proxy which holds a map
 * @createTime: 2024-05-07 16:45
 * @version: 1.0
 */

public class LazyMap<V> {

    private Map<String, V> map = null;

    protected V get(String name) {
        if (this.map == null) {
            return null;
        }
        return this.map.get(name);
    }

    protected Set<String> keySet() {
        if (this.map == null) {
            return Set.of();
        }
        return this.map.keySet();
    }

    protected Enumeration<String> keyEnumeration() {
        if (this.map == null) {
            return Collections.emptyEnumeration();
        }
        return Collections.enumeration(this.map.keySet());
    }

    protected boolean containsKey(String name) {
        if (this.map == null) {
            return false;
        }
        return this.map.containsKey(name);
    }

    protected V put(String name, V value) {
        if (this.map == null) {
            this.map = new HashMap<>();
        }
        return this.map.put(name, value);
    }

    protected V remove(String name) {
        if (this.map != null) {
            return this.map.remove(name);
        }
        return null;
    }

    protected Map<String, V> map() {
        if (this.map == null) {
            return Map.of();
        }
        return Collections.unmodifiableMap(this.map);
    }
}