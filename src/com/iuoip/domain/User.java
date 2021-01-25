package com.iuoip.domain;

import java.util.List;

/**
 * User类
 */
public class User {
    private int id;  //用户id
    private String name;
    private String passWord;
    private String stuCode;
    private String dormCode; //宿舍号码
    private String sex;
    private String tel;
    private int dormBuildId; //学生所住宿舍楼id
    private int roleId; //角色id 0superM  1M  2S
    private  int createUserId; //创建新用户的用户id
    private int disabled;  //账号是否激活

    private DormBuild dormBuild; //学生所住宿舍楼
    private  List<DormBuild> dormBuilds; //宿舍管理员管理宿舍楼

    public User() {
    }

    public User(String name, String passWord, String stuCode, String dormCode, String sex, String tel, int dormBuildId, int roleId) {
        this.name = name;
        this.passWord = passWord;
        this.stuCode = stuCode;
        this.dormCode = dormCode;
        this.sex = sex;
        this.tel = tel;
        this.dormBuildId = dormBuildId;
        this.roleId = roleId;
    }

    public User(String name, String passWord, String sex, String tel, int dormBuildId, int roleId) {
        this.name = name;
        this.passWord = passWord;
        this.sex = sex;
        this.tel = tel;
        this.dormBuildId = dormBuildId;
        this.roleId = roleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getStuCode() {
        return stuCode;
    }

    public void setStuCode(String stuCode) {
        this.stuCode = stuCode;
    }

    public String getDormCode() {
        return dormCode;
    }

    public void setDormCode(String dormCode) {
        this.dormCode = dormCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getDormBuildId() {
        return dormBuildId;
    }

    public void setDormBuildId(int dormBuildId) {
        this.dormBuildId = dormBuildId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public DormBuild getDormBuild() {
        return dormBuild;
    }

    public void setDormBuild(DormBuild dormBuild) {
        this.dormBuild = dormBuild;
    }

    public List<DormBuild> getDormBuilds() {
        return dormBuilds;
    }

    public void setDormBuilds(List<DormBuild> dormBuilds) {
        this.dormBuilds = dormBuilds;
    }
}
