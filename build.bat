@echo off
echo Generating Java JNI headers
cd java
call mvn compile
cd ..
robocopy .\java\generated_headers .\native\java_headers *.h

cd native
echo Compiling native x86 DLL
devenv renderdoc.sln /Build "Release|x86"

echo Compiling native x64 DLL
devenv renderdoc.sln /Build "Release|x64"

echo Copying native DLLs to java resources
copy .\Release\renderdoc.dll ..\java\src\main\resources\renderdoc_wrapper_x86.dll
copy .\x64\Release\renderdoc.dll ..\java\src\main\resources\renderdoc_wrapper_x64.dll
cd ..

echo Generating JAR file
cd java
call mvn package
cd ..

copy .\java\target\*-jar-with-dependencies.jar .