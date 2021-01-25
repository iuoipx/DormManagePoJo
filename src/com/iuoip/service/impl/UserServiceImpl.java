package com.iuoip.service.impl;

import com.iuoip.dao.DormBuildDao;
import com.iuoip.dao.UserDao;
import com.iuoip.dao.impl.DormBuildDaoImpl;
import com.iuoip.dao.impl.UserDaoImpl;
import com.iuoip.domain.DormBuild;
import com.iuoip.domain.User;
import com.iuoip.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();
    private DormBuildDao dormBuildDao = new DormBuildDaoImpl();


    @Override
    public User findUser(String stuCode, String password) {
        return userDao.findUser(stuCode, password);
    }


    @Override
    public List<User> findManager(String searchType, String keyword) {
        //获取宿舍管理员基本信息
        List<User> userList = userDao.findManager(searchType, keyword);

        //获取宿舍管理员管理的楼栋信息
        for (User user : userList) {
            List<DormBuild> dormBuildList = dormBuildDao.findByUserId(user.getId());
            user.setDormBuilds(dormBuildList);
        }

        return userList;
    }

    @Override
    public void saveManager(User user, String[] dormBuildIds) {
        //获取最大的学号（当前数据库最大学号加1）
        String managerMaxStuCode = userDao.findManagerMaxStuCode();
        //设置学号
        user.setStuCode(managerMaxStuCode);

        //保存用户信息分两步
        //1.保存用户(管理员)基本信息
        int userId = userDao.saveManager(user);

        //2.保存管理员管理的宿舍楼栋信息 中间表
        dormBuildDao.saveManagerAndBuild(userId, dormBuildIds);
    }

    /**
     * 根据用户id，获取用户信息
     *
     * @param id
     * @return
     */
    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    @Override
    public void updateManager(User user) {
        userDao.updateManager(user);
    }

    /**
     * 据搜索条件查询学生信息
     *
     * @param dormBuildId
     * @param searchType
     * @param keyword
     * @return
     */
    @Override
    public List<User> findStudent(String dormBuildId, String searchType, String keyword) {
        //获取学生的基本信息
        List<User> studentList = userDao.findStudent(dormBuildId, searchType, keyword);

        //获取学生住的楼栋信息
        for (User user : studentList) {
            DormBuild dormBuild = dormBuildDao.findByDormBuildId(user.getDormBuildId());
            user.setDormBuild(dormBuild);
        }
        return studentList;
    }

    /**
     * 保存学生信息
     *
     * @param user
     */
    @Override
    public void saveStudent(User user) {
        userDao.saveStudent(user);
    }

    /**
     * 更新学生信息
     *
     * @param user
     */
    @Override
    public void updateStudent(User user) {
        userDao.updateStudent(user);
    }

    /**
     * 根据学号查找学生
     *
     * @param stuCode
     * @return
     */
    @Override
    public User findByStuCode(String stuCode) {
        return userDao.findByStuCode(stuCode);
    }

    /**
     * 修改密码
     *
     * @param user
     */
    @Override
    public void updatePassword(User user) {
        userDao.updatePassword(user);
    }
}
