# EpimetheusTool

epimetheus的补丁生成工具

java -jar EpimetheusToolLib.jar old.apk new.apk outputDir version

自动对比新旧两个apk，无侵入，在属性、注解和指令上对比，尽可能细致的找出差异，然后生成补丁包
