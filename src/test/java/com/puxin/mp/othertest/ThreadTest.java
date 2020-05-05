package com.puxin.mp.othertest;

import java.util.Random;

public class ThreadTest {
    public static void main(String[] args) {

        Couplet couplet = new Couplet();
        for (int i = 0; i < 10000; i++) {
            new Thread(){
                public void run(){
                    int r = new Random().nextInt(2);
                    if(r % 2 == 0){
                        Couplet.first();
                    }else{
                        Couplet.second();
                    }
                }
            }.start();
        }

    }



}

class Couplet {

    public static synchronized void first() {

        System.out.println("魑");
        System.out.println("魅");
        System.out.println("魍");
        System.out.println("魉");

    }

    public static void second(){
        synchronized (Couplet.class){

            System.out.println("琴");
            System.out.println("瑟");
            System.out.println("琵");
            System.out.println("琶");
        }
    }

}
