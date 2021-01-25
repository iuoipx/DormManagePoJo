package com.iuoip.service;

import com.iuoip.domain.User;

import java.util.List;

public interface UserService {

    User findUser(String stuCode, String password);

    /**
     * 根据搜索条件查询宿舍管理员信息
     * @param searchType 搜索类型
     * @param keyword 搜索关键字
     * @return 返回宿舍管理员信息
     */
    List<User> findManager(String searchType, String keyword);

    /**
     * 保存用户管理员信息(包括基本信息和管理的宿舍楼信息)
     * @param user 用户基本信息
     * @param dormBuildIds 管理员管理的宿舍楼信息
     */
    void saveManager(User user, String[] dormBuildIds);

    /**
     * 根据用户id，获取用户信息
     * @param id
     * @return
     */
    User findById(int id);

    /**
     * 更新管理员信息
     * @param user
     */
    void updateManager(User user);

    /**
     * 据搜索条件查询学生信息
     * @param dormBuildId
     * @param searchType
     * @param keyword
     * @return
     */
    List<User> findStudent(String dormBuildId, String searchType, String keyword);

    /**
     * 保存学生信息
     * @param user
     */
    void saveStudent(User user);

    /**
     * 更新学生信息
     * @param user
     */
    void updateStudent(User user);

    /**
     * 根据学号查找学生
     * @param stuCode
     * @return
     */
    User findByStuCode(String stuCode);

    /**
     * 修改密码
     * @param user
     */
    void updatePassword(User user);
}
