package com.itcast.netty;

/**
 * @Author: fjl
 * @CreateTime: 2023-01-12
 */
public class Demo {
    public static void main(String[] args) {
        for (int i = 1; i < 100; i++) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            change(i);
        }
    }

    public static void change(Integer n) {
        System.out.println(n);
        n = 3;
        System.out.println(n);
    }
}