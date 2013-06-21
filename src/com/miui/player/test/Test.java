package com.miui.player.test;

/**
 * Created with IntelliJ IDEA.
 * User: jiahuixing
 * Date: 13-6-21
 * Time: 下午7:45
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    private void forTest(){
        for (int i = 0;i<10;i++){
            debug(""+i);
            if (i==4){
                return;
            }
        }
    }

    private void debug(String msg){
        System.out.println(msg);
    }

}
