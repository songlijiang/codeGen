package com.slj.api;

import com.slj.core.annotation.Hello;
import lombok.Data;

@Hello(packagePath = "com.slj.api.dao")
@Data
public class User {

    private String name;

    private int id;
}
