# EpimetheusTool

[Epimetheus](https://github.com/wkigen/Epimetheus)的补丁生成工具

功能：
---------

自动对比新旧两个apk，无侵入，在属性、注解和指令上对比，尽可能的找出差异，然后生成补丁包

原理：
---------

把Dex文件反编译成class，在属性、注解和指令上对比，找出变化的class，最后把所有发生变化的class一起编译成Dex文件

使用：
---------

create_patch.bat

or

java  -jar  EpimetheusToolLib.jar   old.apk   new.apk   outputDir   version


