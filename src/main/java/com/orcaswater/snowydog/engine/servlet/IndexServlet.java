package com.orcaswater.snowydog.engine.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.servlet
 * @className: IndexServlet
 * @author: Orca121
 * @description: a demo servlet
 * @createTime: 2024-05-07 12:02
 * @version: 1.0
 */
@Deprecated
@WebServlet(urlPatterns = "/")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取session
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String html;
        if (username == null) {
            //没找到Session
            html = """
                    <h1>Index Page</h1>
                    <form method="post" action="/login">
                        <legend>Please Login</legend>
                        <p>User Name: <input type="text" name="username"></p>
                        <p>Password: <input type="password" name="password"></p>
                        <p><button type="submit">Login</button></p>
                    </form>
                    """;
        } else {
            //找到Session
            html = """
                    <h1>Index Page</h1>
                    <p>Welcome, {username}!</p>
                    <p><a href="/logout">Logout</a></p>
                    """.replace("{username}", username);
        }
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();
        pw.write(html);
        pw.close();
    }
}
