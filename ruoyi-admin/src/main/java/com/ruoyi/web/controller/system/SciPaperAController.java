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
import com.ruoyi.system.domain.SysApprovalHistory;
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
    private List<Long> collage_role_ids = new ArrayList<>(Arrays.asList(103L, 104L, 105L, 106L, 107L, 108L, 116L, 117L, 118L, 119L));


    /**
     * 论文页面入口
     * 统一返回论文管理页面
     * @return 页面路径
     */
    @RequiresPermissions("system:paper:view")
    @Log(title = "论文页面", businessType = BusinessType.OTHER)
    @GetMapping()
    public String paper() {
        return prefix + "/paper";
    }

    /**
     * 上传文件
     * @param file 上传的文件
     * @param model 模型类型
     * @return 上传结果
     * @throws Exception 上传异常
     * @SQL 无直接SQL操作，调用FileUploadUtils处理文件上传
     */
    @Log(title = "论文文件上传", businessType = BusinessType.OTHER)
    @PostMapping("/upload/{model}")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file, @PathVariable("model") String model) throws Exception {
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
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
     * @param sciPaperA 论文实体，包含查询条件
     * @param year 年份
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 论文列表数据
     * @SQL 根据用户角色不同，执行不同的查询：
     * 1. 教师：执行selectSciPaperAListCx，支持课题名称查询
     * 2. 教研室：执行selectSciPaperAListCxList，支持第一作者、课题名称查询
     * 3. 学院：执行selectSciPaperAListXY，支持专业、第一作者、课题名称查询
     * 4. 科研处：执行selectSciPaperAListKY，支持学院、专业、第一作者、课题名称查询
     * 5. 管理员：执行selectSciPaperAList，支持所有条件查询
     */
    @RequiresPermissions("system:paper:list")
    @Log(title = "论文列表查询", businessType = BusinessType.OTHER)
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SciPaperA sciPaperA, String year, @RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = getUserId();
        sciPaperA.setUid(userId);
        sciPaperA.setYear(year);

        // 统一使用一个查询方法，通过@DataScope控制数据权限
        PageHelper.startPage(pageNum, pageSize);
        List<SciPaperA> list = sciPaperAService.selectSciPaperAListAll(sciPaperA);
        
        return getDataTable(list);
    }
    /**
     * 导出论文列表
     * @param ListRowId 论文ID列表
     * @param sciPaperA 论文实体，包含查询条件
     * @return 导出结果
     * @SQL 执行selectSciPaperAExport查询需要导出的论文数据
     */
    @RequiresPermissions("system:paper:export")
    @Log(title = "论文导出", businessType = BusinessType.EXPORT)
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
     * 新增论文页面
     * @param mmap 模型映射，用于传递数据到前端
     * @return 新增论文页面路径
     * @SQL 执行selectAllUserSchPro查询用户列表
     */
    @Log(title = "论文新增页面", businessType = BusinessType.OTHER)
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
     * @param sciPaperA 论文实体，包含论文信息
     * @return 保存结果
     * @SQL 1. 执行selectSciPaperA检查论文是否存在
     * 2. 执行insertSciPaperA插入论文数据
     * 3. 执行batchInsertPaperUserScore批量插入作者分数数据
     * 4. 执行insertSciPaperAr插入论文操作记录
     */
    @RequiresPermissions("system:paper:add")
    @Log(title = "论文新增", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave(SciPaperA sciPaperA) {
        try {
            if (sciPaperAService.selectSciPaperA(sciPaperA) != 0) {
                return error("该论文已存在");
            } else {
                Long userId = getUserId();
                String userIdStr = String.valueOf(userId);

                // 验证第一位本校老师必须是当前登录用户（按一作、二作、三作、四作顺序检查）
                String firstPersonId = sciPaperA.getFirstPersonId();
                String secondPersonId = sciPaperA.getSecondPersonId();
                String thirdPersonId = sciPaperA.getThirdPersonId();
                String fourthPersonId = sciPaperA.getFourthPersonId();

                String firstValidAuthor = null;
                if (firstPersonId != null && !firstPersonId.isEmpty() && !firstPersonId.equals("")) {
                    firstValidAuthor = firstPersonId;
                } else if (secondPersonId != null && !secondPersonId.isEmpty() && !secondPersonId.equals("")) {
                    firstValidAuthor = secondPersonId;
                } else if (thirdPersonId != null && !thirdPersonId.isEmpty() && !thirdPersonId.equals("")) {
                    firstValidAuthor = thirdPersonId;
                } else if (fourthPersonId != null && !fourthPersonId.isEmpty() && !fourthPersonId.equals("")) {
                    firstValidAuthor = fourthPersonId;
                }

                if (firstValidAuthor == null || !firstValidAuthor.equals(userIdStr)) {
                    return error("当前用户不是第一位本校老师");
                }

                sciPaperA.setUserId(userId);
                String user_name = userService.selectUserByLoginName(getLoginName()).getUserName();
                sciPaperA.setTeacherName(user_name);
                sciPaperA.setState(SciPaperA.PAPER_DRAFT);
//                if (!sciPaperA.getPaperCategory().equals("8") && !sciPaperA.getPaperCategory().equals("9")) {
//                    if (sciPaperA.getSearch_web() == null || sciPaperA.getSearch_web().length() <= 0){
//                        //throw new RuntimeException("论文网址不能为空");
//                    }
//                }
                if (!sciPaperA.getPaperCategory().equals("9")) {
                    if (sciPaperA.getText_paper() == null || sciPaperA.getText_paper().length() <= 0){
                        throw new RuntimeException("非校刊录用/检索证明不能为空");
                    }
                }

                // 计算预计科研分
                String paperCategory = sciPaperA.getPaperCategory();
                if (paperCategory != null && !paperCategory.isEmpty()) {
                    // 构建作者信息Map
                    Map<String, String> authors = new HashMap<>();
                    authors.put("1", sciPaperA.getFirstPersonId());
                    authors.put("2", sciPaperA.getSecondPersonId());
                    authors.put("3", sciPaperA.getThirdPersonId());
                    authors.put("4", sciPaperA.getFourthPersonId());
                    
                    // 获取通讯作者ID
                    String communicationAuthorId = sciPaperA.getCommunicationAuthorId();
                    
                    // 计算分数
                    Map<String, Integer> scores = sciPaperAService.calculatePaperScore(paperCategory, authors, communicationAuthorId);
                    
                    // 取预计科研分：如果有通讯作者，使用通讯作者的分数；否则使用第一作者的分数
                    int expectedScore = 0;
                    if (communicationAuthorId != null && !communicationAuthorId.isEmpty()) {
                        // 检查通讯作者是否在1-4作中
                        boolean isAuthor = false;
                        for (int i = 1; i <= 4; i++) {
                            String authorId = authors.get(String.valueOf(i));
                            if (communicationAuthorId.equals(authorId)) {
                                String key = i + "_" + communicationAuthorId;
                                expectedScore = scores.getOrDefault(key, 0);
                                isAuthor = true;
                                break;
                            }
                        }
                        // 如果通讯作者不在1-4作中，使用专门的通讯作者分数
                        if (!isAuthor) {
                            String commKey = "comm_" + communicationAuthorId;
                            expectedScore = scores.getOrDefault(commKey, 0);
                        }
                    } else {
                        // 没有通讯作者，使用第一作者的分数
                        String firstKey = "1_" + sciPaperA.getFirstPersonId();
                        expectedScore = scores.getOrDefault(firstKey, 0);
                    }
                    sciPaperA.setExpectedResearchScore(String.valueOf(expectedScore));
                }

                //插入论文数据
                int i1 = sciPaperAService.insertSciPaperA(sciPaperA);
                

                // 保存1-4作信息到Paper_user_score表
                int i = savePaperAuthorsToScoreTable(sciPaperA);
                if (i ==0) {
                    throw new RuntimeException("保存作者信息失败");
                }else if (i == -1) {
                    throw new RuntimeException("你不能添加自己不是作者的论文");
                }else if (i == -2) {
                    //throw new RuntimeException("作者数量错误");
                } else if (i==-3) {
                    throw new RuntimeException("此类论文的一作必须为自己");
                } else if (i==-4) {
                    throw new RuntimeException("未找到论文类型");
                } else if (i==-5){
                    throw new RuntimeException("作者重复");
                } else if (i==-6){
                    throw new RuntimeException("一作和通讯作者都是校外人员，只能录入本校论文");
                } else if (i==-7){
                    throw new RuntimeException("没有选择作者");
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
     * 保存1-4作信息到 Paper_user_score 表
     */
    private int savePaperAuthorsToScoreTable(SciPaperA sciPaperA) {
        // 如果作者没有当前登录人，返回失败
        if (!sciPaperA.getAuthorIds().contains(getUserId().toString())) {
            return -1;
        }
        // 这里只是限制了人数 ,没有详细限制是第几作者
        int key = (sciPaperA.getFirstPersonId()==null|| sciPaperA.getFirstPersonId().isEmpty() ?0:1 )+ (sciPaperA.getSecondPersonId()==null|| sciPaperA.getSecondPersonId().isEmpty()?0:1) + (sciPaperA.getThirdPersonId()==null|| sciPaperA.getThirdPersonId().isEmpty()?0:1) + (sciPaperA.getFourthPersonId()==null|| sciPaperA.getFourthPersonId().isEmpty()?0:1);
        
        // 如果四个作者都没有添加，返回错误
        if (key == 0) {
            return -7; // 表示没有选择作者
        }
        
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
            if (key<1){
                return -2;
            }
        }

        // 验证：如果一作和通讯都是校外，不让录入
        boolean isFirstAuthorExternal = sciPaperA.getFirstPersonId() != null && sciPaperA.getFirstPersonId().equals("-1");
        boolean isCorrespondingAuthorExternal = sciPaperA.getCommunicationAuthorId() != null && sciPaperA.getCommunicationAuthorId().equals("-1");
        if (isFirstAuthorExternal && isCorrespondingAuthorExternal) {
            return -6; // 返回错误码-6表示一作和通讯都是校外
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
     * 修改论文页面
     * @param id 论文ID
     * @param mmap 模型映射，用于传递数据到前端
     * @return 修改论文页面路径
     * @SQL 1. 执行selectAllUserSchPro查询用户列表
     * 2. 执行selectSciPaperAById查询论文详情
     * 3. 执行getPaperUserScoreListByPaperId查询作者信息
     */
    @RequiresPermissions("system:paper:edit")
    @Log(title = "论文编辑页面", businessType = BusinessType.OTHER)
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
        SysUser other = new SysUser();
        other.setUserId(-1L);
        other.setUserName("其他");
        userList.add(other);
        mmap.put("sysUsers", userList);
        mmap.put("sciPaperA", sciPaperA);
        return prefix + "/edit";
    }

    /**
     * 论文详情查看
     * @param id 论文ID
     * @param urlFlag URL标识
     * @param mmap 模型映射
     * @return 详情页面
     */
    @RequiresPermissions(value = {"system:paper:xypy", "system:paper:process", "system:paper:kypy", "system:paper:info"}, logical = Logical.OR)
    @Log(title = "论文详情查看", businessType = BusinessType.OTHER)
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Long id, @PathVariable("urlFlag") String urlFlag, ModelMap mmap) {
        SciPaperA sciPaperA = sciPaperAService.selectSciPaperAById(id);
        if (sciPaperA == null) {
            return prefix + "/paper";
        }
        sciPaperA.setUrlFlag(urlFlag);
        mmap.put("sciPaperA", sciPaperA);
        return prefix + "/detail";
    }
    
    /**
     * 论文详情查看（无URL标识）
     * @param id 论文ID
     * @param mmap 模型映射
     * @return 详情页面
     */
    @RequiresPermissions(value = {"system:paper:xypy", "system:paper:process", "system:paper:kypy", "system:paper:info"}, logical = Logical.OR)
    @Log(title = "论文详情查看", businessType = BusinessType.OTHER)
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap) {
        SciPaperA sciPaperA = sciPaperAService.selectSciPaperAById(id);
        if (sciPaperA == null) {
            return prefix + "/paper";
        }
        sciPaperA.setUrlFlag("");
        mmap.put("sciPaperA", sciPaperA);
        return prefix + "/detail";
    }

    /**
     * 修改保存论文
     * @param sciPaperA 论文实体，包含论文信息
     * @return 保存结果
     * @SQL 1. 执行updateSciPaperA更新论文数据
     * 2. 执行deletePaperUserScoreByPaperId删除旧作者信息
     * 3. 执行batchInsertPaperUserScore批量插入新作者分数数据
     */
    @RequiresPermissions("system:paper:edit")
    @Log(title = "论文修改", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult editSave(SciPaperA sciPaperA) {
        try {
            Long userId = getUserId();
            String userIdStr = String.valueOf(userId);

            // 验证第一位本校老师必须是当前登录用户（按一作、二作、三作、四作顺序检查）
            String firstPersonId = sciPaperA.getFirstPersonId();
            String secondPersonId = sciPaperA.getSecondPersonId();
            String thirdPersonId = sciPaperA.getThirdPersonId();
            String fourthPersonId = sciPaperA.getFourthPersonId();

            String firstValidAuthor = null;
            if (firstPersonId != null && !firstPersonId.isEmpty() && !firstPersonId.equals("")) {
                firstValidAuthor = firstPersonId;
            } else if (secondPersonId != null && !secondPersonId.isEmpty() && !secondPersonId.equals("")) {
                firstValidAuthor = secondPersonId;
            } else if (thirdPersonId != null && !thirdPersonId.isEmpty() && !thirdPersonId.equals("")) {
                firstValidAuthor = thirdPersonId;
            } else if (fourthPersonId != null && !fourthPersonId.isEmpty() && !fourthPersonId.equals("")) {
                firstValidAuthor = fourthPersonId;
            }

            if (firstValidAuthor == null || !firstValidAuthor.equals(userIdStr)) {
                return error("当前用户不是第一位本校老师");
            }
//            if (!sciPaperA.getPaperCategory().equals("9")) {
//                if (sciPaperA.getText_paper() == null || sciPaperA.getText_paper().length() <= 0){
//                    throw new RuntimeException("非校刊录用/检索证明不能为空");
//                }
//            }
            // 计算预计科研分
            String paperCategory = sciPaperA.getPaperCategory();
            if (paperCategory != null && !paperCategory.isEmpty()) {
                // 构建作者信息Map
                Map<String, String> authors = new HashMap<>();
                authors.put("1", sciPaperA.getFirstPersonId());
                authors.put("2", sciPaperA.getSecondPersonId());
                authors.put("3", sciPaperA.getThirdPersonId());
                authors.put("4", sciPaperA.getFourthPersonId());
                
                // 获取通讯作者ID
                String communicationAuthorId = sciPaperA.getCommunicationAuthorId();
                
                // 计算分数
                Map<String, Integer> scores = sciPaperAService.calculatePaperScore(paperCategory, authors, communicationAuthorId);
                
                // 取第一作者的分数作为预计科研分
                String firstKey = "1_" + sciPaperA.getFirstPersonId();
                int expectedScore = scores.getOrDefault(firstKey, 0);
                sciPaperA.setExpectedResearchScore(String.valueOf(expectedScore));
            }
            
            // 确保state字段不为null，即使没有修改状态
            if (sciPaperA.getState() == null) {
                // 如果state为null，先从数据库获取当前状态
                SciPaperA existingPaper = sciPaperAService.selectSciPaperAById(sciPaperA.getId());
                if (existingPaper != null) {
                    sciPaperA.setState(existingPaper.getState());
                }
            }
            
            // 如果论文状态是驳回，编辑保存后改为草稿状态，需要重新提交
            if (SciPaperA.PAPER_REJECTED.equals(sciPaperA.getState())) {
                sciPaperA.setState(SciPaperA.PAPER_DRAFT);
            }
            
            // 更新论文基本信息
            int result = sciPaperAService.updateSciPaperA(sciPaperA);
            
            if (result > 0) {
                // 删除旧作者信息
                paperUserScoreService.deletePaperUserScoreByPaperId(sciPaperA.getId());
                
                // 保存新的作者信息
                int authorResult = savePaperAuthorsToScoreTable(sciPaperA);
                if (authorResult == 0) {
                    throw new RuntimeException("保存作者信息失败");
                } else if (authorResult == -1) {
                    throw new RuntimeException("你不能添加自己不是作者的论文");
                } else if (authorResult == -6) {
                    throw new RuntimeException("一作和通讯作者都是校外人员，只能录入本校论文");
                }
            }
            
            return toAjax(result);
        } catch (RuntimeException e) {
            // 直接抛出，Runtime异常会自动触发回滚
            throw e;
        } catch (Exception e) {
            // 捕获检查型异常并转换为Runtime异常
            throw new RuntimeException("论文保存过程中发生错误: " + e.getMessage(), e);
        }
    }

    /**
     * 删除论文
     * @param ids 论文ID列表，逗号分隔
     * @return 删除结果
     * @SQL 执行deleteSciPaperAByIds批量删除论文数据
     */
    @RequiresPermissions("system:paper:remove")
    @Log(title = "论文删除", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sciPaperAService.deleteSciPaperAByIds(ids));
    }

    /**
     * 论文审核通过
     * @param id 论文ID
     * @param comment 审批意见
     * @param paperCategory 论文类别
     * @return 审核结果
     */
    @RequiresPermissions(value = {"system:paper:xypy", "system:paper:process", "system:paper:kypy"}, logical = Logical.OR)
    @Log(title = "论文审核通过", businessType = BusinessType.UPDATE)
    @PostMapping("/pytg/{id}")
    @ResponseBody
    public AjaxResult pytg(@PathVariable("id") String id, String comment, String paperCategory) {
//        String order = paperCategory;
        return toAjax(sciPaperAService.pytg(id, getUserId(), comment, paperCategory));
    }

    /**
     * 论文审核驳回或撤回
     * @param id 论文ID
     * @param remark 驳回或撤回原因
     * @param operationType 操作类型：reject(驳回) 或 recall(撤回)
     * @return 操作结果
     */
    @RequiresPermissions(value = {"system:paper:xypy", "system:paper:process", "system:paper:kypy", "system:paper:xyrevoke", "system:paper:kyrevoke"}, logical = Logical.OR)
    @Log(title = "论文审核驳回", businessType = BusinessType.UPDATE)
    @PostMapping("/pybh/{id}")
    @ResponseBody
    public AjaxResult pybh(@PathVariable("id") String id, String remark, String operationType) {
        return toAjax(sciPaperAService.pybh(id, getUserId(), remark, operationType));
    }

    /**
     * 论文审批操作（通过/驳回/撤回）
     * @param id 论文ID
     * @param comment 审批意见
     * @param operationType 操作类型：approve(通过)、reject(驳回)、recall(撤回)
     * @param paperCategory 论文类别（仅通过时需要）
     * @return 审批结果
     */
    @RequiresPermissions(value = {"system:paper:xypy", "system:paper:process", "system:paper:kypy", "system:paper:xyrevoke", "system:paper:kyrevoke"}, logical = Logical.OR)
    @Log(title = "论文审批操作", businessType = BusinessType.UPDATE)
    @PostMapping("/approve/{id}")
    @ResponseBody
    public AjaxResult approve(@PathVariable("id") String id, String comment, String operationType, String paperCategory) {
        return toAjax(sciPaperAService.approve(id, getUserId(), comment, operationType, paperCategory));
    }


    /**
     * 查看驳回信息
     * @param arid 论文审核记录ID
     * @return 审核记录列表
     * @SQL 执行selectSciPaperArList查询论文审核记录
     */
    @RequiresPermissions("system:apply:edit")
    @Log(title = "论文驳回信息查看", businessType = BusinessType.OTHER)
    @PostMapping("/bhxs/{kid}")
    @ResponseBody
    public TableDataInfo bhxs(@PathVariable("kid") String arid) {
        SciPaperAr sciPaperAr = new SciPaperAr();
        sciPaperAr.setAr_id(Integer.valueOf(arid));
        List<SciPaperAr> list = sciPaperAService.selectSciPaperArList(sciPaperAr);
        return getDataTable(list);
    }

    /**
     * 提交论文草稿
     * @param id 论文ID
     * @return 提交结果
     * @SQL 1. 执行insertSciPaperAr插入草稿提交记录
     * 2. 执行updateSciPaperAState更新论文状态为待审核
     */
    @Log(title = "论文草稿提交", businessType = BusinessType.UPDATE)
    @PostMapping("/tj/{id}")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
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
     * 查询论文名称（模糊查询）
     * @param query 查询关键词
     * @return 论文名称列表
     * @SQL 执行selectAllPaperName模糊查询论文名称
     */
    @Log(title = "论文名称查询", businessType = BusinessType.OTHER)
    @PostMapping("/queryName/{query}")
    @ResponseBody
    public List<SciPaperA> selectAllPaperName(@PathVariable("query") String query) {
        List<SciPaperA> list = sciPaperAService.selectAllPaperName(query);
        return list;
    }

    /**
     * 实时计算论文科研分
     * @param params 计算参数，包含paperCategory（论文类别）、authors（作者信息）、communicationAuthorId（通讯作者ID）
     * @return 科研分计算结果，key为作者排名+用户ID，value为分数
     * @SQL 执行selectSciPaperACfgPointList查询论文类别对应的分数配置
     */
    @Log(title = "论文科研分计算", businessType = BusinessType.OTHER)
    @PostMapping("/calculateScore")
    @ResponseBody
    public AjaxResult calculateScore(@RequestBody Map<String, Object> params) {
        try {
            String paperCategory = (String) params.get("paperCategory");
            Map<String, String> authors = (Map<String, String>) params.get("authors");
            String communicationAuthorId = (String) params.get("communicationAuthorId");
            
            // 调用服务层计算科研分
            Map<String, Integer> scores = sciPaperAService.calculatePaperScore(paperCategory, authors, communicationAuthorId);
            
            return AjaxResult.success(scores);
        } catch (Exception e) {
            return AjaxResult.error("计算科研分失败：" + e.getMessage());
        }
    }
}
