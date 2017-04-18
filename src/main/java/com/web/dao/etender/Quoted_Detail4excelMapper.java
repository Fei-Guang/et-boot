package com.web.dao.etender;

import com.web.model.etender.Quoted_Detail4excel;

public interface Quoted_Detail4excelMapper {
    int deleteByPrimaryKey(Integer detailid);

    int insert(Quoted_Detail4excel record);

    int insertSelective(Quoted_Detail4excel record);

    Quoted_Detail4excel selectByPrimaryKey(Integer detailid);

    int updateByPrimaryKeySelective(Quoted_Detail4excel record);

    int updateByPrimaryKey(Quoted_Detail4excel record);
}