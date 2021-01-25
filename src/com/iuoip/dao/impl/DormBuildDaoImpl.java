package com.iuoip.dao.impl;

import com.iuoip.dao.DormBuildDao;
import com.iuoip.domain.DormBuild;
import com.iuoip.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DormBuildDaoImpl implements DormBuildDao {
    /**
     * 根据用户(管理员)id查找其管理的宿舍楼信息
     *
     * @param id 管理员id
     * @return 该管理员管理的楼栋信息
     */
    @Override
    public List<DormBuild> findByUserId(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DormBuild> dormBuildList = new ArrayList<>();

        try {
            conn = DBUtils.getConnection();

            String sql = "select tb_dormbuild.* from tb_manage_dormbuild left join tb_dormbuild " +
                    "on tb_dormbuild.id = tb_manage_dormbuild.dormBuild_id " +
                    "where user_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            while (rs.next()) {
                DormBuild dormBuild = new DormBuild();
                dormBuild.setId(rs.getInt("id"));
                dormBuild.setName(rs.getString("name"));
                dormBuild.setRemark(rs.getString("remark"));
                dormBuild.setDisabled(rs.getInt("disabled"));
                dormBuildList.add(dormBuild);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return dormBuildList;
    }

    /**
     * 查找所有宿舍楼栋信息
     *
     * @return
     */
    @Override
    public List<DormBuild> findDormBuild() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DormBuild> dormBuildList = new ArrayList<>();

        try {
            conn = DBUtils.getConnection();

            String sql = "select * from tb_dormbuild";
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                DormBuild dormBuild = new DormBuild();
                dormBuild.setId(rs.getInt("id"));
                dormBuild.setName(rs.getString("name"));
                dormBuild.setRemark(rs.getString("remark"));
                dormBuild.setDisabled(rs.getInt("disabled"));
                dormBuildList.add(dormBuild);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return dormBuildList;
    }

    /**
     * 保存宿舍管理员和宿舍楼的中间表
     *
     * @param userId       用户id
     * @param dormBuildIds 宿舍楼id
     */
    @Override
    public void saveManagerAndBuild(int userId, String[] dormBuildIds) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "insert  into tb_manage_dormbuild(user_id,dormBuild_id) value (?,?)";
            ps = conn.prepareStatement(sql);

            for (String s : dormBuildIds) {
                ps.setInt(1, userId);
                ps.setInt(2, Integer.parseInt(s));

                //将sql语句添加到批处理
                ps.addBatch();
            }

            //执行批处理
            ps.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(ps, conn);
        }
    }

    /**
     * 删除当前宿舍管理员管理的楼栋信息 中间表
     *
     * @param id
     */
    @Override
    public void deleteByUserId(int id) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "delete from tb_manage_dormbuild where user_id = ?";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            //执行批处理
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(ps, conn);
        }
    }

    /**
     * 根据学生所在宿舍楼id去中间表查找学生所在的宿舍楼信息
     *
     * @param id
     * @return
     */
    @Override
    public DormBuild findByDormBuildId(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DormBuild dormBuild = new DormBuild();

        try {
            conn = DBUtils.getConnection();

            String sql = "select * from tb_dormbuild where id = ?";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                dormBuild.setId(rs.getInt("id"));
                dormBuild.setName(rs.getString("name"));
                dormBuild.setRemark(rs.getString("remark"));
                dormBuild.setDisabled(rs.getInt("disabled"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return dormBuild;
    }

    /**
     * 新增保存宿舍楼信息
     *
     * @param dormBuild
     */
    @Override
    public void saveBuild(DormBuild dormBuild) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "insert  into tb_dormbuild(name,remark,disabled) value (?,?,?)";
            ps = conn.prepareStatement(sql);

            ps.setString(1, dormBuild.getName());
            ps.setString(2, dormBuild.getRemark());
            ps.setInt(3, dormBuild.getDisabled());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(ps, conn);
        }
    }

    /**
     * 更新宿舍楼信息
     *
     * @param dormBuild
     */
    @Override
    public void updateDormBuild(DormBuild dormBuild) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "update tb_dormbuild set name = ?,remark = ?,disabled = ? where id = ?";

            ps = conn.prepareStatement(sql);

            ps.setString(1, dormBuild.getName());
            ps.setString(2, dormBuild.getRemark());
            ps.setInt(3,dormBuild.getDisabled());
            ps.setInt(4, dormBuild.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(ps, conn);
        }
    }
}
