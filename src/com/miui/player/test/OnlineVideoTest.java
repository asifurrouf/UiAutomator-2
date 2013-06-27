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

    private static final int TOP = 0;
    private static final int BOTTOM = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;
    private static final int CENTRAL = 4;

    private static final String SONG = "song";
    private static final String SINGER = "singer";
    private static final String LIST = "list";
    private static final String ONLINE = "online";
    private static final String ALBUMS = "albums";
    private static final String FOLDER = "folder";

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
        sleep(1000);
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

    public void swipePhone(int type){
        /*滑动手机屏幕*/
        debug("swipePhone+"+type,1);
        int start_x = 0;
        int start_y = 0;
        int end_x = 0;
        int end_y = 0;
        switch (type){
            case TOP:
                start_x = width / 2;
                start_y = height*3 / 4;
                end_x = width / 2;
                end_y = height / 4;
                break;
            case BOTTOM:
                start_x = width / 2;
                start_y = height / 2;
                end_x = width / 2;
                end_y = height*3 / 4;
                break;
            case LEFT:
                start_x = width*5 / 6;
                start_y = height / 2;
                end_x = width / 6;
                end_y = height / 2;
                break;
            case RIGHT:
                start_x = width / 3;
                start_y = height*2 / 3;
                end_x = width*2 / 3;
                end_y = height*2 / 3;
                break;
            case CENTRAL:
                start_x = width / 2;
                start_y = height / 2;
                end_x = width / 2;
                end_y = height / 2;
                break;
        }
        /*debug("point="+start_x+","+start_y+","+end_x+","+end_y,1);*/
        device.swipe(start_x,start_y,end_x,end_y,SWIPE_STEPS);
        sleep(1000);
    }

    public void killPlayer() throws IOException {
        /*杀掉播放器*/
        debug("killPlayer",1);
        String kill = "am kill " + PLAYER_PAC_NAME;
        String force_stop = "am force-stop " + PLAYER_PAC_NAME;
        Runtime.getRuntime().exec(kill);
        Runtime.getRuntime().exec(force_stop);
        sleep(3000);
    }

    public void launchPlayer() throws IOException {
        /*打开播放器*/
        debug("launchPlayer",1);
        String launch = "am start -n com.miui.video/.HomeActivity";
        Runtime.getRuntime().exec(launch);
        sleep(3000);
    }

    public int randomIndex(int area){
        /*获取随机数*/
        debug("randomIndex",1);
        int rnd;
        rnd = (int) (Math.random() * area);
        if (rnd==0) {
            return randomIndex(area);
        }
        else
            debug("rnd="+rnd,1);
        return rnd;
    }


    private void waitMsg(String wait,int timeout){
        debug(wait,1);
        int length;
        length = timeout / 1000;
        for (int i = 0; i < length;i++){
            debug(""+(length-i),0);
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

        //topBanner();
        //tvPage();
        //moviePage();
        showPage();

    }

    private void topBanner() throws IOException, UiObjectNotFoundException {
        /*首页顶栏banner*/
        debug("--------topBanner--------",1);

        killPlayer();
        launchPlayer();

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
            debug("j="+j,0);
            banner.clickAndWaitForNewWindow();
            sleep(1000);
            device.pressBack();
            sleep(1000);
            device.swipe(start_x,start_y,end_x,end_y,SWIPE_STEPS);
            sleep(1000);
        }
        killPlayer();
    }

    private void tvPage() throws IOException, UiObjectNotFoundException {
        /*电视剧*/
        debug("--------tvPage--------",1);

        killPlayer();
        launchPlayer();

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        /*debug("list_view"+list_view.getBounds(),1);*/
        UiObject more_tv;
        more_tv = list_view.getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        debug("more_tv="+more_tv.getBounds(),1);
        more_tv.clickAndWaitForNewWindow();
        String wait;
        wait = "Please wait 5 seconds for the tv content loading.";
        waitMsg(wait,5000);

        /*精选*/
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject banner;
        banner = list_view.getChild(new UiSelector().className("android.widget.ImageView").index(0));
        debug("banner="+banner.getBounds(),1);
        banner.clickAndWaitForNewWindow();
        sleep(2000);
        swipePhone(TOP);
        sleep(1000);
        device.pressBack();
        sleep(1000);
        swipePhone(TOP);
        sleep(2000);

        UiObject rank;
        rank = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        debug("rank="+rank.getBounds(),1);
        UiObject new_tvs;
        new_tvs = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(2));
        debug("new_tvs="+new_tvs.getBounds(),1);

        /*排行*/
        rank.click();
        waitMsg(wait,5000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        more_tv = list_view.getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        debug("more_tv"+more_tv.getBounds(),1);
        more_tv.clickAndWaitForNewWindow();
        waitMsg(wait,5000);
        device.pressBack();
        sleep(1000);
        swipePhone(TOP);
        swipePhone(LEFT);
        sleep(1000);

        /*最新*/
        new_tvs.click();
        waitMsg(wait,5000);
        swipePhone(TOP);
        int rnd;
        UiObject filter;
        filter = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        debug("filter="+filter.getBounds(),1);
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
        debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);
        rnd = randomIndex(5);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        UiObject picker_kind;
        picker_kind = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(1));
        picker_top = picker_kind.getBounds().top;
        picker_bottom = picker_kind.getBounds().bottom;
        start_x = picker_kind.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);
        rnd = randomIndex(5);
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
        debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);
        rnd = randomIndex(5);
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
        killPlayer();
    }

    private void moviePage() throws IOException, UiObjectNotFoundException {
        /*电影*/
        debug("--------moviePage--------",1);

        killPlayer();
        launchPlayer();

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        /*debug("list_view"+list_view.getBounds(),1);*/
        UiObject more_movie;
        more_movie = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(2))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        debug("more_movie="+more_movie.getBounds(),1);
        more_movie.clickAndWaitForNewWindow();
        String wait;
        wait = "Please wait 5 seconds for the movie content loading.";
        waitMsg(wait,5000);
        swipePhone(TOP);
        sleep(1000);

        /*精选*/
        UiObject rank;
        rank = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        debug("rank="+rank.getBounds(),1);
        UiObject new_movies;
        new_movies = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(2));
        debug("new_movies="+new_movies.getBounds(),1);

        /*排行*/
        rank.click();
        waitMsg(wait,5000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        more_movie = list_view.getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        debug("more_movie"+more_movie.getBounds(),1);
        more_movie.clickAndWaitForNewWindow();
        waitMsg(wait,5000);
        device.pressBack();
        sleep(1000);
        swipePhone(TOP);
        swipePhone(LEFT);
        sleep(1000);

        /*最新*/
        new_movies.click();
        waitMsg(wait,5000);
        swipePhone(TOP);
        int rnd;
        UiObject filter;
        filter = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        debug("filter="+filter.getBounds(),1);
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
        debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);
        rnd = randomIndex(5);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        UiObject picker_kind;
        picker_kind = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(1));
        picker_top = picker_kind.getBounds().top;
        picker_bottom = picker_kind.getBounds().bottom;
        start_x = picker_kind.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);
        rnd = randomIndex(5);
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
        debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);
        rnd = randomIndex(5);
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
        killPlayer();
    }

    private void showPage() throws IOException, UiObjectNotFoundException {
        /*综艺*/
        debug("--------showPage--------",1);

        killPlayer();
        launchPlayer();
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

        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        debug("list_view"+list_view.getBounds(),1);
        UiObject more_show;
        more_show = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        debug("more_show="+more_show.getBounds(),1);
        more_show.clickAndWaitForNewWindow();
        String wait;
        wait = "Please wait 5 seconds for the show content loading.";
        waitMsg(wait,5000);

        /*精选*/
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject banner;
        banner = list_view.getChild(new UiSelector().className("android.widget.ImageView").index(0));
        debug(String.format("banner=%s", banner.getBounds()),1);
        banner.clickAndWaitForNewWindow();
        sleep(2000);
        swipePhone(TOP);
        sleep(1000);
        device.pressBack();
        sleep(1000);
        swipePhone(TOP);
        sleep(1000);

        UiObject rank;
        rank = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        debug("rank="+rank.getBounds(),1);
        UiObject new_movies;
        new_movies = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(2));
        debug("new_movies="+new_movies.getBounds(),1);

        /*排行*/
        rank.click();
        waitMsg(wait,5000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        more_show = list_view.getChild(new UiSelector().className("android.widget.LinearLayout"))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.Button"));
        debug("more_show"+more_show.getBounds(),1);
        more_show.clickAndWaitForNewWindow();
        waitMsg(wait,5000);
        device.pressBack();
        sleep(1000);
        swipePhone(TOP);
        swipePhone(LEFT);
        sleep(1000);

        /*最新*/
        new_movies.click();
        waitMsg(wait,5000);
        swipePhone(TOP);
        int rnd;
        UiObject filter;
        filter = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        debug("filter="+filter.getBounds(),1);
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
        debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);
        rnd = randomIndex(5);
        for (int j = 0; j < rnd ;j++) {
            device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        }
        UiObject picker_kind;
        picker_kind = new UiObject(new UiSelector().className("android.widget.NumberPicker").index(1));
        picker_top = picker_kind.getBounds().top;
        picker_bottom = picker_kind.getBounds().bottom;
        start_x = picker_kind.getBounds().centerX();
        start_y = picker_bottom-100;
        end_x = start_x;
        end_y = picker_top+100;
        debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);
        rnd = randomIndex(5);
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
        debug(String.format("%d,%d,%d,%d", start_x, start_y, end_x, end_y),1);
        rnd = randomIndex(5);
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
        killPlayer();
    }

    private void comicPage() throws IOException {
        /*动漫*/
        debug("--------comicPage--------",1);

        killPlayer();
        launchPlayer();

    }

    private void documentPage() throws IOException {
        /*纪录片*/
        debug("--------documentPage--------",1);

        killPlayer();
        launchPlayer();

    }

    private void topicPage() throws IOException {
        /*专题*/
        debug("--------topicPage--------",1);

        killPlayer();
        launchPlayer();

    }
}
