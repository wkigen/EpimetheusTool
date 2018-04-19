@echo off
set /p oldApk=input the old apk:
set /p newApk=input the new apk:
set /p ouputDir=input the output folder:
set /p version=input the version:

java -jar EpimetheusToolLib.jar %oldApk% %newApk% %ouputDir% %version%

pause