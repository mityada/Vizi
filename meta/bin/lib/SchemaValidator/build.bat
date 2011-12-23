@echo off
set classpath=..\ant.jar;..\xercesImpl.jar;
del ..\SchemaValidatorTask.jar
del *.class
call javac -deprecation SchemaValidatorTask.java ViziTools.java
call jar -cf ../SchemaValidatorTask.jar *.class