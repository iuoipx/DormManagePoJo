package com.iuoip.service.impl;

import com.iuoip.dao.DormBuildDao;
import com.iuoip.dao.RecordDao;
import com.iuoip.dao.UserDao;
import com.iuoip.dao.impl.DormBuildDaoImpl;
import com.iuoip.dao.impl.RecordDaoImpl;
import com.iuoip.dao.impl.UserDaoImpl;
import com.iuoip.domain.DormBuild;
import com.iuoip.domain.Record;
import com.iuoip.domain.User;
import com.iuoip.service.RecordService;

import java.util.List;

public class RecordServiceImpl implements RecordService {
    private RecordDao recordDao = new RecordDaoImpl();
    private DormBuildDao dormBuildDao = new DormBuildDaoImpl();
    private UserDao userDao = new UserDaoImpl();

    /**
     * 查找缺勤信息
     *
     * @return
     */
    @Override
    public List<Record> findRecord(String startDate, String endDate, String dormBuildId, String searchType, String keyword) {
        List<Record> recordList = recordDao.findRecord(startDate, endDate, dormBuildId, searchType, keyword);

        //获取学生住的楼栋信息
        for (Record record : recordList) {
            DormBuild dormBuild = dormBuildDao.findByDormBuildId(record.getUser().getDormBuildId());
            record.getUser().setDormBuild(dormBuild);
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
        recordDao.saveRecord(record);
    }

    /**
     * 根据缺勤id查找缺勤记录
     *
     * @param id
     * @return
     */
    @Override
    public Record findById(int id) {
        Record record = recordDao.findById(id);
        User user = userDao.findById(record.getStudentId());
        record.setUser(user);
        return record;
    }

    /**
     * 更新缺勤记录
     *
     * @param record
     */
    @Override
    public void updateRecord(Record record) {
        recordDao.updateRecord(record);
    }
}
