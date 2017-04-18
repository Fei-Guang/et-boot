package com.web.model.etender;

public class Project_Query_User {
    private Integer queryprojectid;

    private String userid;

    private Boolean isacceptor;

    public Integer getQueryprojectid() {
        return queryprojectid;
    }

    public void setQueryprojectid(Integer queryprojectid) {
        this.queryprojectid = queryprojectid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public Boolean getIsacceptor() {
        return isacceptor;
    }

    public void setIsacceptor(Boolean isacceptor) {
        this.isacceptor = isacceptor;
    }
}