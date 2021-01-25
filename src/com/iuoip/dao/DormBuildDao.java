package com.iuoip.dao;

import com.iuoip.domain.DormBuild;

import java.util.List;

public interface DormBuildDao {

    /**
     * 根据用户(管理员)id查找其管理的宿舍楼信息
     *
     * @param id 管理员id
     * @return 该管理员管理的楼栋信息
     */
    List<DormBuild> findByUserId(int id);

    /**
     * 查找所有宿舍楼栋信息
     *
     * @return
     */
    List<DormBuild> findDormBuild();

    /**
     *保存宿舍管理员和宿舍楼的中间表
     * @param userId
     * @param dormBuildIds
     */
    void saveManagerAndBuild(int userId, String[] dormBuildIds);

    /**
     * 删除当前宿舍管理员管理的楼栋信息 中间表
     * @param id
     */
    void deleteByUserId(int id);

    /**
     * 根据学生所在宿舍楼id去中间表查找学生所在的宿舍楼信息
     * @param id
     * @return
     */
    DormBuild findByDormBuildId(int id);

    /**
     * 新增保存宿舍楼信息
     *
     * @param dormBuild
     */
    void saveBuild(DormBuild dormBuild);

    /**
     * 更新宿舍楼信息
     *
     * @param dormBuild
     */
    void updateDormBuild(DormBuild dormBuild);
}
