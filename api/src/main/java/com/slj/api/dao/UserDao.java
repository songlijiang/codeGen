package com.slj.api.dao;

import com.piaoniu.annotations.DaoGen;
import com.slj.api.User;
import java.lang.String;
import org.apache.ibatis.annotations.Param;

@DaoGen
public interface UserDao {


  User findByname(@Param("name") String name);

  User findByid(@Param("id") int id);

}
