package com.web.model.etender;

import java.util.Date;

public class Subproject {
    private Integer subprojectid;

    private Integer queryprojectid;

    private Integer tbqsubprojectid;

    private Byte source;

    private String name;

    private Date endtime;

    private String timezone;

    private Byte status;

    private String remark;

    private String createby;

    private Date createtime;

    private String updateby;

    private Date updatetime;

    private Long logiccounter;

    private Boolean logicdelete;

    public Integer getSubprojectid() {
        return subprojectid;
    }

    public void setSubprojectid(Integer subprojectid) {
        this.subprojectid = subprojectid;
    }

    public Integer getQueryprojectid() {
        return queryprojectid;
    }

    public void setQueryprojectid(Integer queryprojectid) {
        this.queryprojectid = queryprojectid;
    }

    public Integer getTbqsubprojectid() {
        return tbqsubprojectid;
    }

    public void setTbqsubprojectid(Integer tbqsubprojectid) {
        this.tbqsubprojectid = tbqsubprojectid;
    }

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone == null ? null : timezone.trim();
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