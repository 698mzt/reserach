# 基础镜像
FROM 172.16.28.201:17000/eclipse-temurin:17-jdk

# 设置时区（Linux 容器中用环境变量即可）
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Duser.timezone=Asia/Shanghai"

# 创建应用目录
RUN mkdir -p /app

# 复制 Jar 包到 /app
COPY ruoyi-admin/target/ruoyi-admin.jar /app/ruoyi-admin.jar


# 安装 Arthas 诊断工具（构建时集成，下载 arthas-boot.jar 自举包）
RUN mkdir -p /opt/arthas && \
    # 更新证书缓存，解决https下载ssl报错
    update-ca-certificates && \
    curl -fsSL https://arthas.aliyun.com/arthas-boot.jar -o /opt/arthas/arthas-boot.jar && \
    chmod 664 /opt/arthas/arthas-boot.jar && \
    # 新增arthas快捷启动脚本
    echo '#!/bin/sh' > /usr/local/bin/arthas && \
    echo 'java -jar /opt/arthas/arthas-boot.jar' >> /usr/local/bin/arthas && \
    chmod +x /usr/local/bin/arthas

# 设置工作目录
WORKDIR /app

# 暴露端口
EXPOSE 18081

# 启动命令，带时区参数
ENTRYPOINT ["java", "-Duser.timezone=Asia/Shanghai", "-jar", "ruoyi-admin.jar"]
