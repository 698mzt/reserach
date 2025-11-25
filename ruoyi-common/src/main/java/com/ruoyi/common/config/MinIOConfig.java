package com.ruoyi.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MinIO配置类
 * 
 * @author ruoyi
 */
@Component
@ConfigurationProperties(prefix = "minio")
public class MinIOConfig
{
    /** 是否启用MinIO模式 */
    private static boolean enabled;

    /** MinIO服务器地址，例如: http://localhost:9000 */
    private static String endpoint;

    /** 访问密钥 */
    private static String accessKey;

    /** 私钥 */
    private static String secretKey;

    /** 存储桶名称 */
    private static String bucketName;

    public static boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        MinIOConfig.enabled = enabled;
    }

    public static String getEndpoint()
    {
        return endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        MinIOConfig.endpoint = endpoint;
    }

    public static String getAccessKey()
    {
        return accessKey;
    }

    public void setAccessKey(String accessKey)
    {
        MinIOConfig.accessKey = accessKey;
    }

    public static String getSecretKey()
    {
        return secretKey;
    }

    public void setSecretKey(String secretKey)
    {
        MinIOConfig.secretKey = secretKey;
    }

    public static String getBucketName()
    {
        return bucketName;
    }

    public void setBucketName(String bucketName)
    {
        MinIOConfig.bucketName = bucketName;
    }
}