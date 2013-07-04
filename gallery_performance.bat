adb devices
set ANDROID_SERIAL=b1bc8db4
set srcPath=D:\GalleryAutoTest\script
cd /d %srcPath%
adb -s %ANDROID_SERIAL% root
rem adb -s %ANDROID_SERIAL% push 200Pictures /sdcard/Pictures/200Pictures
rem adb -s %ANDROID_SERIAL% push 100Albums /sdcard/Pictures/
adb -s %ANDROID_SERIAL% remount
adb -s %ANDROID_SERIAL% push uiautomator.jar /system/framework/
adb -s %ANDROID_SERIAL% push uiautomator.odex /system/framework/
adb -s %ANDROID_SERIAL% push uitest.jar /data/local/tmp/
adb -s %ANDROID_SERIAL% shell mkdir /sdcard/ResultGalleryAutoTest/
for /l %%i in (1 1 100) do adb -s %ANDROID_SERIAL% shell uiautomator runtest uitest.jar  -c com.gallery.performance.AlbumThumbsnailDecodingSpeed -c com.gallery.performance.PictureThumbsnailDecodingSpeed -c com.gallery.performance.BrowseBigPictures
echo l
pause

