package com.web.dao.etender;

import com.web.model.etender.Quoted_Element4excel;

public interface Quoted_Element4excelMapper {
    int deleteByPrimaryKey(Integer elementid);

    int insert(Quoted_Element4excel record);

    int insertSelective(Quoted_Element4excel record);

    Quoted_Element4excel selectByPrimaryKey(Integer elementid);

    int updateByPrimaryKeySelective(Quoted_Element4excel record);

    int updateByPrimaryKey(Quoted_Element4excel record);
}