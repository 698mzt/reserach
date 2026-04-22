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
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalApplyVertical;
import com.ruoyi.system.domain.SciIntraSchoolPro;
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.system.domain.SciJiaocairuanzhu;
import com.ruoyi.system.domain.SciZhuanliruanzhu;
import com.ruoyi.system.domain.SysReward;
import com.ruoyi.system.domain.SciLectureReport;
import com.ruoyi.system.service.ISciHorizontalApplyService;
import com.ruoyi.system.service.ISciHorizontalApplyVerticalService;
import com.ruoyi.system.service.ISciIntraSchProApplyService;
import com.ruoyi.system.service.ISciPaperAService;
import com.ruoyi.system.service.ISciJiaocairuanzhuService;
import com.ruoyi.system.service.ISciZhuanliruanzhuService;
import com.ruoyi.system.service.ISysRewardService;
import com.ruoyi.system.service.ISciLectureReportService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
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

    // 正确的Service依赖注入
    @Autowired
    private ISciIntraSchProApplyService sciIntraSchProApplyService;

    @Autowired
    private ISciPaperAService sciPaperAService;

    @Autowired
    private ISciJiaocairuanzhuService sciJiaocairuanzhuService;

    @Autowired
    private ISciZhuanliruanzhuService sciZhuanliruanzhuService;

    @Autowired
    private ISysRewardService sysRewardService;

    @Autowired
    private ISciLectureReportService sciLectureReportService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDeptService deptService;


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
     * 下载压缩包请求
     *
     * @param id 文件名称
     */
    @GetMapping("/downloadZip")
    public void doGet(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(Integer.parseInt(id));
        String user= sciHorizontalApplyVertical.getUserName();
        String Topname = sciHorizontalApplyVertical.getTopName();
        String folderName = buildArchiveFolder(
                sciHorizontalApplyVertical.getYname(),
                sciHorizontalApplyVertical.getDname(),
                sciHorizontalApplyVertical.getUserName(),
                "纵向课题",
                sciHorizontalApplyVertical.getTopName(),
                toLong(sciHorizontalApplyVertical.getUserId())
        );


        // 创建压缩包名称
        String zipFileName = user +'-'+ Topname + ".zip";
        String zipFilePath = RuoYiConfig.getUploadPath() + zipFileName;

        // 创建压缩包
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            addFileToZip(zos, folderName, sciHorizontalApplyVertical.getFile());
            addFileToZip(zos, folderName, sciHorizontalApplyVertical.getOpenfile());
            addFileToZip(zos, folderName, sciHorizontalApplyVertical.getMidfile());
            addFileToZip(zos, folderName, sciHorizontalApplyVertical.getOverfile());
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

    /**
     * 下载压缩包请求
     *
     * @param id 文件名称
     */
    @GetMapping("/HXdownloadZip")
    public void HXdownloadZip(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(Integer.parseInt(id));
        String user= sciHorizontalApply.getUserName();
        String Topname = sciHorizontalApply.getTopName();
        String folderName = buildArchiveFolder(
                sciHorizontalApply.getYname(),
                sciHorizontalApply.getDname(),
                sciHorizontalApply.getUserName(),
                "横向课题",
                sciHorizontalApply.getTopName(),
                toLong(sciHorizontalApply.getUserId())
        );


        // 创建压缩包名称
        String zipFileName = user +'-'+ Topname + ".zip";
        String zipFilePath = RuoYiConfig.getUploadPath() + zipFileName;

        // 创建压缩包
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            addFileToZip(zos, folderName, sciHorizontalApply.getFiling());
            addFileToZip(zos, folderName, sciHorizontalApply.getContract());
            addFileToZip(zos, folderName, sciHorizontalApply.getFilingurl());
            addFileToZip(zos, folderName, sciHorizontalApply.getAgreeurl());
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
        if (StringUtils.isEmpty(url)) {
            return "";
        }

        // 处理 MinIO 路径
        if (url.startsWith("/minio/")) {
            // 直接返回 /minio/ 之后的路径，用于后续处理
            return url;
        }

        // 原有的本地文件处理逻辑
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

    /**
     * 批量下载请求
     *
     * @param ids 数据ID列表
     * @param module 模块类型
     */
    @GetMapping("/batchDownload")
    public void batchDownload(@RequestParam("ids") String ids, @RequestParam("module") String module, HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        try {
            log.info("开始批量下载请求，模块: {}, IDs: {}", module, ids);
            log.info("上传路径配置: {}", RuoYiConfig.getUploadPath());

            if (StringUtils.isEmpty(ids)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "未选择数据");
                return;
            }

            List<String> idList = Arrays.asList(ids.split(","));
            if (idList.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "未选择数据");
                return;
            }

            // 创建临时压缩包文件
            String zipFileName = getModuleName(module) + "_批量下载.zip";
            // 确保文件名合法
            zipFileName = zipFileName.replaceAll("[^a-zA-Z0-9\u4e00-\u9fa5_.-]", "_");

            // 获取上传路径并确保目录存在
            String uploadPath = RuoYiConfig.getUploadPath();
            // 确保路径末尾有正确的分隔符
            if (!uploadPath.endsWith("/") && !uploadPath.endsWith("\\")) {
                uploadPath = uploadPath + "/";
            }
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String zipFilePath = uploadPath + zipFileName;
            log.info("创建压缩包文件路径: {}", zipFilePath);

            // 创建压缩包
            try (FileOutputStream fos = new FileOutputStream(zipFilePath);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                // 处理不同模块的数据
                String normalizedModule = module.toLowerCase();
                if ("vertical".equals(normalizedModule) || "纵向课题".equals(module)) {
                    handleVerticalBatchDownload(idList, zos);
                } else if ("horizontal".equals(normalizedModule) || "横向课题".equals(module)) {
                    handleHorizontalBatchDownload(idList, zos);
                } else if ("achievement".equals(normalizedModule) || "intrasch".equals(normalizedModule) || "成果转化".equals(module)) {
                    handleIntraSchProBatchDownload(idList, zos);
                } else if ("paper".equals(normalizedModule) || "论文".equals(module)) {
                    handlePaperBatchDownload(idList, zos);
                } else if ("textbook".equals(normalizedModule) || "教材软著".equals(module)) {
                    handleJiaocairuanzhuBatchDownload(idList, zos);
                } else if ("patent".equals(normalizedModule) || "专利软著".equals(module)) {
                    handleZhuanliruanzhuBatchDownload(idList, zos);
                } else if ("reward".equals(normalizedModule) || "奖励".equals(module)) {
                    handleRewardBatchDownload(idList, zos);
                } else if ("report".equals(normalizedModule) || "讲座报告".equals(module)) {
                    handleReportBatchDownload(idList, zos);
                }
            } catch (IOException e) {
                log.error("创建压缩包失败", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "创建压缩包失败");
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
                log.error("下载压缩包失败", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "下载压缩包失败");
            } finally {
                // 删除临时压缩包
                try {
                    File zipFile = new File(zipFilePath);
                    if (zipFile.exists()) {
                        boolean deleted = zipFile.delete();
                        if (!deleted) {
                            log.warn("无法删除临时压缩包文件: {}", zipFilePath);
                            // 如果立即删除失败，尝试延时删除
                            zipFile.deleteOnExit();
                        } else {
                            log.info("成功删除临时压缩包文件: {}", zipFilePath);
                        }
                    }
                } catch (Exception e) {
                    log.error("删除临时压缩包文件时发生错误: {}", zipFilePath, e);
                }
            }
        } catch (Exception e) {
            log.error("批量下载失败", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "批量下载失败");
        }
    }

    /**
     * 处理纵向课题批量下载
     */
    private void handleVerticalBatchDownload(List<String> idList, ZipOutputStream zos) throws IOException {
        for (String id : idList) {
            try {
                SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(Integer.parseInt(id));
                if (sciHorizontalApplyVertical != null) {
                    String folderName = buildArchiveFolder(
                            sciHorizontalApplyVertical.getYname(),
                            sciHorizontalApplyVertical.getDname(),
                            sciHorizontalApplyVertical.getUserName(),
                            "纵向课题",
                            sciHorizontalApplyVertical.getTopName(),
                            toLong(sciHorizontalApplyVertical.getUserId())
                    );
    
                    // 添加文件到压缩包
                    addFileToZip(zos, folderName, sciHorizontalApplyVertical.getFile());
                    addFileToZip(zos, folderName, sciHorizontalApplyVertical.getOpenfile());
                    addFileToZip(zos, folderName, sciHorizontalApplyVertical.getMidfile());
                    addFileToZip(zos, folderName, sciHorizontalApplyVertical.getOverfile());
                }
            } catch (Exception e) {
                log.error("处理纵向课题 ID: {} 失败", id, e);
            }
        }
    }

    /**
     * 处理横向课题批量下载
     */
    private void handleHorizontalBatchDownload(List<String> idList, ZipOutputStream zos) throws IOException {
        for (String id : idList) {
            try {
                SciHorizontalApply sciHorizontalApply = sciHorizontalApplyService.selectSciHorizontalApplyById(Integer.parseInt(id));
                if (sciHorizontalApply != null) {
                    String folderName = buildArchiveFolder(
                            sciHorizontalApply.getYname(),
                            sciHorizontalApply.getDname(),
                            sciHorizontalApply.getUserName(),
                            "横向课题",
                            sciHorizontalApply.getTopName(),
                            toLong(sciHorizontalApply.getUserId())
                    );
    
                    // 添加文件到压缩包
                    addFileToZip(zos, folderName, sciHorizontalApply.getFiling());
                    addFileToZip(zos, folderName, sciHorizontalApply.getContract());
                    addFileToZip(zos, folderName, sciHorizontalApply.getFilingurl());
                    addFileToZip(zos, folderName, sciHorizontalApply.getAgreeurl());
                    // 添加到账金额佐证材料
                    addFileToZip(zos, folderName, sciHorizontalApply.getReamountUrl());
                }
            } catch (Exception e) {
                log.error("处理横向课题 ID: {} 失败", id, e);
            }
        }
    }

    /**
     * 添加文件到压缩包
     */
    private void addFileToZip(ZipOutputStream zos, String folderName, String fileUrl) throws IOException {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }

        try {
            // 处理 MinIO 路径
            if (fileUrl.startsWith("/minio/")) {
                // 从 MinIO 路径中提取实际的对象名称
                // 路径格式: /minio/{bucket}/{objectPath}
                // 需要去掉 /minio/ 前缀和 bucket 名称
                String[] parts = fileUrl.substring("/minio/".length()).split("/", 2);
                if (parts.length == 2) {
                    String objectName = parts[1];
                    String entryName = buildZipEntryName(folderName, objectName);

                    ZipEntry zipEntry = new ZipEntry(entryName);
                    zos.putNextEntry(zipEntry);

                    // 从 MinIO 下载文件
                    try (InputStream inputStream = MinIOUtils.download(objectName)) {
                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = inputStream.read(bytes)) >= 0) {
                            zos.write(bytes, 0, length);
                        }
                        log.info("成功从MinIO下载文件: {}, object: {}", fileUrl, objectName);
                    } catch (Exception e) {
                        log.error("从MinIO下载文件失败: {}, object: {}", fileUrl, objectName, e);
                        // 即使某个文件下载失败，也要关闭entry，继续处理其他文件
                        zos.closeEntry();
                        return;
                    }
                    zos.closeEntry();
                    log.info("成功添加 MinIO 文件到压缩包：{}, 条目名：{}", fileUrl, entryName);
                    return;
                } else {
                    log.error("MinIO文件路径格式不正确: {}", fileUrl);
                    return;
                }
            }

            // 处理本地文件路径
            String fileName = getFileName(fileUrl);
            if (StringUtils.isEmpty(fileName)) {
                return;
            }

            String entryName = buildZipEntryName(folderName, fileName);
            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);

            String filePath = RuoYiConfig.getUploadPath() + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                }
                log.info("成功添加本地文件到压缩包: {}", filePath);
            } else {
                log.warn("本地文件不存在: {}", filePath);
            }
            zos.closeEntry();
        } catch (Exception e) {
            log.error("添加文件到压缩包失败: {}", fileUrl, e);
        }
    }

    /**
     * 从MinIO路径中获取对象名称
     * 路径格式: /minio/{bucket}/{objectPath}
     * 返回: {objectPath}
     */
    /**
     * 处理成果转化批量下载代码
     */
    private void handleIntraSchProBatchDownload(List<String> idList, ZipOutputStream zos) throws IOException {
        int successCount = 0;
        int totalCount = idList.size();
    
        for (String id : idList) {
            try {
                SciIntraSchoolPro sciIntraSchoolPro = sciIntraSchProApplyService.sel_IntraSchPro_by_id(Integer.parseInt(id));
                if (sciIntraSchoolPro != null) {
                    String folderName = buildArchiveFolder(
                            sciIntraSchoolPro.getYname(),
                            sciIntraSchoolPro.getDname(),
                            sciIntraSchoolPro.getUserName(),
                            "成果转化",
                            sciIntraSchoolPro.getTopName(),
                            toLong(sciIntraSchoolPro.getUserId())
                    );
    
                    // 添加文件到压缩包
                    addFileToZip(zos, folderName, sciIntraSchoolPro.getContract());
                    addFileToZip(zos, folderName, sciIntraSchoolPro.getFiling());
                    addFileToZip(zos, folderName, sciIntraSchoolPro.getOverContract());
                    addFileToZip(zos, folderName, sciIntraSchoolPro.getOverFiling());
                    successCount++;
                }
            } catch (Exception e) {
                log.error("处理成果转化 ID: {} 失败", id, e);
            }
        }
    
        log.info("成果转化批量下载完成：成功处理 {}/{} 条记录", successCount, totalCount);
    }

    /**
     * 处理论文批量下载
     */
    private void handlePaperBatchDownload(List<String> idList, ZipOutputStream zos) throws IOException {
        for (String id : idList) {
            try {
                SciPaperA sciPaperA = sciPaperAService.selectSciPaperAById(Long.parseLong(id));
                if (sciPaperA != null) {
                    String folderName = buildArchiveFolder(
                            sciPaperA.getCollege(),
                            sciPaperA.getResearchRoom(),
                            firstNonBlank(sciPaperA.getUserName(), sciPaperA.getTeacherName()),
                            "论文",
                            sciPaperA.getPaperTitle(),
                            toLong(sciPaperA.getUserId())
                    );
    
                    // 添加文件到压缩包
                    addFileToZip(zos, folderName, sciPaperA.getText_paper());
                    addFileToZip(zos, folderName, sciPaperA.getWord_paper());
                }
            } catch (Exception e) {
                log.error("处理论文 ID: {} 失败", id, e);
            }
        }
    }

    /**
     * 处理教材软著批量下载
     */
    private void handleJiaocairuanzhuBatchDownload(List<String> idList, ZipOutputStream zos) throws IOException {
        for (String id : idList) {
            try {
                SciJiaocairuanzhu sciJiaocairuanzhu = sciJiaocairuanzhuService.selectSciJiaocairuanzhuById(Integer.parseInt(id));
                if (sciJiaocairuanzhu != null) {
                    String folderName = buildArchiveFolder(
                            sciJiaocairuanzhu.getYname(),
                            sciJiaocairuanzhu.getDname(),
                            sciJiaocairuanzhu.getUserName(),
                            "教材软著",
                            sciJiaocairuanzhu.getMingcheng(),
                            toLong(sciJiaocairuanzhu.getUserId())
                    );
    
                    // 添加文件到压缩包
                    addFileToZip(zos, folderName, sciJiaocairuanzhu.getWenjian());
                }
            } catch (Exception e) {
                log.error("处理教材软著 ID: {} 失败", id, e);
            }
        }
    }

    /**
     * 处理专利软著批量下载
     */
    private void handleZhuanliruanzhuBatchDownload(List<String> idList, ZipOutputStream zos) throws IOException {
        int successCount = 0;
        int totalCount = idList.size();
    
        for (String id : idList) {
            try {
                SciZhuanliruanzhu sciZhuanliruanzhu = sciZhuanliruanzhuService.selectSciZhuanliruanzhuById(Integer.parseInt(id));
                if (sciZhuanliruanzhu != null) {
                    String folderName = buildArchiveFolder(
                            sciZhuanliruanzhu.getYname(),
                            sciZhuanliruanzhu.getDname(),
                            sciZhuanliruanzhu.getUserName(),
                            "专利软著",
                            sciZhuanliruanzhu.getMingcheng(),
                            toLong(sciZhuanliruanzhu.getUserId())
                    );
    
                    // 添加文件到压缩包
                    addFileToZip(zos, folderName, sciZhuanliruanzhu.getWenjian());
                    successCount++;
                    log.info("成功处理专利软著 ID: {}, 文件路径：{}", id, sciZhuanliruanzhu.getWenjian());
                }
            } catch (Exception e) {
                log.error("处理专利软著 ID: {} 失败", id, e);
            }
        }
    
        log.info("专利软著批量下载完成：成功处理 {}/{} 条记录", successCount, totalCount);
    }

    /**
     * 处理奖励批量下载
     */
    private void handleRewardBatchDownload(List<String> idList, ZipOutputStream zos) throws IOException {
        for (String id : idList) {
            try {
                SysReward sysReward = sysRewardService.selectSysRewardById(Long.parseLong(id));
                if (sysReward != null) {
                    String folderName = buildArchiveFolder(
                            sysReward.getYname(),
                            sysReward.getDname(),
                            sysReward.getUserName(),
                            "奖励",
                            sysReward.getRewardName(),
                            toLong(sysReward.getUserId())
                    );
    
                    // 添加文件到压缩包
                    addFileToZip(zos, folderName, sysReward.getRewardWenjian());
                }
            } catch (Exception e) {
                log.error("处理奖励 ID: {} 失败", id, e);
            }
        }
    }

    /**
     * 处理讲座报告批量下载
     */
    private void handleReportBatchDownload(List<String> idList, ZipOutputStream zos) throws IOException {
        for (String id : idList) {
            try {
                SciLectureReport sciLectureReport = sciLectureReportService.selectSciLectureReportById(Integer.parseInt(id));
                if (sciLectureReport != null) {
                    String folderName = buildArchiveFolder(
                            sciLectureReport.getXueyuan(),
                            sciLectureReport.getKeyanshi(),
                            sciLectureReport.getTeacherName(),
                            "讲座报告",
                            sciLectureReport.getReportTheme(),
                            toLong(sciLectureReport.getUserId())
                    );
    
                    // 添加文件到压缩包
                    addFileToZip(zos, folderName, sciLectureReport.getReportUrl());
                }
            } catch (Exception e) {
                log.error("处理讲座报告 ID: {} 失败", id, e);
            }
        }
    }

    /**
     * 获取模块名称
     */
    private String getModuleName(String module) {
        String normalizedModule = module.toLowerCase();
        if ("vertical".equals(normalizedModule) || "纵向课题".equals(module)) {
            return "纵向课题";
        } else if ("horizontal".equals(normalizedModule) || "横向课题".equals(module)) {
            return "横向课题";
        } else if ("achievement".equals(normalizedModule) || "intrasch".equals(normalizedModule) || "成果转化".equals(module)) {
            return "成果转化";
        } else if ("paper".equals(normalizedModule) || "论文".equals(module)) {
            return "论文";
        } else if ("textbook".equals(normalizedModule) || "教材软著".equals(module)) {
            return "教材软著";
        } else if ("patent".equals(normalizedModule) || "zhuanliruanzhu".equals(normalizedModule) || "专利软著".equals(module)) {
            return "专利软著";
        } else if ("reward".equals(normalizedModule) || "奖励".equals(module)) {
            return "奖励";
        } else if ("report".equals(normalizedModule) || "讲座报告".equals(module)) {
            return "讲座报告";
        }
        return "批量下载";
    }

    private String buildArchiveFolder(String college, String major, String teacher, String module, String topic, Long userId)
    {
        DeptInfo deptInfo = resolveDeptInfo(userId, college, major);
        return joinZipPath(
                sanitizeZipSegment(deptInfo.college, "未知学院"),
                sanitizeZipSegment(deptInfo.major, "未知专业"),
                sanitizeZipSegment(teacher, "未知教师"),
                sanitizeZipSegment(module, "未分类模块"),
                sanitizeZipSegment(topic, "未命名课题")
        );
    }

    private DeptInfo resolveDeptInfo(Long userId, String college, String major)
    {
        String resolvedCollege = firstNonBlank(college);
        String resolvedMajor = firstNonBlank(major);
        if (StringUtils.isNotEmpty(resolvedCollege) && StringUtils.isNotEmpty(resolvedMajor))
        {
            return new DeptInfo(resolvedCollege, resolvedMajor);
        }

        if (userId == null)
        {
            return new DeptInfo(resolvedCollege, resolvedMajor);
        }

        try
        {
            SysUser user = userService.selectUserById(userId);
            if (user == null || user.getDeptId() == null)
            {
                return new DeptInfo(resolvedCollege, resolvedMajor);
            }

            SysDept majorDept = deptService.selectDeptById(user.getDeptId());
            if (majorDept != null && StringUtils.isEmpty(resolvedMajor))
            {
                resolvedMajor = majorDept.getDeptName();
            }

            if (majorDept != null && majorDept.getParentId() != null && majorDept.getParentId() > 0 && StringUtils.isEmpty(resolvedCollege))
            {
                SysDept collegeDept = deptService.selectDeptById(majorDept.getParentId());
                if (collegeDept != null)
                {
                    resolvedCollege = collegeDept.getDeptName();
                }
            }
        }
        catch (Exception e)
        {
            log.warn("解析下载目录的学院/专业信息失败, userId={}", userId, e);
        }

        return new DeptInfo(resolvedCollege, resolvedMajor);
    }

    private String buildZipEntryName(String folderName, String sourcePath)
    {
        String fileName = resolveDownloadFileName(sourcePath);
        return joinZipPath(folderName, fileName);
    }

    private String resolveDownloadFileName(String sourcePath)
    {
        if (StringUtils.isEmpty(sourcePath))
        {
            return sanitizeZipSegment("", "未命名文件");
        }

        String normalized = sourcePath.replace("\\", "/");
        String fileName = normalized.substring(normalized.lastIndexOf("/") + 1);
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex <= 0)
        {
            return sanitizeZipSegment(fileName, "未命名文件");
        }

        String nameWithoutExt = fileName.substring(0, dotIndex);
        String extension = fileName.substring(dotIndex);
        int lastUnderscoreIndex = nameWithoutExt.lastIndexOf("_");
        if (lastUnderscoreIndex != -1)
        {
            String suffix = nameWithoutExt.substring(lastUnderscoreIndex + 1);
            if (suffix.matches("\\d+"))
            {
                nameWithoutExt = nameWithoutExt.substring(0, lastUnderscoreIndex);
            }
        }
        return sanitizeZipSegment(nameWithoutExt + extension, "未命名文件");
    }

    private String joinZipPath(String... segments)
    {
        List<String> cleanedSegments = new ArrayList<>();
        for (String segment : segments)
        {
            if (StringUtils.isNotEmpty(segment))
            {
                cleanedSegments.add(segment);
            }
        }
        return String.join("/", cleanedSegments);
    }

    private String sanitizeZipSegment(String value, String fallback)
    {
        String candidate = firstNonBlank(value, fallback);
        candidate = candidate.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
        return candidate.isEmpty() ? fallback : candidate;
    }

    private String firstNonBlank(String... values)
    {
        if (values == null)
        {
            return "";
        }
        for (String value : values)
        {
            if (StringUtils.isNotEmpty(value) && !value.trim().isEmpty())
            {
                return value.trim();
            }
        }
        return "";
    }

    private Long toLong(Number value)
    {
        return value == null ? null : value.longValue();
    }

    private static final class DeptInfo
    {
        private final String college;
        private final String major;

        private DeptInfo(String college, String major)
        {
            this.college = college;
            this.major = major;
        }
    }

}
