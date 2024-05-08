package com.orcaswater.snowydog.engine.servlet;

import com.orcaswater.snowydog.utils.ClassPathUtils;
import com.orcaswater.snowydog.utils.DateUtils;
import com.orcaswater.snowydog.utils.HtmlUtils;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.servlet
 * @className: DefaultServlet
 * @author: Orca121
 * @description: Default servlet 匹配 "/" 同时用来浏览文件
 * @createTime: 2024-05-08 17:02
 * @version: 1.0
 */

public class DefaultServlet extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(getClass());
    String indexTemplate;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.indexTemplate = ClassPathUtils.readString("/index.html");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        logger.info("list file or directory: {}", uri);

        // 安全校验
        if (!uri.startsWith("/")) {
            // 不安全的uri:
            logger.debug("skip process insecure uri: {}", uri);
            resp.sendError(404, "Not Found");
            return;
        }
        if (uri.equals("/WEB-INF") || uri.startsWith("/WEB-INF/")) {
            // 禁止使用 WEB-INF:
            logger.debug("prevent access uri: {}", uri);
            resp.sendError(403, "Forbidden");
            return;
        }
        if (uri.indexOf("/../") > 0) {
            // 禁止使用 /abc/../../xyz:
            logger.debug("prevent access insecure uri: {}", uri);
            resp.sendError(404, "Not Found");
            return;
        }

        // 获得真实路径
        String realPath = req.getServletContext().getRealPath(uri);
        Path path = Paths.get(realPath);
        logger.debug("try access path: {}", path);

        if (uri.endsWith("/")) {
            if (Files.isDirectory(path)) {
                // 路径是目录
                // 获取目录下的路径:
                List<Path> files = Files.list(path).collect(Collectors.toList());
                Collections.sort(files, (f1, f2) -> {
                    var s1 = f1.toString();
                    var s2 = f2.toString();
                    return s1.compareToIgnoreCase(s2);
                });
                StringBuilder sb = new StringBuilder(4096);
                if (!uri.equals("/")) {
                    sb.append(tr(path.getParent(), -1, ".."));
                }

                for (Path file : files) {
                    String name = file.getFileName().toString();
                    long size = -1;
                    if (Files.isDirectory(file)) {
                        name = name + "/";
                    } else if (Files.isRegularFile(file)) {
                        size = Files.size(file);
                    }
                    sb.append(tr(file, size, name));
                }
                String trs = sb.toString();
                String html = this.indexTemplate.replace("${URI}", HtmlUtils.encodeHtml(uri)) //
                        .replace("${SERVER}", getServletContext().getServerInfo()) //
                        .replace("${TRS}", trs);
                PrintWriter pw = resp.getWriter();
                pw.write(html);
                pw.flush();
                return;
            }
        } else if (Files.isReadable(path) && Files.isReadable(path)) {
            // 路径非目录，且可读
            logger.debug("read file: {}", path);
            resp.setContentType(getServletContext().getMimeType(uri));
            ServletOutputStream output = resp.getOutputStream();
            try (InputStream input = new BufferedInputStream(new FileInputStream(path.toFile()))) {
                // transferTo 方法将 input 中的数据传输到 output 中。
                // 在这种情况下，它将文件的内容从 InputStream 传输到 ServletOutputStream，从而发送到客户端。
                input.transferTo(output);
            }
            output.flush();
            return;
        }
        resp.sendError(404, "Not Found");
    }
    /**
     * @param file: 一个 Path 对象，表示要显示信息的文件的路径。
     * @param size: 一个 long 型变量，表示文件的大小（通常是以字节为单位）。
     * @param name: 一个 String，表示文件的名称。
     * @return String: 返回一个格式化的HTML字符串，该字符串代表一个表格行，包含三个单元格（<td>）
     * @author: orca121
     * @description: 生成一个HTML表格行（table row）
     * @createTime: 2024/5/8 17:23
     */
    static String tr(Path file, long size, String name) throws IOException {
        /**
            第一个单元格包含一个超链接，链接的文本是文件的名称，
         该名称通过 HtmlUtils.encodeHtml(name) 进行HTML编码以避免XSS攻击，
         并且链接的href属性设置为文件的名称（可能是用于文件下载的URL）。
            第二个单元格显示文件的大小，使用之前定义的 size 方法来格式化文件大小，
         使其显示为“B”, “KB”, “MB”, 或者 “GB”。
            第三个单元格显示文件的最后修改时间，
         使用 Files.getLastModifiedTime(file).toMillis() 获取最后修改时间的毫秒值，
         然后通过 DateUtils.formatDateTimeGMT 方法将其格式化为GMT时间的字符串。
         */
        return "<tr><td><a href=\"" + name + "\">" +
                HtmlUtils.encodeHtml(name) + "</a></td><td>" +
                size(size) + "</td><td>" +
                DateUtils.formatDateTimeGMT(Files.getLastModifiedTime(file).toMillis()) +
                "</td>";
    }

    /**
     * @param size:一个 long 型变量，表示文件的大小（通常是以字节为单位）。
     * @return String: 表示文件大小的字符串
     * @author: orca121
     * @description: 将一个表示文件大小的长整型（long）数值转换成更易于阅读的格式
     * 每种情况都会使用String.format方法来格式化字符串，保留三位小数，并加上相应的单位（GB、MB、KB或B）。
     * @createTime: 2024/5/8 17:14
     */
    static String size(long size) {
        if (size >= 0) {
            // 如果大于1GB
            if (size > 1024 * 1024 * 1024) {
                return String.format("%.3f GB", size / (1024 * 1024 * 1024.0));
            }
            // 如果大于1MB
            if (size > 1024 * 1024) {
                return String.format("%.3f MB", size / (1024 * 1024.0));
            }
            // 如果大于KB
            if (size > 1024) {
                return String.format("%.3f KB", size / 1024.0);
            }
            return size + " B";
        }
        return "";
    }
}
