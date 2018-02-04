package com.slj.api;

import com.slj.core.annotation.CodeGen;
import lombok.Data;

@Data
@CodeGen(packagePath = "com.slj.api.dao")
public class Test {

    int id;

    String name;

    public static void main(String[] args) {

    }
}
