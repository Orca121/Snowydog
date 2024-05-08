package com.orcaswater.snowydog.engine.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.listener
 * @className: HelloHttpSessionListener
 * @author: Orca121
 * @description: 用于监听HttpSession的创建和销毁事件；
 * @createTime: 2024-05-07 22:13
 * @version: 1.0
 */
@Deprecated
@WebListener
public class HelloHttpSessionListener implements HttpSessionListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info(">>> HttpSession created: {}", se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info(">>> HttpSession destroyed: {}", se.getSession());
    }
}
