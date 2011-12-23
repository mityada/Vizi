@echo off
if [%1] == [__CLASSPATH__] (
    set classpath=%classpath%;%2
) else (
    set classpath=%JAVA_HOME%\lib\tools.jar
    for %%a in (meta\bin\lib\*.jar) do call %0 __CLASSPATH__ %%a
    call %java_home%\bin\java org.apache.tools.ant.Main %*
)
