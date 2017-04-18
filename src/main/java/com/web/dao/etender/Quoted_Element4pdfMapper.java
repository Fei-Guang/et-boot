package com.web.dao.etender;

import com.web.model.etender.Quoted_Element4pdf;

public interface Quoted_Element4pdfMapper {
    int deleteByPrimaryKey(Integer elementid);

    int insert(Quoted_Element4pdf record);

    int insertSelective(Quoted_Element4pdf record);

    Quoted_Element4pdf selectByPrimaryKey(Integer elementid);

    int updateByPrimaryKeySelective(Quoted_Element4pdf record);

    int updateByPrimaryKey(Quoted_Element4pdf record);
}