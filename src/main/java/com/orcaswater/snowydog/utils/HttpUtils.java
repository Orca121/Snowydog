package com.orcaswater.snowydog.utils;

import com.sun.net.httpserver.Headers;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.utils
 * @className: HttpUtils
 * @author: Orca121
 * @description: HttpUtils
 * @createTime: 2024-05-07 16:47
 * @version: 1.0
 */

public class HttpUtils {
    static final Pattern QUERY_SPLIT = Pattern.compile("\\&");

    /**
     * Parse query string.
     */
    public static Map<String, List<String>> parseQuery(String query, Charset charset) {
        if (query == null || query.isEmpty()) {
            return Map.of();
        }
        String[] ss = QUERY_SPLIT.split(query);
        Map<String, List<String>> map = new HashMap<>();
        for (String s : ss) {
            int n = s.indexOf('=');
            if (n >= 1) {
                String key = s.substring(0, n);
                String value = s.substring(n + 1);
                List<String> exist = map.get(key);
                if (exist == null) {
                    exist = new ArrayList<>(4);
                    map.put(key, exist);
                }
                exist.add(URLDecoder.decode(value, charset));
            }
        }
        return map;
    }

    public static Map<String, List<String>> parseQuery(String query) {
        return parseQuery(query, StandardCharsets.UTF_8);
    }

    public static String getHeader(Headers headers, String name) {
        List<String> values = headers.get(name);
        return values == null || values.isEmpty() ? null : values.get(0);
    }
}
