package com.itcast.netty.pathAndFile;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class _2FileDemo {
    public static void main(String[] args) throws IOException {
        //检查文件是否存在
//        demo1();
        //建一级目录、创建多级目录用
//        demo2();
        //拷贝文件
//        demo3();
        //移动文件
//        demo4();
        //删除文件、删除目录
//        demo5();
        //遍历文件夹-访问者模式
        demo6();
    }

    //检查文件是否存在
    public static void demo1() {
        //相对路径 不带盘符 使用 user.dir 环境变量来定位 1.txt
        Path path = Paths.get("1.txt");
        Path path2 = Paths.get("/Users/fanjunlin/IdeaProjects/netty-study/src/main/1.txt");
        log.info("file exist?{}", Files.exists(path));
        log.info("file exist?{}", Files.exists(path2));
    }

    //创建一级目录、创建多级目录用
    public static void demo2() {

//        Path path = Paths.get("helloword/d1");
        Path path = Paths.get("helloword/d1/d2");
//        Path path = Paths.get("helloword");
        try {
//            创建一级目录
            //如果目录已存在，会抛异常 FileAlreadyExistsException
            //不能一次创建多级目录，否则会抛异常 NoSuchFileException
//            Files.createDirectory(path);
            //创建多级目录用
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝文件
     *
     * @throws IOException
     */
    public static void demo3() throws IOException {
        Path source = Paths.get("/Users/fanjunlin/IdeaProjects/netty-study/src/main/from.txt");
        Path target = Paths.get("/Users/fanjunlin/IdeaProjects/netty-study/src/main/to.txt");
        //如果文件已存在，会抛异常 FileAlreadyExistsException
//        Files.copy(source,target);
        //如果希望用 source 覆盖掉 target，需要用 StandardCopyOption 来控制
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

    }

    /**
     * 移动文件
     */
    public static void demo4() throws IOException {
        Path source = Paths.get("/Users/fanjunlin/Pictures/paper/wallhaven-g76mml.png");
        Path target = Paths.get("/Users/fanjunlin/IdeaProjects/netty-study/src/main/paper.png");
        //StandardCopyOption.ATOMIC_MOVE 保证文件移动的原子性
        Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
    }

    /**
     * 删除文件、删除目录
     */
    public static void demo5() throws IOException {
        Path path = Paths.get("/Users/fanjunlin/IdeaProjects/netty-study/src/main/todelete.txt");
        //如果文件不存在，会抛异常 NoSuchFileException
        Files.delete(path);

        //如果目录还有内容，会抛异常 DirectoryNotEmptyException
//        Path target = Paths.get("helloword/d1");
//        Files.delete(target);
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
