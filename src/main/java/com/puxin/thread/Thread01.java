package com.puxin.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ThreadDemo{
    public void pland(Integer number){
        for (int i = 0; i < number; i++) {
            System.out.println(Thread.currentThread().getName()+"打印 第" +i +"次");
        }
    }
}

public class Thread01 {

    public static void main(String[] args) {
        Thread01 thread01 = new Thread01();
        thread01.getPland();
    }

    ThreadDemo threadDemo = new ThreadDemo();
    Lock lock = new ReentrantLock();
    Condition condition1 =  lock.newCondition();
    Condition condition2 =  lock.newCondition();
    Condition condition3 =  lock.newCondition();

    public void getPland(){
        for (int i = 0; i < 10; i++) {

            new Thread(()->{
                lock.lock();
                try {
                    condition2.await();
                    condition3.await();
                    threadDemo.pland(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
                condition2.signal();
            },"AA").start();

            new Thread(()->{
                lock.lock();
                try {
                    condition1.await();
                    condition3.await();
                    threadDemo.pland(10);
                }catch (Exception e){

                }finally {
                    lock.unlock();
                }
                condition3.signal();

            },"BB").start();
            new Thread(()->{
                lock.lock();
                try {
                    condition1.await();
                    condition2.await();
                    threadDemo.pland(15);
                }catch (Exception e){

                }finally {
                    lock.unlock();
                }
                condition1.signal();

            },"CC").start();

        }
    }

}
