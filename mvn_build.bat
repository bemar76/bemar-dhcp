@echo off

call mvn clean install
if %ERRORLEVEL% neq 0 (
    echo The maven build failed
	pause
	exit /b 1 
) 

for %%f in (.\target\dhcp-server-*.jar) do copy "%%f" .\docker\
cd docker\

call docker-compose up --build -d

cd ..

