package com.web.model.etender;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

public class Project_Query {
    private Integer queryprojectid;

    private String userid;

    private String name;

    private String code;

    private String type;

    private String supervisor;

    private String floorarea;

    private Integer tbqprojectid;

    private String password;

    private Float totalprice;

    private Byte accuracy;

    private Byte status;

    private String remark;

    private String createby;

    private Date createtime;

    private String updateby;

    private Date updatetime;

    private Long logiccounter;

    private Boolean logicdelete;

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

    @NotEmpty(message = "Project names cannot be null.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor == null ? null : supervisor.trim();
    }

    public String getFloorarea() {
        return floorarea;
    }

    public void setFloorarea(String floorarea) {
        this.floorarea = floorarea == null ? null : floorarea.trim();
    }

    public Integer getTbqprojectid() {
        return tbqprojectid;
    }

    public void setTbqprojectid(Integer tbqprojectid) {
        this.tbqprojectid = tbqprojectid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Float getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Float totalprice) {
        this.totalprice = totalprice;
    }

    public Byte getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Byte accuracy) {
        this.accuracy = accuracy;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby == null ? null : createby.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getUpdateby() {
        return updateby;
    }

    public void setUpdateby(String updateby) {
        this.updateby = updateby == null ? null : updateby.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Long getLogiccounter() {
        return logiccounter;
    }

    public void setLogiccounter(Long logiccounter) {
        this.logiccounter = logiccounter;
    }

    public Boolean getLogicdelete() {
        return logicdelete;
    }

    public void setLogicdelete(Boolean logicdelete) {
        this.logicdelete = logicdelete;
    }
}