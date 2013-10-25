# -*- coding: utf-8 -*-

import os
import sys
import time
import hashlib

DEV_MIUI = 'miui_'
ZIP = 'zip'
DEV_IMAGES = '_images_'
TAR = 'tar'
STABLE_MIUI = 'J'
ORIGIN = 'N'

RomNames = ['M1/M1S 开发版','M1/M1S 开发版',
            'M2/M2S 开发版','M2/M2S 开发版',
            'X3 开发版','X3 开发版',
            'X3W 开发版','X3W 开发版',
            'M2/M2S 台湾版','M2/M2S 台湾版',
            'M2/M2S 香港版','M2/M2S 香港版',
            'M2/M2S 体验版','M2/M2S 体验版',
            'M2A 开发版','M2A 开发版',
            'M2A 体验版','M2A 体验版',
            'M2A 中国移动版','M2A 中国移动版',
            'H2 不稳定版','H2 不稳定版',
            'H2W 不稳定版','H2W 不稳定版',
            'X3 原生版','M2/M2S 原生版',
             'X3W 原生版','X3W 原生版',]
str_Marks = ['_Mioneplus_','mione_plus_',
             '_MI2_','aries_images_',
             '_MI3_','pisces_',
             '_MI2TW_','aries_tw_',
             '_MI2HK_','aries_hk_',
             '_MI2Alpha_','aries_alpha_',
             '_MI2A_','taurus_images',
             '_MI2AAlpha_','taurus_alpha_images',
             '_MI2ATD_','taurus_td_images',
             '_HM2_','wt93007_',
             '_HM2W_','wt98007_',
             '_NativeMI3_'
             '_NativeMI2_',
             '_NativeMI3W_','cancro_',]

def walk_dir(dir,topdown=True):

    info = {}

    for root, dirs, files in os.walk(dir, topdown):
        for file_name in files:
            tmp = []
            print os.path.abspath(file_name)
            if DEV_MIUI in file_name and ZIP in file_name:
                print('开发版zip')
                c_name = getRomCName(file_name)
                size = getRomSize(os.path.join(root,file_name))
                md5 = GetFileMd5(os.path.join(root,file_name))
                tmp.append(size)
                tmp.append(md5)
                tmp.append(file_name)
                if c_name not in info.keys():
                    info[c_name] = []
                    info[c_name].append(tmp)
                else:
                    info[c_name].append(tmp)
            elif DEV_IMAGES in file_name and TAR in file_name:
                print('开发版tar')
                c_name = getRomCName(file_name)
                size = getRomSize(os.path.join(root,file_name))
                md5 = GetFileMd5(os.path.join(root,file_name))
                tmp.append(size)
                tmp.append(md5)
                tmp.append(file_name)
                if c_name not in info.keys():
                    info[c_name] = []
                    info[c_name].append(tmp)
                else:
                    info[c_name].append(tmp)
            elif file_name.startswith(STABLE_MIUI):
                print('稳定版')
            elif file_name.startswith(ORIGIN):
                print('原生')

    #print(info)
    return info

def GetFileMd5(filename):
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

def getRomCName(name):
    rom_c_name = ''
    roms_length = len(RomNames)
    for i in xrange(roms_length):
        mark = str_Marks[i]
        if mark in name:
            rom_c_name = RomNames[i]
            break
    return rom_c_name

def getRomSize(filename):
    size = os.path.getsize(filename)/1024/1024
    size = str(size) + 'M'
    return size

def getDate():
    block = '.'
    year,mon,day= time.strftime('%Y'),time.strftime('%m'),time.strftime('%d')
    year = year[-1]
    mon = str(int(mon))
    day = str(int(day))
    mDate = year + block + mon + block + day
    return mDate

class Generate:

    mFolder = ''

    def __init__(self):
        self.getFolder()

    def getFolder(self):
        argv_len = len(sys.argv)
        if argv_len >= 2:
            mFolder = sys.argv[1]
        else:
            mFolder = getDate()
        self.mFolder = mFolder

    def gDownloadUrl(self):

        mFolder = self.mFolder
        info = walk_dir(mFolder)
        if len(sys.argv)>=3:
            version = sys.argv[2]
        else:
            version = getDate()
        body = ''
        head = '【升级提醒】\n—————————————————————————————————————————————————— \n\n'
        end = ' '
        url_head = 'http://ota.n.miui.com/ota/'+version+'/'
        for key in info.keys():
            if key != '':
                #print('key:%s'%key)
                length = len(info[key])
                #print('length:%s'%length)
                c_name = key
                body += "%s %s\n" % (c_name, version)
                for i in xrange(length):
                    #print('i:%d'%i)
                    tmp = info[key][i]
                    size = tmp[0]
                    md5 = tmp[1]
                    name = tmp[2]
                    if 'tar' in name:
                        rom_type = 'Fastboot线刷包 '
                    else:
                        rom_type = '系统升级卡刷包 '
                    body = '%s%s%s MD5: %s\n%s%s \n\n' % (body, rom_type ,size, md5, url_head, name)
                body += '—————————————————————————————————————————————————— \n\n'
        url =  head + body + end
        return url

if __name__ == '__main__':
    generate = Generate()
    url = generate.gDownloadUrl()
    print url
