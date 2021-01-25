package com.iuoip.dao;

import com.iuoip.domain.User;

import java.util.List;

public interface UserDao {
    /**
     * 根据学号和密码查数据库是否存在 登录
     * @param stuCode 学号
     * @param password 密码
     * @return
     */
    User findUser(String stuCode, String password);

    /**
     * 根据搜索条件查询宿舍管理员信息
     *
     * @param searchType 搜索类型
     * @param keyword    搜索关键字
     * @return 返回宿舍管理员信息
     */
    List<User> findManager(String searchType, String keyword);

    /**
     * 据搜索条件查询学生信息
     * @param searchType1 搜索宿舍楼
     * @param searchType2 搜索类型
     * @param keyword 搜索关键字
     * @return 返回学生信息
     */
    List<User> findStudent(String searchType1, String searchType2, String keyword);

    /**
     * 获取用户表中的最大学号
     *
     * @return
     */
    String findManagerMaxStuCode();

    /**
     * 保存用户基本信息
     *
     * @param user
     * @return
     */
    int saveManager(User user);

    /**
     * 根据用户id，获取用户信息
     *
     * @param id
     * @return
     */
    User findById(int id);

    /**
     * 更新用户信息
     *
     * @param user
     */
    void updateManager(User user);

    /**
     *保存学生信息
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
     *
     * @param stuCode
     * @return
     */
    User findByStuCode(String stuCode);

//    /**
//     * 根据用户id修改用户stuCode
//     * @param id
//     * @param stuCode
//     */
//    void updateByStudentId(int id, String stuCode);

    /**
     * 修改密码
     *
     * @param user
     */
    void updatePassword(User user);
}
