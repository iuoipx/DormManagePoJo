package com.iuoip.controller.servlet;

import com.iuoip.domain.DormBuild;
import com.iuoip.domain.User;
import com.iuoip.service.DormBuildService;
import com.iuoip.service.UserService;
import com.iuoip.service.impl.DormBuildServiceImpl;
import com.iuoip.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/student.action")
public class StudentServlet extends HttpServlet {
    public StudentServlet() {
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        //获取url中的 action
        String action = req.getParameter("action");
        //获取url中的 id
        String id = req.getParameter("id");

        //获取用户基本信息
        UserService userService = new UserServiceImpl();
        //获取宿舍楼信息
        DormBuildService dormBuildService = new DormBuildServiceImpl();
        List<DormBuild> dormBuildList = dormBuildService.findDormBuild();
        req.setAttribute("builds", dormBuildList);

        if (action != null && action.equals("list")) {
            //获取前端传过来的搜索参数
            String dormBuildId = req.getParameter("dormBuildId");
            String searchType = req.getParameter("searchType");
            String keyword = req.getParameter("keyword");

            //查询学生信息
            List<User> studentList = userService.findStudent(dormBuildId, searchType, keyword);

            req.setAttribute("dormBuildId", dormBuildId);
            req.setAttribute("searchType", searchType);
            req.setAttribute("keyword", keyword);
            req.setAttribute("students", studentList);

            req.setAttribute("mainRight", "studentList.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("preAdd")) {
            //学生添加页面
            req.setAttribute("mainRight", "studentAddOrUpdate.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("save")) {
            //添加 学生消息 页面
            String stuCode = req.getParameter("stuCode");
            String name = req.getParameter("name");
            String sex = req.getParameter("sex");
            String tel = req.getParameter("tel");
            String passWord = req.getParameter("passWord");
            String dormBuildId = req.getParameter("dormBuildId");
            String dormCode = req.getParameter("dormCode");

            if (null != id && id.equals("")) {//新增

                //填写的用户信息
                User user = new User(name, passWord, stuCode, dormCode, sex, tel, Integer.parseInt(dormBuildId), 2);
                user.setDisabled(0);

                //当前登录用户信息
                User user1 = (User) req.getSession().getAttribute("session_user");
                user.setCreateUserId(user1.getId());

                //保存用户管理员信息(包括用户基本信息和用户管理的宿舍楼信息)
                userService.saveStudent(user);

            } else { //修改
                User user = userService.findById(Integer.parseInt(id));
                user.setName(name);
                user.setPassWord(passWord);
                user.setStuCode(stuCode);
                user.setDormCode(dormCode);
                user.setSex(sex);
                user.setTel(tel);
                user.setDormBuildId(Integer.parseInt(dormBuildId));

                //更新用户基本信息
                userService.updateStudent(user);
            }

            //跳转到学生管理列表，查看修改过后所有的学生信息
            //重定向:请求链接断开时，不能再下一个servlet或jsp中获取保存在request中的参数
            resp.sendRedirect(getServletContext().getContextPath() + "/student.action?action=list");
        }else if (action != null && action.equals("preUpdate")) {
            //将要修改用户的信息回写到修改页面
            //根据用户id，获取用户信息
            User user = userService.findById(Integer.parseInt(id));

            req.setAttribute("userUpdate", user);
            req.setAttribute("mainRight", "studentAddOrUpdate.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("deleteOrActive")) {
            //获取删除或激活标志
            String disabled = req.getParameter("disabled");

            //根据学生id，获取学生信息
            User user = userService.findById(Integer.parseInt(id));
            user.setDisabled(Integer.parseInt(disabled));

            //更新用户信息
            userService.updateStudent(user);

            //跳转到学生管理员页面查看删除过后的所有学生信息
            resp.sendRedirect(getServletContext().getContextPath() + "/student.action?action=list");
        }
    }
}
