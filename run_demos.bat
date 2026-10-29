@echo off
if not exist bin (
    echo Binaries not found. Compiling first...
    call compile.bat
)
echo Launching Practical Part B Demo Selector...
java -cp bin minibank.demo.AllLabsRunner
pause
