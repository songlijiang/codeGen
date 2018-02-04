package com.slj.api;

import com.slj.core.annotation.CodeGen;
import lombok.Data;

@CodeGen(packagePath = "com.slj.api.dao")
@Data
public class User {

    private String name;

    private int id;
}
