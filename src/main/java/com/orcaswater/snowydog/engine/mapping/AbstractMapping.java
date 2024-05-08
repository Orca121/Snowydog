package com.orcaswater.snowydog.engine.mapping;

import java.util.regex.Pattern;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.mapping
 * @className: AbstractMapping
 * @author: Orca121
 * @description: 抽象类，持有正则表达式，url等
 * @createTime: 2024-05-07 11:57
 * @version: 1.0
 */

public class AbstractMapping implements Comparable<AbstractMapping> {

    // 编译后的正则表达式
    final Pattern pattern;
    final String url;

    public AbstractMapping(String urlPattern) {
        this.url = urlPattern;
        this.pattern = buildPattern(urlPattern);
    }

    public boolean matches(String uri) {
        return pattern.matcher(uri).matches();
    }

    /**
     * @param urlPattern:
     * @return Pattern
     * @author: Orca121
     * @description: 编译为正则表达式
     * @createTime: 2024/5/7 11:59
     */
    Pattern buildPattern(String urlPattern) {
        StringBuilder sb = new StringBuilder(urlPattern.length() + 16);
        sb.append('^');
        for (int i = 0; i < urlPattern.length(); i++) {
            char ch = urlPattern.charAt(i);
            if (ch == '*') {
                sb.append(".*");
            } else if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9') {
                sb.append(ch);
            } else {
                sb.append('\\').append(ch);
            }
        }
        sb.append('$');
        return Pattern.compile(sb.toString());
    }

    @Override
    public int compareTo(AbstractMapping o) {
        // cmp是compare的缩写
        int cmp = this.priority() - o.priority();
        if (cmp == 0) {
            cmp = this.url.compareTo(o.url);
        }
        return cmp;
    }

    int priority() {
        if (this.url.equals("/")) {
            return Integer.MAX_VALUE;
        }
        if (this.url.startsWith("*")) {
            return Integer.MAX_VALUE - 1;
        }
        return 100000 - this.url.length();
    }
}
