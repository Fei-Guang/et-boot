package com.web.dao.etender;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.web.model.etender.User;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(String userid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String userid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	User loadByEmail4Glodon(String email);

	List<String> loadEmails();
}