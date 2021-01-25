package com.iuoip.dao;

import com.iuoip.domain.Record;

import java.util.List;

public interface RecordDao {
    /**
     * 查找缺勤信息,返回缺勤学生的id
     * @return
     */
    List<Record> findRecord(String startDate, String endDate, String dormBuildId, String searchType, String keyword);

    /**
     * 新增缺勤记录
     *
     * @param record
     */
    void saveRecord(Record record);

    /**
     * 根据缺勤id查找缺勤记录
     *
     * @param parseInt
     * @return
     */
    Record findById(int parseInt);

    /**
     * 更新缺勤记录
     *
     * @param record
     */
    void updateRecord(Record record);
}
