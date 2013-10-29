# -*- coding: utf-8 -*-

import os
import sys
import time
import hashlib

BLOCK = ' '
PYTHON_FILE_PATH = '/data/files'
USER_OTA_PATH = '/data/ota/'
ENG_PATH = '/data/eng/'


#PYTHON_FILE_PATH = '/mnt/hgfs/vmshare/jiahuixing/UiAutomator/bin/test'
#USER_OTA_PATH = '/mnt/hgfs/vmshare/jiahuixing/UiAutomator/bin/test/test/ota/'
#ENG_PATH = '/mnt/hgfs/vmshare/jiahuixing/UiAutomator/bin/test/test/eng/'

DEV_MIUI = 'miui_'
ZIP = 'zip'
DEV_IMAGES = '_images_'
TAR = 'tar'
STABLE_MIUI = 'J'
ORIGIN = 'N'

RomNames = ['M1/M1S-开发版','M1/M1S-开发版',
            'M2/M2S-开发版','M2/M2S-开发版',
            'X3-TD-开发版','X3-TD-开发版',
            'X3-WCDMA-开发版','X3-WCDMA-开发版',
            'M2/M2S-台湾版','M2/M2S-台湾版',
            'M2/M2S-香港版','M2/M2S-香港版',
            'M2/M2S-体验版','M2/M2S-体验版',
            'M2A-WCDMA-开发版','M2A-WCDMA-开发版',
            'M2A-WCDMA-体验版','M2A-WCDMA-体验版',
            'M2A-TD-开发版','M2A-TD-开发版',
            'H2-TD-不稳定版','H2-TD-不稳定版',
            'H2-WCDMA-不稳定版','H2-WCDMA-不稳定版',
            'X3-TD-原生版','X3-TD-原生版',
            'M2/M2S-原生版','M2/M2S-原生版',
            'X3-WCDMA-原生版','X3-WCDMA-原生版',]
str_Marks = ['_Mioneplus_','mione_plus_',
             '_MI2_','aries_images_',
             '_MI3_','pisces_',
             '_MI3W_','cancro_',
             '_MI2TW_','aries_tw_',
             '_MI2HK_','aries_hk_',
             '_MI2Alpha_','aries_alpha_',
             '_MI2A_','taurus_images',
             '_MI2AAlpha_','taurus_alpha_images',
             '_MI2ATD_','taurus_td_images',
             '_HM2_','wt93007_',
             '_HM2W_','HM2013023_',
             '_NativeMI3_','pisces_',
             '_NativeMI2_','aries_images_',
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
#            elif file_name.startswith(STABLE_MIUI) and (TAR or ZIP) in file_name:
#                print('稳定版')
#            elif file_name.startswith(ORIGIN):
#                print('原生')
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

def getArgv():
    length = len(sys.argv)
    if length >= 2:
        print('')
    else:
        return


def createPath():
    cwd = str(os.getcwd()).strip('\n').strip('\r')
    version = getDate()
    internal_version = version + '-internal'
#    choose = raw_input('Pls input 1 to create path:\'%s\' and path :\'%s\'.\n'%(version,internal_version))
#    choose = choose.strip('\r').strip('\n')
#    choose = int(choose)
#    if choose == 1:
    if cwd == PYTHON_FILE_PATH:
        print('File path correct,begin to create path.')
        if not os.path.exists(version):
            print('Path %s is not exist'%version)
            os.mkdir(version)
        else:
            print('Path %s is already exist'%version)
        if not os.path.exists(internal_version):
            print('Path %s is not exist'%internal_version)
            os.mkdir(internal_version)
        else:
            print('Path %s is already exist'%internal_version)
    else:
        print('Pls move this python file to path:%s.'%PYTHON_FILE_PATH)
#    else:
#        return

def moveFilesAndPaths():
    cwd = str(os.getcwd()).strip('\n').strip('\r')
    version = getDate()
    internal_version = version + '-internal'
#    choose = raw_input('Pls input 1 to move files to path:\'%s\' and path :\'%s\'.\n'%(version,internal_version))
#    choose = choose.strip('\r').strip('\n')
#    choose = int(choose)
#    if choose == 1:
    if cwd == PYTHON_FILE_PATH:
        move_internal = 'sudo mv *internal* ' + internal_version
        move_other = 'sudo mv *' + version + '*.zip* *' + version + '*.tar* ' + version
        mv_xml = 'sudo mv *.xml ' + internal_version
        print('Move internal files to path:%s.'%internal_version)
        os.system(move_internal)
        print('Move xml files to path:%s.'%internal_version)
        os.system(mv_xml)
        print('Move ota zip and tar files to path:%s.'%version)
        os.system(move_other)
        move_user_ota = 'sudo mv ' + version + BLOCK + USER_OTA_PATH
        move_eng = 'sudo mv ' + internal_version +  BLOCK + ENG_PATH
        print('Move ota path to path:%s.'%USER_OTA_PATH)
        os.system(move_user_ota)
        print('Move internal path to path:%s.'%ENG_PATH)
        os.system(move_eng)
    else:
        print('Pls move this python file to path:%s.'%PYTHON_FILE_PATH)
#    else:
#        return

def writePrintFormat(version,msg):
    read_mode = 'w'
    file_name = version + '-url.txt'
    file_obj = open(file_name,read_mode)
    file_obj.write(msg)
    file_obj.close()

class Generate:

    mFolder = ''
    mVersion = ''

    def __init__(self):
        self.getVersion()
        self.getFolder()

    def getFolder(self):
        argv_len = len(sys.argv)
        if argv_len >= 2:
            mFolder = sys.argv[1]
        else:
            mFolder = getDate()
        self.mFolder = USER_OTA_PATH + mFolder

    def getVersion(self):
        if len(sys.argv)>=3:
            version = sys.argv[2]
        else:
            version = getDate()
        self.mVersion = version

    def getDownloadUrl(self):

        mFolder = self.mFolder
        info = walk_dir(mFolder)
        version = self.mVersion
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
    createPath()
    moveFilesAndPaths()
    generate = Generate()
    url = generate.getDownloadUrl()
    print url
    #writePrintFormat(generate.mVersion,url)

