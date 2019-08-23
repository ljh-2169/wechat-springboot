package com.mycompany.myapp.dao;

import com.mycompany.myapp.pojo.Amessage;

public interface AmessageMapper {
    int insert(Amessage record);

    int insertSelective(Amessage record);
}