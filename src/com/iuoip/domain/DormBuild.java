package com.iuoip.domain;

public class DormBuild {
    private int id; //宿舍楼id
    private String name; //名称
    private String remark; //备注
    private int disabled; //是否激活 0/1 0指可用

    public DormBuild() {
    }

    public DormBuild(int id, String name, String remark, int disabled) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.disabled = disabled;
    }

    public DormBuild(String name, String remark) {
        this.name = name;
        this.remark = remark;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }
}
