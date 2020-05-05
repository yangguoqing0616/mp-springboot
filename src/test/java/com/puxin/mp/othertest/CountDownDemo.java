package com.puxin.mp.othertest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownDemo {
    private static Integer count = 0;
    public static void main(String[] args) {
        
        ExecutorService threadpool = Executors.newFixedThreadPool(100);
        CountDownLatch cdl = new CountDownLatch(2000000);
        for (int i = 1; i<=2000000;i++) {
            final int index = i;
            threadpool.execute(new Runnable() {
                @Override
                public void run() {
                    synchronized (CountDownLatch.class){
                        try {
                            count = count + index;
                            //计数器减一
                        }catch(Exception e){
                            e.printStackTrace();
                        }finally {
                            cdl.countDown();
                        }
                    }
                }
            });
        }
        try {
            cdl.await(); //堵塞当前线程，知道cdl=0的时候再继续往下走 //为了避免程序一致挂起，我们可以设置一个timeout时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(count);
        threadpool.shutdown();
    }

}