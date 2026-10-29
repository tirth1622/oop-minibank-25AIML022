@echo off
echo =============================================================
echo             Compiling MiniBank Core Banking System           
echo =============================================================

if not exist bin mkdir bin
if not exist data mkdir data
if not exist logs mkdir logs

echo Compiling Java source files...
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
del sources.txt

if %ERRORLEVEL% equ 0 (
    echo [SUCCESS] Compilation completed successfully! Binaries written to bin/
) else (
    echo [ERROR] Compilation failed! Check compiler output above.
)
pause
