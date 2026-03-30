package com.ruoyi.web.controller.common;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.service.ISciHorizontalApplyService;
import com.ruoyi.system.service.ISciHorizontalApplyVerticalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MinIOUtils;

/**
 * 通用请求处理
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired private ServerConfig serverConfig;

    @Autowired
    private
    ISciHorizontalApplyVerticalService sciHorizontalApplyVerticalService;

    @Autowired
    private ISciHorizontalApplyService sciHorizontalApplyService;

    private static final String FILE_DELIMETER = ",";

    /**
     * 通用下载请求
     * 
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }

            // 检查是否为MinIO文件路径
            if (MinIOUtils.isEnabled() && fileName.startsWith("/minio/")) {
                // 从MinIO路径中提取bucket和object名称
                String[] parts = fileName.substring("/minio/".length()).split("/", 2);
                if (parts.length == 2) {
                    String bucketName = parts[0];
                    String objectName = parts[1];

                    // 从MinIO下载文件
                    try (InputStream inputStream = MinIOUtils.download(objectName)) {
                        // 提取文件名，去除路径并处理下划线后缀
                        String fullPath = objectName;
                        String fileNameWithExt = fullPath.substring(fullPath.lastIndexOf("/") + 1);
                        String nameWithoutExt = fileNameWithExt.substring(0, fileNameWithExt.lastIndexOf("."));
                        String extension = fileNameWithExt.substring(fileNameWithExt.lastIndexOf("."));
                        
                        // 查找最后一个下划线的位置，移除序列号部分（如 _4）
                        int lastUnderscoreIndex = nameWithoutExt.lastIndexOf("_");
                        if (lastUnderscoreIndex != -1) {
                            String prefix = nameWithoutExt.substring(0, lastUnderscoreIndex);
                            String suffix = nameWithoutExt.substring(lastUnderscoreIndex + 1);
                            
                            // 如果下划线后的部分是数字，则认为是序列号，移除它
                            if (suffix.matches("\\d+")) {
                                nameWithoutExt = prefix;
                            } else {
                                // 如果下划线后的部分不是数字，则保留完整名称
                                nameWithoutExt = nameWithoutExt;
                            }
                        }
                        
                        String realFileName = nameWithoutExt + extension;
                        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                        FileUtils.setAttachmentResponseHeader(response, realFileName);
                        // 将文件流写入响应输出流
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            response.getOutputStream().write(buffer, 0, bytesRead);
                        }
                        response.getOutputStream().flush();
                    } catch (Exception e) {
                        log.error("从MinIO下载文件失败", e);
                        throw e;
                    }
                } else {
                    throw new Exception(StringUtils.format("MinIO文件路径格式不正确: {}", fileName));
                }
            } else {
                // 原来的本地文件下载逻辑
                // 提取文件名，去除路径并处理下划线后缀
                String fileNameWithExt = fileName.substring(fileName.lastIndexOf("/") + 1);
                String nameWithoutExt = fileNameWithExt.substring(0, fileNameWithExt.lastIndexOf("."));
                String extension = fileNameWithExt.substring(fileNameWithExt.lastIndexOf("."));
                
                // 查找最后一个下划线的位置，移除序列号部分（如 _4）
                int lastUnderscoreIndex = nameWithoutExt.lastIndexOf("_");
                if (lastUnderscoreIndex != -1) {
                    String prefix = nameWithoutExt.substring(0, lastUnderscoreIndex);
                    String suffix = nameWithoutExt.substring(lastUnderscoreIndex + 1);
                    
                    // 如果下划线后的部分是数字，则认为是序列号，移除它
                    if (suffix.matches("\\d+")) {
                        nameWithoutExt = prefix;
                    } else {
                        // 如果下划线后的部分不是数字，则保留完整名称
                        nameWithoutExt = nameWithoutExt;
                    }
                }
                
                String realFileName = nameWithoutExt + extension;
                String filePath = RuoYiConfig.getDownloadPath() + fileName;

                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                FileUtils.setAttachmentResponseHeader(response, realFileName);
                FileUtils.writeBytes(filePath, response.getOutputStream());
                if (delete)
                {
                    FileUtils.deleteFile(filePath);
                }
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload/{model}")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file,@PathVariable("model") String model) throws Exception

    {
        try
        {
            log.info("开始处理文件上传请求");
            log.info("上传模块: {}", model);
            // 记录文件基本信息
            if (file != null) {
                log.info("文件原始名称: {}", file.getOriginalFilename());
                log.info("文件大小: {} bytes", file.getSize());
                log.info("文件类型: {}", file.getContentType());
            } else {
                log.warn("上传文件为空");
            }

            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            log.info("文件上传基础路径: {}", filePath);

            // 上传并返回新文件名称
            log.info("调用FileUploadUtils.newupload方法开始上传文件");
            String fileName = FileUploadUtils.newupload(filePath, file, model);
            log.info("文件上传完成，生成的文件路径: {}", fileName);

            String url = "";
            // 根据是否使用MinIO来决定URL生成方式
            if (MinIOUtils.isEnabled() && fileName.startsWith("/minio/")) {
                // 如果使用MinIO且返回的是MinIO路径，直接使用
                url = fileName;
            } else {
                log.error("不是minio路径，文件上传失败");
                return AjaxResult.error("不是minio路径，请联系管理员");
            }
            log.info("文件访问URL: {}", url);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            log.info("文件上传成功，返回结果: {}", ajax);
            return ajax;
        }
        catch (Exception e)
        {
            log.error("文件上传失败", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    @ResponseBody
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files)
            {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = "";
                // 根据是否使用MinIO来决定URL生成方式
                if (MinIOUtils.isEnabled() && fileName.startsWith("/minio/")) {
                    // 如果使用MinIO且返回的是MinIO路径，直接使用
                    url = fileName;
                } else {
                    // 否则使用原来的URL生成方式
                    url = serverConfig.getUrl() + fileName;
                }
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }

            // 检查是否为MinIO文件路径
            if (MinIOUtils.isEnabled() && resource.startsWith("/minio/")) {
                // 从MinIO路径中提取bucket和object名称
                String[] parts = resource.substring("/minio/".length()).split("/", 2);
                if (parts.length == 2) {
                    String bucketName = parts[0];
                    String objectName = parts[1];

                    // 从MinIO下载文件
                    try (InputStream inputStream = MinIOUtils.download(objectName)) {
                        String downloadName = StringUtils.substringAfterLast(objectName, "/");
                        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                        FileUtils.setAttachmentResponseHeader(response, downloadName);
                        // 将文件流写入响应输出流
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            response.getOutputStream().write(buffer, 0, bytesRead);
                        }
                        response.getOutputStream().flush();
                    } catch (Exception e) {
                        log.error(resource,"从MinIO下载文件失败", e);
                        throw e;
                    }
                } else {
                    throw new Exception(StringUtils.format("MinIO文件路径格式不正确: {}", resource));
                }
            } else {
                // 原来的本地资源下载逻辑
                // 本地资源路径
                String localPath = RuoYiConfig.getProfile();
                // 数据库资源地址
                String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
                // 下载名称
                String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                FileUtils.setAttachmentResponseHeader(response, downloadName);
                FileUtils.writeBytes(downloadPath, response.getOutputStream());
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用文件预览请求
     */
    @GetMapping("/preview")
    public void filePreview(String fileName, HttpServletResponse response, HttpServletRequest request)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许预览。 ", fileName));
            }

            // 检查是否为MinIO文件路径
            if (MinIOUtils.isEnabled() && fileName.startsWith("/minio/")) {
                // 从MinIO路径中提取bucket和object名称
                String[] parts = fileName.substring("/minio/".length()).split("/", 2);
                if (parts.length == 2) {
                    String bucketName = parts[0];
                    String objectName = parts[1];

                    // 从MinIO下载文件
                    try (InputStream inputStream = MinIOUtils.download(objectName)) {
                        // 根据文件扩展名设置适当的MIME类型以支持浏览器预览
                        String fileExtension = getFileExtension(objectName);
                        String contentType = getContentTypeByExtension(fileExtension);
                        response.setContentType(contentType);
                        
                        // 将文件流写入响应输出流
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            response.getOutputStream().write(buffer, 0, bytesRead);
                        }
                        response.getOutputStream().flush();
                    } catch (Exception e) {
                        //log.info("从MinIO预览文件失败路径:",);
                        log.error("从MinIO预览文件失败", e);
                        throw e;
                    }
                } else {
                    throw new Exception(StringUtils.format("MinIO文件路径格式不正确: {}", fileName));
                }
            } else {
                // 本地文件预览逻辑
                String filePath = RuoYiConfig.getDownloadPath() + fileName;
                String fileExtension = getFileExtension(fileName);
                String contentType = getContentTypeByExtension(fileExtension);
                response.setContentType(contentType);
                FileUtils.writeBytes(filePath, response.getOutputStream());
            }
        }
        catch (Exception e)
        {
            log.error("预览文件失败", e);
        }
    }

    /**
     * 根据文件扩展名获取MIME类型
     */
    private String getContentTypeByExtension(String fileExtension) {
        if (fileExtension == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        switch (fileExtension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "html":
            case "htm":
                return "text/html";
            case "xml":
                return "application/xml";
            case "json":
                return "application/json";
            case "mp4":
                return "video/mp4";
            case "avi":
                return "video/x-msvideo";
            case "mp3":
                return "audio/mpeg";
            default:
                return MediaType.APPLICATION_OCTET_STREAM_VALUE; // 默认为二进制流
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 下载压缩包请求（支持单个或多个ID）
     *
     * @param ids 文件ID，多个ID用逗号分隔
     */
    @GetMapping("/downloadZip")
    public void downloadZip(@RequestParam("ids") String ids, HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        List<String> allFiles = new ArrayList<>();
        StringBuilder zipNameBuilder = new StringBuilder();

        // 解析多个ID
        String[] idArray = ids.split(",");

        for (String id : idArray) {
            if (StringUtils.isEmpty(id)) {
                continue;
            }

            try {
                SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(Integer.parseInt(id.trim()));
                if (sciHorizontalApplyVertical == null) {
                    continue;
                }

                // 收集所有文件
                addFileToList(allFiles, sciHorizontalApplyVertical.getFile());
                addFileToList(allFiles, sciHorizontalApplyVertical.getOpenfile());
                addFileToList(allFiles, sciHorizontalApplyVertical.getMidfile());
                addFileToList(allFiles, sciHorizontalApplyVertical.getOverfile());

                // 构建压缩包名称
                if (zipNameBuilder.length() == 0) {
                    String user = sciHorizontalApplyVertical.getUserName();
                    String topName = sciHorizontalApplyVertical.getTopName();
                    if (StringUtils.isNotEmpty(user) && StringUtils.isNotEmpty(topName)) {
                        zipNameBuilder.append(user).append("-").append(topName);
                    }
                }
            } catch (NumberFormatException e) {
                log.error("Invalid ID format: {}", id);
            }
        }

        // 如果没有找到任何文件，返回错误
        if (allFiles.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No files found for download");
            return;
        }

        // 创建压缩包名称
        String zipFileName = (zipNameBuilder.length() > 0 ? zipNameBuilder.toString() : "download") + ".zip";
        // 处理文件名中的特殊字符
        zipFileName = zipFileName.replaceAll("[\\\\/:*?\"<>|]", "_");
        String zipFilePath = RuoYiConfig.getUploadPath() + "/temp_" + System.currentTimeMillis() + ".zip";

        // 确保上传目录存在
        File zipFile = new File(zipFilePath);
        if (!zipFile.getParentFile().exists()) {
            zipFile.getParentFile().mkdirs();
        }

        // 创建压缩包
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String filePath : allFiles) {
                if (StringUtils.isEmpty(filePath)) {
                    continue;
                }

                // 处理MinIO路径
                if (filePath.startsWith("/minio/")) {
                    // 从MinIO下载文件并添加到ZIP
                    addMinIOFileToZip(zos, filePath);
                } else {
                    // 处理本地文件路径
                    addLocalFileToZip(zos, filePath);
                }
            }
        } catch (IOException e) {
            log.error("File compression error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File compression error");
            return;
        }

        // 设置响应头，使浏览器下载文件
        response.setContentType("application/zip");
        String encodedFileName = URLEncoder.encode(zipFileName, StandardCharsets.UTF_8.toString());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);

        // 设置输入流和输出流
        try (FileInputStream inStream = new FileInputStream(zipFilePath);
             OutputStream outStream = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.flush();
        } catch (IOException e) {
            log.error("File download error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File download error");
        } finally {
            // 删除临时文件
            try {
                Files.deleteIfExists(Paths.get(zipFilePath));
            } catch (IOException e) {
                log.error("Failed to delete temp file: {}", zipFilePath, e);
            }
        }
    }

    /**
     * 添加文件到列表（去重）
     */
    private void addFileToList(List<String> list, String filePath) {
        if (StringUtils.isNotEmpty(filePath) && !list.contains(filePath)) {
            list.add(filePath);
        }
    }

    /**
     * 从MinIO添加文件到ZIP
     */
    private void addMinIOFileToZip(ZipOutputStream zos, String filePath) throws IOException {
        String[] parts = filePath.substring("/minio/".length()).split("/", 2);
        if (parts.length != 2) {
            log.error("Invalid MinIO path format: {}", filePath);
            return;
        }

        String objectName = parts[1];
        String entryName = getEntryName(filePath);

        try (InputStream inputStream = MinIOUtils.download(objectName)) {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                zos.write(buffer, 0, bytesRead);
            }
            zos.closeEntry();
        } catch (Exception e) {
            log.error("Failed to download from MinIO: {}", objectName, e);
        }
    }

    /**
     * 添加本地文件到ZIP
     */
    private void addLocalFileToZip(ZipOutputStream zos, String filePath) throws IOException {
        String fileName = getFileName(filePath);
        if (StringUtils.isEmpty(fileName)) {
            return;
        }

        String fullPath = RuoYiConfig.getUploadPath() + "/" + fileName;
        File file = new File(fullPath);

        if (!file.exists()) {
            log.error("File does not exist: {}", fullPath);
            return;
        }

        String entryName = getEntryName(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, bytesRead);
            }
            zos.closeEntry();
        }
    }

    /**
     * 获取ZIP条目名称
     */
    private String getEntryName(String filePath) {
        // 从路径中提取文件名
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

        // 移除序列号后缀（如 _4）
        if (fileName.contains(".")) {
            String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf("."));
            String extension = fileName.substring(fileName.lastIndexOf("."));

            int lastUnderscoreIndex = nameWithoutExt.lastIndexOf("_");
            if (lastUnderscoreIndex != -1) {
                String suffix = nameWithoutExt.substring(lastUnderscoreIndex + 1);
                if (suffix.matches("\\d+")) {
                    return nameWithoutExt.substring(0, lastUnderscoreIndex) + extension;
                }
            }
        }

        return fileName;
    }

    /**
     * 下载压缩包请求
     *
     * @param id 文件名称
     */
    @GetMapping("/HXdownloadZip")
    public void HXdownloadZip(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        List<String> list = new ArrayList<>();
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(Integer.parseInt(id));
        list.add(getFileName(sciHorizontalApply.getFiling()));
        list.add(getFileName(sciHorizontalApply.getContract()));
        list.add(getFileName(sciHorizontalApply.getFilingurl()));
        list.add(getFileName(sciHorizontalApply.getAgreeurl()));
        String user= sciHorizontalApply.getUserName();
        String Topname = sciHorizontalApply.getTopName();


        // 创建压缩包名称
        String zipFileName = user +'-'+ Topname + ".zip";
        String zipFilePath = RuoYiConfig.getUploadPath() + zipFileName;

        // 确保上传目录存在
        File zipFile = new File(zipFilePath);
        if (!zipFile.getParentFile().exists()) {
            zipFile.getParentFile().mkdirs();
        }

        // 创建压缩包
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String fileName : list) {
                if (fileName != null && !fileName.isEmpty()) {
                    String filePath = RuoYiConfig.getUploadPath() +'/'+ fileName;
                    File file = new File(filePath);

                    if (file.exists()) {
                        FileInputStream fis = new FileInputStream(file);
                        ZipEntry zipEntry = new ZipEntry(fileName.substring(fileName.lastIndexOf("/") + 1));
                        zos.putNextEntry(zipEntry);

                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = fis.read(bytes)) >= 0) {
                            zos.write(bytes, 0, length);
                        }
                        zos.closeEntry();
                        fis.close();
                    }else {
                        // 文件不存在，记录日志
                        Logger logger = LoggerFactory.getLogger(CommonController.class);
                        logger.error("File does not exist: " + filePath);
                    }
                } else {
                    // 文件名为空，记录日志
                    Logger logger = LoggerFactory.getLogger(CommonController.class);
                    logger.error("File name is empty or null: " + fileName);
                }
            }
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File compression error");
            return;
        }

        // 设置响应头，使浏览器下载文件
        response.setContentType("application/zip");
        String encodedFileName = URLEncoder.encode(zipFileName, StandardCharsets.UTF_8.toString());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
        response.getOutputStream().flush();


        // 设置输入流和输出流
        try (FileInputStream inStream = new FileInputStream(zipFilePath);
             OutputStream outStream = response.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.flush();
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File download error");
        }
    }

    public String getFileName(String url)
    {
        String fileName = "";
        String[] name = url.split("/");
        int index =-1;
        for (int i = 0; i < name.length; i++){
            if ("upload".equals(name[i])){
                index = i+1;
                break;
            }
        }
        if (index != -1){
            fileName = String.join("/", Arrays.copyOfRange(name, index, name.length));
        }
        return fileName;
    }

}
