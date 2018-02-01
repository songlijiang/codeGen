package com.slj.core.annotation;

import com.piaoniu.annotations.DaoGen;
import org.apache.ibatis.annotations.Param;

public @interface Hello {

    String projectPath () default "api/src/main/java";

    String packagePath () default "com.slj.api.dao1";


    Class daoAnnotation() default DaoGen.class;

    Class paramAnnotation() default Param.class;
}
