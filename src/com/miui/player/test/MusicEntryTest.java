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


public class MusicEntryTest extends UiAutomatorTestCase{

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
            System.out.println(" <" + debug_msg + "> ");
        }   else {
            System.out.print(" <" + debug_msg + "> ");
        }
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

    public void testMmsWithAudio() throws UiObjectNotFoundException {

        /*短信中音频*/
        debug("",1);

        launchSms();
        UiObject new_sms;
        new_sms = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        debug(String.format("new_sms=%s", new_sms.getBounds()),1);
        new_sms.clickAndWaitForNewWindow();
        sleep(1000);
        UiObject add_attach;
        add_attach = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(1));
        debug(String.format("add_attach=%s", add_attach.getBounds()),1);
        add_attach.click();
        sleep(1000);
        UiObject view;
        view = new UiObject(new UiSelector().className("android.view.View").index(2));
        debug(String.format("view=%s", view.getBounds()),1);
        UiObject audio;
        audio = view.getChild(new UiSelector().className("android.widget.LinearLayout").index(3))
                .getChild(new UiSelector().className("android.widget.ImageView").index(0));
        debug(String.format("audio=%s", audio.getBounds()),1);
        audio.clickAndWaitForNewWindow();
        sleep(500);
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        UiObject local_audio;
        local_audio = list_view.getChild(new UiSelector().className("android.widget.TextView").index(1));
        debug(String.format("local_audio=%s", local_audio.getBounds()),1);
        local_audio.clickAndWaitForNewWindow();
        sleep(500);
        UiObject loading;
        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        while (true){
            if (loading.exists()){
                debug("wait for 0.5 second.",1);
                sleep(500);
            }   else {
                break;
            }
        }
        UiObject media_storage;
        media_storage = new UiObject(new UiSelector().className("android.widget.TextView").text("媒体存储"));
        debug(String.format("media_storage=%s", media_storage.getBounds()),1);
        media_storage.clickAndWaitForNewWindow();
        sleep(500);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
        debug(String.format("list_view=%s", list_view.getBounds()),1);
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        int rnd;
        rnd = randomIndex(list_view_child_count);
        UiObject media;
        media = list_view.getChild(new UiSelector().className("miui.widget.CheckedTextView").index(rnd));
        debug(String.format("media=%s", media.getBounds()),1);
        media.click();
        UiObject confirm;
        confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        confirm.click();
        sleep(500);
        UiObject mms_content;
        mms_content = new UiObject(new UiSelector().className("android.widget.ScrollView"));
        debug(String.format("mms_content=%s", mms_content.getBounds()),1);
        media = mms_content.getChild(new UiSelector().className("android.widget.ImageView"));
        debug(String.format("media=%s", media.getBounds()),1);
        media.click();
        int start_x = 0,start_y = 0,end_x = 0,end_y = 0;
        device.swipe(start_x,end_x,start_y,end_y,SWIPE_STEPS);

    }

    private void launchSms() throws UiObjectNotFoundException {

        /*启动短信*/
        debug("launchSms",1);
        device.pressHome();
        sleep(1000);
        UiObject sms;
        sms = new UiObject(new UiSelector().className("android.widget.FrameLayout").description("短信"));
        debug(String.format("sms=%s", sms.getBounds()),1);
        sms.clickAndWaitForNewWindow();
        sleep(1000);

    }


}
