package com.mycompany.myapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycompany.myapp.pojo.Address;

public interface AddressMapper {
    int insert(Address record);

    int insertSelective(Address record);
    
    List<String> selectFriendId(@Param("user_id") String user_id);
    
    List<Address> selectAddressById(@Param("user_id") String user_id);
}