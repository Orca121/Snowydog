package com.orcaswater.snowydog.engine.listener;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.listener
 * @className: HelloServletRequestListener
 * @author: Orca121
 * @description: 用于监听ServletRequest的创建和销毁事件；
 * @createTime: 2024-05-07 22:14
 * @version: 1.0
 */

@WebListener
public class HelloServletRequestListener implements ServletRequestListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        logger.info(">>> ServletRequest initialized: {}", sre.getServletRequest());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        logger.info(">>> ServletRequest destroyed: {}", sre.getServletRequest());
    }
}
