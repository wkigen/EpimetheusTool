@echo off
set /p oldApk=请输入旧版本apk路径:
set /p newApk=请输入新版本apk路径:
set /p ouputDir=请输入输出目录:
set /p version=请输入版本号:

echo 正在生成补丁...
java -jar EpimetheusToolLib.jar %oldApk% %newApk% %ouputDir% %version%
echo 已生成补丁

pause