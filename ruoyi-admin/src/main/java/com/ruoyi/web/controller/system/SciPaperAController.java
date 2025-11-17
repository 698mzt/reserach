package com.ruoyi.web.controller.system;

import java.util.*;


import com.github.pagehelper.PageHelper;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.system.mapper.SciPaperAMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciPaperAr;
import com.ruoyi.system.domain.Paper_user_score;
import com.ruoyi.system.service.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 论文Controller
 *
 * @author ruoyi
 * @date 2024-11-07
 */
@Controller
@RequestMapping("/system/paper")
public class SciPaperAController extends BaseController {
    private String prefix = "system/paper";

    @Resource
    private ISciPaperAService sciPaperAService;

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private ISysUserService userService;

    @Resource
    private SciPaperAMapper sciPaperAMapper;

    @Resource
    private IPaperUserScoreService paperUserScoreService;


    @RequiresPermissions("system:paper:view")
    @GetMapping()
    public String paper() {
        Long userId = getUserId();
        System.out.println("userId = " + userId);
        List<String> roleId = sciPaperAService.selectSciPaperAByroleId(userId);
        //教研室+普通老师身份

        if (roleId.contains("102") && roleId.contains("100")&&!roleId.contains("101")) {
            System.out.println("教研室+普通老师身份");
            return prefix + "/paper_jy";
        }
        //单独教研室身份
        else if (roleId.contains("102")&& !roleId.contains("100")&&!roleId.contains("101")) {
            System.out.println("单独教研室身份");

            return prefix + "/paper";
        }
        //科研处+普通老师身份
        else if (roleId.contains("101") && roleId.contains("100")) {
            System.out.println("科研处+普通老师身份");

            return prefix + "/paper_ky";
        }
        //学院+普通老师身份
        else if ((roleId.contains("103") || roleId.contains("104") || roleId.contains("105") || roleId.contains("106") || roleId.contains("107") || roleId.contains("108")) && roleId.contains("100")) {
            System.out.println("学院+普通老师身份");
            return prefix + "/paper_xy";
        } else if (roleId.contains("100") && roleId.size() == 1) {
            System.out.println("普通老师身份");
            return prefix + "/paper_pt";
        } else {
            return prefix + "/paper";
            //return "error/404";
        }

    }

    @PostMapping("/upload/{model}")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file, @PathVariable("model") String model) throws Exception {
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
//            String fileName = FileUploadUtils.upload(filePath, file);
            String fileName = FileUploadUtils.newupload(filePath, file, model);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询论文列表
     */
    @RequiresPermissions("system:paper:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciPaperA sciPaperA, String year ,@RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = getUserId();
        System.out.println("userId = " + userId);
        List<String> roleId = sciPaperAService.selectSciPaperAByroleId(userId);
        System.out.println("roleId = " + roleId);
        sciPaperA.setUid(userId);
        sciPaperA.setYear(year);

        List<SciPaperA> list = new ArrayList<>();
// 所有的审核方都会有普通老师身份，单身份的判断暂时先留着，除非增加审核方的时候少给他加上普通老师身份，否则不会报错
        //教研室+普通老师身份
        // or((pa1.user_id = #{uid} or  pa2.user_id = #{uid}) or pa3.user_id = #{uid}  or pa4.user_id = #{uid} or pac.user_id = #{uid})
        if (roleId.contains("102") && roleId.contains("100")&& !roleId.contains("101")) {
            PageHelper.startPage(pageNum, pageSize);
            list = sciPaperAService.selectSciPaperAListCxList(sciPaperA);
        }
        //单独教研室身份 暂时不用写
        else if (roleId.contains("102")&& !roleId.contains("100")&& !roleId.contains("101")) {
            PageHelper.startPage(pageNum, pageSize);
            list = sciPaperAService.selectSciPaperAList(sciPaperA);
        }
        //科研处+普通老师身份
        else if (roleId.contains("101") && roleId.contains("100")) {
            System.out.println("roleId = " + roleId);
            PageHelper.startPage(pageNum, pageSize);
            list = sciPaperAService.selectSciPaperAListKY(sciPaperA);
        }
        //学院+普通老师身份
        else if ((roleId.contains("103") || roleId.contains("104") || roleId.contains("105") || roleId.contains("106") || roleId.contains("107") || roleId.contains("108")) && roleId.contains("100")) {
            //学院身份
            SysUser user = getSysUser();
            sciPaperA.setCollegeId(String.valueOf(user.getParentId()));
            PageHelper.startPage(pageNum, pageSize);
            list = sciPaperAService.selectSciPaperAListXY(sciPaperA);
            System.out.println("list = " + list);
        } else if (roleId.contains("100") && roleId.size() == 1) {
            System.out.println("单个老师进入方法");
            //System.out.println(" sciPaperA=" + sciPaperAService.selectSciPaperAListCx(sciPaperA));
            PageHelper.startPage(pageNum, pageSize);
            list = sciPaperAService.selectSciPaperAListCx(sciPaperA);
        } else if (userId == 1L) {
            //admin进入
            PageHelper.startPage(pageNum, pageSize);
            list = sciPaperAService.selectSciPaperAList(sciPaperA);
        }

        System.out.println("year = " + year);
        
        return getDataTable(list);
    }
    /**
     * 导出论文列表
     */
    @RequiresPermissions("system:paper:export")
    @Log(title = "论文", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(@RequestParam List<String> ListRowId, SciPaperA sciPaperA) {
        try {
            List<SciPaperA> list = sciPaperAService.selectSciPaperAExport(ListRowId, sciPaperA);
            ExcelUtil<SciPaperA> util = new ExcelUtil<SciPaperA>(SciPaperA.class);
            return util.exportExcel(list, "论文数据");
        } catch (Exception e) {
            return error(e.getMessage());
        }

    }

    /**
     * 新增论文
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        List<SysUser> userList = userService.selectAllUserSchPro(getUserId());
        for (int a = 0; a < userList.size(); a++) {
            if (userList.get(a).getUserId() == getUserId()) {
                SysUser user = userList.get(a);
                user.setFlag(true);
                userList.set(a, user);
                break;
            }
        }
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        userList.add(other);
        mmap.put("sysUsers", userList);
        return prefix + "/add";
    }

    /**
     * 新增保存论文
     * 论文信息和积分在一张表里面
     */
    @RequiresPermissions("system:paper:add")
    @Log(title = "论文", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave(SciPaperA sciPaperA) {
        try {
            if (sciPaperAService.selectSciPaperA(sciPaperA) != 0) {
                return error("该论文已存在");
            } else {
                Long userId = getUserId();
                sciPaperA.setUserId(userId);
                String user_name = userService.selectUserByLoginName(getLoginName()).getUserName();
                sciPaperA.setTeacherName(user_name);
                sciPaperA.setState("99");
//                if (!sciPaperA.getPaperCategory().equals("8") && !sciPaperA.getPaperCategory().equals("9")) {
//                    if (sciPaperA.getSearch_web() == null || sciPaperA.getSearch_web().length() <= 0){
//                        //throw new RuntimeException("论文网址不能为空");
//                    }
//                }
                //插入论文数据
                sciPaperAService.insertSciPaperA(sciPaperA);

                // 保存1-4作信息到Paper_user_score表
                int i = savePaperAuthorsToScoreTable(sciPaperA);
                if (i ==0) {
                    throw new RuntimeException("保存作者信息失败");
                }else if (i == -1) {
                    throw new RuntimeException("你不能添加自己不是作者的论文");
                }else if (i == -2) {
                    throw new RuntimeException("作者数量错误");
                } else if (i==-3) {
                    throw new RuntimeException("此类论文的一作必须为自己");
                } else if (i==-4) {
                    throw new RuntimeException("未找到论文类型");
                }else if (i==-5){
                    throw new RuntimeException("作者重复");
                }

                SciPaperAr sciPaperAr = new SciPaperAr();
                sciPaperAr.setUid(getUserId());
                sciPaperAr.setAr_id(Math.toIntExact(sciPaperA.getId()));
                sciPaperAr.setConcate("提交草稿");
                sciPaperAr.setState("提交草稿");

                //增加批阅记录
                return toAjax(sciPaperAMapper.insertSciPaperAr(sciPaperAr));
            }

        }  catch (RuntimeException e) {
            // 直接抛出，Runtime异常会自动触发回滚
            throw e;
        } catch (Exception e) {
            // 捕获检查型异常并转换为Runtime异常
            throw new RuntimeException("论文保存过程中发生错误: " + e.getMessage(), e);
        }
    }

    /**
     * 保存1-4作信息到Paper_user_score表
     */
    private int savePaperAuthorsToScoreTable(SciPaperA sciPaperA) {
        // 如果作者没有当前登录人，返回失败
        if (!sciPaperA.getAuthorIds().contains(getUserId().toString())) {
            return -1;
        }
        // 这里只是限制了人数 ,没有详细限制是第几作者
        int key = (sciPaperA.getFirstPersonId()==null|| sciPaperA.getFirstPersonId().isEmpty() ?0:1 )+ (sciPaperA.getSecondPersonId()==null|| sciPaperA.getSecondPersonId().isEmpty()?0:1) + (sciPaperA.getThirdPersonId()==null|| sciPaperA.getThirdPersonId().isEmpty()?0:1) + (sciPaperA.getFourthPersonId()==null|| sciPaperA.getFourthPersonId().isEmpty()?0:1);
        Set<String> countAuthors = new HashSet<>();
        if (sciPaperA.getFirstPersonId() != null && !sciPaperA.getFirstPersonId().isEmpty()) {
            countAuthors.add(sciPaperA.getFirstPersonId());
        }
        if (sciPaperA.getSecondPersonId() != null && !sciPaperA.getSecondPersonId().isEmpty()) {
            countAuthors.add(sciPaperA.getSecondPersonId());
        }
        if (sciPaperA.getThirdPersonId() != null && !sciPaperA.getThirdPersonId().isEmpty()) {
            countAuthors.add(sciPaperA.getThirdPersonId());
        }
        if (sciPaperA.getFourthPersonId() != null && !sciPaperA.getFourthPersonId().isEmpty()) {
            countAuthors.add(sciPaperA.getFourthPersonId());
        }
        // 获取论文类型
        if (sciPaperA.getPaperCategory()!=null){
            if (sciPaperA.getPaperCategory().equals("11") || sciPaperA.getPaperCategory().equals("12") || sciPaperA.getPaperCategory().equals("10")){
//                if (key!=2){
//                    return -2;
//                }
            } else if (sciPaperA.getPaperCategory().equals("8") || sciPaperA.getPaperCategory().equals("9")) {
                // 普通和校办论文 只有一个人
                if (key!=1){
                    return -2;
                }
                // 如果普通和校办论文的这个人不是自己
                //if (!sciPaperA.getCommunicationAuthorId().equals(String.valueOf(getUserId())) || !sciPaperA.getFirstPersonId().equals(String.valueOf(getUserId()))){
                if (!sciPaperA.getFirstPersonId().equals(String.valueOf(getUserId()))){
                    return -3 ;
                }
            }
        }else{
            return -4;
        }
        if (countAuthors.size()!=key){
            return -5;
        }
        //  判断独立作者类型论文人数
        if (sciPaperA.getIsIndependentauthor().equals("1")){
             if (key!=1){
                 return -2;
             }
        } else if (sciPaperA.getIsIndependentauthor().equals("0")) {
            if (key<=1){
                return -2;
            }
        }


        List<Paper_user_score> paperUserScoreList = new ArrayList<>();
        int res = 0;
        // 获取当前时间
        Date now = new Date();
        String currentUser = getLoginName();
        
        // 处理一作
        if (sciPaperA.getFirstPersonId() != null && !sciPaperA.getFirstPersonId().isEmpty()) {
            Paper_user_score firstAuthor = new Paper_user_score();
            firstAuthor.setPaperId(sciPaperA.getId());
            firstAuthor.setUserId(Long.valueOf(sciPaperA.getFirstPersonId()));
            firstAuthor.setAuthorOrder("1");
            firstAuthor.setAuthorLevel("1");
            firstAuthor.setScore("0"); // 初始分数为0，后续根据规则计算
            firstAuthor.setCreateBy(currentUser);
            firstAuthor.setCreateTime(now);
            firstAuthor.setUpdateBy(currentUser);
            firstAuthor.setUpdateTime(now);
            paperUserScoreList.add(firstAuthor);
            res+=1;
        }
        
        // 处理二作
        if (sciPaperA.getSecondPersonId() != null && !sciPaperA.getSecondPersonId().isEmpty()) {
            Paper_user_score secondAuthor = new Paper_user_score();
            secondAuthor.setPaperId(sciPaperA.getId());
            secondAuthor.setUserId(Long.valueOf(sciPaperA.getSecondPersonId()));
            secondAuthor.setAuthorOrder("2");
            secondAuthor.setAuthorLevel("2");
            secondAuthor.setScore("0");
            secondAuthor.setCreateBy(currentUser);
            secondAuthor.setCreateTime(now);
            secondAuthor.setUpdateBy(currentUser);
            secondAuthor.setUpdateTime(now);
            paperUserScoreList.add(secondAuthor);
            res+=1;
        }
        
        // 处理三作
        if (sciPaperA.getThirdPersonId() != null && !sciPaperA.getThirdPersonId().isEmpty()) {
            Paper_user_score thirdAuthor = new Paper_user_score();
            thirdAuthor.setPaperId(sciPaperA.getId());
            thirdAuthor.setUserId(Long.valueOf(sciPaperA.getThirdPersonId()));
            thirdAuthor.setAuthorOrder("3");
            thirdAuthor.setAuthorLevel("3");
            thirdAuthor.setScore("0");
            thirdAuthor.setCreateBy(currentUser);
            thirdAuthor.setCreateTime(now);
            thirdAuthor.setUpdateBy(currentUser);
            thirdAuthor.setUpdateTime(now);
            paperUserScoreList.add(thirdAuthor);
            res+=1;
        }
        
        // 处理四作
        if (sciPaperA.getFourthPersonId() != null && !sciPaperA.getFourthPersonId().isEmpty()) {
            Paper_user_score fourthAuthor = new Paper_user_score();
            fourthAuthor.setPaperId(sciPaperA.getId());
            fourthAuthor.setUserId(Long.valueOf(sciPaperA.getFourthPersonId()));
            fourthAuthor.setAuthorOrder("4");
            fourthAuthor.setAuthorLevel("4");
            fourthAuthor.setScore("0");
            fourthAuthor.setCreateBy(currentUser);
            fourthAuthor.setCreateTime(now);
            fourthAuthor.setUpdateBy(currentUser);
            fourthAuthor.setUpdateTime(now);
            paperUserScoreList.add(fourthAuthor);
            res+=1;
        }
        
        // 处理通讯作者
        if (sciPaperA.getCommunicationAuthorId() != null && !sciPaperA.getCommunicationAuthorId().isEmpty()) {
            // 检查通讯作者是否已经在1-4作中
            boolean isAlreadyInList = paperUserScoreList.stream()
                .anyMatch(author -> author.getUserId().equals(Long.valueOf(sciPaperA.getCommunicationAuthorId())));
            
            if (!isAlreadyInList) {
                // 通讯作者不在1-4作中，添加新的通讯作者记录
//                Paper_user_score correspondingAuthor = new Paper_user_score();
//                correspondingAuthor.setPaperId(sciPaperA.getId());
//                correspondingAuthor.setUserId(Long.valueOf(sciPaperA.getCommunicationAuthorId()));
//                correspondingAuthor.setAuthorOrder("通讯作者");
//                correspondingAuthor.setAuthorLevel("通讯作者");
//                correspondingAuthor.setScore("0");
//                correspondingAuthor.setCreateBy(currentUser);
//                correspondingAuthor.setCreateTime(now);
//                correspondingAuthor.setUpdateBy(currentUser);
//                correspondingAuthor.setUpdateTime(now);
//                paperUserScoreList.add(correspondingAuthor);
                return 0;
            } else {
                // 通讯作者在1-4作中，更新对应的authorLevel为"0"
                paperUserScoreList.stream()
                    .filter(author -> author.getUserId().equals(Long.valueOf(sciPaperA.getCommunicationAuthorId())))
                    .findFirst()
                    .ifPresent(author -> author.setAuthorLevel("0"));
                res+=1;
            }
        }
        
        // 批量插入到Paper_user_score表
        if (!paperUserScoreList.isEmpty()) {
            paperUserScoreService.batchInsertPaperUserScore(paperUserScoreList);
        }
        return res;
    }

    /**
     * 修改论文
     */
    @RequiresPermissions("system:paper:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        List<SysUser> userList = userService.selectAllUserSchPro(getUserId());
        for (int a = 0; a < userList.size(); a++) {
            if (userList.get(a).getUserId() == getUserId()) {
                SysUser user = userList.get(a);
                user.setFlag(true);
                userList.set(a, user);
                break;
            }
        }
        SciPaperA sciPaperA = sciPaperAService.selectSciPaperAById(id);
        
        // 获取作者信息
        List<Paper_user_score> authorList = paperUserScoreService.getPaperUserScoreListByPaperId(id);
        if (authorList != null && !authorList.isEmpty()) {
            for (Paper_user_score author : authorList) {
                // 根据author_order设置1-4作
                String authorOrder = author.getAuthorOrder();
                if ("1".equals(authorOrder)) {
                    sciPaperA.setFirstPersonId(String.valueOf(author.getUserId()));
                } else if ("2".equals(authorOrder)) {
                    sciPaperA.setSecondPersonId(String.valueOf(author.getUserId()));
                } else if ("3".equals(authorOrder)) {
                    sciPaperA.setThirdPersonId(String.valueOf(author.getUserId()));
                } else if ("4".equals(authorOrder)) {
                    sciPaperA.setFourthPersonId(String.valueOf(author.getUserId()));
                }
                
                // 根据author_level判断是否是通讯作者（author_level='0'表示通讯作者）
                if ("0".equals(author.getAuthorLevel())) {
                    sciPaperA.setCommunicationAuthorId(String.valueOf(author.getUserId()));
                }
            }
        }
        
        mmap.put("sysUsers", userList);
        mmap.put("sciPaperA", sciPaperA);
        return prefix + "/edit";
    }

    /**
     * 批阅
     */
    @RequiresPermissions(value = {"system:paper:xypy", "system:paper:process", "system:paper:kypy", "system:paper:info"}, logical = Logical.OR)
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Long id, @PathVariable("urlFlag") String urlFlag, ModelMap mmap) {
        System.out.println("urlFlag = " + urlFlag);
        SciPaperA sciPaperA = sciPaperAService.selectSciPaperAById(id);
        if (sciPaperA == null) {
            return prefix + "/paper";
        }
        sciPaperA.setUrlFlag(urlFlag);
        mmap.put("sciPaperA", sciPaperA);
        return prefix + "/detail";
    }

    /**
     * 修改保存论文 编辑
     */
    @RequiresPermissions("system:paper:edit")
    @Log(title = "论文", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional
    public AjaxResult editSave(SciPaperA sciPaperA) {
        try {
            // 更新论文基本信息
            int result = sciPaperAService.updateSciPaperA(sciPaperA);
            
            if (result > 0) {
                // 删除旧作者信息
                paperUserScoreService.deletePaperUserScoreByPaperId(sciPaperA.getId());
                
                // 保存新的作者信息
                int authorResult = savePaperAuthorsToScoreTable(sciPaperA);
                if (authorResult == 0) {
                    return error("保存作者信息失败");
                } else if (authorResult == -1) {
                    return error("你不能添加自己不是作者的论文");
                }
            }
            
            return toAjax(result);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 删除论文
     */
    @RequiresPermissions("system:paper:remove")
    @Log(title = "论文", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sciPaperAService.deleteSciPaperAByIds(ids));
    }

    /**
     * 论文通过
     */
    @RequiresPermissions(value = {"system:paper:xypy", "system:paper:process", "system:paper:kypy"}, logical = Logical.OR)
    @Log(title = "论文审核通过", businessType = BusinessType.UPDATE)
    @PostMapping("/pytg/{id}")
    @ResponseBody
    public AjaxResult pytg(@PathVariable("id") String id, String urlFlag, String paperCategory, String paperRanking) {
        String order = paperCategory;
        //System.out.println("paperCategory = " + paperCategory);
        String user_order = paperRanking;
        //System.out.println("paperRanking = " + paperRanking);
        return toAjax(sciPaperAService.pytg(id, getUserId(), urlFlag, order, user_order));
    }

    /**
     * 驳回+撤回
     * @param id
     * @param remark
     * @param urlFlag
     * @return
     */
    @RequiresPermissions(value = {"system:paper:xypy", "system:paper:process", "system:paper:kypy", "system:paper:xyrevoke", "system:paper:kyrevoke"}, logical = Logical.OR)
    @Log(title = "论文审核驳回", businessType = BusinessType.UPDATE)
    @PostMapping("/pybh/{id}")
    @ResponseBody
    public AjaxResult pybh(@PathVariable("id") String id, String remark, String urlFlag) {
        return toAjax(sciPaperAService.pybh(id, getUserId(), remark, urlFlag));
    }


    @RequiresPermissions("system:apply:edit")
    @PostMapping("/bhxs/{kid}")//驳回显示
    @ResponseBody
    public TableDataInfo bhxs(@PathVariable("kid") String arid) {
        SciPaperAr sciPaperAr = new SciPaperAr();
        sciPaperAr.setAr_id(Integer.valueOf(arid));
        List<SciPaperAr> list = sciPaperAService.selectSciPaperArList(sciPaperAr);
        System.out.println("list = " + list);
        return getDataTable(list);
    }

    /**
     * 提交后将state状态设置为1
     */
    @PostMapping("/tj/{id}")
    @ResponseBody
    public AjaxResult tj(@PathVariable("id") Integer id) {
        SciPaperAr sciPaperAr = new SciPaperAr();
        sciPaperAr.setAr_id(id);
        sciPaperAr.setUid(getUserId());
        sciPaperAr.setConcate("草稿提交");
        sciPaperAr.setState("草稿提交");
        sciPaperAMapper.insertSciPaperAr(sciPaperAr);
        return toAjax(sciPaperAService.updateSciPaperAState(id));
    }

    /**
     * 查询所有的论文名称并需要进行模糊查询
     */
    @PostMapping("/queryName/{query}")
    @ResponseBody
    public List<SciPaperA> selectAllPaperName(@PathVariable("query") String query) {
        List<SciPaperA> list = sciPaperAService.selectAllPaperName(query);
        System.out.println("list = " + list);
        return list;
    }
}
