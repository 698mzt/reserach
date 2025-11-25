package com.ruoyi.common.utils.file;

import java.io.InputStream;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.ruoyi.common.config.MinIOConfig;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;

/**
 * MinIO文件存储工具类
 * 
 * @author ruoyi
 */
@Component
public class MinIOUtils
{
    private static final Logger log = LoggerFactory.getLogger(MinIOUtils.class);

    private static MinioClient minioClient;

    @PostConstruct
    public void init()
    {
        try
        {
            // 检查是否启用MinIO
            if (!MinIOConfig.isEnabled())
            {
                log.info("MinIO未启用，将使用本地文件存储");
                return;
            }

            // 初始化MinIO客户端
            minioClient = MinioClient.builder()
                    .endpoint(MinIOConfig.getEndpoint())
                    .credentials(MinIOConfig.getAccessKey(), MinIOConfig.getSecretKey())
                    .build();

            // 检查存储桶是否存在，不存在则创建
            createBucketIfNotExists();
        }
        catch (Exception e)
        {
            log.error("初始化MinIO客户端失败: {}", e.getMessage());
            throw new RuntimeException("初始化MinIO客户端失败", e);
        }
    }

    /**
     * 创建存储桶（如果不存在）
     */
    private void createBucketIfNotExists() throws Exception
    {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(MinIOConfig.getBucketName()).build());
        if (!found)
        {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(MinIOConfig.getBucketName()).build());
            log.info("创建MinIO存储桶: {}", MinIOConfig.getBucketName());
        }
        else
        {
            log.info("MinIO存储桶已存在: {}", MinIOConfig.getBucketName());
        }
    }

    /**
     * 上传文件
     * 
     * @param filename 文件名
     * @param stream 文件流
     * @param size 文件大小
     * @param contentType 文件类型
     * @return 文件访问路径
     */
    public static String upload(String filename, InputStream stream, long size, String contentType)
    {
        try
        {
            if (!MinIOConfig.isEnabled())
            {
                throw new RuntimeException("MinIO未启用");
            }

            // 使用UUID生成唯一的文件名，避免文件名冲突
            // 保持原始的路径结构，并在前面添加UUID以确保唯一性
            String uniqueFilename = UUID.randomUUID().toString().replace("-", "") + "_" + filename;
            
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(MinIOConfig.getBucketName())
                            .object(uniqueFilename)
                            .stream(stream, size, -1)
                            .contentType(contentType)
                            .build());

            // 返回文件访问路径
            return "/minio/" + MinIOConfig.getBucketName() + "/" + uniqueFilename;
        }
        catch (Exception e)
        {
            log.error("上传文件到MinIO失败: {}", e.getMessage());
            throw new RuntimeException("上传文件到MinIO失败", e);
        }
    }

    /**
     * 删除文件
     * 
     * @param objectName 对象名称
     */
    public static void delete(String objectName)
    {
        try
        {
            if (!MinIOConfig.isEnabled())
            {
                throw new RuntimeException("MinIO未启用");
            }

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(MinIOConfig.getBucketName())
                            .object(objectName)
                            .build());
        }
        catch (Exception e)
        {
            log.error("删除MinIO文件失败: {}", e.getMessage());
            throw new RuntimeException("删除MinIO文件失败", e);
        }
    }

    /**
     * 下载文件
     * 
     * @param objectName 对象名称
     * @return 文件流
     */
    public static InputStream download(String objectName)
    {
        try
        {
            if (!MinIOConfig.isEnabled())
            {
                throw new RuntimeException("MinIO未启用");
            }

            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(MinIOConfig.getBucketName())
                            .object(objectName)
                            .build());
        }
        catch (Exception e)
        {
            log.error("下载MinIO文件失败: {}", e.getMessage());
            throw new RuntimeException("下载MinIO文件失败", e);
        }
    }

    /**
     * 检查MinIO是否已启用
     */
    public static boolean isEnabled()
    {
        return MinIOConfig.isEnabled();
    }
}