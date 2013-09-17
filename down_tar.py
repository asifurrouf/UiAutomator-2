__author__ = 'jiahuixing'
# -*- coding: utf-8 -*-

import sys
import os
import time
import urllib
import urllib2

Tar_Properties = [

    ['Mi2_Alpha','aries_alpha_images_','_4.1.tar'],
    ['Mi2','aries_images_','_4.1.tar'],
    ['Mi1','mione_plus_images_','_4.1.tar'],
    ['Mi3_TD','pisces_images_','_4.2.tar'],
    ['Mi3_WCDMA','cancro_images_','_4.3.tar'],
    ['Mi2A_Alpha','taurus_alpha_images_','_4.1.tar'],
    ['Mi2A','taurus_images_','_4.1.tar'],

]

def getDate():
    block = '.'
    year,mon,day= time.strftime('%Y'),time.strftime('%m'),time.strftime('%d')
    year = year[-1]
    mon = str(int(mon))
    day = str(int(day))
    date = year + block + mon + block + day
    return date

def chooseTar():
    title = 'Pls input the num for the tars.'
    wrap = '\r\n'
    block = ' : '
    tars_order = ''
    for i in xrange(len(Tar_Properties)):
        tar = Tar_Properties[i][0]
        tars_order = tars_order + str(i) + block + tar + wrap
    content = title + wrap + tars_order
    choose = raw_input(content)
    return  int(choose)

def checkTodayTar():
    home_page = 'http://ota.n.miui.com/ota/'
    today_page = ''
    length = len(sys.argv)
    if length > 1:
        date = sys.argv[1]
    else:
        date = getDate()
    tar_index = chooseTar()
    url = urllib.urlopen(home_page)
    url_read = url.read()
    if date in url_read:
        length = len(Tar_Properties)
        if tar_index < length:
            today_page = home_page + date + '/'
            print(today_page)
            url = urllib.urlopen(today_page)
            url_read = url.read()
            tar = Tar_Properties[tar_index][1] + date + Tar_Properties[tar_index][2]
            print(tar)
            if tar in url_read:
                tar_file_url = today_page + tar
                print(tar_file_url)
                print('Begin to download file......')
                begin = time.time()
                urllib.urlretrieve(tar_file_url,tar)
                end = time.time()
                cost = int(end - begin)
                print('Done and cost time : %d seconds.'% cost )
            else:
                print('Tar file not found.')
        else:
            print('******U choose a wrong num******')
            checkTodayTar()
    else:
        print('False1')

checkTodayTar()