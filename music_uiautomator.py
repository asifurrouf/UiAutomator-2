__author__ = 'jiahuixing'
# -*- coding: utf-8 -*-

import os
import time

command_push = 'adb push /mnt/hgfs/vmshare/jiahuixing/UiAutomator/bin/uiautomator.jar /data/local/tmp/'
command_run = 'adb shell uiautomator runtest uiautomator.jar -c com.miui.player.test.MusicPlayerTest'
command_bugreport = 'adb bugreport > '
command_bugreport_analyse = 'java -jar /mnt/hgfs/vmshare/jiahuixing/UiAutomator/bin/chkbugreport.jar '

def getDate():

    block = '.'
    year,mon,day= time.strftime('%Y'),time.strftime('%m'),time.strftime('%d')
    year = year[-1]
    mon = str(int(mon))
    day = str(int(day))
    date = year + block + mon + block + day
    return date

def musicUi():
    TEST_TIMES = input('Pls input num of test times:')
    TEST_TIMES = int(TEST_TIMES)
    print('----------Push jar to the device----------')
    os.system(command_push)
    begin = time.time()
    print('----------Begin to run test----------')
    for i in xrange(TEST_TIMES):
        print('----------test:%d----------' %(i+1))
        os.system(command_run)
    print('----------Test done----------')
    print('----------Begin to get bugreport----------')
    date = getDate()
    if not os.path.exists(date):
        os.mkdir(date)
    block = ':'
    path = os.path.abspath(date)
    bugreport_name = date + '-' + time.strftime('%H') + block + time.strftime('%M') + block + time.strftime('%S') + '.txt'
    bugreport = '%s%s/%s' % (command_bugreport, path, bugreport_name)
    print('----------bugreport:%s----------' % bugreport)
    os.system(bugreport)
    print('----------Begin to analyse the bugreort----------')
    analyse = command_bugreport_analyse + bugreport
    os.system(analyse)
    end = time.time()
    cost = end - begin
    print('----------Cost time:%s----------' % cost)


musicUi()

