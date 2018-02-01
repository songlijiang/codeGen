package com.slj.api;

import com.slj.core.annotation.Hello;
import lombok.Data;

@Data
@Hello(packagePath = "com.slj.api.dao")
public class Test {

    int id;

    String name;

    public static void main(String[] args) {

    }
}
