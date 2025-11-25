package com.ruoyi.common.utils.file;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.config.MinIOConfig;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.file.FileNameLengthLimitExceededException;
import com.ruoyi.common.exception.file.FileSizeLimitExceededException;
import com.ruoyi.common.exception.file.InvalidExtensionException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.Seq;

import static com.ruoyi.common.utils.ShiroUtils.getSysUser;

/**
 * 文件上传工具类
 *
 * @author ruoyi
 */
public class FileUploadUtils {
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 200;
    private static final Logger log = LoggerFactory.getLogger(FileUploadUtils.class);

    /**
     * 默认上传的地址
     */
    private static String defaultBaseDir = RuoYiConfig.getProfile();

    public static void setDefaultBaseDir(String defaultBaseDir) {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    public static String getDefaultBaseDir() {
        return defaultBaseDir;
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    public static final String upload(MultipartFile file) throws IOException {
        try {
            return upload(getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException {
        try {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir          相对应用的基目录
     * @param file             上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException       如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException                          比如读写文件出错时
     * @throws InvalidExtensionException            文件校验异常
     */
    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException {
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, allowedExtension);

        // 检查是否启用MinIO，如果启用则上传到MinIO，否则上传到本地
        if (MinIOUtils.isEnabled()) {
            return uploadToMinIO(file);
        } else {
            String fileName = extractFilename(file);
            String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();
            file.transferTo(Paths.get(absPath));
            return getPathFileName(baseDir, fileName);
        }
    }

    /**
     * 上传文件到MinIO
     *
     * @param file 上传的文件
     * @return 文件访问路径
     * @throws IOException
     */
    private static String uploadToMinIO(MultipartFile file) throws IOException {
        try {
            String fileName = extractFilename(file);
            String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
            String uniqueFileName = MinIOUtils.upload(fileName, file.getInputStream(), file.getSize(), contentType);
            return uniqueFileName;
        } catch (Exception e) {
            throw new IOException("上传文件到MinIO失败: " + e.getMessage(), e);
        }
    }
    public static final String newupload(String baseDir, MultipartFile file,String model)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH)
        {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }
        assertAllowed(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);

        // 检查是否启用MinIO，如果启用则上传到MinIO，否则上传到本地
        if (MinIOUtils.isEnabled()) {
            log.info("MinIO已启用，将上传文件到MinIO");
            return uploadToMinIONew(file, model);
        } else {
            log.info("MinIO未启用，将上传文件到本地");
            String fileName = newFilename(file, model);
            String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();
            file.transferTo(Paths.get(absPath));
            return getPathFileName(baseDir, fileName);
        }
    }

    /**
     * 上传文件到MinIO（新的文件名生成方式）
     *
     * @param file  上传的文件
     * @param model 模型参数
     * @return 文件访问路径
     * @throws IOException
     */
    private static String uploadToMinIONew(MultipartFile file, String model) throws IOException {
        try {
            String fileName = newFilename(file, model);
            String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
            String uniqueFileName = MinIOUtils.upload(fileName, file.getInputStream(), file.getSize(), contentType);
            return uniqueFileName;
        } catch (Exception e) {
            throw new IOException("上传文件到MinIO失败: " + e.getMessage(), e);
        }
    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(MultipartFile file) {
        return StringUtils.format("{}/{}_{}.{}", DateUtils.datePath(),
                FilenameUtils.getBaseName(file.getOriginalFilename()), Seq.getId(Seq.uploadSeqType), getExtension(file));
    }
    /**
     * 编码文件名
     */
    public static final String newFilename(MultipartFile file,String model)
    {
//        获取学院name
        String parentName = "admin";
        if (getSysUser().getParentId() == 101){
            parentName = "软件学院";
        }else
        if (getSysUser().getParentId() == 102){
            parentName = "商学院";
        }
//        获取当前年份
        LocalDate nowDate = LocalDate.now();
        String year = String.valueOf(nowDate.getYear());
//        获得模块名
        String modelName = "admin";
        switch (model){
            case "hxkt":
                modelName="横向课题";
                break;
            case "zxkt":
                modelName="纵向课题";
                break;
            case "cgzh":
                modelName="成果转化";
                break;
            case "lw":
                modelName="论文";
                break;
            case "jcrz":
                modelName="教材软著";
                break;
            case "zlrz":
                modelName="专利软著";
                break;
            case "jzbg":
                modelName="讲座报告";
                break;
            case "jl":
                modelName="奖励";
                break;
        }
        return StringUtils.format("{}/{}/{}/{}/{}.{}",
                parentName,
                getSysUser().getDept().getDeptName(),
                getSysUser().getUserName(),
                modelName,
                FilenameUtils.getBaseName(file.getOriginalFilename())+"-"+getSysUser().getUserName(),
//                Seq.getId(Seq.uploadSeqType),
                getExtension(file));
    }

    public static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.exists()) {
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    public static final String getPathFileName(String uploadDir, String fileName) throws IOException {
        int dirLastIndex = RuoYiConfig.getProfile().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        return Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws InvalidExtensionException
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException {
        long size = file.getSize();
        if (size > DEFAULT_MAX_SIZE) {
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
        }

        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
                        fileName);
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
                        fileName);
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
                        fileName);
            } else if (allowedExtension == MimeTypeUtils.VIDEO_EXTENSION) {
                throw new InvalidExtensionException.InvalidVideoExtensionException(allowedExtension, extension,
                        fileName);
            } else {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            }
        }
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }




    /**
     * 成果转化 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload_cgzh(String baseDir, MultipartFile file) throws IOException {
        try {
            return upload_cgzh(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }


    /**
     * 成果转化 文件上传
     *
     * @param baseDir          相对应用的基目录
     * @param file             上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException       如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException                          比如读写文件出错时
     * @throws InvalidExtensionException            文件校验异常
     */
    public static final String upload_cgzh(String baseDir, MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException {
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, allowedExtension);

        // 检查是否启用MinIO，如果启用则上传到MinIO，否则上传到本地
        if (MinIOUtils.isEnabled()) {
            return uploadToMinIO(file);
        } else {
            String fileName = extractFilename_cgzh(file);
            String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();
            file.transferTo(Paths.get(absPath));
            return getPathFileName(baseDir, fileName);
        }
    }

    /**
     *  成果转化 编码文件名
     */
    public static final String extractFilename_cgzh(MultipartFile file) {
        return StringUtils.format("{}_{}.{}",
                FilenameUtils.getBaseName(file.getOriginalFilename()),
                Seq.getId(Seq.uploadSeqType),
                getExtension(file)
        );
    }
}
