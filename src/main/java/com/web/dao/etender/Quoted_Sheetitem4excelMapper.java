package com.web.dao.etender;

import com.web.model.etender.Quoted_Sheetitem4excel;

public interface Quoted_Sheetitem4excelMapper {
    int deleteByPrimaryKey(Integer sheetid);

    int insert(Quoted_Sheetitem4excel record);

    int insertSelective(Quoted_Sheetitem4excel record);

    Quoted_Sheetitem4excel selectByPrimaryKey(Integer sheetid);

    int updateByPrimaryKeySelective(Quoted_Sheetitem4excel record);

    int updateByPrimaryKey(Quoted_Sheetitem4excel record);
}