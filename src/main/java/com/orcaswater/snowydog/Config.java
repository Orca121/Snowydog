package com.orcaswater.snowydog;

import java.util.Map;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog
 * @className: config
 * @author: Orca121
 * @description: 配置类
 * @createTime: 2024-05-07 23:06
 * @version: 1.0
 */

public class Config {

    public Server server;

    public static class Server {
        public String host;
        public Integer port;
        public Integer backlog;
        public String requestEncoding;
        public String responseEncoding;
        public String name;
        public String mimeDefault;
        public int threadPoolSize;
        public boolean enableVirtualThread;
        public Map<String, String> mimeTypes;
        public WebApp webApp;
        public ForwardedHeaders forwardedHeaders;

        public String getMimeType(String url) {
            int n = url.lastIndexOf('.');
            if (n < 0) {
                return this.mimeDefault;
            }
            String ext = url.substring(n).toLowerCase();
            return this.mimeTypes.getOrDefault(ext, this.mimeDefault);
        }

        public static class WebApp {
            public String name;
            public boolean fileListings;
            public String virtualServerName;
            public String sessionCookieName;
            public Integer sessionTimeout;
        }

        public static class ForwardedHeaders {
            public String forwardedProto;
            public String forwardedHost;
            public String forwardedFor;
        }
    }
}
