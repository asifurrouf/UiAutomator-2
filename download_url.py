# -*- coding: utf-8 -*-

import os
import sys
import time
import re
import hashlib

'''

[color=#000][font=Simsun][size=3][table=90%]
[tr][td][url=http://bigota.d.miui.com/2.4.25/miui_Mioneplus_2.4.25_492c2922a2_4.0.zip]Mione Plus 2.4.25[/url][/td][td]139M[/td][td]492c2922a27fa9cc9c56b4736a25cb3f[/td][/tr]
[tr][td][url=http://bigota.d.miui.com/2.4.25/miui_NS_2.4.25_a6d4b305ee_4.0.zip]Nexus S 2.4.25[/url][/td][td]114M[/td][td]a6d4b305ee4e211f617f60ff59fb9d9b[/td][/tr]
[tr][td][url=http://bigota.d.miui.com/2.4.25/miui_GalaxyNexus_2.4.25_096a7e2882_4.0.zip]Galaxy Nexus 2.4.25[/url][/td][td]135M[/td][td]096a7e28828eecd4593f101e51209ad7[/td][/tr]
[tr][td][url=http://bigota.d.miui.com/2.4.25/miui_Honor_2.4.25_244e0b1c96_4.0.zip]Honor 2.4.25[/url][/td][td]158M[/td][td]244e0b1c9664f244c849f3f411f4ee2e[/td][/tr]
[tr][td][url=http://bigota.d.miui.com/2.4.25/miui_Sensation_2.4.25_6d86e8e034_4.0.zip]Sensation 2.4.25[/url][/td][td]241M[/td][td]6d86e8e0342b29a1ea7b00f88a233b11[/td][/tr]
[tr][td][url=http://bigota.d.miui.com/2.4.25/miui_i9100_2.4.25_e04e7af513_4.0.zip]Galaxy SII 2.4.25[/url][/td][td]198M[/td][td]e04e7af51383ce2d3506193634c90fbc[/td][/tr]
[tr][td][url=http://bigota.d.miui.com/2.4.25/miui_LT18i_2.4.25_70bef5c20a_4.0.zip]Xperia arc S 2.4.25[/url][/td][td]133M[/td][td]70bef5c20a893022a067b3ea972e55d0[/td][/tr]
[/table][/size][/font][/color]

'''

def walk_dir(dir,topdown=True):
    
    miui = 'miui_'
    sizes = list()
    md5s = list()
    li = list()
    roms = list()
    for root, dirs, files in os.walk(dir, topdown):
        for name in files:
            print os.path.abspath(name)
            #print '进来了1'
            if miui in name:
                #print '进来了2'
                rom = romName(name)
                size = os.path.getsize(os.path.join(root,name))/1024/1024
                size = str(size) + 'M'
                #print 'size=='+str(size)
                sizes.append(size)
                md5 = GetFileMd5(os.path.join(root,name))
                #print 'md5=='+str(md5)
                md5s.append(md5)
                li.append(name)
                roms.append(rom)

    return sizes,md5s,li,roms

def romName(name):
    
    RomNames = ('MI1/MI1S','MI2/MI2S','MI3','MiTwo TW','MiTwo HK','MiTwo alpha','Mioneplus JB','MI2A','MiTwo A Alpha','MiTwo A TD','Lenovo S820','OPPO Find 5','MEIZU MX2','Nexus S','Galaxy Nexus','Nexus 7','Honor','Honor 2','Ascend D1','Ascend P1','Desire S','Incredible S','Sensation','One S','One X','EVO 3D','Galaxy S2','Galaxy S3','Galaxy Note','Xperia arc S','Xperia S','Finder','V8','LU6200','ZTE U970','Atrix 2','Razr','Galaxy Note 2')
    subStrs = ('_Mioneplus_','_MI2_','_MI3_','_MI2TW_','_MI2HK_','_MI2Alpha_','_NativeMioneplus_','_MI2A_','_MI2AAlpha_','_MI2ATD_','_S820_','_Find5_','_MX2_','_NS_','_GalaxyNexus_','_Nexus7_','_Honor_','_Honor2_','_AscendD1_','_P1_','_DesireS_','_IncredibleS_','_Sensation_','_OneS_','_OneX_','_X515M_','_i9100_','_i9300_','_GalaxyNote_','_LT18i_','_LT26i','_Finder_','_V8_','_LU6200_','_U970_','_ME865_','_Razr_','_Note2_')
    rom = ''
    ronlen = len(RomNames)
    sublen = len(subStrs)
    for i in range(0,ronlen):
        sub = subStrs[i]
        if sub in name:
            rom = RomNames[i]
            break
    return rom
    
def GetFileMd5(filename):
    if not os.path.isfile(filename):
        return
    myhash = hashlib.md5()
    f = file(filename,'rb')
    while True:
        b = f.read(8096)
        if not b :
            break
        myhash.update(b)
    f.close()
    return myhash.hexdigest().lower()

def changeYM(li,flag):

    liMon = ('01','02','03','04','05','06','07','08','09')
    liYear =  ('11','12','13','14','15','16','17','18','19')
    licha = ('1','2','3','4','5','6','7','8','9')
    if li in liMon and flag == 0:
        idx = liMon.index(li)
        li = licha[idx]
    if li in liYear and flag == 1:
        idx = liYear.index(li)
        li = licha[idx]
    return li

class Generate:

    mFolder = ''
    
    def getFolder(self):

        argvlen = len(sys.argv)
        '''
        print len(sys.argv)
        print type(sys.argv)
        print str(sys.argv)
        for a in range(1, len(sys.argv)):
                print sys.argv[a]
        '''
        year = ''
        mon = ''
        day = ''
        if argvlen >= 2:
            mFolder = sys.argv[1]
        else:
            #localtime = time.localtime(time.time())
            year = time.strftime('%y',time.localtime(time.time()))
            mon = time.strftime('%m',time.localtime(time.time()))
            #转换
            day = time.strftime('%d',time.localtime(time.time()))
            year = changeYM(year,1)
            mon = changeYM(mon,0)
            day = changeYM(day,0)
            mFolder = year+'.'+mon+'.'+day
        return mFolder

    def gDownloadUrl(self,mFolder):

        #mFolder = Generate.getFolder()
        #print 'mFolder = '+ str(mFolder)
        sizes,md5s,li,roms = walk_dir(mFolder)
        version = sys.argv[2]
        url = ''
        body = ''
        head = '[color=#000][font=Simsun][size=3][table=90%]\n'
        end = '[/table][/size][/font][/color]'
        urlhead = '[tr][td][url=http://bigota.d.miui.com'+'/'+version+'/'

        url_end = ']'
        urlend = '[/url][/td][td]'
        sizeend = '[/td][td]'
        md5end = '[/td][/tr]\n'
        length = len(li)
        i = 0
        print 'length = ' + str(length)
        for i in range(0,length):
            name = li[i]
            size = sizes[i]
            md5 = md5s[i]
            rom = roms[i]
            body = body + urlhead + name + url_end+rom + " " + version + urlend + size + sizeend + md5 + md5end
            i = i+1
        url =  head + body + end
        return url

if __name__ == '__main__':
    #walk_dir(sys.argv[1])
    generate = Generate()
    mFolder = ''
    generate.mFolder = generate.getFolder()
    #print generate.mFolder
    generate.getFolder()
    mFolder = generate.mFolder
    url = generate.gDownloadUrl(mFolder)
    print url

    
    
