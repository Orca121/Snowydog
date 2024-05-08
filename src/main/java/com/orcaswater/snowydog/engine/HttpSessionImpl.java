package com.orcaswater.snowydog.engine;

import com.orcaswater.snowydog.engine.support.Attributes;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import java.util.Enumeration;


/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine
 * @className: HttpSessionImpl
 * @author: Orca121
 * @description: TODO
 * @createTime: 2024-05-07 17:50
 * @version: 1.0
 */

public class HttpSessionImpl implements HttpSession {
    // ServletContext
    final ServletContextImpl servletContext;
    // SessionID
    String sessionId;
    // 过期时间(s)
    int maxInactiveInterval;
    // 创建时间(ms)
    long creationTime;
    // 最后一次访问时间(ms)
    long lastAccessedTime;
    // getAttribute/setAttribute
    Attributes attributes;

    public HttpSessionImpl(ServletContextImpl servletContext, String sessionId, int interval) {
        this.servletContext = servletContext;
        this.sessionId = sessionId;
        this.creationTime = this.lastAccessedTime = System.currentTimeMillis();
        this.attributes = new Attributes(true);
        setMaxInactiveInterval(interval);
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return this.sessionId;
    }

    @Override
    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    @Override
    public void invalidate() {
        checkValid();
        this.servletContext.sessionManager.remove(this);
        this.sessionId = null;
    }

    @Override
    public boolean isNew() {
        return this.creationTime == this.lastAccessedTime;
    }

    // attribute get/set操作

    @Override
    public Object getAttribute(String name) {
        checkValid();
        return this.attributes.getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        checkValid();
        return this.attributes.getAttributeNames();
    }

    @Override
    public void setAttribute(String name, Object value) {
        checkValid();
        if (value == null) {
            removeAttribute(name);
        } else {
            Object oldValue = this.attributes.setAttribute(name, value);
            if (oldValue == null) {
                this.servletContext.invokeHttpSessionAttributeAdded(this, name, value);
            } else {
                this.servletContext.invokeHttpSessionAttributeReplaced(this, name, value);
            }
        }
    }

    @Override
    public void removeAttribute(String name) {
        checkValid();
        Object oldValue = this.attributes.removeAttribute(name);
        this.servletContext.invokeHttpSessionAttributeRemoved(this, name, oldValue);
    }

    void checkValid() {
        if (this.sessionId == null) {
            throw new IllegalStateException("Session is already invalidated.");
        }
    }

    @Override
    public String toString() {
        return String.format("HttpSessionImpl@%s[id=%s]", Integer.toHexString(hashCode()), this.getId());
    }
}
