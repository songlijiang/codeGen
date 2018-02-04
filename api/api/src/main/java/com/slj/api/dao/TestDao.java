package com.slj.api.dao;

import com.piaoniu.annotations.DaoGen;
import com.slj.api.Test;
import java.lang.String;
import org.apache.ibatis.annotations.Param;

@DaoGen
public interface TestDao {
  Test findByid(@Param("id") int id);

  Test findByname(@Param("name") String name);
}
