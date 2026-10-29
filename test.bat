@echo off
if not exist bin (
    echo Binaries not found. Compiling first...
    call compile.bat
)
echo Running Automated System Test Suite...
java -cp bin minibank.test.MiniBankTestSuite
pause
