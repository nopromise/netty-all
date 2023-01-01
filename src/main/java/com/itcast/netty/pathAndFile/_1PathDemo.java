package com.itcast.netty.pathAndFile;

import java.nio.file.Path;
import java.nio.file.Paths;

public class _1PathDemo {
    public static void main(String[] args) {
//        //相对路径；相对路径 不带盘符 使用 user.dir 环境变量来定位 1.txt
//        Path path = Paths.get("data.txt");
//        //绝对路径
//        Path path1 = Paths.get("/Users/fanjunlin/IdeaProjects/netty-study/src/main/data.txt");
//        Path path2 = Paths.get("/Users/fanjunlin/IdeaProjects/netty-study", "src/main/data.txt");
        demo1();
    }

    public static void demo1(){
//        Path path = Paths.get("d:\\data\\projects\\a\\..\\b");
        Path path = Paths.get("Users/fanjunlin/../src/main/data.txt");
        System.out.println(path);
        System.out.println(path.normalize()); // 正常化路径 会去除 . 以及 ..
    }
}
