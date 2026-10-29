@echo off
if not exist minibank.jar (
    echo minibank.jar not found! Packaging first...
    call package_jar.bat
)
echo Launching minibank.jar via java -jar...
java -jar minibank.jar
pause
