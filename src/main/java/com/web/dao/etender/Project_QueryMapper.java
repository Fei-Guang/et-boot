package com.web.dao.etender;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.web.model.etender.Project_Query;

public interface Project_QueryMapper {
	int deleteByPrimaryKey(Integer queryprojectid);

	int insert(Project_Query record);

	int insertSelective(Project_Query record);

	Project_Query selectByPrimaryKey(Integer queryprojectid);

	int updateByPrimaryKeySelective(Project_Query record);

	int updateByPrimaryKey(Project_Query record);

	boolean projectNameExisted(String name);

	List<Project_Query> loadQueryProject(String userid);

	List<String> selectProjectNameByUserID(String userid);

	int deleteProjectByLogic(Integer queryprojectid);

	int countProjectNameByUserID(@Param("userid") String userid,
			@Param("name") String name);

	int updateProjectStatus2Inquiring(int queryprojectid);

	Project_Query loadLastTBQProject(@Param("userid") String userid,
			@Param("tbqprojectid") int tbqProjectID);
}