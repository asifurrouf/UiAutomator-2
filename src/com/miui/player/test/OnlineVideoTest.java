package com.miui.player.test;

/**
 * Created with IntelliJ IDEA.
 * User: jiahuixing
 * Date: 13-6-25
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */

import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.android.uiautomator.core.UiObjectNotFoundException;
import java.util.Calendar;
import java.io.IOException;

import static java.lang.String.format;


public class OnlineVideoTest extends UiAutomatorTestCase{

    protected UiDevice device = null;

    private static final int TEST_TIMES = 30;
    private static final String LOG_TAG = "MIUI_PlayerTest";
    private static final String PLAYER_PAC_NAME = "com.miui.video";
    private static final int SWIPE_STEPS = 20;
    private int width;
    private int height;

    private static final int P_480 = 0;
    private static final int P_720 = 1;
    private static final int P_1080 = 2;

    private static final String TOP = "Top";
    private static final String BOTTOM = "Bottom";
    private static final String LEFT = "Left";
    private static final String RIGHT = "Right";
    private static final String CENTRAL = "Central";

    private static final String ZERO = "Zero";
    private static final String NOT_ZERO = "Not_Zero";

    private static final int[][] unlock_start_point= {
            {240,360,540,},
            {920,920,920,},
    };
    private static final int[][] unlock_end_point= {
            {240,360,540,},
            {1180,1180,1180,},
    };


    protected void setUp() throws Exception {
        /*setUp*/
        debug("setUp",1);
        super.setUp();
        device = getUiDevice();
        width = device.getDisplayWidth();
        height = device.getDisplayHeight();
        debug("width=" + width + " height=" + height,1);
        wakePhone();

    }

    private void debug(String msg,int wrap){
        /*打印信息*/
        if (wrap==1){
            System.out.println("<"+msg+">");
        }
        else {
            System.out.print(" <"+msg+"> ");
        }
    }

    public void lockPhone() throws RemoteException {
        /*锁屏*/
        debug("lockPhone",1);
        if (device.isScreenOn())
            device.sleep();
        sleep(2000);
    }


    public void wakePhone() throws RemoteException {
        /*唤醒手机*/
        debug("wakePhone",1);
        device.wakeUp();
        sleep(3000);
    }

    public void unlockPhone() throws RemoteException {
        /*解锁*/
        debug("unlockPhone",1);
        int phone_type = phoneType();
        int start_x = 0;
        int start_y = 0;
        int end_x = 0;
        int end_y = 0;
        start_x = unlock_start_point[0][phone_type];
        start_y = unlock_start_point[1][phone_type];
        end_x = unlock_end_point[0][phone_type];
        end_y = unlock_end_point[1][phone_type];
        /*debug("swipe_point="+","+start_x+","+start_y+","+end_x+","+end_y,1);*/
        device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        sleep(2000);
    }

    private int phoneType(){
        /*手机分辨率类型*/
        debug("phoneType",1);
        int phone_type = P_720;
        if (width == 720 && height == 1280) {
            phone_type =  P_720;
        }
        else if ((width == 480 && height == 800)||(width == 480 && height == 854)){
            phone_type =  P_480;
        }
        else if (width == 1080 && height == 1920){
            phone_type = P_1080;
        }
        /*debug("phone_type="+phone_type,1);*/
        return phone_type;
    }

    public void swipePhone(String type,int times){
        /*滑动手机屏幕*/
        debug(String.format("swipePhone:%s Times:%d", type, times),1);
        int start_x = 0;
        int start_y = 0;
        int end_x = 0;
        int end_y = 0;
        if (type.equals(TOP)) {
            start_x = width / 2;
            start_y = height*2 / 3;
            end_x = width / 3;
            end_y = height / 4;
        }
        else if (type.equals(BOTTOM)) {
            start_x = width / 2;
            start_y = height / 2;
            end_x = width / 2;
            end_y = height*3 / 4;
        }
        else if (type.equals(LEFT)) {
            start_x = width*5 / 6;
            start_y = height / 2;
            end_x = width / 6;
            end_y = height / 2;
        }
        else if (type.equals(RIGHT)) {
            start_x = width / 3;
            start_y = height*2 / 3;
            end_x = width*2 / 3;
            end_y = height*2 / 3;
        }
        else if (type.equals(CENTRAL)) {
            start_x = width / 2;
            start_y = height / 2;
            end_x = width / 2;
            end_y = height / 2;
        }
        /*debug("point="+start_x+","+start_y+","+end_x+","+end_y,1);*/
        for (int j = 0; j < times ; j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(2000);
    }

    public void killVideo() throws IOException {
        /*杀掉视频*/
        debug("killVideo",1);
        String kill = "am kill " + PLAYER_PAC_NAME;
        String force_stop = "am force-stop " + PLAYER_PAC_NAME;
        Runtime.getRuntime().exec(kill);
        Runtime.getRuntime().exec(force_stop);
        sleep(2000);
    }

    public void launchVideo() throws IOException {
        /*打开视频*/
        debug("launchVideo",1);
        String launch = "am start -n com.miui.video/.HomeActivity";
        Runtime.getRuntime().exec(launch);
        sleep(2000);
    }

    public int randomIndex(int area,String type){
        /*获取随机数*/
        int rnd;
        rnd = (int) (Math.random() * area);
        if (type.equals(NOT_ZERO)){
            debug(String.format("randomIndex>%s>type>%s",rnd,type),1);
            if (rnd==0) {
                debug("ReRandom>>>",0);
                return randomIndex(area, type);
            }
            else {
                return rnd;
            }
        }else if (type.equals(ZERO)){
            debug(String.format("randomIndex>%s>type>%s",rnd,type),1);
        }
        return rnd;
    }

    private void waitMsg(String wait,int timeout){
        /*打印等待信息*/
        debug(wait,1);
        int length;
        length = timeout / 1000;
        for (int i = 0; i < length;i++){
            debug(String.format("%d", length - i),0);
            /*System.out.print(""+(length-i)+" ");*/
            sleep(1000);
        }
        debug("Next.",1);
    }

    public void testOnlineVideo() throws RemoteException, IOException, UiObjectNotFoundException {
        /*测试*/
        debug("--------testOnlineVideo--------",1);

        lockPhone();
        wakePhone();
        unlockPhone();

        for (int j = 0;j < TEST_TIMES;j++){
            debug(String.format("--------Test:%d--------", j + 1),1);
            onlineVideo();
            myVideo();
        }

        lockPhone();

    }

    private void onlineVideo() throws IOException, UiObjectNotFoundException {
         /*在线视频*/
        debug("--------onlineVideo--------",1);
        onlineSearch();
        topBanner();
        tvPage();
        moviePage();
        showPage();
        comicPage();
        documentPage();
        topicPage();
        debug("--------onlineVideo Done--------",1);
    }

    private void myVideo(){
        /*我的视频*/
        debug("--------myVideo--------",1);

        debug("--------myVideo Done--------",1);
    }

    private void onlineSearch() throws IOException, UiObjectNotFoundException {
        /*搜索*/
        debug("--------onlineSearch--------",1);

        killVideo();
        launchVideo();

        UiObject search;
        search = new UiObject(new UiSelector().className("android.view.View")).getChild(new UiSelector().className("android.widget.ImageView"));
        /*debug("search="+search.getBounds(),1);*/
        search.click();
        UiObject edit;
        String txt;
        txt = "123";
        edit = new UiObject(new UiSelector().className("android.widget.EditText"));
        edit.setText(txt);
        sleep(2000);
        device.pressKeyCode(KeyEvent.KEYCODE_ENTER);
        String wait;
        wait = "Please wait 5 seconds for the searching.";
        waitMsg(wait,5000);

        killVideo();
    }

    private void topBanner() throws IOException, UiObjectNotFoundException {
        /*首页顶栏banner*/
        debug("--------topBanner--------",1);

        killVideo();
        launchVideo();

        UiObject banner;
        banner = new UiObject(new UiSelector().className("android.widget.ListView"))
                .getChild(new UiSelector().className("android.view.View").index(0));
        int start_x;
        int end_x;
        int start_y;
        int end_y;
        start_x = width*3 / 4;
        end_x = width / 4;
        start_y = banner.getBounds().centerY();
        end_y = start_y;
        for (int j = 0;j<4;j++){
            /*debug("j="+j,0);*/
            banner.clickAndWaitForNewWindow();
            sleep(2000);
            swipePhone(TOP,1);
            sleep(1000);
            device.pressBack();
            sleep(1000);
            device.swipe(start_x,start_y,end_x,end_y,SWIPE_STEPS);
            sleep(1000);
        }

        device.pressBack();
        killVideo();
    }

    private void tvPage() throws IOException, UiObjectNotFoundException {
        /*电视剧*/
        debug("--------tvPage--------",1);

        killVideo();
        launchVideo();

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        /*debug("list_view"+list_view.getBounds(),1);*/
        UiObject more_tv;
        more_tv = list_view.getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_tv="+more_tv.getBounds(),1);*/
        more_tv.clickAndWaitForNewWindow();
        String wait;
        wait = "Please wait 5 seconds for the tv content loading.";
        waitMsg(wait,5000);

        /*精选*/
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject banner;
        banner = list_view.getChild(new UiSelector().className("android.widget.ImageView").index(0));
        /*debug("banner="+banner.getBounds(),1);*/
        banner.clickAndWaitForNewWindow();
        sleep(2000);
        swipePhone(TOP,1);
        sleep(1000);
        device.pressBack();
        sleep(1000);
        int rnd;
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);

        UiObject rank;
        rank = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug("rank="+rank.getBounds(),1);*/
        UiObject new_tvs;
        new_tvs = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(2));
        /*debug("new_tvs="+new_tvs.getBounds(),1);*/

        /*排行*/
        rank.click();
        waitMsg(wait,5000);
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        more_tv = list_view.getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_tv="+more_tv.getBounds(),1);*/
        more_tv.clickAndWaitForNewWindow();
        waitMsg(wait,5000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        if (list_view_child_count > 1) {
            rnd = randomIndex(list_view_child_count, ZERO);
        } else {
            rnd = 0;
        }
        UiObject f_lay;
        f_lay = list_view.getChild(new UiSelector().className("android.widget.FrameLayout").index(rnd));
        int f_lay_child_count;
        f_lay_child_count = f_lay.getChildCount();
        rnd = randomIndex(f_lay_child_count,ZERO);
        UiObject play;
        play = f_lay.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        play.clickAndWaitForNewWindow();
        device.pressBack();
        sleep(2000);
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        device.pressBack();
        sleep(2000);

        /*最新*/
        new_tvs.click();
        waitMsg(wait,5000);
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(1000);
        UiObject filter;
        filter = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        /*debug("filter="+filter.getBounds(),1);*/
        filter.clickAndWaitForNewWindow();

        int start_x,start_y,end_x,end_y;
        int picker_top,picker_bottom;
        UiObject picker_area;
        picker_area = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(0));
        picker_top = picker_area.getBounds().top;
        picker_bottom = picker_area.getBounds().bottom;
        start_x = picker_area.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject picker_kind;
        picker_kind = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(1));
        picker_top = picker_kind.getBounds().top;
        picker_bottom = picker_kind.getBounds().bottom;
        start_x = picker_kind.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject picker_year;
        picker_year = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(2));
        picker_top = picker_year.getBounds().top;
        picker_bottom = picker_year.getBounds().bottom;
        start_x = picker_year.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x,start_y,end_x,end_y,SWIPE_STEPS);
        }
        sleep(1000);
        UiObject confirm;
        confirm = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(2))
                .getChild(new UiSelector().className("android.widget.Button").index(1));
        confirm.click();
        wait = "Wait 5 second to filter.";
        waitMsg(wait,5000);

        device.pressBack();
        device.pressBack();
        killVideo();
    }

    private void moviePage() throws IOException, UiObjectNotFoundException {
        /*电影*/
        debug("--------moviePage--------",1);

        killVideo();
        launchVideo();

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        /*debug("list_view"+list_view.getBounds(),1);*/
        UiObject more_movie;
        more_movie = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(2))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_movie="+more_movie.getBounds(),1);*/
        more_movie.clickAndWaitForNewWindow();
        String wait;
        wait = "Please wait 5 seconds for the movie content loading.";
        waitMsg(wait,5000);
        int rnd;
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);

        /*精选*/

        /*排行*/
        UiObject rank;
        rank = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug("rank="+rank.getBounds(),1);*/
        rank.click();
        waitMsg(wait,5000);
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        more_movie = list_view.getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_movie="+more_movie.getBounds(),1);*/
        more_movie.clickAndWaitForNewWindow();
        waitMsg(wait,5000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        if (list_view_child_count > 1) {
            rnd = randomIndex(list_view_child_count, ZERO);
        } else {
            rnd = 0;
        }
        UiObject f_lay;
        f_lay = list_view.getChild(new UiSelector().className("android.widget.FrameLayout").index(rnd));
        int f_lay_child_count;
        f_lay_child_count = f_lay.getChildCount();
        rnd = randomIndex(f_lay_child_count,ZERO);
        UiObject play;
        play = f_lay.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        play.clickAndWaitForNewWindow();
        device.pressBack();
        sleep(2000);

        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        device.pressBack();
        sleep(2000);

        /*最新*/
        UiObject new_movies;
        new_movies = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(2));
        /*debug("new_movies="+new_movies.getBounds(),1);*/
        new_movies.click();
        waitMsg(wait,5000);
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        UiObject filter;
        filter = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        /*debug("filter="+filter.getBounds(),1);*/
        filter.clickAndWaitForNewWindow();

        int start_x,start_y,end_x,end_y;
        int picker_top,picker_bottom;
        UiObject picker_lang;
        picker_lang = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(0));
        picker_top = picker_lang.getBounds().top;
        picker_bottom = picker_lang.getBounds().bottom;
        start_x = picker_lang.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject picker_kind;
        picker_kind = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(1));
        picker_top = picker_kind.getBounds().top;
        picker_bottom = picker_kind.getBounds().bottom;
        start_x = picker_kind.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject picker_year;
        picker_year = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(2));
        picker_top = picker_year.getBounds().top;
        picker_bottom = picker_year.getBounds().bottom;
        start_x = picker_year.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject confirm;
        confirm = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(2))
                .getChild(new UiSelector().className("android.widget.Button").index(1));
        confirm.click();
        wait = "Wait 5 second to filter.";
        waitMsg(wait,5000);

        device.pressBack();
        device.pressBack();
        killVideo();
    }

    private void showPage() throws IOException, UiObjectNotFoundException {
        /*综艺*/
        debug("--------showPage--------",1);

        killVideo();
        launchVideo();

        int start_x,start_y,end_x,end_y;
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject tmp;
        tmp = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(2).instance(1))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        /*debug(String.format("%s",tmp.getBounds()),1);*/
        start_x = width / 2;
        end_x = start_x;
        start_y = tmp.getBounds().centerY();
        end_y = height / 3;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        sleep(2000);

        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        /*debug("list_view="+list_view.getBounds(),1);*/
        UiObject more_show;
        more_show = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_show="+more_show.getBounds(),1);*/
        more_show.clickAndWaitForNewWindow();
        String wait;
        wait = "Please wait 5 seconds for the show content loading.";
        waitMsg(wait,5000);

        /*精选*/
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject banner;
        banner = list_view.getChild(new UiSelector().className("android.widget.ImageView").index(0));
        /*debug(String.format("banner=%s", banner.getBounds()),1);*/
        banner.clickAndWaitForNewWindow();
        sleep(2000);
        swipePhone(TOP,1);
        sleep(2000);
        device.pressBack();
        sleep(2000);
        int rnd;
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);

        /*排行*/
        UiObject rank;
        rank = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug("rank="+rank.getBounds(),1);*/
        rank.click();
        sleep(2000);
        waitMsg(wait,5000);
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        more_show = list_view.getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_show="+more_show.getBounds(),1);*/
        more_show.clickAndWaitForNewWindow();
        waitMsg(wait,5000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        if (list_view_child_count > 1) {
            rnd = randomIndex(list_view_child_count, ZERO);
        } else {
            rnd = 0;
        }
        UiObject f_lay;
        f_lay = list_view.getChild(new UiSelector().className("android.widget.FrameLayout").index(rnd));
        int f_lay_child_count;
        f_lay_child_count = f_lay.getChildCount();
        rnd = randomIndex(f_lay_child_count,ZERO);
        UiObject play;
        play = f_lay.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        play.clickAndWaitForNewWindow();
        device.pressBack();
        sleep(2000);

        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        device.pressBack();
        sleep(2000);

        /*最新*/
        UiObject new_shows;
        new_shows = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(2));
        /*debug("new_shows="+new_shows.getBounds(),1);*/
        new_shows.click();
        waitMsg(wait,5000);
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        UiObject filter;
        filter = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        /*debug("filter="+filter.getBounds(),1);*/
        filter.clickAndWaitForNewWindow();

        int picker_top,picker_bottom;
        UiObject picker_area;
        picker_area = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(0));
        picker_top = picker_area.getBounds().top;
        picker_bottom = picker_area.getBounds().bottom;
        start_x = picker_area.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject picker_kind;
        picker_kind = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(1));
        picker_top = picker_kind.getBounds().top;
        picker_bottom = picker_kind.getBounds().bottom;
        start_x = picker_kind.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject picker_tv;
        picker_tv = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(2));
        picker_top = picker_tv.getBounds().top;
        picker_bottom = picker_tv.getBounds().bottom;
        start_x = picker_tv.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject confirm;
        confirm = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(2))
                .getChild(new UiSelector().className("android.widget.Button").index(1));
        confirm.click();
        wait = "Wait 5 second to filter.";
        waitMsg(wait,5000);

        device.pressBack();
        device.pressBack();
        killVideo();
    }

    private void comicPage() throws IOException, UiObjectNotFoundException {
        /*动漫*/
        debug("--------comicPage--------",1);

        killVideo();
        launchVideo();

        int start_x,start_y,end_x,end_y;
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject tmp;
        tmp = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(2).instance(1))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        /*debug(String.format("%s",tmp.getBounds()),1);*/
        start_x = width / 2;
        end_x = start_x;
        start_y = tmp.getBounds().centerY();
        end_y = height / 3;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        sleep(2000);

        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        /*debug("list_view="+list_view.getBounds(),1);*/
        UiObject more_comic;
        more_comic = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(2))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_comic="+more_comic.getBounds(),1);*/
        more_comic.clickAndWaitForNewWindow();
        String wait;
        wait = "Please wait 5 seconds for the comic content loading.";
        waitMsg(wait,5000);

        /*精选*/
        sleep(2000);
        int rnd;
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);

        /*排行*/
        UiObject rank;
        rank = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug("rank="+rank.getBounds(),1);*/
        rank.click();
        waitMsg(wait,5000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        if (list_view_child_count > 1) {
            rnd = randomIndex(list_view_child_count, ZERO);
        } else {
            rnd = 0;
        }
        UiObject f_lay;
        f_lay = list_view.getChild(new UiSelector().className("android.widget.FrameLayout").index(rnd));
        int f_lay_child_count;
        f_lay_child_count = f_lay.getChildCount();
        rnd = randomIndex(f_lay_child_count,ZERO);
        UiObject play;
        play = f_lay.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        /*debug("play="+play.getBounds(),1);*/
        play.clickAndWaitForNewWindow();
        device.pressBack();
        sleep(2000);

        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        more_comic = list_view.getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_comic="+more_comic.getBounds(),1);*/
        more_comic.clickAndWaitForNewWindow();
        waitMsg(wait,5000);
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        device.pressBack();
        sleep(2000);

        /*最新*/
        UiObject new_comics;
        new_comics = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(2));
        /*debug("new_comics="+new_comics.getBounds(),1);*/
        new_comics.click();
        waitMsg(wait,5000);
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        UiObject filter;
        filter = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        /*debug("filter="+filter.getBounds(),1);*/
        filter.clickAndWaitForNewWindow();

        int picker_top,picker_bottom;
        UiObject picker_area;
        picker_area = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(0));
        picker_top = picker_area.getBounds().top;
        picker_bottom = picker_area.getBounds().bottom;
        start_x = picker_area.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject picker_kind;
        picker_kind = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(1));
        picker_top = picker_kind.getBounds().top;
        picker_bottom = picker_kind.getBounds().bottom;
        start_x = picker_kind.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject picker_age;
        picker_age = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(2));
        picker_top = picker_age.getBounds().top;
        picker_bottom = picker_age.getBounds().bottom;
        start_x = picker_age.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject confirm;
        confirm = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(2))
                .getChild(new UiSelector().className("android.widget.Button").index(1));
        confirm.click();
        wait = "Wait 5 second to filter.";
        waitMsg(wait,5000);

        device.pressBack();
        device.pressBack();
        killVideo();
    }

    private void documentPage() throws IOException, UiObjectNotFoundException {
        /*纪录片*/
        debug("--------documentPage--------",1);

        killVideo();
        launchVideo();

        int start_x,start_y,end_x,end_y;
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject tmp;
        tmp = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(2).instance(1))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        /*debug(String.format("%s",tmp.getBounds()),1);*/
        start_x = width / 2;
        end_x = start_x;
        start_y = tmp.getBounds().centerY();
        end_y = height / 3;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        for (int j = 0;j<2;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(2000);

        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        /*debug("list_view="+list_view.getBounds(),1);*/
        UiObject more_doc;
        more_doc = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_comic="+more_doc.getBounds(),1);*/
        more_doc.clickAndWaitForNewWindow();
        String wait;
        wait = "Please wait 5 seconds for the document content loading.";
        waitMsg(wait,5000);

        /*精选*/
        sleep(2000);
        int rnd;
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);

        /*排行*/
        UiObject rank;
        rank = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug("rank="+rank.getBounds(),1);*/
        rank.click();
        waitMsg(wait,5000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        more_doc = list_view.getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_doc="+more_doc.getBounds(),1);*/
        more_doc.clickAndWaitForNewWindow();
        waitMsg(wait,5000);
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        if (list_view_child_count > 1) {
            rnd = randomIndex(list_view_child_count, ZERO);
        } else {
            rnd = 0;
        }
        UiObject f_lay;
        f_lay = list_view.getChild(new UiSelector().className("android.widget.FrameLayout").index(rnd));
        int f_lay_child_count;
        f_lay_child_count = f_lay.getChildCount();
        rnd = randomIndex(f_lay_child_count,ZERO);
        UiObject play;
        play = f_lay.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        play.clickAndWaitForNewWindow();
        device.pressBack();
        sleep(2000);

        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        device.pressBack();
        sleep(2000);

        /*最新*/
        UiObject new_docs;
        new_docs = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(2));
        /*debug("new_docs="+new_docs.getBounds(),1);*/
        new_docs.click();
        waitMsg(wait,5000);
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        UiObject filter;
        filter = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        /*debug("filter="+filter.getBounds(),1);*/
        filter.clickAndWaitForNewWindow();

        int picker_top,picker_bottom;
        UiObject picker_kind;
        picker_kind = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(0));
        picker_top = picker_kind.getBounds().top;
        picker_bottom = picker_kind.getBounds().bottom;
        start_x = picker_kind.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        rnd = randomIndex(3,ZERO);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(1000);
        UiObject confirm;
        confirm = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(2))
                .getChild(new UiSelector().className("android.widget.Button").index(1));
        confirm.click();
        wait = "Wait 5 second to filter.";
        waitMsg(wait,5000);

        device.pressBack();
        device.pressBack();
        killVideo();
    }

    private void topicPage() throws IOException, UiObjectNotFoundException {
        /*专题*/
        debug("--------topicPage--------",1);

        killVideo();
        launchVideo();

        int start_x,start_y,end_x,end_y;
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject tmp;
        tmp = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(2).instance(1))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        /*debug(String.format("%s",tmp.getBounds()),1);*/
        start_x = width / 2;
        end_x = start_x;
        start_y = tmp.getBounds().centerY();
        end_y = height / 3;
        /*debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);*/
        for (int j = 0;j<3;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        sleep(2000);

        UiObject topic;
        topic = new UiObject(new UiSelector().className("android.widget.Button"));
        /*debug("topic="+topic.getBounds(),1);*/
        topic.clickAndWaitForNewWindow();
        String wait;
        wait = "Please wait 5 seconds for the topic content loading.";
        waitMsg(wait,5000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        /*debug("list_view_child_count="+list_view_child_count,1);*/
        int rnd;
        rnd = randomIndex(list_view_child_count,ZERO);
        topic = list_view.getChild(new UiSelector().index(rnd));
        topic.clickAndWaitForNewWindow();
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        list_view_child_count = list_view.getChildCount();
        if (list_view_child_count > 1) {
            rnd = randomIndex(list_view_child_count, ZERO);
        } else {
            rnd = 0;
        }
        UiObject f_lay;
        f_lay = list_view.getChild(new UiSelector().className("android.widget.FrameLayout").index(rnd));
        int f_lay_child_count;
        f_lay_child_count = f_lay.getChildCount();
        rnd = randomIndex(f_lay_child_count,ZERO);
        UiObject play;
        play = f_lay.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        play.clickAndWaitForNewWindow();
        sleep(2000);
        swipePhone(TOP,1);
        sleep(2000);
        device.pressBack();
        sleep(2000);

        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);

        device.pressBack();
        device.pressBack();
        killVideo();

    }

}
