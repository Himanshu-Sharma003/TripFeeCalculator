ECHO OFF
setlocal EnableExtensions EnableDelayedExpansion

set location=%CD%

echo %location%

if not exist %location%\classes mkdir %location%\classes
cd src/main/java
javac -d %location%\classes  com\trip\calculator\commons\*.java

javac -d %location%\classes -classpath %location%\classes com\trip\calculator\model\*.java

javac -d %location%\classes com\trip\calculator\cache\ITripChargeCache.java

javac -d %location%\classes -classpath %location%\classes com\trip\calculator\cache\impl\*.java

javac  -Xlint:none -d %location%\classes -classpath %location%\classes com\trip\calculator\cache\TripChargeCacheManager.java

javac -d %location%\classes -classpath %location%\classes com\trip\calculator\commons\exception\*.java

javac -d %location%\classes -classpath %location%\classes com\trip\calculator\TripCalculator.java

cd ../resources
copy /y *.properties %location%\classes

javac -d %location%\classes  ../../test/java/com/trip/calculator/common/*.java

javac -d %location%\classes  -classpath %location%\classes  ../../test/java/com/trip/calculator/test/*.java

javac -d %location%\classes  -classpath %location%\classes ../../test/java/com/trip/calculator/*.java

set /p "userInput=Do you want to execute test cases (Y/N)?"

IF "%userInput%"== "y" set  true=1
IF "%userInput%"== "Y"  set true=1
IF defined TRUE (
	echo "##########################################################"
	echo "Executing test  scenario"
	java -classpath %location%\classes com/trip/calculator/TestExecutor
	echo "##########################################################"
) 

cd ../java
java -classpath %location%\classes com/trip/calculator/TripCalculator
pause
