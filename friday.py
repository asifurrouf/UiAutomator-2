__author__ = 'jiahuixing'
# -*- coding: utf-8 -*-

import os
import sys
import time
import hashlib

Rom_Properties = [
    ['小米手机1/小米手机1S','开发版','miui_MI1_',],
    ['小米手机1/小米手机1S','开发版','miui_Mioneplus_',],
    ['小米手机1/小米手机1S','稳定版','mione_plus_images_JLB',],
    ['小米手机2/小米手机2S','开发版','miui_MI2_',],
    ['小米手机2/小米手机2S','体验版','miui_MI2Alpha_',],
    ['小米手机2/小米手机2S','稳定版','miui_MI2_JLB',],
    ['小米手机2/小米手机2S','台湾版','miui_MI2TW_JLB',],
    ['小米手机2/小米手机2S','香港版','miui_MI2HK_JLB',],
    ['小米手机2/小米手机2S','电信版','miui_MiTwo_CT_JLB',],
    ['小米手机2/小米手机2S','联通版','miui_MiTwo_CU_JLB',],
    ['小米手机2/小米手机2S','香港版','miui_MiTwo_HK_JLB',],
    ['小米手机2/小米手机2S','台湾版','miui_MiTwo_TW_JLB',],
    ['小米手机2/小米手机2S','稳定版','miui_MiTwo_JLB',],
    ['小米手机2A','开发版','miui_MI2A_',],
    ['小米手机2A','开发版','miui_MI2A_',],
    ['小米手机2A','联通版','miui_MiTwoA_CU_JLB',],
    ['小米手机2A','稳定版','miui_MiTwoA_JLB',],
    ['小米手机2A','体验版','miui_MI2AAlpha_',],
    ['小米手机2A_TD','开发版','miui_MI2ATD_',],
    ['小米手机3','开发版','miui_MI3-TD_',],
    ['小米手机3','开发版','miui_MI3_',],
    ['红米手机_WCDMA','开发版','miui_HM2W_',],
    ['红米手机_TD','开发版','miui_HM2_',],
    ]

Rom_Type = [
    ['zip','卡刷包',],
    ['exe','线刷包',],
    ['tar','线刷包'],
    ]



def getFileMd5(filename):
    if not os.path.isfile(filename):
        return
    my_hash = hashlib.md5()
    f = file(filename,'rb')
    while True:
        b = f.read(8096)
        if not b :
            break
        my_hash.update(b)
    f.close()
    return my_hash.hexdigest().lower()

def getRomSize(filename):
    if not os.path.isfile(filename):
        return
    size = os.path.getsize(filename)/1024/1024
    size = str(size) + 'M'
    return size

def getVersion():
    block = '.'
    argv_len = len(sys.argv)
    if argv_len >= 3:
        version = sys.argv[2]
    else:
        year,mon,day= time.strftime('%Y'),time.strftime('%m'),time.strftime('%d')
        year = year[-1]
        mon = str(int(mon))
        day = str(int(day))
        version = year + block + mon + block + day
    return version

def getRomDetail(rom_name):
    length = len(Rom_Properties)
    index = 0
    for i in xrange(length):
        rom_property = Rom_Properties[i][2]
        if rom_property in rom_name:
            index = i+1
            break
    return index

def getRomType(rom_name):
    rom_type = ''
    for i in xrange(len(Rom_Type)):
        info = Rom_Type[i]
        sub = info[0]
        if sub in rom_name:
            rom_type = info[1]
            break
    return rom_type


def walk_dir(dir,topdown=True):

    miui = 'miui_'
    rom_info = list()
    for root, dirs, files in os.walk(dir, topdown):
        for name in files:
            if miui in name:
                index = getRomDetail(name)
                print os.path.abspath(os.path.join(root,name))
                print(index)
                if index > 0 :
                    info = []
                    info.append(index)
                    info.append(name)
                    rom_type = getRomType(name)
                    info.append(rom_type)
                    size = getRomSize(os.path.join(root,name))
                    info.append(size)
                    md5 = getFileMd5(os.path.join(root,name))
                    info.append(md5)
                    rom_info.append(info)
    return rom_info

class Generate:

    mFolder = ''
    version = ''
    print_format = ''

    def __init__(self):
        if len(sys.argv) >= 2:
            self.mFolder = sys.argv[1]
            self.version = getVersion()
            print('Folder:' + self.mFolder)
            print('Version:' + self.version)
        else:
            print('No argv given.')
            return

    def getPrintFormat(self):
        wrap = '\r\n'
        print_format = '' + wrap
        tab = '     '
        sub_url = 'http://bigota.d.miui.com/'
        if self.mFolder != '':
            if os.path.exists(self.mFolder):
                rom_info = walk_dir(self.mFolder)
                for i in xrange(len(rom_info)):
                    info = rom_info[i]
                    index = info[0] - 1
                    name = info[1]
                    rom_type = info[2]
                    size = info[3]
                    md5 = info[4]
                    rom_property = Rom_Properties[index]
                    c_name = rom_property[0]
                    dev_type = rom_property[1]
                    url = sub_url + self.version + '/' + name
                    print_format = "%s%s %s %s %s%s%s%s%s%s%s%s%s" % (
                        print_format, c_name, dev_type, self.version, rom_type, wrap, url, wrap, size, tab, md5,
                        wrap,wrap)
                self.print_format = print_format
            else:
                print('Folder not found.')
                sys.exit()
        else:
            print('Folder not given.')
            sys.exit()

    def writePrintFormat(self):
        read_mode = 'w'
        file_name = self.version + '.txt'
        file_obj = open(file_name,read_mode)
        file_obj.write(self.print_format)
        file_obj.close()

if __name__ == '__main__':

    print('.........Begin to work.........')
    begin = time.time()
    ge = Generate()
    ge.getPrintFormat()
    end = time.time()
    cost = int(end-begin)
    print('.........Work done.........')
    print(".........Cost time : %d seconds........."%cost)
    print(ge.print_format)
    #ge.writePrintFormat()

