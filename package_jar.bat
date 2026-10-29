@echo off
echo =============================================================
echo             Packaging MiniBank Runnable JAR                  
echo =============================================================

if not exist bin (
    echo Compiling classes...
    call compile.bat
)

set JAR_CMD=jar
where jar >nul 2>&1
if %ERRORLEVEL% neq 0 (
    if exist "%JAVA_HOME%\bin\jar.exe" (
        set JAR_CMD="%JAVA_HOME%\bin\jar.exe"
    ) else if exist "C:\Program Files\Java\jdk-24\bin\jar.exe" (
        set JAR_CMD="C:\Program Files\Java\jdk-24\bin\jar.exe"
    ) else (
        for /d %%D in ("C:\Program Files\Java\jdk*") do (
            if exist "%%D\bin\jar.exe" set JAR_CMD="%%D\bin\jar.exe"
        )
    )
)

echo Using JAR executable: %JAR_CMD%
echo Packaging minibank.jar with Main-Class: minibank.MiniBank...
%JAR_CMD% cfe minibank.jar minibank.MiniBank -C bin .

if %ERRORLEVEL% equ 0 (
    echo [SUCCESS] minibank.jar created successfully!
    echo To execute: java -jar minibank.jar
) else (
    echo [ERROR] JAR packaging failed.
)
pause
