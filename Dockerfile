# 基础镜像
FROM 172.16.28.201:17000/eclipse-temurin:17-jre

# 设置时区（Linux 容器中用环境变量即可）
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Duser.timezone=Asia/Shanghai"

# 创建应用目录
RUN mkdir -p /app

# 复制 Jar 包到 /app
COPY ruoyi-admin/target/ruoyi-admin.jar /app/ruoyi-admin.jar

# 设置工作目录
WORKDIR /app

# 暴露端口
EXPOSE 18081

# 启动命令，带时区参数
ENTRYPOINT ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "ruoyi-admin.jar"]
