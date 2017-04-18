package com.web.dao.etender;

import com.web.model.etender.Quoted_Detail4pdf;

public interface Quoted_Detail4pdfMapper {
    int deleteByPrimaryKey(Integer detailid);

    int insert(Quoted_Detail4pdf record);

    int insertSelective(Quoted_Detail4pdf record);

    Quoted_Detail4pdf selectByPrimaryKey(Integer detailid);

    int updateByPrimaryKeySelective(Quoted_Detail4pdf record);

    int updateByPrimaryKey(Quoted_Detail4pdf record);
}