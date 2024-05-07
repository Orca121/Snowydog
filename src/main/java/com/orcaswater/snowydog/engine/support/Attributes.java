package com.orcaswater.snowydog.engine.support;

import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.support
 * @className: Attributes
 * @author: Orca121
 * @description: 重写覆盖Attributes
 * @createTime: 2024-05-07 19:46
 * @version: 1.0
 */

public class Attributes extends LazyMap<Object> {

    public Attributes(boolean concurrent) {
        super(concurrent);
    }

    public Attributes() {
        this(false);
    }

    public Object getAttribute(String name) {
        Objects.requireNonNull(name, "name is null.");
        return super.get(name);
    }

    public Enumeration<String> getAttributeNames() {
        return super.keyEnumeration();
    }

    public Object setAttribute(String name, Object value) {
        Objects.requireNonNull(name, "name is null.");
        return super.put(name, value);
    }

    public Object removeAttribute(String name) {
        Objects.requireNonNull(name, "name is null.");
        return super.remove(name);
    }

    public Map<String, Object> getAttributes() {
        return super.map();
    }
}
