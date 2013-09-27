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


public class MusicPlayerTest extends UiAutomatorTestCase{

    protected static UiDevice device = null;

    private static final int TEST_TIMES = 30;
    private static final String LOG_TAG = "MIUI_PlayerTest";
    private static final String PLAYER_PAC_NAME = "com.miui.player";
    private static final int SWIPE_STEPS = 10;
    private static int width;
    private static int height;
    private static int clear_data = 0;

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

    private static final String ZERO = "zero";
    private static final String NOT_ZERO = "not_zero";

    private static final int[][] unlock_start_point= {
            {240,360,540,},
            {615,920,920,},
    };
    private static final int[][] unlock_end_point= {
            {240,360,540,},
            {820,1180,1180,},
    };

    protected void setUp() throws Exception {
        /*setUp*/
        debug("setUp",1);
        super.setUp();
        clearMusicData();

    }

    public void log(String log_msg){
        /*log*/
        Log.d(LOG_TAG,log_msg);
    }

    public void lockPhone() throws RemoteException {
        /*锁屏*/
        debug("lockPhone",1);
        if (device.isScreenOn())
            device.sleep();
        sleep(1000);
    }


    public void wakePhone() throws RemoteException {
        /*唤醒手机*/
        debug("wakePhone",1);
        device.wakeUp();
        sleep(1000);
    }

    public void unlockPhone() throws RemoteException {
        /*解锁*/
        debug("unlockPhone",1);
        int phone_type = phoneType();
        debug(String.format("phone_type=%d", phone_type),1);
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

    public void swipePhone(String type){
        /*滑动手机屏幕*/
        debug("swipePhone:"+type,1);
        int start_x = 0;
        int start_y = 0;
        int end_x = 0;
        int end_y = 0;
        if (type.equals(TOP)) {
            start_x = width / 2;
            start_y = height*3 / 4;
            end_x = width / 2;
            end_y = height / 2;
        }
        else if (type.equals(BOTTOM)) {
            start_x = width / 2;
            start_y = height / 2;
            end_x = width / 2;
            end_y = height*3 / 4;
        }
        else if (type.equals(LEFT)) {
            start_x = width*2 / 3;
            start_y = height*2 / 3;
            end_x = width / 3;
            end_y = height*2 / 3;
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
        sleep(1000);
    }

    public void launchPlayer() throws IOException {
        /*打开播放器*/
        debug("launchPlayer",1);
        String launch = "am start -n com.miui.player/.ui.MusicBrowserActivity";
        Runtime.getRuntime().exec(launch);
        sleep(1000);
    }



    public void debug(String debug_msg,int wrap){
        /*打印信息*/
        if (wrap == 1)
            System.out.println("<"+debug_msg+">");
        else
            System.out.print(" <"+debug_msg+"> ");
    }

    public int randomIndex(int area,String zero_or_not){
        /*获取随机数*/
        debug(String.format("randomIndex:%s", zero_or_not),1);
        int rnd;
        rnd = (int) (Math.random() * area);
        if (zero_or_not.equals(ZERO)){
            return rnd;
        }else {
            if (rnd==0) {
                return randomIndex(area,NOT_ZERO);
            }
            else  {
                debug("rnd="+rnd,1);
                return rnd;
            }
        }
    }

/*    public void testPlayer() throws IOException, UiObjectNotFoundException, RemoteException {
        *//*测试*//*
        debug("testPlayer",1);

        lockPhone();
        wakePhone();
        unlockPhone();

        clearMusicData();

        for (int i = 0;i < TEST_TIMES;i++){
            debug("--------Test:"+(i+1)+"--------",1);
            homeTop();
            songPage();
            singerPage();
            listPage();
            onlinePage();
            nowplayingPage();
            albumsPage();
            folderPage();
            listdetailPage();
            onlinedetailPage();
            getBugreport();
        }

        lockPhone();
    }*/

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

    private void clearMusicData() throws IOException, RemoteException, UiObjectNotFoundException {
        /*音乐数据清空*/
        if (clear_data == 0){

            device = getUiDevice();
            width = device.getDisplayWidth();
            height = device.getDisplayHeight();
            debug("width=" + width + " height=" + height,1);
            wakePhone();
            lockPhone();
            wakePhone();
            unlockPhone();

            debug("--------clearMusicData--------",1);

            killPlayer();
            device.pressRecentApps();
            UiObject clear_all;
            clear_all = new UiObject(new UiSelector().className("android.view.View").index(2));
            clear_all.click();
            sleep(1000);
            device.pressBack();
            sleep(500);
            launchPlayer();
            sleep(1000);
            device.pressHome();
            sleep(1000);
            device.pressRecentApps();
            sleep(1000);

            UiObject view;
            view = new UiObject(new UiSelector().className("android.view.View").index(1));
            UiObject music;
            music = view.getChild(new UiSelector().className("android.widget.TextView").index(0));
        /*debug(String.format("music=%s", music.getBounds()),1);*/
            music.longClick();
            sleep(1000);
            swipePhone(TOP);
            sleep(1000);
            UiObject clear;
            clear = new UiObject(new UiSelector().className("android.widget.Button").index(0).instance(0));
        /*debug(String.format("clear=%s", clear.getBounds()),1);*/
            while (true){
                if (clear.isEnabled()){
                    break;
                }else{
                    String wait;
                    wait = "clear button is not enable now,pls wait";
                    waitMsg(wait,1000);
                }
            }
            clear.click();
            sleep(1000);
            UiObject confirm;
            confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        /*debug(String.format("confirm=%s", confirm.getBounds()),1);*/
            confirm.click();
            sleep(1000);
            device.pressBack();
            device.pressBack();
            clear_data = 1;
        }
    }

    private void gotoPage(String page) throws UiObjectNotFoundException {
        /*进入指定页面*/

        debug(page,1);

        if (page.equals(SINGER)){
            for (int i = 0;i < 1;i++){
                swipePhone(LEFT);
                sleep(1000);
            }
            swipePhone(TOP);
            sleep(1000);
        }
        else if (page.equals(LIST)){
            for (int i = 0;i < 2;i++){
                swipePhone(LEFT);
                sleep(1000);
            }
            swipePhone(TOP);
            sleep(1000);
        }
        else if (page.equals(ONLINE)){
            for (int i = 0;i < 3;i++){
                swipePhone(LEFT);
                sleep(1000);
            }
            swipePhone(TOP);
            sleep(1000);
        }
        else if (page.equals(ALBUMS)){
            for (int i = 0;i < 1;i++){
                swipePhone(LEFT);
                sleep(1000);
            }

            UiObject list_view;
            list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(1));
            UiObject albums;
            albums = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
            albums.click();
            sleep(1000);
        }
        else if (page.equals(FOLDER)){
            for (int i = 0;i < 2;i++){
                swipePhone(LEFT);
                sleep(1000);
            }

            UiObject list_view;
            UiObject folders;
            list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(2));
            folders = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
            folders.click();
            sleep(1000);
        }
    }

    private void pageMenu(String page) throws IOException, UiObjectNotFoundException {
        /*首页Menu菜单*/
        debug("pageMenu:"+page,1);

        UiObject menu_list;
        int menu_list_child_count;
        UiObject menu_button;
        if (page == SONG){
            /*歌曲页Menu*/
            device.pressMenu();
            sleep(1000);
            menu_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
            /*menu_list_child_count = menu_list.getChildCount();*/
            sleep(1000);
            for (int j = 0; j < 1;j++){
                sleep(1000);
                menu_button = menu_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                /*debug("menu_button"+menu_button.getBounds(),1);*/
                menu_button.click();
                sleep(1000);
            }
        }
        else if (page == SINGER){
            /*歌手页Menu*/
            menu_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
            /*menu_list_child_count = menu_list.getChildCount();*/
            for (int j = 0; j < 1;j++){
                device.pressMenu();
                sleep(1000);
                menu_button = menu_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                /*debug("menu_button"+menu_button.getBounds(),1);*/
                menu_button.click();
                sleep(1000);
            }
        }
        else if (page == ALBUMS){
            /*专辑页Menu*/
            menu_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
            /*menu_list_child_count = menu_list.getChildCount();*/
            sleep(1000);
            for (int j = 0; j < 1;j++){
                device.pressMenu();
                sleep(1000);
                menu_button = menu_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                /*debug("menu_button"+menu_button.getBounds(),1);*/
                menu_button.click();
                sleep(1000);
            }
        }
        else if (page == COMMON){
            device.pressMenu();
            sleep(1000);
            menu_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
            menu_list_child_count = menu_list.getChildCount();
            for (int j = 0; j < menu_list_child_count;j++){
                menu_button = menu_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                if (j==1){
                    menu_button.click();
                    sleep(1000);
                    device.pressBack();
                    sleep(1000);
                }
                else if (j==2){
                    device.pressMenu();
                    sleep(1000);
                    menu_button.click();
                    sleep(1000);
                }
                else if (j==3){
                    device.pressMenu();
                    sleep(1000);
                    menu_button.click();
                    sleep(1000);
                    String text = "12";
                    UiObject search;
                    search = new UiObject(new UiSelector().className("android.widget.EditText").index(1));
/*                    debug("search="+search.getBounds(),1);*/
                    search.setText(text);
                    sleep(1000);
                    device.pressKeyCode(KeyEvent.KEYCODE_ENTER);
                    sleep(1000);
                    device.pressBack();
                }
                else if (j==4){
                    device.pressMenu();
                    sleep(1000);
                    menu_button.click();
                    sleep(1000);
                    UiObject seek_bar;
                    UiObject confirm;
                    seek_bar = new UiObject(new UiSelector().className("android.widget.SeekBar").index(1));
                    confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
                    int start_x,start_y,end_x,end_y;
                    start_y = seek_bar.getBounds().centerY();
                    end_y = start_y;
                    start_x = width / 4;
                    end_x = width*3 / 4;
                    device.swipe(start_x,start_y,end_x,end_y,SWIPE_STEPS);
                    sleep(1000);
                    confirm.click();
                    sleep(1000);
                }
                else if (j==5){
                    device.pressMenu();
                    sleep(1000);
                    menu_button.click();
                    sleep(1000);
                    sleep(1000);
                    device.pressBack();
                    sleep(1000);
                }
            }
        }
    }

    private void musicStatement() throws UiObjectNotFoundException {

        UiObject state;
        state = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        if (state.exists()){
            state.click();
            sleep(1000);
        }

    }

    public void testHomeTop() throws UiObjectNotFoundException, IOException {
        /*首页顶栏*/
        debug("--------homeTop--------",1);

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        musicStatement();

        UiObject top_view;
        top_view = new UiObject(new UiSelector().className("android.view.View").index(0))
                .getChild(new UiSelector().className("android.widget.RelativeLayout").index(4));
        /*debug("top_view="+top_view.getBounds(),1);*/
        int top_view_child_count;
        top_view_child_count = top_view.getChildCount();
        UiObject play_pause = null;
        UiObject title = null;
        for (int i = 0; i < top_view_child_count;i++){
            top_view = new UiObject(new UiSelector().className("android.view.View").index(0))
                    .getChild(new UiSelector().className("android.widget.RelativeLayout").index(4));
            if (i==0){
                title = top_view.getChild(new UiSelector().className("android.widget.TextView").index(0).instance(0));
                /*debug("title="+title.getBounds(),1);*/
            }
            else if (i==1){
                play_pause = top_view.getChild(new UiSelector().className("android.widget.ImageView").index(2).instance(0));
                /*debug("play_pause="+play_pause.getBounds(),1);*/
            }
        }
        if (null != play_pause) {
            play_pause.click();
            sleep(1000);
            play_pause.click();
            sleep(1000);
        }
        if (null != title) {
            title.click();
            sleep(1000);
        }
        device.pressBack();
        sleep(1000);
    }

    public void testSongPage() throws UiObjectNotFoundException, IOException {
        /*歌曲页*/
        debug("--------songPage--------",1);

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        musicStatement();

        for (int i = 0;i < 1;i++){
            swipePhone(TOP);
        }
        sleep(1000);

        pageMenu(SONG);
        pageMenu(COMMON);

        UiObject page;
        page = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        /*debug("tmp=" + tmp.getBounds(),1);*/
        UiObject play_all;
        play_all = page.getChild(new UiSelector().className("android.widget.RelativeLayout").index(0));
        /*debug("play_all=" + play_all.getBounds(),1);*/
        play_all.click();
        sleep(1000);
        device.pressBack();
        sleep(1000);
        int list_count;
        list_count = list_view.getChildCount() -2;
        /*debug("list_count=" + list_count,1);*/
        int rnd;
        rnd = randomIndex(list_count,ZERO)+1;
        /*debug("rnd=" + rnd,1);*/
        UiObject song;
        song = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(rnd));
        /*debug("song="+song.getBounds(),1);*/
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
        /*debug("select_buttons_count="+select_buttons_count,1);*/
        for (int i = 0; i<select_buttons_count;i++){
            select_buttons = new UiObject(new UiSelector().className("android.view.View").index(1))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
            //debug("i>>"+i+">"+select_buttons.getChild(new UiSelector().className(android.widget.TextView.class.getName()).index(i)).getBounds(),1);
            if (i==0)
                cancel = select_buttons.getChild(new UiSelector().index(i));
            else if (i==2)
                select_all = select_buttons.getChild(new UiSelector().index(i));
        }
        /*debug("select_all=" + select_all.getBounds(),1);*/
        for (int i = 0;i<2;i++){
            if (null != select_all) {
                select_all.click();
            }
            sleep(500);
        }
        /*debug("cancel=" + cancel.getBounds(),1);*/
        if (null != cancel) {
            cancel.click();
        }
        sleep(500);
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
        /*debug("more_menu="+more_menu.getBounds(),1);*/
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
                /*debug("m_fav="+m_fav.getBounds(),1);*/
            }
            else if (i==1){
                m_send = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_send=" + m_send.getBounds(),1);*/
            }
            else if (i==2){
                m_set_ring = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_set_ring="+m_set_ring.getBounds(),1);*/
            }
            else if (i==3){
                m_id3 = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_id3="+m_id3.getBounds(),1);*/
            }
        }
        /*喜欢*/
        m_fav.click();
        /*发送*/
        song.longClick();
        e_more.click();
        m_send.click();
        device.pressBack();
        device.pressBack();
        /*用作手机铃声*/
        song.longClick();
        e_more.click();
        m_set_ring.click();
        device.pressBack();
        /*修改歌曲信息*/
        /*song.longClick();*/
        song.longClick();
        e_more.click();
        m_id3.click();
        device.pressBack();
        device.pressBack();
        killPlayer();
        sleep(1000);
    }

    public void testSingerPage() throws IOException, UiObjectNotFoundException {
        /*歌手页*/
        debug("--------singerPage--------",1);

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        musicStatement();

        gotoPage(SINGER);

        pageMenu(SINGER);

        UiObject page;
        page = new UiObject(new UiSelector().className("android.widget.ListView").index(1));
        /*debug("list_view=" + page.getBounds(),1);*/
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(1));
        /*debug("list_view=" + list_view.getBounds(),1);*/
        UiObject albums;
        albums = page.getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
        /*debug("albums=" + albums.getBounds(),1);*/
        albums.click();
        sleep(1000);
        device.pressBack();
        sleep(1000);
        int list_count;
        list_count = list_view.getChildCount()-2;
        int rnd;
        rnd = randomIndex(list_count,ZERO)+1;
        UiObject singer;
        singer = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
        /*debug("singer="+singer.getBounds(),1);*/
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
        /*debug("select_buttons_count="+select_buttons_count,1);*/
        for (int i = 0; i<select_buttons_count;i++){
            select_buttons = new UiObject(new UiSelector().className("android.view.View").index(1))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
            if (i==0)
                cancel = select_buttons.getChild(new UiSelector().index(i));
            else if (i==2)
                select_all = select_buttons.getChild(new UiSelector().index(i));
        }
        /*debug("select_all=" + select_all.getBounds(),1);*/
        for (int i = 0;i<2;i++){
            if (null != select_all) {
                select_all.click();
            }
            sleep(500);
        }
        /*debug("cancel=" + cancel.getBounds(),1);*/
        if (null != cancel) {
            cancel.click();
        }
        sleep(500);
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
                    singer.longClick();
                    UiObject e_add_to;
                    e_add_to = new UiObject(new UiSelector().className("android.widget.Button").instance(1));
                    e_add_to.click();
                    device.pressBack();
                    break;
                case 2:
                    /*删除*/
                    singer.longClick();
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

    public void testListPage() throws IOException, UiObjectNotFoundException {
        /*列表页*/
        debug("--------listPage--------",1);

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        musicStatement();

        gotoPage(LIST);

        UiObject list_view;
        /*debug("list_view=" + list_view.getBounds(),1);*/

        UiObject folders;
        UiObject fav_list = null;
        UiObject last_played_list = null;
        UiObject last_added_list = null;
        UiObject most_played_list = null;
        UiObject new_list;

        for (int i = 0; i < 6;i++){
            list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(2));
            /*debug("list_view=" + list_view.getBounds(),1);*/
            switch (i){
                case 0:
                    folders = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
                    /*debug("folders=" + folders.getBounds(),1);*/
                    folders.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 1:
                    fav_list = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
                    /*debug("fav_list=" + fav_list.getBounds(),1);*/
                    fav_list.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 2:
                    last_played_list = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
                    /*debug("last_played_list=" + last_played_list.getBounds(),1);*/
                    last_played_list.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 3:
                    last_added_list = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
                    /*debug("last_added_list=" + last_added_list.getBounds(),1);*/
                    last_added_list.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 4:
                    most_played_list = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
                    /*debug("most_played_list=" + most_played_list.getBounds(),1);*/
                    most_played_list.click();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 5:
                    new_list = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(i));
                    /*debug("new_list=" + new_list.getBounds(),1);*/
                    new_list.click();
                    sleep(1000);
                    UiObject new_list_name = new UiObject(new UiSelector().className("android.widget.EditText").index(0));
                    int list_num;
                    list_num = randomIndex(99999,ZERO);
                    /*new_list_name.clearTextField();*/
                    new_list_name.setText("new list"+list_num);
                    sleep(1000);
                    UiObject confirm_button = new UiObject(new UiSelector().className("android.widget.Button").index(1));
                    /*debug("confirm_button="+confirm_button.getBounds(),1);*/
                    confirm_button.click();
                    sleep(1000);
                    UiObject select_all;
                    select_all = new UiObject(new UiSelector().className("android.view.View").index(0))
                            .getChild(new UiSelector().className("android.widget.Button").index(2));
                    /*debug("select_all="+select_all.getBounds(),1);*/
                    select_all.click();
                    sleep(500);
                    UiObject finish_button;
                    finish_button = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0))
                            .getChild(new UiSelector().className("android.widget.Button").index(0));
                    /*debug("finish_button="+finish_button.getBounds(),1);*/
                    finish_button.click();
                    sleep(500);
                    break;
            }
            sleep(1000);
        }

        UiObject the_create_list;
        the_create_list = new UiObject(new UiSelector().className("android.widget.ListView").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(5));
        /*debug("the_create_list="+the_create_list.getBounds(),1);*/
        the_create_list.longClick();
        UiObject long_click_list;
        int long_click_list_child_count = 0;
        long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject long_click_rename_button;
        long_click_rename_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(2));
        long_click_rename_button.click();
        sleep(1000);
        UiObject rename_list;
        rename_list = new UiObject(new UiSelector().className("android.widget.EditText"));
        int list_num;
        list_num = randomIndex(99999,ZERO);
                    /*new_list_name.clearTextField();*/
        rename_list.setText(""+list_num);
        sleep(1000);
        UiObject confirm_button = new UiObject(new UiSelector().className("android.widget.Button").index(1));
        confirm_button.click();
        sleep(1000);
        UiObject enter_box;
        enter_box = new UiObject(new UiSelector().className("android.widget.EditText"));
        /*debug("enter_box"+enter_box.getBounds(),1);*/
        while (true){
            /*debug("while",1);*/
            if (enter_box.exists()) {
                /*debug("enter_box=1",1);*/
                device.pressBack();
            } else {
                /*debug("enter_box=0",1);*/
                break;
            }
        }
        the_create_list.longClick();
        UiObject long_click_add_song_button;
        long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        long_click_add_song_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(3));
        long_click_add_song_button.click();
        sleep(1000);
        device.pressBack();
        the_create_list.longClick();
        UiObject long_click_delete_button;
        long_click_delete_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
        /*debug("long_click_delete_button="+long_click_delete_button.getBounds(),1);*/
        long_click_delete_button.click();
        sleep(1000);

        /*长按操作*/
        for (int i = 0; i < 4 ;i++){
            switch (i){
                case 0:
                    /*debug("fav_list",1);*/
                    fav_list.longClick();
                    sleep(1000);
                    long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                    long_click_list_child_count = long_click_list.getChildCount();
                    for (int j = 0;j < long_click_list_child_count;j++){
                        if (j != 0) {
                            fav_list.longClick();
                        }
                        sleep(1000);
                        long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                        UiObject list_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                        /*debug(String.format("list_button(%d) %s", j, list_button.getBounds()),1);*/
                        list_button.click();
                        sleep(1000);
                        device.pressBack();
                        sleep(1000);
                    }
                    break;
                case 1:
                    /*debug("last_played_list",1);*/
                    last_played_list.longClick();
                    sleep(1000);
                    long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                    long_click_list_child_count = long_click_list.getChildCount();
                    for (int j = 0;j < long_click_list_child_count;j++){
                        if (j != 0) {
                            last_played_list.longClick();
                        }
                        sleep(1000);
                        long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                        UiObject list_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                        /*debug(String.format("list_button(%d) %s", j, list_button.getBounds()),1);*/
                        list_button.click();
                        sleep(1000);
                        device.pressBack();
                        sleep(1000);
                    }
                    break;
                case 2:
                    /*debug("last_added_list",1);*/
                    last_added_list.longClick();
                    sleep(1000);
                    long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                    long_click_list_child_count = long_click_list.getChildCount();
                    for (int j = 0;j < long_click_list_child_count;j++){
                        if (j != 0) {
                            last_added_list.longClick();
                        }
                        sleep(1000);
                        long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                        UiObject list_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                        /*debug(String.format("list_button(%d) %s", j, list_button.getBounds()),1);*/
                        list_button.click();
                        sleep(1000);
                        device.pressBack();
                        sleep(1000);
                    }
                    break;
                case 3:
                    /*debug("most_played_list",1);*/
                    most_played_list.longClick();
                    sleep(1000);
                    long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                    long_click_list_child_count = long_click_list.getChildCount();
                    for (int j = 0;j < long_click_list_child_count;j++){
                        if (j != 0) {
                            most_played_list.longClick();
                        }
                        sleep(1000);
                        long_click_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
                        UiObject list_button = long_click_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(j));
                        /*debug(String.format("list_button(%d) %s", j, list_button.getBounds()),1);*/
                        list_button.click();
                        sleep(1000);
                        device.pressBack();
                        sleep(1000);
                    }
                    break;
            }
        }
        killPlayer();
    }

    public void testNowplayingPage() throws IOException, UiObjectNotFoundException {
        /*正在播放页*/
        debug("--------nowplayingPage--------",1);

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        musicStatement();

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject play_all;
        play_all = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(0));
        play_all.click();
        sleep(1000);

        for (int i = 0;i < 2;i++){
            /*防止停留在列表页*/
            swipePhone(RIGHT);
        }
        for (int i = 0;i < 2;i++){
            /*歌词页面*/
            swipePhone(LEFT);
        }
        for (int i = 0;i < 1;i++){
            /*返回中间封面页*/
            swipePhone(RIGHT);
        }
        swipePhone(CENTRAL);

        /*喜欢*/
        UiObject top;
        top = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                .getChild(new UiSelector().className("android.widget.RelativeLayout").index(1));
        /*debug("top="+top.getBounds(),1);*/
        UiObject fav;
        fav = top.getChild(new UiSelector().className("android.widget.ImageView").index(1));
        /*debug("fav="+fav.getBounds(),1);*/
        fav.click();
        sleep(500);
        fav.click();
        sleep(500);

        /*米联 均衡器 顺序 下载 按钮*/
        UiObject function_buttons_list;
        UiObject mi_button;
        UiObject equalizer;
        UiObject order;
        UiObject download;
        function_buttons_list = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(1));
        /*debug("function_buttons_list"+function_buttons_list.getBounds(),1);*/
        int function_buttons_list_child_count;
        function_buttons_list_child_count = function_buttons_list.getChildCount();
        for (int i = 0; i < function_buttons_list_child_count ;i++){
            function_buttons_list = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(1));
            if (function_buttons_list_child_count > 3 ){
                switch (i){
                    case 0:
                        mi_button = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("mi_button="+mi_button.getBounds(),1);*/
                        if (mi_button.isEnabled()){
                            mi_button.click();
                            sleep(1000);
                            device.pressBack();
                            sleep(1000);
                        }
                        break;
                    case 1:
                        equalizer = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("equalizer="+equalizer.getBounds(),1);*/
                        equalizer.click();
                        sleep(1000);
                        device.pressBack();
                        break;
                    case 2:
                        order = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("order="+order.getBounds(),1);*/
                        for (int j = 0; j < 2 ;j++){
                            order.click();
                            sleep(1000);
                        }
                        break;
                    case 3:
                        download = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("download="+download.getBounds(),1);*/
                        if (download.isEnabled()){
                            download.click();
                            sleep(1000);
                        }
                        break;
                }
            }
            else {
                switch (i){
                    case 0:
                        equalizer = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("equalizer="+equalizer.getBounds(),1);*/
                        equalizer.click();
                        sleep(1000);
                        device.pressBack();
                        break;
                    case 1:
                        order = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("order="+order.getBounds(),1);*/
                        for (int j = 0; j < 2 ;j++){
                            order.click();
                            sleep(1000);
                        }
                        break;
                    case 2:
                        download = function_buttons_list.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("download="+download.getBounds(),1);*/
                        if (download.isEnabled()){
                            download.click();
                            sleep(1000);
                        }
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
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(7))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
        int control_buttons_child_count;
        control_buttons_child_count = control_buttons.getChildCount();
        for (int i = 0; i<control_buttons_child_count;i++){
            control_buttons = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(7))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
            switch (i){
                case 0:
                    prev = control_buttons.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("prev="+prev.getBounds(),1);*/
                    prev.click();
                    sleep(1000);
                    break;
                case 1:
                    play_pause = control_buttons.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("play_pause="+play_pause.getBounds(),1);*/
                    for (int j = 0; j < 2;j++){
                        play_pause.click();
                        sleep(1000);
                    }
                    break;
                case 2:
                    next = control_buttons.getChild(new UiSelector().className("android.widget.ImageView").index(i));
                    /*debug("next="+next.getBounds(),1);*/
                    next.click();
                    sleep(1000);
                    break;
            }
        }
        UiObject seek_bar;
        seek_bar = new UiObject(new UiSelector().className("android.widget.SeekBar").index(1));
        int seek_bar_x,seek_bar_y;
        seek_bar_y = seek_bar.getBounds().centerY();
        for (int j = 0;j<5;j++){
            debug("seek_bar:"+j,1);
            seek_bar_x = randomIndex(width,ZERO);
            device.click(seek_bar_x,seek_bar_y);
            sleep(1000);
        }

        /*Menu菜单项*/
        device.pressMenu();
        sleep(1000);
        UiObject menu_list;
        menu_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject menu_button;
        int menu_list_child_count;
        menu_list_child_count = menu_list.getChildCount();
        for (int i = 0; i < menu_list_child_count;i++){
            if (i != 0){
                device.pressMenu();
                sleep(1000);
            }
            menu_list = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
            menu_button = menu_list.getChild(new UiSelector().className("android.widget.LinearLayout").index(i));
            /*debug("menu_button="+menu_button.getBounds(),1);*/
            menu_button.click();
            sleep(1000);
            if (i == 2){
                UiObject down_lyric;
                UiObject down_cover;
                UiObject confirm_button;
                for (int j = 0;j<2;j++){
                    UiObject loading;
                    list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
                    if (j==0){
                        down_lyric = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(2));
                        down_lyric.click();
                        confirm_button = new UiObject(new UiSelector().className("android.widget.Button").index(1));
                        confirm_button.click();
                        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
                        while (true){
                            if (loading.exists()){
                                String wait;
                                wait = "Wait loading.";
                                waitMsg(wait,1000);
                            }else {
                                break;
                            }
                        }
                        list_view = new UiObject(new UiSelector().className("android.widget.ListView"));
                        UiObject lyric_list;
                        lyric_list = list_view.getChild(new UiSelector().className("miui.widget.CheckedTextView"));
                        if (lyric_list.exists()){
                            device.pressBack();
                            sleep(500);
                        }
                        device.pressMenu();
                        menu_button.click();
                    }else if (j==1){
                        down_cover = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(4));
                        down_cover.click();
                        confirm_button = new UiObject(new UiSelector().className("android.widget.Button").index(1));
                        confirm_button.click();
                        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
                        while (true){
                            if (loading.exists()){
                                String wait;
                                wait = "Wait loading.";
                                waitMsg(wait,1000);
                            }else {
                                break;
                            }
                        }
                        UiObject choose_cover;
                        choose_cover = new UiObject(new UiSelector().className("android.widget.TextView").text("请选择专辑封面"));
                        if (choose_cover.exists()){
                            device.pressBack();
                            sleep(500);
                        }
                    }

                }


            }
            device.pressBack();
            sleep(1000);
        }

        killPlayer();
    }

    public void testAlbumsPage() throws IOException, UiObjectNotFoundException {
        /*专辑页*/
        debug("--------albumsPage--------",1);

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        musicStatement();

        gotoPage(ALBUMS);

        pageMenu(ALBUMS);

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        int rnd;
        rnd = randomIndex(list_view_child_count,ZERO);
        UiObject album;
        album = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
        album.click();
        sleep(1000);
        device.pressBack();

        /*编辑模式*/
        album.longClick();
        sleep(1000);
        /* 编辑模式：取消 全选 取消全选*/
        UiObject select_buttons;
        UiObject cancel = null;
        select_buttons = new UiObject(new UiSelector().className("android.view.View").index(0))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
        /*debug("select_buttons="+select_buttons.getBounds(),1);*/
        UiObject select_all = null;
        int select_buttons_count;
        select_buttons_count = select_buttons.getChildCount();
        /*debug("select_buttons_count="+select_buttons_count,1);*/
        /*debug("select_buttons_count="+select_buttons_count,1);*/
        for (int i = 0; i<select_buttons_count;i++){
            select_buttons = new UiObject(new UiSelector().className("android.view.View").index(0))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
            if (i==0)
                cancel = select_buttons.getChild(new UiSelector().index(i));
            else if (i==2)
                select_all = select_buttons.getChild(new UiSelector().index(i));
        }
        /*debug("select_all=" + select_all.getBounds(),1);*/
        for (int i = 0;i<2;i++){
            if (null != select_all) {
                select_all.click();
            }
            sleep(500);
        }
        /*debug("cancel=" + cancel.getBounds(),1);*/
        if (null != cancel) {
            cancel.click();
        }
        sleep(500);
        for (int i=0;i < 3;i++) {
            album.longClick();
            switch (i){
                case 0:
                    /*播放*/
                    UiObject e_play;
                    e_play = new UiObject(new UiSelector().className("android.widget.Button").instance(0));
                    e_play.click();
                    break;
                case 1:
                    /*添加到*/
                    UiObject e_add_to;
                    e_add_to = new UiObject(new UiSelector().className("android.widget.Button").instance(1));
                    e_add_to.click();
                    device.pressBack();
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

    public void testFolderPage() throws IOException, UiObjectNotFoundException {
        /*文件夹页*/
        debug("--------folderPage--------",1);

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        musicStatement();

        gotoPage(FOLDER);

        pageMenu(FOLDER);

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        int rnd;
        rnd = randomIndex(list_view_child_count,ZERO);
        UiObject folder;
        folder = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
        folder.click();
        sleep(1000);
        device.pressBack();
        sleep(1000);

        /*编辑模式*/
        folder.longClick();
        sleep(1000);
        /* 编辑模式：取消 全选 取消全选*/
        UiObject select_buttons;
        UiObject cancel = null;
        select_buttons = new UiObject(new UiSelector().className("android.view.View").index(0))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
        /*debug("select_buttons="+select_buttons.getBounds(),1);*/
        UiObject select_all = null;
        int select_buttons_count;
        select_buttons_count = select_buttons.getChildCount();
        /*debug("select_buttons_count="+select_buttons_count,1);*/
        /*debug("select_buttons_count="+select_buttons_count,1);*/
        for (int i = 0; i<select_buttons_count;i++){
            select_buttons = new UiObject(new UiSelector().className("android.view.View").index(0))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
            if (i==0)
                cancel = select_buttons.getChild(new UiSelector().index(i));
            else if (i==2)
                select_all = select_buttons.getChild(new UiSelector().index(i));
        }
        /*debug("select_all=" + select_all.getBounds(),1);*/
        for (int i = 0;i<2;i++){
            if (null != select_all) {
                select_all.click();
            }
            sleep(500);
        }
        /*debug("cancel=" + cancel.getBounds(),1);*/
        if (null != cancel) {
            cancel.click();
        }
        sleep(500);
        for (int i=0;i < 3;i++) {
            folder.longClick();
            switch (i){
                case 0:
                    /*播放*/
                    UiObject e_play;
                    e_play = new UiObject(new UiSelector().className("android.widget.Button").instance(0));
                    e_play.click();
                    break;
                case 1:
                    /*添加到*/
                    UiObject e_add_to;
                    e_add_to = new UiObject(new UiSelector().className("android.widget.Button").instance(1));
                    e_add_to.click();
                    device.pressBack();
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

    public void testListDetailPage() throws IOException, UiObjectNotFoundException {
        /*列表详情页*/
        debug("--------listDetailPage--------",1);

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        musicStatement();

        gotoPage(LIST);

        UiObject list_view;
        /*debug("list_view=" + list_view.getBounds(),1);*/
        UiObject fav_list;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(2));
        /*debug("list_view=" + list_view.getBounds(),1);*/
        fav_list = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
        /*debug("folders=" + folders.getBounds(),1);*/
        fav_list.click();
        sleep(1000);

        /*添加歌曲*/
        UiObject add_songs;
        add_songs = new UiObject(new UiSelector().className("android.widget.Button").index(0));
        add_songs.click();
        sleep(500);
        UiObject select_buttons;
        UiObject cancel = null;
        select_buttons = new UiObject(new UiSelector().className("android.view.View").index(0))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
        /*debug("select_buttons="+select_buttons.getBounds(),1);*/
        UiObject select_all;
        select_all = select_buttons.getChild(new UiSelector().index(2));
        select_all.click();
        sleep(500);
        UiObject confirm;
        confirm = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(1))
                .getChild(new UiSelector().className("android.widget.Button").index(0));
        /*debug("confirm="+confirm.getBounds(),1);*/
        confirm.click();
        sleep(500);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject play_all;
        int list_view_child_count;
        list_view_child_count = list_view.getChildCount();
        play_all = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(0));
        play_all.click();
        sleep(1000);
        device.pressBack();
        sleep(500);

        /*编辑模式*/
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject song;
        int rnd;
        rnd = randomIndex(list_view_child_count,ZERO);
        song = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(rnd));
        song.longClick();
        sleep(1000);

        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        list_view_child_count = list_view.getChildCount();
        rnd = randomIndex(list_view_child_count,ZERO);
        song = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(rnd));
        UiObject change_pos = song.getChild(new UiSelector().index(0));

        int swipe_pos_x;
        int swipe_pos_y;
        swipe_pos_x = change_pos.getBounds().centerX();
        swipe_pos_y = change_pos.getBounds().centerY();

        int end_y;
        end_y = height - swipe_pos_y;
        for (int i = 0; i < 3 ;i++){
            device.swipe(swipe_pos_x,swipe_pos_y,swipe_pos_x,end_y,SWIPE_STEPS*3);
            sleep(1000);
        }
        device.pressBack();

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
                    sleep(1000);
                    device.pressBack();
                    break;
                case 1:
                    /*添加到*/
                    e_add_to.click();
                    device.pressBack();
                    sleep(1000);
                    device.pressBack();
                    break;
                case 2:
                    /*删除*/
                    e_delete.click();
                    UiObject confirm_delete;
                    confirm_delete = new UiObject(new UiSelector().className("android.widget.Button").index(1));
                    confirm_delete.click();
                    sleep(1000);
                    break;
            }
        }
        song.longClick();
        UiObject e_more;
        e_more = new UiObject(new UiSelector().className("android.widget.Button").instance(3));
        e_more.click();
        UiObject more_menu;
        more_menu = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(3))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
        /*debug("more_menu="+more_menu.getBounds(),1);*/
        int more_menu_child_count;
        more_menu_child_count = more_menu.getChildCount();
        UiObject m_fav = null;
        UiObject m_down = null;
        UiObject m_send = null;
        UiObject m_set_ring = null;
        UiObject m_id3 = null;
        for (int i = 0; i < more_menu_child_count;i++){
            more_menu = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(3))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(1));

            if (more_menu_child_count>4){
                if (i==0) {
                    m_fav = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_fav="+m_fav.getBounds(),1);*/
                }
                if (i==1){
                    m_down = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                    /*debug("m_down="+m_down.getBounds(),1);*/
                }
                else if (i==2){
                    m_send = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_send=" + m_send.getBounds(),1);*/
                }
                else if (i==3){
                    m_set_ring = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_set_ring="+m_set_ring.getBounds(),1);*/
                }
                else if (i==4){
                    m_id3 = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_id3="+m_id3.getBounds(),1);*/
                }
            }
            else if (more_menu_child_count == 4){
                if (i==0) {
                    m_fav = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_fav="+m_fav.getBounds(),1);*/
                }
                else if (i==1){
                    m_send = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_send=" + m_send.getBounds(),1);*/
                }
                else if (i==2){
                    m_set_ring = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_set_ring="+m_set_ring.getBounds(),1);*/
                }
                else if (i==3){
                    m_id3 = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_id3="+m_id3.getBounds(),1);*/
                }
            }

        }
        /*喜欢*/
        m_fav.click();
        /*发送*/
        song.longClick();
        e_more.click();
        if (null != m_down){
            m_down.click();
            confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
            confirm.click();
            sleep(1000);
            song.longClick();
            e_more.click();
            if (m_send.isEnabled()) {
                m_send.click();
                sleep(1000);
                device.pressBack();
            }
            e_more.click();
            if (m_set_ring.isEnabled()){
                m_set_ring.click();
                sleep(1000);
            }
            /*修改歌曲信息*/
            e_more.click();
            m_id3.click();
            sleep(1000);
        }
        else {
            m_send.click();
            sleep(1000);
            device.pressBack();
            e_more.click();
            m_set_ring.click();
            sleep(1000);
            e_more.click();
            m_id3.click();
            sleep(1000);
        }
        device.pressBack();
        device.pressBack();
        killPlayer();
    }

    public void testOnlineDetailPage() throws IOException, UiObjectNotFoundException {
        /*在线详情页*/
        debug("--------onlineDetailPage--------",1);

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        musicStatement();

        gotoPage(ONLINE);

        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(3));
        /*debug("list_view=" + list_view.getBounds(),1);*/
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
                    /*debug("recommend="+recommend.getBounds(),1);*/
                    recommend.click();
                    sleep(1000);
                    break;
                case 1:
                    top = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(i));
                    /*debug("top="+top.getBounds(),1);*/
                    top.click();
                    sleep(1000);
                    break;
                case 2:
                    fm = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(i));
                    /*debug("fm="+fm.getBounds(),1);*/
                    fm.click();
                    sleep(1000);
                    break;
            }
            UiObject confirm;
            confirm = new UiObject(new UiSelector().className("android.widget.Button").index(1));
            if (confirm.exists()){
                confirm.click();
                sleep(1000);
            }
            device.pressBack();
            sleep(500);
        }

        String wait;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(3));
        /*debug("list_view=" + list_view.getBounds(),1);*/
        recommend = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(0));
        /*debug("recommend="+recommend.getBounds(),1);*/
        recommend.click();
        sleep(1000);
        UiObject loading;
        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        UiObject retry;
        retry = new UiObject(new UiSelector().className("android.widget.TextView").text("重试"));
        while (true){
            if (loading.exists()){
                wait = "Please wait 1 seconds to load online data.";
                waitMsg(wait,1000);
            }else {
                if (retry.exists()){
                    debug("loading fail and retry.",1);
                    retry.click();
                    sleep(1000);
                }else {
                    debug("loading done",1);
                    sleep(1000);
                    break;
                }
            }
        }

        /*更多*/
        UiObject sc_view;
        UiObject more_albums = null;
        UiObject more_singers = null;

        sc_view = new UiObject(new UiSelector().className("android.widget.ScrollView"))
                .getChild(new UiSelector().className("android.widget.LinearLayout"));
        /*debug("sc_view="+sc_view.getBounds(),1);*/
        more_albums = sc_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug("more_albums"+more_albums.getBounds(),1);*/

        more_albums.click();
        sleep(1000);
        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        retry = new UiObject(new UiSelector().className("android.widget.TextView").text("重试"));
        while (true){
            if (loading.exists()){
                wait = "Please wait 1 seconds to load online data.";
                waitMsg(wait,1000);
            }else {
                if (retry.exists()){
                    debug("loading fail and retry.",1);
                    retry.click();
                    sleep(1000);
                }else {
                    debug("loading done",1);
                    sleep(1000);
                    break;
                }
            }
        }
        device.pressBack();
        sleep(1000);

        sc_view = new UiObject(new UiSelector().className("android.widget.ScrollView"))
                .getChild(new UiSelector().className("android.widget.LinearLayout"));
        more_singers = sc_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(2).instance(1))
                .getChild(new UiSelector().className("android.widget.TextView").index(1));
        /*debug("more_singers="+more_singers.getBounds(),1);*/
        more_singers.click();
        sleep(1000);
        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        retry = new UiObject(new UiSelector().className("android.widget.TextView").text("重试"));
        while (true){
            if (loading.exists()){
                wait = "Please wait 1 seconds to load online data.";
                waitMsg(wait,1000);
            }else {
                if (retry.exists()){
                    debug("loading fail and retry.",1);
                    retry.click();
                    sleep(1000);
                }else {
                    debug("loading done",1);
                    sleep(1000);
                    break;
                }
            }
        }
        device.pressBack();
        sleep(1000);

        UiObject to_search;
        to_search = new UiObject(new UiSelector().className("android.widget.ScrollView").index(0))
                .getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
        to_search.click();
        sleep(1000);
        UiObject edit_text;
        edit_text = new UiObject(new UiSelector().className("android.widget.EditText").index(1));
        String search_string;
        search_string = "123";
        /*String all_s = "0123456789abcdefghijklmnopqrstuvwxyz";
        int length  = all_s.length();
        char[] tmp = all_s.toCharArray();
        for (int i = 0; i< 8;i++){
            int rnd = (int) (Math.random()*length);
            search_string = search_string + tmp[rnd];
            debug("i="+ i + ",rnd=" + rnd + ",search_string="+search_string,1);
        }*/
        edit_text.setText(search_string);
        sleep(1000);
        device.pressKeyCode(KeyEvent.KEYCODE_ENTER);
        sleep(1000);
        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        retry = new UiObject(new UiSelector().className("android.widget.TextView").text("重试"));
        while (true){
            if (loading.exists()){
                wait = "Please wait 1 seconds to load online data.";
                waitMsg(wait,1000);
            }else {
                if (retry.exists()){
                    debug("loading fail and retry.",1);
                    retry.click();
                    sleep(1000);
                }else {
                    debug("loading done",1);
                    sleep(1000);
                    break;
                }
            }
        }
        device.pressMenu();
        sleep(1000);
        device.pressBack();
        sleep(1000);
        device.pressBack();
        sleep(1000);

        UiObject online_albums;
        online_albums = new UiObject(new UiSelector().className("android.widget.GridView").index(0));
        int online_albums_child_count;
        online_albums_child_count = online_albums.getChildCount();
        int rnd;
        rnd = randomIndex(online_albums_child_count,ZERO);
        UiObject online_album;
        online_album = online_albums.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
        online_album.click();
        sleep(1000);
        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        retry = new UiObject(new UiSelector().className("android.widget.TextView").text("重试"));
        while (true){
            if (loading.exists()){
                wait = "Please wait 1 seconds to load online data.";
                waitMsg(wait,1000);
            }else {
                if (retry.exists()){
                    debug("loading fail and retry.",1);
                    retry.click();
                    sleep(1000);
                }else {
                    debug("loading done",1);
                    sleep(1000);
                    break;
                }
            }
        }

        swipePhone(TOP);
        sleep(1000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        list_view_child_count = list_view.getChildCount();
        UiObject play_all;
        play_all = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(0));
        play_all.click();
        wait = "Please wait 10 seconds for the song.";
        waitMsg(wait,10000);
        device.pressBack();
        sleep(1000);
        rnd = randomIndex(list_view_child_count,ZERO);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject online_album_song;
        online_album_song = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(rnd));
        online_album_song.click();
        wait = "Please wait 10 second for the song loading.";
        waitMsg(wait, 10000);
        device.pressBack();
        sleep(1000);
        UiObject download_online_album_song;
        download_online_album_song = online_album_song.getChild(new UiSelector().className("android.widget.ImageView").index(2));
        /*debug("download_online_album_song=("+download_online_album_song.getBounds().centerX()+","+download_online_album_song.getBounds().centerY()+")"+download_online_album_song.getBounds(),1);*/
        download_online_album_song.click();
        wait = "Please wait 10 seconds for this online song downloading.";
        waitMsg(wait, 10000);

        /*编辑模式*/
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        online_album_song = list_view.getChild(new UiSelector().className("android.widget.RelativeLayout").index(rnd));
        online_album_song.longClick();
        sleep(1000);
        /* 编辑模式：取消 全选 取消全选*/
        UiObject select_buttons;
        UiObject cancel = null;
        select_buttons = new UiObject(new UiSelector().className("android.view.View").index(1))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
        UiObject select_all = null;
        int select_buttons_count;
        select_buttons_count = select_buttons.getChildCount();
        /*debug("select_buttons_count="+select_buttons_count,1);*/
        for (int i = 0; i<select_buttons_count;i++){
            select_buttons = new UiObject(new UiSelector().className("android.view.View").index(1))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
            //debug("i>>"+i+">"+select_buttons.getChild(new UiSelector().className(android.widget.TextView.class.getName()).index(i)).getBounds(),1);
            if (i==0)
                cancel = select_buttons.getChild(new UiSelector().index(i));
            else if (i==2)
                select_all = select_buttons.getChild(new UiSelector().index(i));
        }
        /*debug("select_all=" + select_all.getBounds(),1);*/
        for (int i = 0;i<2;i++){
            if (null != select_all) {
                select_all.click();
            }
            sleep(500);
        }
        /*debug("cancel=" + cancel.getBounds(),1);*/
        if (null != cancel) {
            cancel.click();
        }
        sleep(500);
        online_album_song.longClick();
        UiObject e_more;
        e_more = new UiObject(new UiSelector().className("android.widget.Button").instance(3));
        e_more.click();
        UiObject more_menu;
        more_menu = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                .getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
        /*debug("more_menu="+more_menu.getBounds(),1);*/
        int more_menu_child_count;
        more_menu_child_count = more_menu.getChildCount();
        UiObject m_fav = null;
        UiObject m_down = null;
        UiObject m_send = null;
        UiObject m_set_ring = null;
        UiObject m_id3 = null;
        /*debug("more_menu_child_count="+more_menu_child_count,1);*/
        for (int i = 0; i < more_menu_child_count;i++){
            more_menu = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(2))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
            if (more_menu_child_count == 4){
                if (i==0) {
                    m_fav = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_fav="+m_fav.getBounds(),1);*/
                }
                else if (i==1){
                    m_send = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                    /*debug("m_send="+m_send.isEnabled(),1);*/
                /*debug("m_send=" + m_send.getBounds(),1);*/
                }
                else if (i==2){
                    m_set_ring = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                    /*debug("m_set_ring="+m_set_ring.isEnabled(),1);*/
                /*debug("m_set_ring="+m_set_ring.getBounds(),1);*/
                }
                else if (i==3){
                    m_id3 = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_id3="+m_id3.getBounds(),1);*/
                }
            }
            else if (more_menu_child_count == 5){
                if (i==0) {
                    m_fav = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_fav="+m_fav.getBounds(),1);*/
                }
                else if (i==1){
                    m_down = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                    /*debug("m_down="+m_down.isEnabled(),1);*/
                }
                else if (i==2){
                    m_send = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                    /*debug("m_send="+m_send.isEnabled(),1);*/
                /*debug("m_send=" + m_send.getBounds(),1);*/
                }
                else if (i==3){
                    m_set_ring = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                    /*debug("m_set_ring="+m_set_ring.isEnabled(),1);*/
                /*debug("m_set_ring="+m_set_ring.getBounds(),1);*/
                }
                else if (i==4){
                    m_id3 = more_menu.getChild(new UiSelector().className("android.widget.TextView").index(i));
                /*debug("m_id3="+m_id3.getBounds(),1);*/
                }
            }
        }
        /*喜欢*/
        m_fav.click();
        /*下载*/
        if (null != m_down){
            online_album_song.longClick();
            e_more.click();
            sleep(1000);
            m_down.click();
            sleep(1000);
            UiObject confirm_down;
            confirm_down = new UiObject(new UiSelector().className("android.widget.Button").index(1));
            confirm_down.click();
            sleep(1000);
        }

        /*发送*/
        online_album_song.longClick();
        e_more.click();
        if (m_send.isEnabled()){
            m_send.click();
            sleep(1000);
            device.pressBack();
        }

        /*用作手机铃声*/
        e_more.click();
        if (m_set_ring.isEnabled()){
            m_set_ring.click();
            sleep(1000);
        }
        else {
            device.pressBack();
        }
        /*修改歌曲信息*/
        e_more.click();
        m_id3.click();
        sleep(1000);
        device.pressBack();
        device.pressBack();

        device.pressMenu();
        sleep(1000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject m_down_all;
        m_down_all = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
        m_down_all.click();
        sleep(1000);
        if (list_view_child_count > 2){
            UiObject confirm_down;
            confirm_down = new UiObject(new UiSelector().className("android.widget.Button").index(1));
            confirm_down.click();
            wait = "Please wait 30 seconds for downloading all songs.";
            waitMsg(wait, 30000);
        }
        else {
            sleep(1000);
        }

        for (int i=0;i < 3;i++) {
            online_album_song.longClick();
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
                    sleep(1000);
                    device.pressBack();
                    break;
                case 1:
                    /*添加到*/
                    e_add_to.click();
                    sleep(1000);
                    device.pressBack();
                    device.pressBack();
                    break;
                case 2:
                    /*删除*/
                    if (list_view_child_count >2 ){
                        select_all.click();
                    }
                    e_delete.click();
                    sleep(1000);
                    UiObject confirm_delete;
                    confirm_delete = new UiObject(new UiSelector().className("android.widget.Button").index(1));
                    confirm_delete.click();
                    sleep(500);
                    break;
            }
            sleep(1000);
        }
        device.pressBack();
        sleep(1000);

        /*在线歌手*/
        swipePhone(TOP);
        sleep(1000);
        UiObject online_singers;
        online_singers = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(3))
                .getChild(new UiSelector().className("android.widget.GridView").index(0));
        int online_singers_child_count;
        online_singers_child_count = online_singers.getChildCount();
        rnd = randomIndex(online_singers_child_count,ZERO);
        UiObject online_singer;
        online_singer = online_singers.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
        online_singer.click();
        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        retry = new UiObject(new UiSelector().className("android.widget.TextView").text("重试"));
        while (true){
            if (loading.exists()){
                wait = "Please wait 1 seconds to load online data.";
                waitMsg(wait,1000);
            }else {
                if (retry.exists()){
                    debug("loading fail and retry.",1);
                    retry.click();
                    sleep(1000);
                }else {
                    debug("loading done",1);
                    sleep(1000);
                    break;
                }
            }
        }
        swipePhone(LEFT);
        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        retry = new UiObject(new UiSelector().className("android.widget.TextView").text("重试"));
        while (true){
            if (loading.exists()){
                wait = "Please wait 1 seconds to load online data.";
                waitMsg(wait,1000);
            }else {
                if (retry.exists()){
                    debug("loading fail and retry.",1);
                    retry.click();
                    sleep(1000);
                }else {
                    debug("loading done",1);
                    sleep(1000);
                    break;
                }
            }
        }
        swipePhone(TOP);
        sleep(1000);
        UiObject online_singer_albums;
        online_singer_albums = new UiObject(new UiSelector().className("android.widget.GridView").index(0));
        int online_singer_albums_child_count;
        online_singer_albums_child_count = online_singer_albums.getChildCount();
        rnd = randomIndex(online_singer_albums_child_count,ZERO);
        UiObject online_singer_album;
        online_singer_album =  online_singer_albums.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
        online_singer_album.click();
        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        retry = new UiObject(new UiSelector().className("android.widget.TextView").text("重试"));
        while (true){
            if (loading.exists()){
                wait = "Please wait 1 seconds to load online data.";
                waitMsg(wait,1000);
            }else {
                if (retry.exists()){
                    debug("loading fail and retry.",1);
                    retry.click();
                    sleep(1000);
                }else {
                    debug("loading done",1);
                    sleep(1000);
                    break;
                }
            }
        }
        device.pressBack();
        device.pressBack();
        sleep(1000);

        /*热门推荐*/
        swipePhone(LEFT);
        sleep(1000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        list_view_child_count = list_view.getChildCount();
        rnd = (int) (Math.random()*list_view_child_count);
        UiObject board;
        board = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
        /*debug("board="+board.isClickable(),1);*/
        board.click();
        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        retry = new UiObject(new UiSelector().className("android.widget.TextView").text("重试"));
        while (true){
            if (loading.exists()){
                wait = "Please wait 1 seconds to load online data.";
                waitMsg(wait,1000);
            }else {
                if (retry.exists()){
                    debug("loading fail and retry.",1);
                    retry.click();
                    sleep(1000);
                }else {
                    debug("loading done",1);
                    sleep(1000);
                    break;
                }
            }
        }
        device.pressBack();
        sleep(1000);
        swipePhone(TOP);
        sleep(1000);

        /*随心听*/
        swipePhone(LEFT);
        sleep(1000);
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        list_view_child_count = list_view.getChildCount();
        rnd = (int) (Math.random()*list_view_child_count);
        fm = list_view.getChild(new UiSelector().className("android.widget.LinearLayout").index(rnd));
        /*debug("fm="+fm.isClickable(),1);*/
        fm.click();
        loading = new UiObject(new UiSelector().className("android.widget.ProgressBar"));
        retry = new UiObject(new UiSelector().className("android.widget.TextView").text("重试"));
        while (true){
            if (loading.exists()){
                wait = "Please wait 1 seconds to load online data.";
                waitMsg(wait,1000);
            }else {
                if (retry.exists()){
                    debug("loading fail and retry.",1);
                    retry.click();
                    sleep(1000);
                }else {
                    debug("loading done",1);
                    sleep(1000);
                    break;
                }
            }
        }
        device.pressBack();
        swipePhone(TOP);
        sleep(1000);
        device.pressBack();
        sleep(1000);

        killPlayer();
    }

    public void testLandscapeMode() throws IOException, UiObjectNotFoundException, RemoteException {
        debug("--------LandscapeMode--------",1);

        killPlayer();
        launchPlayer();
        sleep(1000);
        assertEquals(PLAYER_PAC_NAME, device.getCurrentPackageName());

        musicStatement();

        for (int i = 0;i < 1;i++){
            swipePhone(TOP);
        }
        sleep(1000);

        UiObject page;
        page = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        UiObject list_view;
        list_view = new UiObject(new UiSelector().className("android.widget.ListView").index(0));
        /*debug("tmp=" + tmp.getBounds(),1);*/
        UiObject play_all;
        play_all = page.getChild(new UiSelector().className("android.widget.RelativeLayout").index(0));
        /*debug("play_all=" + play_all.getBounds(),1);*/
        play_all.click();
        sleep(1000);
        if (device.isNaturalOrientation()){
            device.unfreezeRotation();
            sleep(500);
            device.setOrientationLeft();
        }
        sleep(2000);
        if (!device.isNaturalOrientation()){
            device.unfreezeRotation();
            sleep(500);
            device.setOrientationNatural();
        }

        killPlayer();

    }

    private void getBugreport() throws IOException {
        /*抓log*/
        String log_name;
        Date date=new Date();
        String fmt = "yyyy-MM-dd-HH-mm-ss";
        SimpleDateFormat s = new SimpleDateFormat(fmt);
        log_name = s.format(date);
        log_name += ".txt";
        debug("--------getBugreport="+log_name+"--------",1);
        String bugreport;
        bugreport = "bugreport > " + "sdcard/" +log_name;
        Runtime.getRuntime().exec(bugreport);
    }

}
