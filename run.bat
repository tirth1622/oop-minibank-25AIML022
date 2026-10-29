@echo off
if not exist bin (
    echo Binaries not found. Compiling first...
    call compile.bat
)
echo Launching MiniBank Interactive Console Application...
java -cp bin minibank.MiniBank
pause
