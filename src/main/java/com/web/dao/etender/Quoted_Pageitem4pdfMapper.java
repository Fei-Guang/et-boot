package com.web.dao.etender;

import com.web.model.etender.Quoted_Pageitem4pdf;

public interface Quoted_Pageitem4pdfMapper {
    int deleteByPrimaryKey(Integer pageid);

    int insert(Quoted_Pageitem4pdf record);

    int insertSelective(Quoted_Pageitem4pdf record);

    Quoted_Pageitem4pdf selectByPrimaryKey(Integer pageid);

    int updateByPrimaryKeySelective(Quoted_Pageitem4pdf record);

    int updateByPrimaryKey(Quoted_Pageitem4pdf record);
}