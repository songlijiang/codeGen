package com.slj.core;

import com.slj.core.annotation.CodeGen;

@CodeGen
public class Test {


    public static void main(String[] args) throws Exception{
        Class.forName("TestProcessor");
        System.out.println("args = [" + args + "]");
    }
}
