package com.iuoip.service;

import com.iuoip.domain.DormBuild;

import java.util.List;

public interface DormBuildService {
    /**
     *查找所有的宿舍楼栋信息
     * @return
     */
    List<DormBuild> findDormBuild();

    /**
     * 根据宿舍管理员id获取宿舍管理员管理的楼栋信息
     * @param id
     * @return
     */
    List<DormBuild> findByUserId(int id);

    /**
     * 删除当前宿舍管理员管理的楼栋信息 中间表
     * @param id
     */
    void deleteByUserId(int id);

    /**
     *添加新增当前宿舍管理员管理的楼栋信息 中间表
     * @param id
     */
    void saveManagerAndBuild(int id, String[] dormBuildIds);

    /**
     * 根据用户所在宿舍楼栋id查找楼栋信息
     * @param dormBuildId
     * @return
     */
    DormBuild findById(int dormBuildId);

    /**
     * 新增保存宿舍楼信息
     * @param dormBuild
     */
    void saveBuild(DormBuild dormBuild);

    /**
     * 更新宿舍楼信息
     * @param dormBuild
     */
    void updateDormBuild(DormBuild dormBuild);
}
