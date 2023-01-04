package com.itcast.netty.pathAndFile;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class _3FileWalk {
    public static void main(String[] args) throws IOException {
        //拷贝整个文件夹内容到另一个文件夹
        String source = "/Users/fanjunlin/Documents/test/javadesin";
        String target = "/Users/fanjunlin/Documents/test/javadesincopy";
        //
        Files.walk(Paths.get(source)).forEach(path -> {
            String targetName = path.toString().replace(source, target);
            try {
                //是目录，创建
                if (Files.isDirectory(path)) {
                    Files.createDirectory(Paths.get(targetName));
                } else if (Files.isRegularFile(path)) {
                    //是文件，拷贝
                    Files.copy(path, Paths.get(targetName));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
