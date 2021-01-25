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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/dormManager.action")
public class DormManagerServlet extends HttpServlet {
    public DormManagerServlet() {
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
            String searchType = req.getParameter("searchType");
            String keyword = req.getParameter("keyword");

            //查询宿舍管理员信息(包括基本信息和宿舍楼信息)
            List<User> userList = userService.findManager(searchType, keyword);

            req.setAttribute("searchType", searchType);
            req.setAttribute("keyword", keyword);
            req.setAttribute("users", userList);

            req.setAttribute("mainRight", "dormManagerList.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("preAdd")) {
            //宿舍管理员添加页面
            req.setAttribute("mainRight", "dormManagerAddOrUpdate.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("save")) {
            //添加 管理员信息和管理宿舍楼信息 页面
            String name = req.getParameter("name");
            String passWord = req.getParameter("passWord");
            String sex = req.getParameter("sex");
            String tel = req.getParameter("tel");
            String[] dormBuildIds = req.getParameterValues("dormBuildId");

            if (null != id && id.equals("")) {//新增

                //填写的用户信息
                User user = new User(name, passWord, sex, tel, 0, 1);
                user.setDisabled(0);

                //当前登录用户信息
                User user1 = (User) req.getSession().getAttribute("session_user");
                user.setCreateUserId(user1.getId());

                //保存用户管理员信息(包括用户基本信息和用户管理的宿舍楼信息)
                userService.saveManager(user, dormBuildIds);
            } else { //修改
                User user = userService.findById(Integer.parseInt(id));
                user.setName(name);
                user.setPassWord(passWord);
                user.setSex(sex);
                user.setTel(tel);

                //更新用户基本信息
                userService.updateManager(user);

                //删除当前宿舍管理员管理的楼栋信息 中间表
                dormBuildService.deleteByUserId(user.getId());
                //添加新增当前宿舍管理员管理的楼栋信息 中间表
                dormBuildService.saveManagerAndBuild(user.getId(), dormBuildIds);
            }
            //跳转到宿舍管理员列表，查看修改过后所有的宿舍管理员信息
            //重定向:请求链接断开时，不能再下一个servlet或jsp中获取保存在request中的参数
            resp.sendRedirect(getServletContext().getContextPath() + "/dormManager.action?action=list");
        } else if (action != null && action.equals("preUpdate")) {
            //将要修改用户的信息回写到修改页面

            //根据用户id，获取用户信息
            User user = userService.findById(Integer.parseInt(id));

            //根据宿舍管理员id获取宿舍管理员管理的楼栋信息
            List<DormBuild> dormBuilds = dormBuildService.findByUserId(user.getId());
            user.setDormBuilds(dormBuilds);

            //宿舍管理员管理的楼栋id
            List<Integer> dormBuildIds = new ArrayList<>();
            for (DormBuild dormBuild : dormBuilds) {
                dormBuildIds.add(dormBuild.getId());
            }

            req.setAttribute("userBuildids", dormBuildIds);
            req.setAttribute("user", user);
            req.setAttribute("mainRight", "dormManagerAddOrUpdate.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("deleteOrActive")) {
            //获取删除或激活标志
            String disabled = req.getParameter("disabled");

            //根据宿舍管理id，获取宿舍管理员信息
            User user = userService.findById(Integer.parseInt(id));
            user.setDisabled(Integer.parseInt(disabled));

            //更新用户信息
            userService.updateManager(user);

            //跳转到宿舍管理员页面查看删除过后的所有宿舍管理员
            resp.sendRedirect(getServletContext().getContextPath() + "/dormManager.action?action=list");
        }
    }
}
