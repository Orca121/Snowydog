package com.orcaswater.snowydog.engine;

import com.orcaswater.snowydog.utils.DateUtils;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine
 * @className: SessionManager
 * @author: Orca121
 * @description: 管理所有的Session,由ServletContextImpl持有唯一实例
 * @createTime: 2024-05-07 17:49
 * @version: 1.0
 */

public class SessionManager implements Runnable {

    final Logger logger = LoggerFactory.getLogger(getClass());
    // 引用ServletContext:
    final ServletContextImpl servletContext;
    // Session存储器： 持有SessionID -> Session:
    final Map<String, HttpSessionImpl> sessions = new ConcurrentHashMap<>();
    // Session默认过期时间(秒):
    final int inactiveInterval;

    /**
     * @param servletContext: 持有该引用即持有SessionManager，以创建Session
     * @param interval: Session默认过期时间(秒)
     * @return null
     * @author: orca121
     * @description: :给SessionManager加一个Runnable接口，并启动一个Daemon线程,实现Session的自动过期
     * 由于Session实际是以Map存储, 所以让Session自动过期就是定期扫描所有Session，
     * 然后根据最后一次访问时间将过期的Session自动删除。
     * @createTime: 2024/5/7 20:03
     */
    public SessionManager(ServletContextImpl servletContext, int interval) {
        this.servletContext = servletContext;
        this.inactiveInterval = interval;
        // 启动Daemon线程:
        Thread t = new Thread(this, "Session-Cleanup-Thread");
        t.setDaemon(true);
        t.start();
    }

    /**
     * @param :
     * @return void
     * @author: orca121
     * @description: Deamon线程定期（60秒）扫描所有Session线程，清除过期Session
     * @createTime: 2024/5/7 20:01
     */
    @Override
    public void run() {
        for (;;) {
            // 每60秒扫描一次:
            try {
                Thread.sleep(60_000L);
            } catch (InterruptedException e) {
                break;
            }
            // 当前时间:
            long now = System.currentTimeMillis();
            // 遍历Session:
            for (String sessionId : sessions.keySet()) {
                HttpSession session = sessions.get(sessionId);
                // 判断是否过期:
                if (session.getLastAccessedTime() + session.getMaxInactiveInterval() * 1000L < now) {
                    // 删除过期的Session:
                    logger.warn("remove expired session: {}, last access time: {}", sessionId, DateUtils.formatDateTimeGMT(session.getLastAccessedTime()));
                    session.invalidate();
                }
            }
        }
    }

    /**
     * @param sessionId:
     * @return HttpSession
     * @author: Orca121
     * @description: 根据SessionID获取一个Session:
     * @createTime: 2024/5/7 17:52
     */
    public HttpSession getSession(String sessionId) {
        HttpSessionImpl session = sessions.get(sessionId);
        if (session == null) {
//            logger.info("创建Session: "+ sessionId);
            // Session未找到，创建一个新的Session:
            session = new HttpSessionImpl(this.servletContext, sessionId, inactiveInterval);
            sessions.put(sessionId, session);
            this.servletContext.invokeHttpSessionCreated(session);
        } else {
            // Session已存在，更新最后访问时间:
            session.lastAccessedTime = System.currentTimeMillis();
        }
        return session;
    }

    /**
     * @param session:
     * @return void
     * @author: patri
     * @description: 删除Session
     * @createTime: 2024/5/7 20:01
     */
    public void remove(HttpSession session) {
        this.sessions.remove(session.getId());
        this.servletContext.invokeHttpSessionDestroyed(session);
    }


}
