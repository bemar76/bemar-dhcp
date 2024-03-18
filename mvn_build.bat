@echo off
call mvn clean install
for %%f in (.\target\dhcp-server-*.jar) do copy "%%f" .\docker\
