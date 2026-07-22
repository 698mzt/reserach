# 基础镜像
FROM eclipse-temurin:17-jdk

# 设置时区（Linux 容器中用环境变量即可）
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Duser.timezone=Asia/Shanghai"

# 创建应用目录
RUN mkdir -p /app

# 复制 Jar 包到 /app
COPY ruoyi-admin/target/ruoyi-admin.jar /app/ruoyi-admin.jar

# 安装 Arthas 诊断工具（构建时集成，下载 arthas-boot.jar 自举包）
RUN mkdir -p /opt/arthas && \
    curl -fsSL https://arthas.aliyun.com/arthas-boot.jar -o /opt/arthas/arthas-boot.jar && \
    chmod 644 /opt/arthas/arthas-boot.jar

# 设置工作目录
WORKDIR /app

# 暴露端口
EXPOSE 18081

# 启动命令，带时区参数
ENTRYPOINT ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "ruoyi-admin.jar"]
