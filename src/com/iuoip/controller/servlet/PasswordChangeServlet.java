package com.iuoip.controller.servlet;

import com.iuoip.domain.User;
import com.iuoip.service.UserService;
import com.iuoip.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/password.action")
public class PasswordChangeServlet extends HttpServlet {
    public PasswordChangeServlet() {
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //post请求乱码
        req.setCharacterEncoding("utf-8");
        //获取url中的 action
        String action = req.getParameter("action");

        UserService userService = new UserServiceImpl();

        User user = (User) req.getSession().getAttribute("session_user");

        if (action != null && action.equals("preChange")) {

            req.setAttribute("mainRight", "passwordChange.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("ajaxOldPassWord")) {  //校验旧密码是否正确
            //获取url地址中的旧密码
            String oldPassWord = req.getParameter("oldPassWord");

            if (!oldPassWord.equals(user.getPassWord())) {
                resp.setCharacterEncoding("utf-8");
                PrintWriter printWriter = resp.getWriter();
                printWriter.print("输入的原密码错误，请重新输入");
            }
        } else if (action != null && action.equals("change")) {
            //获取前端传过来的参数
            String newPassword = req.getParameter("newPassword");
            user.setPassWord(newPassword);

            //修改密码
            userService.updatePassword(user);

            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }
    }
}
