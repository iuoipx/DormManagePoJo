package com.iuoip.domain;

import java.sql.Date;

public class Record {
    private int id;
    private int studentId;
    private Date date;
    private String remark;
    private int disabled;
    private User user;

    public Record() {
    }

    public Record(int id, int studentId, Date date, String remark, int disabled) {
        this.id = id;
        this.studentId = studentId;
        this.date = date;
        this.remark = remark;
        this.disabled = disabled;
    }

    public Record(User user, Date date, String remark) {
        this.user = user;
        this.date = date;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
