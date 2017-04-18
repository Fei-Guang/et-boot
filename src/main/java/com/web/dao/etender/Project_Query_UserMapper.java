package com.web.dao.etender;

import com.web.model.etender.Project_Query_User;

public interface Project_Query_UserMapper {
    int deleteByPrimaryKey(Integer queryprojectid);

    int insert(Project_Query_User record);

    int insertSelective(Project_Query_User record);

    Project_Query_User selectByPrimaryKey(Integer queryprojectid);

    int updateByPrimaryKeySelective(Project_Query_User record);

    int updateByPrimaryKey(Project_Query_User record);
}