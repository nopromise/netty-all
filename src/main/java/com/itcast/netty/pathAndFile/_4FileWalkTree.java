package com.itcast.netty.pathAndFile;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class _4FileWalkTree {
    public static void main(String[] args) throws IOException {
        //遍历文件夹-访问者模式
        demo6();
    }

    /**
     * 不能直接删除含有文件的目录，需要先把文件删除再删除目录
     * 遍历
     * 访问者模式
     * 可以使用Files工具类中的walkFileTree(Path, FileVisitor)方法，其中需要传入两个参数
     */
    public static void demo6() throws IOException {
        Path path = Paths.get("/Users/fanjunlin/Documents/testfilewalktree");
        // 文件目录数目
        AtomicInteger dirCount = new AtomicInteger();
        // 文件数目
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("进入：" + dir);
                // 增加文件目录数
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("文件:" + file);
                // 增加文件数
                fileCount.incrementAndGet();
                //删除文件()
//                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("退出：" + dir);
                //删除文件夹
//                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
        // 打印数目
        System.out.println("文件目录数:" + dirCount.get());
        System.out.println("文件数:" + fileCount.get());

    }
}
