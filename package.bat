@echo off
rem 项目打包脚本 (Windows版本)
rem 使用方法:
rem package.bat test   # 打包测试环境
rem package.bat prod   # 打包生产环境

set ENVIRONMENT=%1
set APP_NAME=ruoyi-admin

if "%ENVIRONMENT%"=="test" goto TEST_ENV
if "%ENVIRONMENT%"=="prod" goto PROD_ENV

echo 使用方法: %0 [test^|prod]
echo   test - 打包测试环境
echo   prod - 打包生产环境
goto END

:TEST_ENV
echo 开始打包测试环境...
call mvn clean package -Ptest -DskipTests
if %ERRORLEVEL% EQU 0 goto SUCCESS_TEST
echo ❌ 打包失败！
goto END

:PROD_ENV
echo 开始打包生产环境...
call mvn clean package -Pprod -DskipTests
if %ERRORLEVEL% EQU 0 goto SUCCESS_PROD
echo ❌ 打包失败！
goto END

:SUCCESS_TEST
echo ✅ 打包成功！
echo 📦 JAR文件位置: ruoyi-admin\target\%APP_NAME%-test.jar
echo 📦 WAR文件位置: ruoyi-admin\target\%APP_NAME%-test.war
echo.
echo 🚀 启动命令 (JAR):
echo java -jar ruoyi-admin\target\%APP_NAME%-test.jar
echo.
echo 🚀 启动命令 (WAR):
echo 将 ruoyi-admin\target\%APP_NAME%-test.war 部署到Tomcat即可
goto END

:SUCCESS_PROD
echo ✅ 打包成功！
echo 📦 JAR文件位置: ruoyi-admin\target\%APP_NAME%-prod.jar
echo 📦 WAR文件位置: ruoyi-admin\target\%APP_NAME%-prod.war
echo.
echo 🚀 启动命令 (JAR):
echo java -jar ruoyi-admin\target\%APP_NAME%-prod.jar
echo.
echo 🚀 启动命令 (WAR):
echo 将 ruoyi-admin\target\%APP_NAME%-prod.war 部署到Tomcat即可
goto END

:END
pause