package com.orcaswater.snowydog.engine.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.servlet
 * @className: LoginServlet
 * @author: Orca121
 * @description: TODO
 * @createTime: 2024-05-07 17:47
 * @version: 1.0
 */
@Deprecated
@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    // 极其简陋的数据库
    Map<String, String> users = Map.of( // user database
            "bob", "bob123", //
            "alice", "alice123", //
            "patrick", "patrick123",
            "root", "admin123" //
    );

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String expectedPassword = users.get(username.toLowerCase());
        if (expectedPassword == null || !expectedPassword.equals(password)) {
            // 登录失败
            PrintWriter pw = resp.getWriter();
            pw.write("""
                    <h1>Login Failed</h1>
                    <p>Invalid username or password.</p>
                    <p><a href="/">Try again</a></p>
                    """);
            pw.close();
        } else {
            // 登录成功，获取Session
            HttpSession session = req.getSession();
            // 将用户名放入Session:
            session.setAttribute("username", username);
            // 返回首页:
            resp.sendRedirect("/");
        }
    }
}
