@echo off

for %%X in (devenv.exe) do (set DEVENV_FOUND=%%~$PATH:X)
if not defined DEVENV_FOUND (
    echo devenv.exe was not found. Make sure the Visual Studio developer tools are installed and in PATH
    goto end
)

for %%X in (mvn.cmd) do (set MVN_FOUND=%%~$PATH:X)
if not defined MVN_FOUND (
    echo mvn.cmd was not found. Make sure Maven is installed and in PATH
    goto end
)

echo [95mGenerating Java JNI headers[0m
cd java
call mvn compile
cd ..

echo [95mCopying header files[0m
robocopy .\java\generated_headers .\native\java_headers *.h

cd native
echo [95mCompiling native x86 DLL[0m
devenv renderdoc.sln /Build "Release|x86"

echo [95mCompiling native x64 DLL[0m
devenv renderdoc.sln /Build "Release|x64"

echo [95mCopying native DLLs to java resources[0m
copy .\Release\renderdoc.dll ..\java\src\main\resources\renderdoc_wrapper_x86.dll
copy .\x64\Release\renderdoc.dll ..\java\src\main\resources\renderdoc_wrapper_x64.dll
cd ..

echo [95mGenerating JAR file[0m
cd java
call mvn package
cd ..

copy .\java\target\*-jar-with-dependencies.jar .
echo [95mDone[0m
:end