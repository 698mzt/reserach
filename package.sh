#!/bin/bash

# 项目打包脚本
# 使用方法:
# ./package.sh test   # 打包测试环境
# ./package.sh prod   # 打包生产环境

# 设置变量
ENVIRONMENT=$1
APP_NAME="ruoyi-admin"

if [ "$ENVIRONMENT" != "test" ] && [ "$ENVIRONMENT" != "prod" ]; then
    echo "使用方法: $0 [test|prod]"
    echo "  test - 打包测试环境"
    echo "  prod - 打包生产环境"
    exit 1
fi

echo "开始打包 $ENVIRONMENT 环境..."

# 清理并打包
mvn clean package -P$ENVIRONMENT -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ 打包成功！"
    echo "📦 JAR文件位置: ruoyi-admin/target/${APP_NAME}-${ENVIRONMENT}.jar"
    echo "📦 WAR文件位置: ruoyi-admin/target/${APP_NAME}-${ENVIRONMENT}.war"
    echo ""
    echo "🚀 启动命令 (JAR):"
    echo "java -jar ruoyi-admin/target/${APP_NAME}-${ENVIRONMENT}.jar --spring.profiles.active=druid-${ENVIRONMENT}"
    echo ""
    echo "🚀 启动命令 (WAR):"
    echo "将 ruoyi-admin/target/${APP_NAME}-${ENVIRONMENT}.war 部署到Tomcat即可"
else
    echo "❌ 打包失败！"
    exit 1
fi