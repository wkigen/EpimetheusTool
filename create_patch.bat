@echo off
set /p oldApk=������ɰ汾apk·��:
set /p newApk=�������°汾apk·��:
set /p ouputDir=���������Ŀ¼:
set /p version=������汾��:

echo �������ɲ���...
java -jar EpimetheusToolLib.jar %oldApk% %newApk% %ouputDir% %version%
echo �����ɲ���

pause