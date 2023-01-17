package com.itcast.netty.util;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class LogUtils {
    public static void logThreadAndContent(String threadName, String content) {
        log.info("线程:{},内容:{}", threadName, content);
    }
    public static void logThreadAndContent(String threadName, ByteBuf buf) {
        String content = buf.toString(StandardCharsets.UTF_8);
        log.info("线程:{},内容:{}", threadName, content);
    }
}
