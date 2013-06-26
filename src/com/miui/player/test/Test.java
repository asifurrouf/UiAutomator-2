package com.miui.player.test;
/**
 * Created with IntelliJ IDEA.
 * User: jiahuixing
 * Date: 13-6-21
 * Time: 下午7:45
 * To change this template use File | Settings | File Templates.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private void dateTest(){


        Date date=new Date();
        String fmt = "yyyy-MM-dd-HH-mm-ss";
        SimpleDateFormat s = new SimpleDateFormat(fmt);
        String ss = s.format(date);
        debug(ss);
    }

    private void random(){
        String search_string;
        search_string = "";
        String all_s = "0123456789abcdefghijklmnopqrstuvwxyz";
        int length  = all_s.length();
        char[] tmp = all_s.toCharArray();
        for (int i = 0; i< 8;i++){
            int rnd = (int) (Math.random()*length);
            search_string = search_string + tmp[rnd];
            debug("i="+ i + ",rnd=" + rnd + ",search_string="+search_string);
        }
    }


    public static void main(String args[]){

        Test test = new Test();
        test.dateTest();

    }

}
