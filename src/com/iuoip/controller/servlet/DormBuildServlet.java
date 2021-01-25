package com.iuoip.controller.servlet;

import com.iuoip.domain.DormBuild;
import com.iuoip.domain.User;
import com.iuoip.service.DormBuildService;
import com.iuoip.service.impl.DormBuildServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/dormBuild.action")
public class DormBuildServlet extends HttpServlet {
    public DormBuildServlet() {
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        //获取url中的 action
        String action = req.getParameter("action");
        //获取url中的 id
        String id = req.getParameter("id");

        //获取宿舍楼信息
        DormBuildService dormBuildService = new DormBuildServiceImpl();
        List<DormBuild> dormBuildList = dormBuildService.findDormBuild();
        req.setAttribute("builds", dormBuildList);
        req.setAttribute("buildSelects", dormBuildList);
        if (action != null && action.equals("list")) {
            //获取前端传过来的搜索参数
            String ids = req.getParameter("id");


            if (ids != null && !ids.equals("")) {
                List<DormBuild> dormBuilds = new ArrayList<>();
                DormBuild byId = dormBuildService.findById(Integer.parseInt(ids));
                dormBuilds.add(byId);
                req.setAttribute("builds", dormBuilds);
            }

            //回写
            req.setAttribute("buildSelects", dormBuildList);
            req.setAttribute("id", id);
            req.setAttribute("mainRight", "dormBuildList.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("preAdd")) {
            //宿舍楼添加页面
            req.setAttribute("mainRight", "dormBuildAddOrUpdate.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("save")) {
            //添加 宿舍楼消息 页面
            String name = req.getParameter("name");
            String remark = req.getParameter("remark");

            if (null != id && id.equals("")) {//新增

                //填写的宿舍楼信息
                DormBuild dormBuild = new DormBuild(name, remark);
                dormBuild.setDisabled(0);

                //保存宿舍楼信息
                dormBuildService.saveBuild(dormBuild);

            } else { //修改
                DormBuild dormBuild = dormBuildService.findById(Integer.parseInt(id));
                dormBuild.setName(name);
                dormBuild.setRemark(remark);

                //更新宿舍楼基本信息
                dormBuildService.updateDormBuild(dormBuild);
            }

            //跳转到宿舍楼管理列表，查看修改过后所有的宿舍楼信息
            //重定向:请求链接断开时，不能再下一个servlet或jsp中获取保存在request中的参数
            resp.sendRedirect(getServletContext().getContextPath() + "/dormBuild.action?action=list");
        } else if (action != null && action.equals("preUpdate")) {
            //将要修改宿舍楼的信息回写到修改页面
            //根据宿舍楼id，获取宿舍楼信息
            DormBuild dormBuild = dormBuildService.findById(Integer.parseInt(id));

            req.setAttribute("build", dormBuild);
            req.setAttribute("mainRight", "dormBuildAddOrUpdate.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("deleteOrActive")) {
            //获取删除或激活标志
            String disabled = req.getParameter("disabled");

            //根据宿舍楼id，获取宿舍楼信息
            DormBuild dormBuild = dormBuildService.findById(Integer.parseInt(id));
            dormBuild.setDisabled(Integer.parseInt(disabled));

            //更新宿舍楼信息
            dormBuildService.updateDormBuild(dormBuild);

            //跳转到宿舍楼管理员页面查看删除过后的所有宿舍楼信息
            resp.sendRedirect(getServletContext().getContextPath() + "/dormBuild.action?action=list");
        }
    }
}
