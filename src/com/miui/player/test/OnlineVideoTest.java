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
    private static final String VIDEO_PAC_NAME = "com.miui.video";
    private static final String PLAYER_PAC_NAME = "com.miui.videoplayer";
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
        debug(String.format("swipePhone:%s----Times:%d", type, times),1);
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
        String kill = "am kill " + VIDEO_PAC_NAME;
        String force_stop = "am force-stop " + VIDEO_PAC_NAME;
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

    public void testVideo() throws RemoteException, IOException, UiObjectNotFoundException {
        /*测试*/
        debug("--------------------------------testOnlineVideo begin--------------------------------",1);

        lockPhone();
        wakePhone();
        unlockPhone();

        for (int j = 0;j < TEST_TIMES;j++){
            debug(String.format("--------------Test:%d--------------", j + 1),1);

            onlineVideo();
            onlineVideoMenu();
            myVideo();

        }

        lockPhone();
        debug("--------------------------------testOnlineVideo done--------------------------------",1);

    }

    private void onlineVideo() throws IOException, UiObjectNotFoundException {
         /*在线视频*/
        debug("--------onlineVideo--------",1);

        onlineSearch();
        onlineTopBanner();
        onlineTvPage();
        onlineMoviePage();
        onlineShowPage();
        onlineComicPage();
        onlineDocumentPage();
        onlineTopicPage();

        debug("--------onlineVideo Done--------",1);
    }

    private void onlineVideoMenu() throws IOException, UiObjectNotFoundException {
        /*在线Menu*/
        debug("--------onlineVideoMenu--------",1);

        killVideo();
        launchVideo();

        device.pressMenu();
        sleep(1000);
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject menu;
        menu = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
        menu.click();
        sleep(1000);

        device.pressBack();
        killVideo();
    }

    private void myVideo() throws IOException, UiObjectNotFoundException {
        /*我的视频*/
        debug("--------myVideo--------",1);

        myLastPlayedPage();
        myCollectAndFavPage();
        myRecordsPage();
        myDownloadPage();
        myOtherVideo();

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
        UiObject progress_bar = null;
        progress_bar = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        if (progress_bar.exists()){
            waitMsg(wait,5000);
        }
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
/*        debug(String.format("%s %s","list_view=",list_view.getBounds()),1);*/
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        UiObject search_result;
        int rnd;
        rnd = randomIndex(list_view_child_count,ZERO);
        search_result = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
        search_result.clickAndWaitForNewWindow();
        sleep(2000);
        device.pressBack();
        device.pressBack();
        search.click();
        sleep(1000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        /*debug(String.format("%s %s","list_view=",list_view.getBounds()),1);*/
        UiObject search_history;
        search_history = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
        search_history.click();
        waitMsg(wait,5000);
        device.pressBack();
        search.click();
        sleep(2000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        list_view_child_count = list_view.getChildCount();
        UiObject clear_history;
        clear_history = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(list_view_child_count-1));
        clear_history.click();
        device.pressBack();
        device.pressBack();

        killVideo();
    }

    private void onlineTopBanner() throws IOException, UiObjectNotFoundException {
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

    private void onlineTvPage() throws IOException, UiObjectNotFoundException {
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
        rnd = randomIndex(list_view_child_count,ZERO);
        UiObject f_lay;
        f_lay = list_view.getChild(new UiSelector().className("android.widget.FrameLayout").index(rnd));
        int f_lay_child_count;
        f_lay_child_count = f_lay.getChildCount();
        rnd = randomIndex(f_lay_child_count,ZERO);
        UiObject play;
        play = f_lay.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        play.clickAndWaitForNewWindow();

        sleep(1000);
        UiObject collect;
        collect = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        String collect_type;
        collect_type = collect.getText();
        /*debug("collect_type="+collect_type,1);*/
        collect.click();
        sleep(1000);
        if (collect_type.equals("追剧")){
            UiObject confirm;
            confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
            confirm.click();
        }
        UiObject go_play;
        go_play = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        /*debug(String.format("go_play=%s", go_play.getBounds()),1);*/
        go_play.clickAndWaitForNewWindow();
        String detail;
        detail = "Please wait 10 seconds for the web loading.";
        waitMsg(detail,10000);

        UiObject full_screen;
        full_screen = new UiObject(new UiSelector().className("android.widget.Button").index(0).instance(1));
        /*debug("full_screen="+full_screen.getBounds(),1);*/
        for (int j = 0; j < 10 ; j++){
            if (full_screen.isEnabled()){
                /*debug("isEnabled",0);*/
                full_screen.clickAndWaitForNewWindow();
                sleep(5000);
                device.pressBack();
                device.pressBack();
                break;
            }
            else {
                if (j < 9){
                    debug("Continue waiting>"+j,0);
                }else {
                    debug("Fail and back.",1);
                }
                sleep(1000);
            }
        }
        device.pressBack();

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

    private void onlineLivePage() throws IOException, UiObjectNotFoundException {
        /*在线直播*/
        debug("--------onlineLivePage--------",1);

        killVideo();
        launchVideo();

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        debug(String.format("list_view=%s", list_view.getBounds()),1);
        UiObject more_live;
        more_live = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(2))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_movie="+more_movie.getBounds(),1);*/
        more_live.clickAndWaitForNewWindow();

        killVideo();
    }

    private void onlineMoviePage() throws IOException, UiObjectNotFoundException {
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
        rnd = randomIndex(list_view_child_count,ZERO);
        UiObject f_lay;
        f_lay = list_view.getChild(new UiSelector().className("android.widget.FrameLayout").index(rnd));
        int f_lay_child_count;
        f_lay_child_count = f_lay.getChildCount();
        rnd = randomIndex(f_lay_child_count,ZERO);
        UiObject play;
        play = f_lay.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        play.clickAndWaitForNewWindow();

        sleep(1000);
        UiObject collect;
        collect = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        String collect_type;
        collect_type = collect.getText();
        /*debug("collect_type="+collect_type,1);*/
        collect.click();
        sleep(1000);
        if (collect_type.equals("追剧")){
            UiObject confirm;
            confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
            confirm.click();
        }
        UiObject go_play;
        go_play = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        /*debug(String.format("go_play=%s", go_play.getBounds()),1);*/
        go_play.clickAndWaitForNewWindow();
        String detail;
        detail = "Please wait 10 seconds for the web loading.";
        waitMsg(detail,10000);

        UiObject full_screen;
        full_screen = new UiObject(new UiSelector().className("android.widget.Button").index(0).instance(1));
        /*debug("full_screen="+full_screen.getBounds(),1);*/
        for (int j = 0; j < 10 ; j++){
            if (full_screen.isEnabled()){
                /*debug("isEnabled",0);*/
                full_screen.clickAndWaitForNewWindow();
                sleep(5000);
                device.pressBack();
                device.pressBack();
                break;
            }
            else {
                if (j < 9){
                    debug("Continue waiting>"+j,0);
                }else {
                    debug("Fail and back.",1);
                }
                sleep(1000);
            }
        }
        device.pressBack();

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

    private void onlineShowPage() throws IOException, UiObjectNotFoundException {
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
        rnd = randomIndex(list_view_child_count,ZERO);
        UiObject f_lay;
        f_lay = list_view.getChild(new UiSelector().className("android.widget.FrameLayout").index(rnd));
        int f_lay_child_count;
        f_lay_child_count = f_lay.getChildCount();
        rnd = randomIndex(f_lay_child_count,ZERO);
        UiObject play;
        play = f_lay.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        play.clickAndWaitForNewWindow();

        sleep(1000);
        UiObject collect;
        collect = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        String collect_type;
        collect_type = collect.getText();
        /*debug("collect_type="+collect_type,1);*/
        collect.click();
        sleep(1000);
        if (collect_type.equals("追剧")){
            UiObject confirm;
            confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
            confirm.click();
        }
        UiObject go_play;
        go_play = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        /*debug(String.format("go_play=%s", go_play.getBounds()),1);*/
        go_play.clickAndWaitForNewWindow();
        String detail;
        detail = "Please wait 10 seconds for the web loading.";
        waitMsg(detail,10000);

        UiObject full_screen;
        full_screen = new UiObject(new UiSelector().className("android.widget.Button").index(0).instance(1));
        /*debug("full_screen="+full_screen.getBounds(),1);*/
        for (int j = 0; j < 10 ; j++){
            if (full_screen.isEnabled()){
                /*debug("isEnabled",0);*/
                full_screen.clickAndWaitForNewWindow();
                sleep(5000);
                device.pressBack();
                device.pressBack();
                break;
            }
            else {
                if (j < 9){
                    debug("Continue waiting>"+j,0);
                }else {
                    debug("Fail and back.",1);
                }
                sleep(1000);
            }
        }
        device.pressBack();

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

    private void onlineComicPage() throws IOException, UiObjectNotFoundException {
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

        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        rnd = randomIndex(list_view_child_count,ZERO);
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

        sleep(1000);
        UiObject collect;
        collect = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        String collect_type;
        collect_type = collect.getText();
        /*debug("collect_type="+collect_type,1);*/
        collect.click();
        sleep(1000);
        if (collect_type.equals("追剧")){
            UiObject confirm;
            confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
            confirm.click();
        }
        UiObject go_play;
        go_play = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        /*debug(String.format("go_play=%s", go_play.getBounds()),1);*/
        go_play.clickAndWaitForNewWindow();
        String detail;
        detail = "Please wait 10 seconds for the web loading.";
        waitMsg(detail,10000);

        UiObject full_screen;
        full_screen = new UiObject(new UiSelector().className("android.widget.Button").index(0).instance(1));
        /*debug("full_screen="+full_screen.getBounds(),1);*/
        for (int j = 0; j < 10 ; j++){
            if (full_screen.isEnabled()){
                /*debug("isEnabled",0);*/
                full_screen.clickAndWaitForNewWindow();
                sleep(5000);
                device.pressBack();
                device.pressBack();
                break;
            }
            else {
                if (j < 9){
                    debug("Continue waiting>"+j,0);
                }else {
                    debug("Fail and back.",1);
                }
                sleep(1000);
            }
        }
        device.pressBack();

        device.pressBack();
        sleep(2000);
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

    private void onlineDocumentPage() throws IOException, UiObjectNotFoundException {
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
        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);

        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        more_doc = list_view.getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        /*debug("more_doc="+more_doc.getBounds(),1);*/
        more_doc.clickAndWaitForNewWindow();
        waitMsg(wait,5000);

        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        rnd = randomIndex(list_view_child_count,ZERO);
        UiObject f_lay;
        f_lay = list_view.getChild(new UiSelector().className("android.widget.FrameLayout").index(rnd));
        int f_lay_child_count;
        f_lay_child_count = f_lay.getChildCount();
        rnd = randomIndex(f_lay_child_count,ZERO);
        UiObject play;
        play = f_lay.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        play.clickAndWaitForNewWindow();

        sleep(1000);
        UiObject collect;
        collect = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        String collect_type;
        collect_type = collect.getText();
        /*debug("collect_type="+collect_type,1);*/
        collect.click();
        sleep(1000);
        if (collect_type.equals("追剧")){
            UiObject confirm;
            confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
            confirm.click();
        }
        UiObject go_play;
        go_play = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        /*debug(String.format("go_play=%s", go_play.getBounds()),1);*/
        go_play.clickAndWaitForNewWindow();
        String detail;
        detail = "Please wait 10 seconds for the web loading.";
        waitMsg(detail,10000);

        UiObject full_screen;
        full_screen = new UiObject(new UiSelector().className("android.widget.Button").index(0).instance(1));
        /*debug("full_screen="+full_screen.getBounds(),1);*/
        for (int j = 0; j < 10 ; j++){
            if (full_screen.isEnabled()){
                /*debug("isEnabled",0);*/
                full_screen.clickAndWaitForNewWindow();
                sleep(5000);
                device.pressBack();
                device.pressBack();
                break;
            }
            else {
                if (j < 9){
                    debug("Continue waiting>"+j,0);
                }else {
                    debug("Fail and back.",1);
                }
                sleep(1000);
            }
        }
        device.pressBack();

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

    private void onlineTopicPage() throws IOException, UiObjectNotFoundException {
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
        rnd = randomIndex(list_view_child_count, NOT_ZERO);
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

        UiObject collect;
        collect = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        String collect_type;
        collect_type = collect.getText();
        /*debug("collect_type="+collect_type,1);*/
        collect.click();
        sleep(1000);
        if (collect_type.equals("追剧")){
            UiObject confirm;
            confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
            confirm.click();
        }
        UiObject go_play;
        go_play = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        /*debug(String.format("go_play=%s", go_play.getBounds()),1);*/
        go_play.clickAndWaitForNewWindow();
        String detail;
        detail = "Please wait 10 seconds for the web loading.";
        waitMsg(detail,10000);

        UiObject full_screen;
        full_screen = new UiObject(new UiSelector().className("android.widget.Button").index(0).instance(1));
        /*debug("full_screen="+full_screen.getBounds(),1);*/
        for (int j = 0; j < 10 ; j++){
            if (full_screen.isEnabled()){
                /*debug("isEnabled",0);*/
                full_screen.clickAndWaitForNewWindow();
                sleep(5000);
                device.pressBack();
                device.pressBack();
                break;
            }
            else {
                if (j < 9){
                    debug("Continue waiting>"+j,0);
                }else {
                    debug("Fail and back.",1);
                }
                sleep(1000);
            }
        }
        device.pressBack();

        device.pressBack();
        sleep(2000);

        rnd = randomIndex(3,ZERO);
        swipePhone(TOP,rnd);
        sleep(2000);

        device.pressBack();
        device.pressBack();
        killVideo();
    }

    private void myLastPlayedPage() throws IOException, UiObjectNotFoundException {
        /*最近播放*/
        debug("--------myLastPlayedPage--------",1);

        killVideo();
        launchVideo();

        UiObject l_lay;
        l_lay = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(0));
        /*debug(format("%s--%s","l_lay",l_lay.getBounds()),1);*/
        UiObject my_video;
        my_video = l_lay.getChild(new UiSelector().className("android.widget.FrameLayout"))
                .getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug(format("%s--%s","my_video",my_video.getBounds()),1);*/
        my_video.click();
        sleep(1000);

        String wait;
        wait = "Please wait 5 seconds for the content loading.";
        waitMsg(wait,5000);
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject my_last_play;
        my_last_play = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
        /*debug(format("%s--%s","my_last_play",my_last_play.getBounds()),1);*/
        my_last_play.clickAndWaitForNewWindow();
        sleep(1000);

        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        if (list_view_child_count !=0 ){
            int rnd;
            rnd = randomIndex(list_view_child_count,ZERO);
            my_last_play = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
            /*debug(format("%s--%s","my_last_play",my_last_play.getBounds()),1);*/
            my_last_play.clickAndWaitForNewWindow();
            sleep(2000);
            device.pressBack();
            sleep(1000);
            UiObject clear_history = null;
            clear_history = new UiObject(new UiSelector().className("android.widget.Button"));
            if (clear_history.exists()){
                clear_history.click();
                sleep(1000);
                UiObject confirm;
                confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
                confirm.click();
                sleep(1000);
            }
        }

        killVideo();
    }

    private void myCollectAndFavPage() throws IOException, UiObjectNotFoundException {
        /*收藏&追剧*/
        debug("--------myCollectAndFavPage--------",1);

        killVideo();
        launchVideo();

        UiObject l_lay;
        l_lay = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(0));
        /*debug(format("%s--%s","l_lay",l_lay.getBounds()),1);*/
        UiObject my_video;
        my_video = l_lay.getChild(new UiSelector().className("android.widget.FrameLayout"))
                .getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug(format("%s--%s","my_video",my_video.getBounds()),1);*/
        my_video.click();
        sleep(1000);

        String wait;
        wait = "Please wait 5 seconds for the content loading.";
        waitMsg(wait,5000);
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject collect_fav;
        collect_fav = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(2));
        /*debug(format("%s--%s","collect_fav",collect_fav.getBounds()),1);*/
        collect_fav.clickAndWaitForNewWindow();
        sleep(1000);

        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        /*debug(String.format("list_view_child_count=%d", list_view_child_count),1);*/
        if (list_view_child_count > 0){
            int rnd;
            rnd = randomIndex(list_view_child_count,ZERO);
            UiObject my_collect;
            my_collect = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
            /*debug(String.format("my_collect=%s", my_collect.getBounds()),1);*/
            my_collect.clickAndWaitForNewWindow();

            UiObject collect;
            collect = new UiObject(new UiSelector().className("android.widget.Button").index(0));
            String collect_type;
            collect_type = collect.getText();
        /*debug("collect_type="+collect_type,1);*/
            collect.click();
            sleep(1000);
            if (collect_type.equals("追剧")){
                UiObject confirm;
                confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
                confirm.click();
            }
            UiObject go_play;
            go_play = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        /*debug(String.format("go_play=%s", go_play.getBounds()),1);*/
            go_play.clickAndWaitForNewWindow();
            String detail;
            detail = "Please wait 10 seconds for the web loading.";
            waitMsg(detail,10000);

            UiObject full_screen;
            full_screen = new UiObject(new UiSelector().className("android.widget.Button").index(0).instance(1));
        /*debug("full_screen="+full_screen.getBounds(),1);*/
            for (int j = 0; j < 10 ; j++){
                if (full_screen.isEnabled()){
                /*debug("isEnabled",0);*/
                    full_screen.clickAndWaitForNewWindow();
                    sleep(5000);
                    device.pressBack();
                    device.pressBack();
                    break;
                }
                else {
                    if (j < 9){
                        debug("Continue waiting>"+j,0);
                    }else {
                        debug("Fail and back.",1);
                    }
                    sleep(1000);
                }
            }
            device.pressBack();

            sleep(1000);
            device.pressBack();
            my_collect.longClick();
            sleep(1000);
            UiObject view;
            view = new UiObject(new UiSelector().className("android.view.View").index(0));
            UiObject select_all;
            select_all = view.getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.TextView").index(2));
            if (list_view_child_count > 1){
                select_all.click();
                sleep(1000);
            }
            UiObject dis_fav;
            dis_fav = new UiObject(new UiSelector().className("android.widget.Button").index(0));
            dis_fav.click();
            sleep(1000);
            device.pressBack();
        }
        else {
            device.pressBack();
        }

        killVideo();
    }

    private void myRecordsPage() throws IOException, UiObjectNotFoundException {
        /*我拍摄的视频*/
        debug("--------myRecordsPage--------",1);

        killVideo();
        launchVideo();

        UiObject l_lay;
        l_lay = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(0));
        /*debug(format("%s--%s","l_lay",l_lay.getBounds()),1);*/
        UiObject my_video;
        my_video = l_lay.getChild(new UiSelector().className("android.widget.FrameLayout"))
                .getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug(format("%s--%s","my_video",my_video.getBounds()),1);*/
        my_video.click();
        sleep(1000);

        String wait;
        wait = "Please wait 5 seconds for the content loading.";
        waitMsg(wait,5000);
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject record;
        record = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(3));
        /*debug(format("%s--%s","record",record.getBounds()),1);*/
        record.clickAndWaitForNewWindow();
        sleep(1000);

        UiObject central_to_record = null;
        UiObject top_to_record;
        central_to_record = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        /*debug("central_to_record="+central_to_record.getBounds(),1);*/
        top_to_record = new UiObject(new UiSelector().className("android.widget.Button").index(2));
        /*debug("top_to_record="+top_to_record.getBounds(),1);*/
        if (central_to_record.exists()){
            /*debug("central_to_record!=null",1);*/
            central_to_record.clickAndWaitForNewWindow();
            sleep(3000);
            UiObject to_record;
            to_record = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(5))
                    .getChild(new UiSelector().className("android.widget.RelativeLayout").index(2));
            to_record.click();
            String wait_record;
            wait_record = "Need 10 seconds to record.";
            waitMsg(wait_record,10000);
            to_record.click();
            sleep(2000);
            UiObject save;
            save = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(5))
                    .getChild(new UiSelector().className("android.widget.RelativeLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.ImageView").index(1));
            save.click();
            sleep(2000);
        }
        top_to_record.clickAndWaitForNewWindow();
        sleep(3000);
        UiObject to_record;
        to_record = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(5))
                .getChild(new UiSelector().className("android.widget.RelativeLayout").index(2));
        to_record.click();
        String wait_record;
        wait_record = "Need 10 seconds to record.";
        waitMsg(wait_record,10000);
        to_record.click();
        sleep(2000);
        UiObject save;
        save = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(5))
                .getChild(new UiSelector().className("android.widget.RelativeLayout").index(0))
                .getChild(new UiSelector().className("android.widget.ImageView").index(1));
        save.click();
        sleep(2000);

        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        int rnd;
        rnd = randomIndex(list_view_child_count,ZERO);
        UiObject my_record;
        my_record = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
        my_record.clickAndWaitForNewWindow();
        sleep(2000);
        device.pressBack();
        device.pressBack();
        my_record.longClick();
        sleep(1000);
        UiObject view;
        view = new UiObject(new UiSelector().className("android.view.View").index(0));
        UiObject select_all;
        select_all = view.getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                .getChild(new UiSelector().className("android.widget.TextView").index(2));
        if (list_view_child_count > 1){
            select_all.click();
            sleep(1000);
        }
        UiObject fav;
        fav = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        fav.click();
        sleep(1000);
        my_record.longClick();
        if (list_view_child_count > 1){
            select_all.click();
            sleep(1000);
        }
        UiObject delete;
        delete = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        delete.click();
        UiObject confirm;
        confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        confirm.click();
        sleep(1000);

        killVideo();
    }

    private void myDownloadPage() throws IOException, UiObjectNotFoundException {
        /*下载的离线视频*/
        debug("--------myDownloadPage--------",1);

        killVideo();
        launchVideo();

        UiObject l_lay;
        l_lay = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(0));
        /*debug(format("%s--%s","l_lay",l_lay.getBounds()),1);*/
        UiObject my_video;
        my_video = l_lay.getChild(new UiSelector().className("android.widget.FrameLayout"))
                .getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug(format("%s--%s","my_video",my_video.getBounds()),1);*/
        my_video.click();
        sleep(1000);

        String wait;
        wait = "Please wait 5 seconds for the content loading.";
        waitMsg(wait,5000);
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject download;
        download = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(4));
        /*debug(format("%s--%s","download",download.getBounds()),1);*/
        download.clickAndWaitForNewWindow();
        sleep(1000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        if (list_view_child_count > 0){
            int rnd;
            rnd = randomIndex(list_view_child_count,ZERO);
            UiObject movie;
            movie = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
            debug("movie="+movie.getBounds(),1);
            movie.clickAndWaitForNewWindow();
            if (PLAYER_PAC_NAME.equals(device.getCurrentPackageName())){
                sleep(2000);
                device.pressBack();
                device.pressBack();
            }
        }

        killVideo();
    }

    private void myOtherVideo() throws IOException, UiObjectNotFoundException {
        /*我的视频*/
        debug("--------myDownloadPage--------",1);

        killVideo();
        launchVideo();

        UiObject l_lay;
        l_lay = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(0));
        /*debug(format("%s--%s","l_lay",l_lay.getBounds()),1);*/
        UiObject my_video;
        my_video = l_lay.getChild(new UiSelector().className("android.widget.FrameLayout"))
                .getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug(format("%s--%s","my_video",my_video.getBounds()),1);*/
        my_video.click();
        sleep(1000);

        String wait;
        wait = "Please wait 5 seconds for the content loading.";
        waitMsg(wait,5000);
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        UiObject movie = null;
        if (list_view_child_count > 5){
            int rnd;
            rnd = 5 + randomIndex((list_view_child_count - 5), ZERO);
            movie = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
            if (movie.exists()){
                movie.clickAndWaitForNewWindow();
                sleep(2000);
                if (PLAYER_PAC_NAME.equals(device.getCurrentPackageName())){
                    sleep(2000);
                    device.pressBack();
                    device.pressBack();
                    device.pressBack();
                }else {
                    list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
                    list_view_child_count = list_view.getChildCount();
                    rnd = randomIndex(list_view_child_count,ZERO);
                    movie = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
                    movie.clickAndWaitForNewWindow();
                    sleep(5000);
                    if (PLAYER_PAC_NAME.equals(device.getCurrentPackageName())){
                        device.pressBack();
                        device.pressBack();
                    }
                    device.pressBack();
                }
                sleep(1000);
                movie.longClick();
                sleep(1000);
                UiObject view;
                view = new UiObject(new UiSelector().className("android.view.View").index(0));
                UiObject select_all;
                select_all = view.getChild(new UiSelector().className("android.widget.TextView").index(2));
                if (list_view_child_count > 6){
                    select_all.click();
                    sleep(1000);
                }
                UiObject delete;
                UiObject fav;
                UiObject visible;
                delete = new UiObject(new UiSelector().className("android.widget.Button").index(0));
                fav = new UiObject(new UiSelector().className("android.widget.Button").index(1));
                visible = new UiObject(new UiSelector().className("android.widget.Button").index(2));
                visible.click();
                sleep(1000);
                device.pressMenu();
                sleep(1000);
                list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
                UiObject menu;
                menu = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
                menu.click();
                movie.longClick();
                if (list_view_child_count > 6){
                    select_all.click();
                    sleep(1000);
                }
                visible.click();
                movie.longClick();
                fav.click();
                movie.longClick();
                delete.click();
                device.pressBack();
            }
        }

        killVideo();
    }
}
