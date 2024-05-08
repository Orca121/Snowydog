package com.orcaswater.snowydog.engine.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.listener
 * @className: HelloHttpSessionAttributeListener
 * @author: Orca121
 * @description: 用于监听HttpSession属性的添加、修改和删除事件；
 * @createTime: 2024-05-07 22:12
 * @version: 1.0
 */
@Deprecated

@WebListener
public class HelloHttpSessionAttributeListener implements HttpSessionAttributeListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        logger.info(">>> HttpSession attribute added: {} = {}", event.getName(), event.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        logger.info(">>> HttpSession attribute removed: {} = {}", event.getName(), event.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        logger.info(">>> HttpSession attribute replaced: {} = {}", event.getName(), event.getValue());
    }
}
