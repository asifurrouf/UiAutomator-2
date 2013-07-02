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

    private static final String ZERO = "Zero";
    private static final String NOT_ZERO = "Not_Zero";

    private void forTest(){
        for (int i = 0;i<10;i++){
            debug(""+i,0);
            if (i==4){
                return;
            }
        }

    }

    private void debug(String msg,int wrap){
        if (wrap==1) {
            System.out.println(" <"+msg+"> ");
        }
        else{
            System.out.print(" <"+msg+"> ");
        }


    }

    private void dateTest(){


        Date date=new Date();
        String fmt = "yyyy-MM-dd-HH-mm-ss";
        SimpleDateFormat s = new SimpleDateFormat(fmt);
        String ss = s.format(date);
        debug(ss,1);
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
            debug("i="+ i + ",rnd=" + rnd + ",search_string="+search_string,1);
        }
    }

    public int randomIndex(int area){
        /*获取随机数*/
        debug("randomIndex",1);
        int rnd;
        rnd = (int) (Math.random() * area);
        debug("rnd="+rnd,1);
        return rnd;
    }

    public int randomIndex(int area,String type){
        /*获取随机数*/
        int rnd;
        rnd = (int) (Math.random() * area);
        if (type.equals(NOT_ZERO)){
            debug(String.format("randomIndex>%s>type>%s",rnd,type),1);
            if (rnd==0) {
                debug("ReRandom>>>",0);
                randomIndex(area, type);
            }
            else {
                return rnd;
            }
        }else if (type.equals(ZERO)){
            debug(String.format("randomIndex>%s>type>%s",rnd,type),1);
        }
        return rnd;
    }



    public static void main(String args[]){

        Test test = new Test();
        test.debug(String.format("%s--%s","111111","2222222"),1);

    }

}
