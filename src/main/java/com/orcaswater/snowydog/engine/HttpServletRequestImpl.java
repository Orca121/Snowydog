package com.orcaswater.snowydog.engine;

import com.orcaswater.snowydog.connector.HttpExchangeRequest;
import com.orcaswater.snowydog.engine.support.HttpHeaders;
import com.orcaswater.snowydog.engine.support.Parameters;
import com.orcaswater.snowydog.utils.HttpUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.connector
 * @className: HttpServletRequestImpl
 * @author: Orca121
 * @description: HttpServletRequest的实现类，仅实现了代码调用时用到的部分方法
 * @createTime: 2024-05-07 11:00
 * @version: 1.0
 */

public class HttpServletRequestImpl implements HttpServletRequest {

    final ServletContextImpl servletContext;
    final HttpExchangeRequest exchangeRequest;
    final HttpServletResponse response;
    final HttpHeaders headers;
    final Parameters parameters;

    Boolean inputCalled = null;

    public HttpServletRequestImpl(ServletContextImpl servletContext, HttpExchangeRequest exchangeRequest, HttpServletResponse response) {
        this.servletContext = servletContext;
        this.exchangeRequest = exchangeRequest;
        this.response = response;
        this.headers = new HttpHeaders(exchangeRequest.getRequestHeaders());
        this.parameters = new Parameters(exchangeRequest, "UTF-8");
    }

    @Override
    public String getParameter(String name) {
        return this.parameters.getParameter(name);
    }

    Map<String, String> parseQuery(String query) {
        if (query == null || query.isEmpty()) {
            return Map.of();
        }
        String[] ss = Pattern.compile("\\&").split(query);
        Map<String, String> map = new HashMap<>();
        for (String s : ss) {
            int n = s.indexOf('=');
            if (n >= 1) {
                String key = s.substring(0, n);
                String value = s.substring(n + 1);
                map.putIfAbsent(key, URLDecoder.decode(value, StandardCharsets.UTF_8));
            }
        }
        return map;
    }

    @Override
    public String getMethod() {
        return exchangeRequest.getRequestMethod();
    }

    @Override
    public String getRequestURI() {
        return this.exchangeRequest.getRequestURI().getPath();
    }


    @Override
    public HttpSession getSession(boolean create) {
        String sessionId = null;
        // 获取所有Cookie:
        Cookie[] cookies = getCookies();
        if (cookies != null) {
            // 查找JSESSIONID:
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    // 拿到Session ID:
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        // 未获取到SessionID，且create=false，返回null:
        if (sessionId == null && !create) {
            return null;
        }
        // 未获取到SessionID，但create=true，创建新的Session:
        if (sessionId == null) {
            // 如果Header已经发送，则无法创建Session，因为无法添加Cookie:
            if (this.response.isCommitted()) {
                throw new IllegalStateException("Cannot create session for response is commited.");
            }
            // 创建随机字符串作为SessionID:
            sessionId = UUID.randomUUID().toString();
            // 构造一个名为JSESSIONID的Cookie:
            String cookieValue = "JSESSIONID=" + sessionId + "; Path=/; SameSite=Strict; HttpOnly";
            // 添加到HttpServletResponse的Header:
            this.response.addHeader("Set-Cookie", cookieValue);
        }
        // 返回一个Session对象:
        return this.servletContext.sessionManager.getSession(sessionId);
    }

    /**
     * @param :
     * @return HttpSession
     * @author: orca121
     * @description: 默认true
     * @createTime: 2024/5/7 19:37
     */
    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return true;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public Cookie[] getCookies() {
        String cookieValue = this.getHeader("Cookie");
        return HttpUtils.parseCookies(cookieValue);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputCalled == null) {
            this.inputCalled = Boolean.TRUE;
            return new ServletInputStreamImpl(this.exchangeRequest.getRequestBody());
        }
        throw new IllegalStateException("Cannot reopen input stream after " + (this.inputCalled ? "getInputStream()" : "getReader()") + " was called.");
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.inputCalled == null) {
            this.inputCalled = Boolean.FALSE;
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.exchangeRequest.getRequestBody()), StandardCharsets.UTF_8));
        }
        throw new IllegalStateException("Cannot reopen input stream after " + (this.inputCalled ? "getInputStream()" : "getReader()") + " was called.");
    }

    // 请求头操作

    @Override
    public long getDateHeader(String name) {
        return this.headers.getDateHeader(name);
    }

    @Override
    public String getHeader(String name) {
        return this.headers.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> hs = this.headers.getHeaders(name);
        if (hs == null) {
            return Collections.emptyEnumeration();
        }
        return Collections.enumeration(hs);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(this.headers.getHeaderNames());
    }

    @Override
    public int getIntHeader(String name) {
        return this.headers.getIntHeader(name);
    }

    // not implemented yet:
    @Override
    public Object getAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        // TODO Auto-generated method stub
    }

    @Override
    public int getContentLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getContentType() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Enumeration<String> getParameterNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getProtocol() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getScheme() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getServerName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getServerPort() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public String getRemoteAddr() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRemoteHost() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttribute(String name, Object o) {
        // TODO Auto-generated method stub
    }

    @Override
    public void removeAttribute(String name) {
        // TODO Auto-generated method stub
    }

    @Override
    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isSecure() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getRemotePort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getLocalName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLocalAddr() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLocalPort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRequestId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getProtocolRequestId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServletConnection getServletConnection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAuthType() {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public String getPathInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPathTranslated() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getContextPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getQueryString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRemoteUser() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public StringBuffer getRequestURL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getServletPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String changeSessionId() {
        throw new UnsupportedOperationException("changeSessionId() is not supported.");
    }



    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {
        // TODO Auto-generated method stub
    }

    @Override
    public void logout() throws ServletException {
        // TODO Auto-generated method stub
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        // TODO Auto-generated method stub
        return null;
    }
}
