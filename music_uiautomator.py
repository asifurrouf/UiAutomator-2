__author__ = 'jiahuixing'
# -*- coding: utf-8 -*-

import os
import time

command_push = 'adb -s %s push /mnt/hgfs/vmshare/jiahuixing/UiAutomator/bin/uiautomator.jar /data/local/tmp/'
command_run = 'adb -s %s shell uiautomator runtest uiautomator.jar -c com.miui.player.test.MusicPlayerTest'
command_bugreport = 'adb -s %s bugreport > '
command_bugreport_analyse = 'java -jar /mnt/hgfs/vmshare/jiahuixing/UiAutomator/bin/chkbugreport.jar '
command_adb_devices = 'adb devices'

def getDate():

    block = '.'
    year,mon,day= time.strftime('%Y'),time.strftime('%m'),time.strftime('%d')
    year = year[-1]
    mon = str(int(mon))
    day = str(int(day))
    date = year + block + mon + block + day
    return date

def getDevices():

    mark = 'device'
    device_ids = []
    device_count = 0
    devices = os.popen(command_adb_devices)
    for line in devices:
        if mark in line:
            line = line.strip()
            line = line.split()
            if len(line) == 2:
                device_count += 1
                print(line)
                device_id = line[0]
                print('device_id:%s'%device_id)
                device_ids.append(device_id)
    print('device_count:%d'%device_count)
    return device_ids

def musicUi():
    TEST_TIMES = input('Pls input num of test times:')
    TEST_TIMES = int(TEST_TIMES)
    devices = getDevices()
    length = len(devices)
    begin = time.time()
    print('----------Begin to run test----------')
    if length > 0:
        print('----------Push jar to the device----------')
        for j in xrange(length):
            device = devices[j]
            print('device:%s'%device)
            push = command_push%device
            os.system(push)
            for i in xrange(TEST_TIMES):
                print('----------test:%d----------' %(i+1))
                run = command_run%device
                os.system(run)
    print('----------Test done----------')
    bugreport_flag = input('Pls input num 1 to catch a bugreport other for not:')
    bugreport_flag = int(bugreport_flag)
    if bugreport_flag == 1:
        devices = getDevices()
        length = len(devices)
        if length > 0:
            print('----------Begin to get bugreport----------')
            for i in xrange(length):
                device = devices[i]
                date = getDate()
                if not os.path.exists(date):
                    os.mkdir(date)
                block = '-'
                path = os.path.abspath(date)
                bugreport_name = date + '-' + time.strftime('%H') + block + time.strftime('%M') + block + time.strftime('%S') + '.txt'
                command_bugreport_tmp = command_bugreport%device
                bugreport = '%s%s/%s' % (command_bugreport_tmp, path, bugreport_name)
                print('----------bugreport:%s----------' % bugreport)
                os.system(bugreport)
                bugreport_file = '%s/%s'%(path,bugreport_name)
                analyse = '%s/%s' % (command_bugreport_analyse, bugreport_file)
                print('----------analyse:%s----------' % analyse)
                if os.path.exists(bugreport_file) and os.path.getsize(bugreport_file) > 0 :
                    print('----------Begin to analyse the bugreort----------')
                    os.system(analyse)
                else:
                    if not os.path.exists(bugreport_file):
                        print('----------bugreport not exist----------')
                    elif os.path.getsize(bugreport_file) == 0:
                        print('----------bugreport size 0----------')
    end = time.time()
    cost = end - begin
    print('----------Cost time:%s----------' % cost)

musicUi()

