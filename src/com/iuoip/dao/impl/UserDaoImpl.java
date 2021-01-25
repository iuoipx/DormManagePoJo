package com.iuoip.dao.impl;

import com.iuoip.dao.UserDao;
import com.iuoip.domain.User;
import com.iuoip.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    /**
     * 根据学号和密码查询数据库
     *
     * @param stuCode  学号
     * @param password 密码
     * @return 用户信息
     */
    @Override
    public User findUser(String stuCode, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "select * from tb_user where stu_code = ? and passWord = ? and disabled = 0";
            ps = conn.prepareStatement(sql);

            ps.setString(1, stuCode);
            ps.setString(2, password);

            rs = ps.executeQuery();

            while (rs.next()) {
                user = selectUser(rs);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }

        return user;
    }

    /**
     * 根据搜索条件查询宿舍管理员信息
     *
     * @param searchType 搜索类型
     * @param keyword    搜索关键字
     * @return 返回宿舍管理员信息
     */
    @Override
    public List<User> findManager(String searchType, String keyword) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();

        try {
            conn = DBUtils.getConnection();

            String tempSql = "";
            //说明用户点击搜索按钮进行查询
            if (keyword != null && !keyword.equals("")) {
                if (searchType.equals("name")) {
                    tempSql = " and name like '%" + keyword + "%'";
                } else if (searchType.equals("sex")) {
                    tempSql = " and sex = '" + keyword + "'";
                } else if (searchType.equals("tel")) {
                    tempSql = " and tel = " + keyword;
                }
            }
            String sql = "select * from tb_user where role_id = 1" + tempSql;
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                User user = selectUser(rs);

                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return userList;
    }

    /**
     * 据搜索条件查询学生信息
     *
     * @param searchType1 搜索宿舍楼
     * @param searchType2 搜索类型
     * @param keyword     搜索关键字
     * @return 返回学生信息
     */
    @Override
    public List<User> findStudent(String searchType1, String searchType2, String keyword) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();

        try {
            conn = DBUtils.getConnection();

            String tempSql = "";
            //表示用户进行楼栋选择
            if (searchType1 != null && !searchType1.equals("")) {
                if (searchType1.equals("1")) {
                    tempSql += " and dormBuildId = 1";
                } else if (searchType1.equals("2")) {
                    tempSql += " and dormBuildId = 2";
                } else if (searchType1.equals("3")) {
                    tempSql += " and dormBuildId = 3";
                } else if (searchType1.equals("4")) {
                    tempSql += " and dormBuildId = 4";
                }
            }
            //说明用户点击搜索按钮进行查询
            if (keyword != null && !keyword.equals("")) {
                if (searchType2.equals("name")) {
                    tempSql += " and name like '%" + keyword + "%'";
                } else if (searchType2.equals("stuCode")) {
                    tempSql += " and stu_code = '" + keyword + "'";
                } else if (searchType2.equals("dormCode")) {
                    tempSql += " and dorm_Code = '" + keyword + "'";
                } else if (searchType2.equals("sex")) {
                    tempSql += " and sex = '" + keyword + "'";
                } else if (searchType2.equals("tel")) {
                    tempSql += " and tel = " + keyword;
                }
            }

            String sql = "select * from tb_user where role_id = 2" + tempSql;
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                User user = selectUser(rs);

                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return userList;
    }

    /**
     * 返回最大学号+1
     *
     * @return
     */
    @Override
    public String findManagerMaxStuCode() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String maxStuCode = "";
        try {
            conn = DBUtils.getConnection();

            String sql = "select MAX(stu_code) as maxNo from tb_user";
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                String maxNo = rs.getString("maxNo");
                maxStuCode = "00" + (Integer.parseInt(maxNo) + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return maxStuCode;
    }

    /**
     * 保存用户基本信息
     *
     * @param user 用户基本信息
     * @return 用户id
     */

    @Override
    public int saveManager(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "insert into tb_user (name,passWord,stu_code,sex,tel,role_id,create_user_id,disabled) " +
                    "value (?,?,?,?,?,?,?,?);";
            //Statement.RETURN_GENERATED_KEYS 指定返回生成的是注解
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, user.getName());
            ps.setString(2, user.getPassWord());
            ps.setString(3, user.getStuCode());
            ps.setString(4, user.getSex());
            ps.setString(5, user.getTel());
            ps.setInt(6, user.getRoleId());
            ps.setInt(7, user.getCreateUserId());
            ps.setInt(8, user.getDisabled());

            ps.executeUpdate();

            //获取执行sql后的主键值
            rs = ps.getGeneratedKeys();

            rs.next();

            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return 0;
    }

    /**
     * 根据用户id，获取用户信息
     *
     * @param id
     * @return
     */
    @Override
    public User findById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = new User();

        try {
            conn = DBUtils.getConnection();

            String sql = "select * from tb_user where id = ?;";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            rs = ps.executeQuery();

            while (rs.next()) {
                user = selectUser(rs);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return user;
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    @Override
    public void updateManager(User user) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "update tb_user set name = ?,passWord = ? ,sex = ?,Tel = ?," +
                    "disabled = ? where id = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getPassWord());
            ps.setString(3, user.getSex());
            ps.setString(4, user.getTel());
            ps.setInt(5, user.getDisabled());
            ps.setInt(6, user.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(ps, conn);
        }
    }

    /**
     * 保存学生信息
     *
     * @param user
     */
    @Override
    public void saveStudent(User user) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "insert into tb_user (name,passWord,stu_code,dorm_Code,sex,tel,dormBuildId,role_id,create_user_id,disabled) " +
                    "value (?,?,?,?,?,?,?,?,?,?);";

            ps = conn.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getPassWord());
            ps.setString(3, user.getStuCode());
            ps.setString(4, user.getDormCode());
            ps.setString(5, user.getSex());
            ps.setString(6, user.getTel());
            ps.setInt(7, user.getDormBuildId());
            ps.setInt(8, user.getRoleId());
            ps.setInt(9, user.getCreateUserId());
            ps.setInt(10, user.getDisabled());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(ps, conn);
        }
    }

    /**
     * 更新学生信息
     *
     * @param user
     */
    @Override
    public void updateStudent(User user) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "update tb_user set name = ?,passWord = ?,stu_code = ?,dorm_Code = ?,sex = ?,Tel = ?,dormBuildId = ?," +
                    "disabled = ? where id = ?";

            ps = conn.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getPassWord());
            ps.setString(3, user.getStuCode());
            ps.setString(4, user.getDormCode());
            ps.setString(5, user.getSex());
            ps.setString(6, user.getTel());
            ps.setInt(7, user.getDormBuildId());
            ps.setInt(8, user.getDisabled());
            ps.setInt(9, user.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(ps, conn);
        }
    }

    /**
     * 根据学号查找学生
     *
     * @param stuCode
     * @return
     */
    @Override
    public User findByStuCode(String stuCode) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = new User();

        try {
            conn = DBUtils.getConnection();

            String sql = "select * from tb_user where stu_code = ?;";
            ps = conn.prepareStatement(sql);

            ps.setString(1, stuCode);

            rs = ps.executeQuery();

            while (rs.next()) {
                user = selectUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return user;
    }

//    /**
//     * 根据用户id修改用户stuCode
//     *
//     * @param id
//     * @param stuCode
//     */
//    @Override
//    public void updateByStudentId(int id, String stuCode) {
//        Connection conn = null;
//        PreparedStatement ps = null;
//
//        try {
//            conn = DBUtils.getConnection();
//
//            String sql = "update tb_user set stu_code = ? where id = ?";
//
//            ps = conn.prepareStatement(sql);
//
//            ps.setString(1, stuCode);
//            ps.setInt(2, id);
//
//            ps.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            DBUtils.close(ps, conn);
//        }
//    }

    /**
     * 修改密码
     *
     * @param user
     */
    @Override
    public void updatePassword(User user) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "update tb_user set passWord = ? where stu_code = ?";

            ps = conn.prepareStatement(sql);

            ps.setString(1, user.getPassWord());
            ps.setString(2, user.getStuCode());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(ps, conn);
        }
    }

    /**
     * 从返回结果集中获取用户信息
     *
     * @param rs 返回结果集
     * @return user
     */
    private User selectUser(ResultSet rs) {
        User user = new User();
        try {
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPassWord(rs.getString("passWord"));
            user.setStuCode(rs.getString("stu_code"));
            user.setDormCode(rs.getString("dorm_Code"));
            user.setSex(rs.getString("sex"));
            user.setTel(rs.getString("tel"));
            user.setDormBuildId(rs.getInt("dormBuildId"));
            user.setRoleId(rs.getInt("role_id"));
            user.setCreateUserId(rs.getInt("create_user_id"));
            user.setDisabled(rs.getInt("disabled"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }


}
