package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SciHorizontalApply;
import com.ruoyi.system.domain.SciHorizontalPiyue;
import com.ruoyi.system.domain.SciPaperAr;
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

/**
 * 论文Controller
 * 
 * @author ruoyi
 * @date 2024-11-07
 */
@Controller
@RequestMapping("/system/paper")
public class SciPaperAController extends BaseController
{
    private String prefix = "system/paper";

    @Autowired
    private ISciPaperAService sciPaperAService;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("system:paper:view")
    @GetMapping()
    public String paper() {
        return prefix + "/paper";
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
    public TableDataInfo list(SciPaperA sciPaperA,String year)
    {

        Long userId = getUserId();
        System.out.println("userId = " + userId);
        List<String> roleId = sciPaperAService.selectSciPaperAByroleId(userId);
        System.out.println("roleId = " + roleId);
        sciPaperA.setUid(userId);
        sciPaperA.setYear(year);

        List<SciPaperA> list= new ArrayList<>();

            //教研室与普通老师
            if (roleId.contains("102") && roleId.contains("100")){
                list.addAll(sciPaperAService.selectSciPaperAListCxList(sciPaperA));
            }
            //教研室
            else  if (roleId.contains("102")) {
                list.addAll(sciPaperAService.selectSciPaperAList(sciPaperA));
            }
            //科研处
            else if (roleId.contains("101")) {
                System.out.println("roleId = " + roleId);
                list.addAll(sciPaperAService.selectSciPaperAListKY(sciPaperA));
            }
            //学院
            else  if (roleId.contains("103") || roleId.contains("104") || roleId.contains("105") || roleId.contains("106") || roleId.contains("107") || roleId.contains("108")) {
                list.addAll(sciPaperAService.selectSciPaperAListXY(sciPaperA));
                System.out.println("list = " + list);
            }
            else if (roleId.contains("100") && roleId.size()==1) {
                System.out.println("单个老师进入方法");
                System.out.println(" sciPaperA=" + sciPaperA);
                list.addAll(sciPaperAService.selectSciPaperAListCx(sciPaperA));
            }


        System.out.println("year = " + year);
        List<Map<String,Object>> data =new ArrayList<>();
        startPage();

        return getDataTable(list);
    }

    /**
     * 导出论文列表
     */
    @RequiresPermissions("system:paper:export")
    @Log(title = "论文", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(@RequestParam List<String> ListRowId, SciPaperA sciPaperA)
    {
        logger.info("导出论文数据 {}", ListRowId);
//        List<SciPaperA> list = sciPaperAService.selectSciPaperAList(sciPaperA);
        List<SciPaperA> list =sciPaperAService.selectSciPaperAExport(ListRowId,sciPaperA);
        ExcelUtil<SciPaperA> util = new ExcelUtil<SciPaperA>(SciPaperA.class);
        return util.exportExcel(list, "论文数据");
    }

    /**
     * 新增论文
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        List<SysUser> userList =  userService.selectAllUser();
        SysUser sysUser=null;
        Long user_id = getUserId();
        for (int a = 0; a<userList.size();a++) {
            if(userList.get(a).getUserId().equals(user_id)){
                sysUser=userList.get(a);
                break;
            }
        }
        mmap.put("user",sysUser);
        if (sysUser != null) {
            mmap.addAttribute("user",sysUser.getUserId());
        }
        return prefix + "/add";
    }

    /**
     * 新增保存论文
     */
    @RequiresPermissions("system:paper:add")
    @Log(title = "论文", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SciPaperA sciPaperA)
    {
        try {
            if ( sciPaperAService.selectSciPaperA(sciPaperA)!=0){
                return error("该论文已存在");
            }else {
                Long userId = getUserId();
                sciPaperA.setUserId(userId);
                String user_name = userService.selectUserByLoginName(getLoginName()).getUserName();
                sciPaperA.setTeacherName(user_name);
                sciPaperA.setState("99");
                return toAjax(sciPaperAService.insertSciPaperA(sciPaperA));
            }

        }catch (Exception e){
            return error(e.getMessage());
        }
    }

    /**
     * 修改论文
     */
    @RequiresPermissions("system:paper:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysUser currentUser = ShiroUtils.getSysUser();
        SciPaperA sciPaperA = sciPaperAService.selectSciPaperAById(id);
        mmap.put("sysUsers",currentUser);
        mmap.put("sciPaperA", sciPaperA);
        return prefix + "/edit";
    }
    /**
     * 批阅
     * */
    @RequiresPermissions(value={"system:paper:xypy","system:paper:process","system:paper:kypy","system:paper:info"},logical= Logical.OR)
    @GetMapping("/detail/{id}/{urlFlag}")
    public String detail(@PathVariable("id") Long id,@PathVariable("urlFlag") String urlFlag, ModelMap mmap)
    {
        SciPaperA sciPaperA = sciPaperAService.selectSciPaperAById(id);
        if (sciPaperA == null) {
            return prefix+ "/paper";
        }
        sciPaperA.setUrlFlag(urlFlag);
        mmap.put("sciPaperA", sciPaperA);
        return prefix + "/detail";
    }

    /**
     * 修改保存论文
     */
    @RequiresPermissions("system:paper:edit")
    @Log(title = "论文", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SciPaperA sciPaperA)
    {
        sciPaperA.setState("1");
        return toAjax(sciPaperAService.updateSciPaperA(sciPaperA));
    }

    /**
     * 删除论文
     */
    @RequiresPermissions("system:paper:remove")
    @Log(title = "论文", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sciPaperAService.deleteSciPaperAByIds(ids));
    }

    /**
     * 论文通过
     */
    @RequiresPermissions(value={"system:paper:xypy","system:paper:process","system:paper:kypy"},logical= Logical.OR)
    @Log(title = "论文审核通过", businessType = BusinessType.UPDATE)
    @PostMapping("/pytg/{id}")
    @ResponseBody
    public AjaxResult pytg(@PathVariable("id") String id,String urlFlag,String paperCategory,String paperRanking) {
        String order = paperCategory;
        System.out.println("paperCategory = " + paperCategory);
        String user_order = paperRanking;
        System.out.println("paperRanking = " + paperRanking);
        return toAjax(sciPaperAService.pytg(id, getUserId(), urlFlag,order,user_order));
    }
    @RequiresPermissions(value={"system:paper:xypy","system:paper:process","system:paper:kypy","system:paper:xyrevoke","system:paper:kyrevoke"},logical= Logical.OR)
    @Log(title = "论文审核驳回", businessType = BusinessType.UPDATE)
    @PostMapping( "/pybh/{id}")
    @ResponseBody
    public AjaxResult pybh(@PathVariable("id")String id,String remark,String urlFlag)
    {
        System.out.println("remark = " + remark);
        return toAjax(sciPaperAService.pybh(id,getUserId(),remark,urlFlag));
    }


    @RequiresPermissions("system:apply:edit")
    @PostMapping("/bhxs/{kid}")//驳回显示
    @ResponseBody
    public TableDataInfo bhxs(@PathVariable("kid")Integer arid)
    {
        SciPaperAr sciPaperAr = new SciPaperAr();
        sciPaperAr.setAr_id(arid);
        List<SciPaperAr> list = sciPaperAService.selectSciPaperArList(sciPaperAr);
        System.out.println("list = " + list);
        return getDataTable(list);
    }

    /**
     * 提交后将state状态设置为1
     */
    @PostMapping("/tj/{id}")
    @ResponseBody
    public AjaxResult tj(@PathVariable("id")Integer id)
    {
        return toAjax(sciPaperAService.updateSciPaperAState(id));

    }
    /**
     *查询所有的论文名称并需要进行模糊查询
     */
    @PostMapping("/queryName/{query}")
    @ResponseBody
    public List<SciPaperA> selectAllPaperName(@PathVariable("query")String query)
    {
        List<SciPaperA> list = sciPaperAService.selectAllPaperName(query);
        System.out.println("list = " + list);
        return list;
    }
}
