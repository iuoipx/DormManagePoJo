package com.iuoip.domain;

import java.util.List;

public class ManageDormBuild {
    private int id;
    private int userId;
    private List dormBuildId;

    public ManageDormBuild() {
    }

    public ManageDormBuild(int id, int userId, List dormBuildId) {
        this.id = id;
        this.userId = userId;
        this.dormBuildId = dormBuildId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List getDormBuildId() {
        return dormBuildId;
    }

    public void setDormBuildId(List dormBuildId) {
        this.dormBuildId = dormBuildId;
    }
}
