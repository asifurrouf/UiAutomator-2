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

import java.io.IOException;


public class OnlineVideoTest extends UiAutomatorTestCase{

    protected UiDevice device = null;

    private static final int TEST_TIMES = 30;
    private static final String LOG_TAG = "MIUI_PlayerTest";
    private static final String PLAYER_PAC_NAME = "com.miui.video";
    private static final int SWIPE_STEPS = 10;
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
                end_y = height / 2;
                break;
            case BOTTOM:
                start_x = width / 2;
                start_y = height / 2;
                end_x = width / 2;
                end_y = height*3 / 4;
                break;
            case LEFT:
                start_x = width*2 / 3;
                start_y = height*2 / 3;
                end_x = width / 3;
                end_y = height*2 / 3;
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
        String launch = "am start -n com.miui.player/.ui.MusicBrowserActivity";
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

    public void testOnlineVideo() throws RemoteException {
        /*测试*/
        debug("testOnlineVideo",1);

        lockPhone();
        wakePhone();
        unlockPhone();


    }

    private void tvPage() throws IOException {
        /*电视剧*/
        debug("tvPage",1);

        killPlayer();
        launchPlayer();

    }

    private void moviePage() throws IOException {
        /*电影*/
        debug("moviePage",1);

        killPlayer();
        launchPlayer();

    }

    private void showPage() throws IOException {
        /*综艺*/
        debug("showPage",1);

        killPlayer();
        launchPlayer();

    }

    private void comicPage() throws IOException {
        /*动漫*/
        debug("comicPage",1);

        killPlayer();
        launchPlayer();

    }

    private void documentPage() throws IOException {
        /*纪录片*/
        debug("documentPage",1);

        killPlayer();
        launchPlayer();

    }

    private void topicPage() throws IOException {
        /*专题*/
        debug("topicPage",1);

        killPlayer();
        launchPlayer();

    }
}
