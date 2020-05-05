package com.puxin.mp.othertest;

public class DeadLock {
    private static String fileA = "A文件";
    private static String fileB = "B文件";

    public static void main(String[] args) {
        new Thread() {
            public void run() {
                while (true) {
                    synchronized(fileA){
                        System.out.println(this.getName()+"系统读取A文件----------");
                        synchronized (fileB){
                            System.out.println(this.getName()+"系统读取B文件-------");
                        }
                        System.out.println(this.getName() + ":所有文件保存");
                    }
                }
            }
        }.start();

        new Thread() {
            public void run() {
                while (true) {
                    synchronized(fileB){
                        System.out.println(this.getName()+"系统读取B文件----------");
                        synchronized (fileA){
                            System.out.println(this.getName()+"系统读取A文件-------");
                        }
                        System.out.println(this.getName() + ":所有文件保存");
                    }
                }
            }
        }.start();

    }
}
