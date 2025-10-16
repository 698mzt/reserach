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
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
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
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath() ;
            // 上传并返回新文件名称
//            String fileName = FileUploadUtils.upload(filePath, file);
            String fileName = FileUploadUtils.newupload(filePath, file,model);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", fileName);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
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
                String url = serverConfig.getUrl() + fileName;
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
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 下载压缩包请求
     *
     * @param id 文件名称
     */
    @GetMapping("/downloadZip")
    public void doGet(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        List<String> list = new ArrayList<>();
        SciHorizontalApplyVertical sciHorizontalApplyVertical = sciHorizontalApplyVerticalService.selectSciHorizontalApplyVerticalById(Integer.parseInt(id));
        list.add(getFileName(sciHorizontalApplyVertical.getFile()));
        list.add(getFileName(sciHorizontalApplyVertical.getOpenfile()));
        list.add(getFileName(sciHorizontalApplyVertical.getMidfile()));
        list.add(getFileName(sciHorizontalApplyVertical.getOverfile()));
        String user= sciHorizontalApplyVertical.getUserName();
        String Topname = sciHorizontalApplyVertical.getTopName();


        // 创建压缩包名称
        String zipFileName = user +'-'+ Topname + ".zip";
        String zipFilePath = RuoYiConfig.getUploadPath() + zipFileName;

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
