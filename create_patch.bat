@echo off
set /p ouputDir=���������Ŀ¼:
set /p version=������汾��:

echo �������ɲ���...
java -jar EpimetheusToolLib.jar %oldApk% %newApk% %ouputDir% %version%
echo �����ɲ���

pause
