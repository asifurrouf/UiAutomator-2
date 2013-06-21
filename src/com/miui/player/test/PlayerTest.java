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
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.android.uiautomator.core.UiObjectNotFoundException;

import java.io.IOException;


public class PlayerTest extends UiAutomatorTestCase{

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

    private static final int TOP = 0;
    private static final int BOTTOM = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;
    private static final int CENTRAL = 4;

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
        debug("setUp");
        super.setUp();
        device = getUiDevice();
        width = device.getDisplayWidth();
        height = device.getDisplayHeight();
        debug("width=" + width + " height=" + height);
        wakePhone();

    }

    public void log(String log_msg){
        /*log*/
        Log.d(LOG_TAG,log_msg);
    }

    public void lockPhone() throws RemoteException {
        /*锁屏*/
        debug("lockPhone");
        if (device.isScreenOn())
            device.sleep();
        sleep(2000);
    }


    public void wakePhone() throws RemoteException {
        /*唤醒手机*/
        debug("wakePhone");
        device.wakeUp();
        sleep(3000);
    }

    public void unlockPhone() throws RemoteException {
        /*解锁*/
        debug("unlockPhone");
        int phone_type = phoneType();
        int start_x = 0;
        int start_y = 0;
        int end_x = 0;
        int end_y = 0;
        start_x = unlock_start_point[0][phone_type];
        start_y = unlock_start_point[1][phone_type];
        end_x = unlock_end_point[0][phone_type];
        end_y = unlock_end_point[1][phone_type];
        /*debug("swipe_point="+","+start_x+","+start_y+","+end_x+","+end_y);*/
        device.swipe(start_x, start_y, end_x, end_y, SWIPE_STEPS);
        sleep(1000);
    }

    private int phoneType(){
        /*手机分辨率类型*/
        debug("phoneType");
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
        /*debug("phone_type="+phone_type);*/
        return phone_type;
    }

    public void swipePhone(int type){
        /*滑动手机屏幕*/
        debug("swipePhone+"+type);
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
        /*debug("point="+start_x+","+start_y+","+end_x+","+end_y);*/
        device.swipe(start_x,start_y,end_x,end_y,SWIPE_STEPS);
        sleep(1000);
    }

    public void killPlayer() throws IOException {
        /*杀掉播放器*/
        debug("killPlayer");
        String kill = "am kill " + PLAYER_PAC_NAME;
        String force_stop = "am force-stop " + PLAYER_PAC_NAME;
        Runtime.getRuntime().exec(kill);
        Runtime.getRuntime().exec(force_stop);
        sleep(2000);
    }

    public void launchPlayer() throws IOException {
        /*打开播放器*/
        debug("launchPlayer");
        String launch = "am start -n com.miui.player/.ui.MusicBrowserActivity";
        Runtime.getRuntime().exec(launch);
        sleep(3000);
    }

    public void debug(String debug_msg){
        /*打印信息*/
        System.out.println("<"+debug_msg+">");
    }

    public int randomIndex(int area){
        /*获取随机数*/
        debug("randomIndex");
        int rnd;
        rnd = (int) (Math.random() * area);
        if (rnd==0) {
            return randomIndex(area);
        }
        else
            debug("rnd="+rnd);
            return rnd;
    }

    public void testPlayer() throws IOException, UiObjectNotFoundException, RemoteException {
        /*测试*/
        debug("testPlayer");

        lockPhone();
        wakePhone();
        unlockPhone();

        //songPage();
        //homeTop();
        //singerPage();
        //listPage();
        //onlinePage();
        nowplayingPage();
    }

    private void homeTop() throws UiObjectNotFoundException, IOException {
        /*首页顶栏*/
        debug("homeTop");

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        UiObject top_view;
        top_view = new UiObject(new UiSelector().className("android.view.View").index(0))
                .getChild(new UiSelector().className("android.widget.RelativeLayout").index(3));
        /*debug("top_view="+top_view.getBounds());*/
        int top_view_child_count;
        top_view_child_count = top_view.getChildCount();
        UiObject play_pause = null;
        UiObject title = null;
        for (int i = 0; i < top_view_child_count;i++){
            top_view = new UiObject(new UiSelector().className("android.view.View").index(0))
                    .getChild(new UiSelector().className("android.widget.RelativeLayout").index(3));
            if (i==0){
                title = top_view.getChild(new UiSelector().className("android.widget.TextView").index(0));
                /*debug("title="+title.getBounds());*/
            }
            else if (i==1){
                play_pause = top_view.getChild(new UiSelector().className("android.widget.ImageView").index(2));
                /*debug("play_pause="+play_pause.getBounds());*/
            }
        }
        if (null != play_pause) {
            play_pause.click();
            sleep(2000);
            play_pause.click();
            sleep(2000);
        }
        if (null != title) {
            title.click();
            sleep(1000);
        }
        device.pressBack();
        sleep(1000);
    }

    private void songPage() throws UiObjectNotFoundException, IOException {
        /*歌曲页*/
        debug("songPage");

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        for (int i = 0;i < 1;i++){
            /*debug("swipe=" + i);*/
            swipePhone(TOP);
        }
        sleep(1000);
        UiObject page;
        page = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        /*debug("tmp=" + tmp.getBounds());*/
        UiObject play_all;
        play_all = page.getChild(new UiSelector().className("android.widget.RelativeLayout").index(0));
        /*debug("play_all=" + play_all.getBounds());*/
        play_all.click();
        sleep(1000);
        device.pressBack();
        sleep(1000);
        int list_count;
        list_count = list_view.getChildCount() -2;
        /*debug("list_count=" + list_count);*/
        int rnd;
        rnd = randomIndex(list_count)+1;
        /*debug("rnd=" + rnd);*/
        UiObject song;
        song = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(rnd));
        /*debug("song="+song.getBounds());*/
        sleep(1000);
        song.click();
        sleep(1000);
        device.pressBack();
        sleep(1000);

        /* 编辑模式 */
        song.longClick();
        /* 编辑模式：取消 全选 取消全选*/
        UiObject select_buttons;
        UiObject cancel = null;
        select_buttons = new UiObject(new UiSelector().className("android.view.View").index(1))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
        UiObject select_all = null;
        int select_buttons_count;
        select_buttons_count = select_buttons.getChildCount();
        /*debug("select_buttons_count="+select_buttons_count);*/
        for (int i = 0; i<select_buttons_count;i++){
            select_buttons = new UiObject(new UiSelector().className("android.view.View").index(1))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
            //debug("i>>"+i+">"+select_buttons.getChild(new UiSelector().className(android.widget.TextView.class.getName()).index(i)).getBounds());
            if (i==0)
                cancel = select_buttons.getChild(new UiSelector().index(i));
            else if (i==2)
                select_all = select_buttons.getChild(new UiSelector().index(i));
        }
        /*debug("select_all=" + select_all.getBounds());*/
        for (int i = 0;i<2;i++){
            if (null != select_all) {
                select_all.click();
            }
            sleep(2000);
        }
        /*debug("cancel=" + cancel.getBounds());*/
        if (null != cancel) {
            cancel.click();
        }
        sleep(1000);
        for (int i=0;i < 3;i++) {
            song.longClick();
            UiObject e_play;
            e_play = new UiObject(new UiSelector().className("android.widget.Button").instance(0));
            UiObject e_add_to;
            e_add_to = new UiObject(new UiSelector().className("android.widget.Button").instance(1));
            UiObject e_delete;
            e_delete = new UiObject(new UiSelector().className("android.widget.Button").instance(2));
            switch (i){
                case 0:
                    /*播放*/
                    e_play.click();
                    break;
                case 1:
                    /*添加到*/
                    e_add_to.click();
                    device.pressBack();
                    break;
                case 2:
                    /*删除*/
                    e_delete.click();
                    device.pressBack();
                    break;
            }
            sleep(1000);
            device.pressBack();
        }
        song.longClick();
        UiObject e_more;
        e_more = new UiObject(new UiSelector().className("android.widget.Button").instance(3));
        e_more.click();
        UiObject more_menu;
        more_menu = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
        /*debug("more_menu="+more_menu.getBounds());*/
        int more_menu_child_count;
        more_menu_child_count = more_menu.getChildCount();
        UiObject m_fav = null;
        UiObject m_send = null;
        UiObject m_set_ring = null;
        UiObject m_id3 = null;
        for (int i = 0; i < more_menu_child_count;i++){
            more_menu = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
            if (i==0) {
                m_fav = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_fav="+m_fav.getBounds());*/
            }
            else if (i==1){
                m_send = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(1));
                /*debug("m_send=" + m_send.getBounds());*/
            }
            else if (i==2){
                m_set_ring = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(2));
                /*debug("m_set_ring="+m_set_ring.getBounds());*/
            }
            else if (i==3){
                m_id3 = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(3));
                /*debug("m_id3="+m_id3.getBounds());*/
            }
        }
        /*喜欢*/
        m_fav.click();
        /*发送*/
        song.longClick();
        e_more.click();
        m_send.click();
        device.pressBack();
        /*用作手机铃声*/
        e_more.click();
        m_set_ring.click();
        /*修改歌曲信息*/
        song.longClick();
        e_more.click();
        m_id3.click();
        device.pressBack();
        device.pressBack();
        killPlayer();
        sleep(1000);
    }

    private void singerPage() throws IOException, UiObjectNotFoundException {
        /*歌手页*/
        debug("singerPage");

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        for (int i = 0;i < 1;i++){
            swipePhone(LEFT);
            sleep(1000);
        }
        swipePhone(TOP);
        sleep(1000);

        UiObject page;
        page = new UiObject(new UiSelector().className("android.widget.ListView").index(1));
        debug("list_view=" + page.getBounds());
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(1));
        debug("list_view=" + list_view.getBounds());
        UiObject albums;
        albums = page.getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
        debug("albums=" + albums.getBounds());
        albums.click();
        sleep(1000);
        device.pressBack();
        sleep(1000);
        int list_count;
        list_count = list_view.getChildCount()-2;
        int rnd;
        rnd = randomIndex(list_count)+1;
        UiObject singer;
        singer = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
        debug("singer="+singer.getBounds());
        singer.click();
        sleep(1000);
        device.pressBack();

        /*编辑模式*/
        singer.longClick();
        /* 编辑模式：取消 全选 取消全选*/
        UiObject select_buttons;
        UiObject cancel = null;
        select_buttons = new UiObject(new UiSelector().className("android.view.View").index(1))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
        UiObject select_all = null;
        int select_buttons_count;
        select_buttons_count = select_buttons.getChildCount();
        /*debug("select_buttons_count="+select_buttons_count);*/
        for (int i = 0; i<select_buttons_count;i++){
            select_buttons = new UiObject(new UiSelector().className("android.view.View").index(1))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
            if (i==0)
                cancel = select_buttons.getChild(new UiSelector().index(i));
            else if (i==2)
                select_all = select_buttons.getChild(new UiSelector().index(i));
        }
        /*debug("select_all=" + select_all.getBounds());*/
        for (int i = 0;i<2;i++){
            if (null != select_all) {
                select_all.click();
            }
            sleep(2000);
        }
        /*debug("cancel=" + cancel.getBounds());*/
        if (null != cancel) {
            cancel.click();
        }
        sleep(1000);
        for (int i=0;i < 3;i++) {
            switch (i){
/*                MIUI-15431
                        <音乐> 歌手tab页 编辑模式 选择项目 点击编辑模式的播放按钮 不退出编辑模式*/
                case 0:
                    /*播放*/
                    singer.longClick();
                    UiObject e_play;
                    e_play = new UiObject(new UiSelector().className("android.widget.Button").instance(0));
                    e_play.click();
                    break;
                case 1:
                    /*添加到*/
                    UiObject e_add_to;
                    e_add_to = new UiObject(new UiSelector().className("android.widget.Button").instance(1));
                    e_add_to.click();
                    break;
                case 2:
                    /*删除*/
                    UiObject e_delete;
                    e_delete = new UiObject(new UiSelector().className("android.widget.Button").instance(2));
                    e_delete.click();
                    device.pressBack();
                    break;
            }
            sleep(1000);
            device.pressBack();
            sleep(1000);
        }
        killPlayer();
    }

    private void listPage() throws IOException, UiObjectNotFoundException {
        /*列表页*/
        debug("listPage");

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        for (int i = 0;i < 2;i++){
            swipePhone(LEFT);
            sleep(1000);
        }
        swipePhone(TOP);
        sleep(1000);

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(2));
        /*debug("list_view=" + list_view.getBounds());*/

        UiObject folders;
        UiObject fav_list = null;
        UiObject last_played_list = null;
        UiObject last_added_list = null;
        UiObject most_played_list = null;
        UiObject new_list;

        for (int i = 0; i < 6;i++){
            list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(2));
            /*debug("list_view=" + list_view.getBounds());*/
            switch (i){
                case 0:
                    folders = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
                    /*debug("folders=" + folders.getBounds());*/
                    folders.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 1:
                    fav_list = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
                    /*debug("fav_list=" + fav_list.getBounds());*/
                    fav_list.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 2:
                    last_played_list = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
                    /*debug("last_played_list=" + last_played_list.getBounds());*/
                    last_played_list.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 3:
                    last_added_list = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
                    /*debug("last_added_list=" + last_added_list.getBounds());*/
                    last_added_list.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 4:
                    most_played_list = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
                    /*debug("most_played_list=" + most_played_list.getBounds());*/
                    most_played_list.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 5:
                    new_list = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(i));
                    /*debug("new_list=" + new_list.getBounds());*/
                    new_list.click();
                    sleep(1000);
                    UiObject new_list_name = new UiObject(new UiSelector().className("android.widget.EditText").index(0));
                    int list_num;
                    list_num = randomIndex(99999);
                    new_list_name.setText("new list"+list_num);
                    sleep(2000);
                    UiObject confirm_button = new UiObject(new UiSelector().className("android.widget.Button").index(1));
                    /*debug("confirm_button="+confirm_button.getBounds());*/
                    confirm_button.click();
                    sleep(1000);
                    UiObject select_all;
                    select_all = new UiObject(new UiSelector().className("android.view.View").index(0))
                            .getChild(new UiSelector().className("android.widget.Button").index(2));
                    /*debug("select_all="+select_all.getBounds());*/
                    select_all.click();
                    sleep(1000);
                    UiObject finish_button;
                    finish_button = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0))
                            .getChild(new UiSelector().className("android.widget.Button").index(0));
                    /*debug("finish_button="+finish_button.getBounds());*/
                    finish_button.click();
                    sleep(1000);
                    break;
            }
            sleep(1000);
        }

        UiObject the_create_list;
        the_create_list = new UiObject(new UiSelector().className("android.widget.ListView").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(5));
        /*debug("the_create_list="+the_create_list.getBounds());*/
        the_create_list.longClick();
        UiObject long_click_list;
        int long_click_list_child_count = 0;
        long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject long_click_delete_button;
        long_click_delete_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
        /*debug("long_click_delete_button="+long_click_delete_button.getBounds());*/
        long_click_delete_button.click();
        sleep(1000);

        /*长按操作*/
        for (int i = 0; i < 4 ;i++){
            switch (i){
                case 0:
                    debug("fav_list");
                    fav_list.longClick();
                    sleep(1000);
                    long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                    long_click_list_child_count = long_click_list.getChildCount();
                    device.pressBack();
                    for (int j = 0;j < long_click_list_child_count;j++){
                        fav_list.longClick();
                        sleep(1000);
                        long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                        UiObject list_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                        /*debug(String.format("list_button(%d) %s", j, list_button.getBounds()));*/
                        list_button.click();
                        sleep(2000);
                        device.pressBack();
                        sleep(2000);
                    }
                    break;
                case 1:
                    debug("last_played_list");
                    last_played_list.longClick();
                    sleep(1000);
                    long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                    long_click_list_child_count = long_click_list.getChildCount();
                    device.pressBack();
                    for (int j = 0;j < long_click_list_child_count;j++){
                        last_played_list.longClick();
                        sleep(1000);
                        long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                        UiObject list_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                        debug(String.format("list_button(%d) %s", j, list_button.getBounds()));
                        list_button.click();
                        sleep(2000);
                        if (j != 0)
/*                            MIUI-15430
                                    <音乐> 长按最近播放列表 选择播放 不进入正在播放页*/
                            device.pressBack();
                        sleep(2000);
                    }
                    break;
                case 2:
                    debug("last_added_list");
                    last_added_list.longClick();
                    sleep(1000);
                    long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                    long_click_list_child_count = long_click_list.getChildCount();
                    device.pressBack();
                    for (int j = 0;j < long_click_list_child_count;j++){
                        last_added_list.longClick();
                        sleep(1000);
                        long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                        UiObject list_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                        debug(String.format("list_button(%d) %s", j, list_button.getBounds()));
                        list_button.click();
                        sleep(2000);
                        device.pressBack();
                        sleep(2000);
                    }
                    break;
                case 3:
                    debug("most_played_list");
                    most_played_list.longClick();
                    sleep(1000);
                    long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                    long_click_list_child_count = long_click_list.getChildCount();
                    device.pressBack();
                    for (int j = 0;j < long_click_list_child_count;j++){
                        most_played_list.longClick();
                        sleep(1000);
                        long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                        UiObject list_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                        debug(String.format("list_button(%d) %s", j, list_button.getBounds()));
                        list_button.click();
                        sleep(2000);
                        device.pressBack();
                        sleep(2000);
                    }
                    break;
            }
        }
        killPlayer();
    }

    private void onlinePage() throws IOException, UiObjectNotFoundException {
        /*在线页*/
        debug("onlinePage");

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        for (int i = 0;i < 3;i++){
            swipePhone(LEFT);
            sleep(1000);
        }
        swipePhone(TOP);
        sleep(1000);

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(3));
        /*debug("list_view=" + list_view.getBounds());*/
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        UiObject recommend;
        UiObject top;
        UiObject fm;
        for (int i = 0; i < list_view_child_count;i++){
            list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(3));
            switch (i){
                case 0:
                    recommend = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(i));
                    /*debug("recommend="+recommend.getBounds());*/
                    recommend.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 1:
                    top = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(i));
                    /*debug("top="+top.getBounds());*/
                    top.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 2:
                    fm = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(i));
                    /*debug("fm="+fm.getBounds());*/
                    fm.click();
                    sleep(1000);
                    device.pressBack();
                    break;
            }
            sleep(1000);
        }
        killPlayer();
    }

    private void nowplayingPage() throws IOException, UiObjectNotFoundException {
        /*正在播放页*/
        debug("nowplayingPage");

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject play_all;
        play_all = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(0));
        play_all.clickAndWaitForNewWindow();
        sleep(2000);

        for (int i = 0;i < 2;i++){
            /*防止停留在列表页*/
            swipePhone(RIGHT);
            sleep(1000);
        }
        for (int i = 0;i < 2;i++){
            /*歌词页面*/
            swipePhone(LEFT);
            sleep(1000);
        }
        for (int i = 0;i < 1;i++){
            /*返回中间封面页*/
            swipePhone(RIGHT);
            sleep(1000);
        }
        swipePhone(CENTRAL);
        sleep(1000);

        /*喜欢*/
        UiObject top;
        top = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.RelativeLayout").index(1));
        /*debug("top="+top.getBounds());*/
        UiObject fav;
        fav = top.getChild(new UiSelector().className("android.widget.ImageView").index(1));
        /*debug("fav="+fav.getBounds());*/
        fav.click();
        sleep(1000);
        fav.click();
        sleep(1000);

        /*米联 均衡器 顺序 下载 按钮*/
        UiObject function_buttons_list;
        UiObject mi_button;
        UiObject equalizer;
        UiObject order;
        UiObject download;
        function_buttons_list = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(1));
        /*debug("function_buttons_list"+function_buttons_list.getBounds());*/
        int function_buttons_list_child_count;
        function_buttons_list_child_count = function_buttons_list.getChildCount();
        for (int i = 0; i < function_buttons_list_child_count ;i++){
            function_buttons_list = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(1));
            if (function_buttons_list_child_count > 3 ){
                switch (i){
                    case 0:
                        mi_button = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("mi_button="+mi_button.getBounds());*/
                        mi_button.click();
                        sleep(1000);
                        device.pressBack();
                        sleep(1000);
                        break;
                    case 1:
                        equalizer = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("equalizer="+equalizer.getBounds());*/
                        equalizer.click();
                        sleep(1000);
                        device.pressBack();
                        break;
                    case 2:
                        order = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("order="+order.getBounds());*/
                        for (int j = 0; j < 3 ;j++){
                            order.click();
                            sleep(1000);
                        }
                        break;
                    case 3:
                        download = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("download="+download.getBounds());*/
                        download.click();
                        sleep(1000);
                        break;
                }
            }
            else {
                switch (i){
                    case 0:
                        equalizer = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("equalizer="+equalizer.getBounds());*/
                        equalizer.click();
                        sleep(1000);
                        device.pressBack();
                        break;
                    case 1:
                        order = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("order="+order.getBounds());*/
                        for (int j = 0; j < 3 ;j++){
                            order.click();
                            sleep(1000);
                        }
                        break;
                    case 2:
                        download = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("download="+download.getBounds());*/
                        download.click();
                        sleep(1000);
                        break;
                }
            }

        }

        /*播放控制*/
        UiObject control_buttons;
        UiObject prev;
        UiObject play_pause;
        UiObject next;
        control_buttons = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(6))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
        int control_buttons_child_count;
        control_buttons_child_count = control_buttons.getChildCount();
        for (int i = 0; i<control_buttons_child_count;i++){
            control_buttons = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(6))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
            switch (i){
                case 0:
                    prev = control_buttons.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("prev="+prev.getBounds());*/
                    prev.click();
                    sleep(1000);
                    break;
                case 1:
                    play_pause = control_buttons.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("play_pause="+play_pause.getBounds());*/
                    for (int j = 0; j < 2;j++){
                        play_pause.click();
                        sleep(1000);
                    }
                    break;
                case 2:
                    next = control_buttons.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("next="+next.getBounds());*/
                    next.click();
                    sleep(1000);
                    break;
            }
        }
        UiObject seek_bar;
        seek_bar = new UiObject(new UiSelector().className("android.widget.SeekBar").index(1));
        debug("seek_bar="+seek_bar.getBounds());
        seek_bar.click();
        sleep(5000);
        seek_bar.click();
        sleep(2000);

        /*Menu菜单项*/
        device.pressMenu();
        sleep(1000);
        UiObject menu_list;
        menu_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject menu_button;
        int menu_list_child_count;
        menu_list_child_count = menu_list.getChildCount();
        for (int i = 0; i < menu_list_child_count;i++){
            menu_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
            menu_button = menu_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
            debug("menu_button="+menu_button.getBounds());
            menu_button.click();
            sleep(1000);
            device.pressBack();
            sleep(1000);
            device.pressMenu();
            sleep(1000);
        }

        killPlayer();
    }

}
