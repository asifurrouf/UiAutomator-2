package com.miui.player.test;
/**
 * Created with IntelliJ IDEA.
 * User: jiahuixing
 * Date: 13-6-8
 * Time: 下午4:55
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
import com.android.uiautomator.core.UiScrollable;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.IOException;
import java.util.Date;


public class MmsWithAudio extends UiAutomatorTestCase{

    protected UiDevice device = null;

    private static final int TEST_TIMES = 30;
    private static final String LOG_TAG = "MIUI_PlayerTest";
    private static final String PLAYER_PAC_NAME = "com.miui.player";
    private static final int SWIPE_STEPS = 10;
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

    private static final String SONG = "song";
    private static final String SINGER = "singer";
    private static final String LIST = "list";
    private static final String ONLINE = "online";
    private static final String ALBUMS = "albums";
    private static final String FOLDER = "folder";
    private static final String COMMON = "common";

    private static final int[][] unlock_start_point= {
            {240,360,540,},
            {615,920,920,},
    };
    private static final int[][] unlock_end_point= {
            {240,360,540,},
            {820,1180,1180,},
    };

    private void debug(String debug_msg,int wrap){
        if (wrap == 1){
            System.out.println(" <"+debug_msg+"> ");
        }   else {
            System.out.print(" <"+debug_msg+"> ");
        }
    }

    public void testMmsWithAudio(){

    }

    private void launchSms(){

        debug("launchSms",1);
        sleep(2000);


    }


}
