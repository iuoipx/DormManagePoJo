package com.iuoip.dao.impl;

import com.iuoip.dao.RecordDao;
import com.iuoip.domain.Record;
import com.iuoip.domain.User;
import com.iuoip.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RecordDaoImpl implements RecordDao {

    /**
     * 查找缺勤信息
     *
     * @return
     */
    @Override
    public List<Record> findRecord(String startDate, String endDate, String dormBuildId, String searchType, String keyword) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Record> recordList = new ArrayList<>();
        try {
            conn = DBUtils.getConnection();

            String tempSql = "";

            //表示用户进行日期筛选
            if (startDate != null && !startDate.equals("")) {
                if (endDate != null && !endDate.equals("")) {
                    tempSql += " and date between '" + startDate + "' And '" + endDate + "'";
                }
            }

            //表示用户进行楼栋选择
            if (dormBuildId != null && !dormBuildId.equals("")) {
                if (dormBuildId.equals("1")) {
                    tempSql += " and dormBuildId = 1";
                } else if (dormBuildId.equals("2")) {
                    tempSql += " and dormBuildId = 2";
                } else if (dormBuildId.equals("3")) {
                    tempSql += " and dormBuildId = 3";
                } else if (dormBuildId.equals("4")) {
                    tempSql += " and dormBuildId = 4";
                }
            }


            //说明用户点击搜索按钮进行查询
            if (keyword != null && !keyword.equals("")) {
                if (searchType.equals("name")) {
                    tempSql += " and tb_user.name like '%" + keyword + "%'";
                } else if (searchType.equals("stuCode")) {
                    tempSql += " and tb_user.stu_code = '" + keyword + "'";
                } else if (searchType.equals("dormCode")) {
                    tempSql += " and tb_user.dorm_Code = '" + keyword + "'";
                } else if (searchType.equals("sex")) {
                    tempSql += " and tb_user.sex = '" + keyword + "'";
                }
            }

            String sql = "select tb_user.*,tb_record.* from tb_record left join tb_user " +
                    "on tb_record.student_id = tb_user.id where tb_record.id>0 " + tempSql;
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                Record record = new Record();
                record.setId(rs.getInt("tb_record.id"));
                record.setStudentId(rs.getInt("student_id"));
                record.setDate(rs.getDate("date"));
                record.setRemark(rs.getString("remark"));
                record.setDisabled(rs.getInt("tb_record.disabled"));

                User user = new User();
                user.setId(rs.getInt("tb_user.id"));
                user.setName(rs.getString("name"));
                user.setPassWord(rs.getString("passWord"));
                user.setStuCode(rs.getString("stu_code"));
                user.setDormCode(rs.getString("dorm_Code"));
                user.setSex(rs.getString("sex"));
                user.setTel(rs.getString("tel"));
                user.setDormBuildId(rs.getInt("dormBuildId"));
                user.setRoleId(rs.getInt("role_id"));
                user.setCreateUserId(rs.getInt("create_user_id"));
                user.setDisabled(rs.getInt("tb_record.disabled"));

                record.setUser(user);
                recordList.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }

        return recordList;
    }

    /**
     * 新增缺勤记录
     *
     * @param record
     */
    @Override
    public void saveRecord(Record record) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "insert into tb_record(student_id,date,remark,disabled) value(?,?,?,?)";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, record.getUser().getId());
            ps.setDate(2, record.getDate());
            ps.setString(3, record.getRemark());
            ps.setInt(4, record.getDisabled());

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtils.close(ps, conn);
        }
    }

    /**
     * 根据缺勤id查找缺勤记录
     *
     * @param id
     * @return
     */
    @Override
    public Record findById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Record record = new Record();

        try {
            conn = DBUtils.getConnection();

            String sql = "select * from tb_record where id = ?";
            ps = conn.prepareStatement(sql);

            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                record.setId(rs.getInt("id"));
                record.setStudentId(rs.getInt("student_id"));
                record.setDate(rs.getDate("date"));
                record.setRemark(rs.getString("remark"));
                record.setDisabled(rs.getInt("disabled"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(rs, ps, conn);
        }
        return record;
    }

    /**
     * 更新缺勤记录
     *
     * @param record
     */
    @Override
    public void updateRecord(Record record) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtils.getConnection();

            String sql = "update tb_record set student_id = ?, date = ?,remark = ?,disabled = ? where id = ?";

            ps = conn.prepareStatement(sql);

            ps.setInt(1, record.getUser().getId());
            ps.setDate(2, record.getDate());
            ps.setString(3, record.getRemark());
            ps.setInt(4, record.getDisabled());
            ps.setInt(5, record.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(ps, conn);
        }
    }
}
