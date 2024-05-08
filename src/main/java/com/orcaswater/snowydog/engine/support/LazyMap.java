package com.orcaswater.snowydog.engine.support;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.support
 * @className: LazyMap
 * @author: Orca121
 * @description: Map的包装类，它实现了延迟初始化的功能。实际的Map对象直到第一次需要时才会被创建。
 * @createTime: 2024-05-07 16:45
 * @version: 1.0
 */

public class LazyMap<V> {

    // 被包装的类
    private Map<String, V> map = null;
    // 如果为 true，则使用 ConcurrentHashMap；如果为 false，则使用 HashMap。
    private final boolean concurrent;

    public LazyMap(boolean concurrent) {
        this.concurrent = concurrent;
    }

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

    //返回映射中所有键的枚举。如果映射尚未初始化，返回一个空的枚举。
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
            this.map = concurrent ? new ConcurrentHashMap<>() : new HashMap<>();
        }
        return this.map.put(name, value);
    }

    protected V remove(String name) {
        if (this.map != null) {
            return this.map.remove(name);
        }
        return null;
    }

    // 返回映射对象的只读视图。如果映射尚未初始化，返回一个空的映射。
    protected Map<String, V> map() {
        if (this.map == null) {
            return Map.of();
        }
        return Collections.unmodifiableMap(this.map);
    }
}
