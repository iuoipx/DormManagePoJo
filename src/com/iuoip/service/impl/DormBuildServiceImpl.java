package com.iuoip.service.impl;

import com.iuoip.dao.DormBuildDao;
import com.iuoip.dao.impl.DormBuildDaoImpl;
import com.iuoip.domain.DormBuild;
import com.iuoip.service.DormBuildService;

import java.util.List;

public class DormBuildServiceImpl implements DormBuildService {
    private DormBuildDao dormBuildDao = new DormBuildDaoImpl();

    /**
     * 查找所有宿舍楼栋信息
     *
     * @return
     */
    @Override
    public List<DormBuild> findDormBuild() {
        return dormBuildDao.findDormBuild();
    }

    /**
     * 根据宿舍管理员id获取该宿舍管理员管理的楼栋信息
     *
     * @param id 管理员id
     * @return 该管理员管理的楼栋信息
     */
    @Override
    public List<DormBuild> findByUserId(int id) {
        return dormBuildDao.findByUserId(id);
    }

    /**
     * 删除当前宿舍管理员管理的楼栋信息 中间表
     *
     * @param id
     */
    @Override
    public void deleteByUserId(int id) {
        dormBuildDao.deleteByUserId(id);
    }

    /**
     * 添加新增当前宿舍管理员管理的楼栋信息 中间表
     *
     * @param id
     */
    @Override
    public void saveManagerAndBuild(int id, String[] dormBuildIds) {
        dormBuildDao.saveManagerAndBuild(id, dormBuildIds);
    }

    /**
     * 根据用户所在宿舍楼栋id查找楼栋信息
     *
     * @param dormBuildId
     * @return
     */
    @Override
    public DormBuild findById(int dormBuildId) {
        return dormBuildDao.findByDormBuildId(dormBuildId);
    }

    /**
     * 新增保存宿舍楼信息
     *
     * @param dormBuild
     */
    @Override
    public void saveBuild(DormBuild dormBuild) {
        dormBuildDao.saveBuild(dormBuild);
    }

    /**
     * 更新宿舍楼信息
     *
     * @param dormBuild
     */
    @Override
    public void updateDormBuild(DormBuild dormBuild) {
        dormBuildDao.updateDormBuild(dormBuild);
    }

}
