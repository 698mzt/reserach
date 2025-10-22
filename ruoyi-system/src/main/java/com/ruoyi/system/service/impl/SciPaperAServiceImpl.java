package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SciPaperAr;
import com.ruoyi.system.mapper.SciPaperACfgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SciPaperAMapper;
import com.ruoyi.system.domain.SciPaperA;
import com.ruoyi.system.service.ISciPaperAService;
import com.ruoyi.common.core.text.Convert;

/**
 * 论文Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-07
 */
@Service
public class SciPaperAServiceImpl implements ISciPaperAService {
  @Autowired
  private SciPaperAMapper sciPaperAMapper;
  @Autowired
  private SciPaperACfgMapper sciPaperACfgMapper;

  /**
   * 查询论文
   *
   * @param id 论文主键
   * @return 论文
   */
  @Override
  @DataScope(deptAlias = "d", userAlias = "u")
  public SciPaperA selectSciPaperAById(Long id) {
    return sciPaperAMapper.selectSciPaperAById(id);
  }

  /**
   * 查询论文列表
   *
   * @param sciPaperA 论文
   * @return 论文
   */
  @Override
  @DataScope(deptAlias = "d", userAlias = "u")
  public List<SciPaperA> selectSciPaperAList(SciPaperA sciPaperA) {
    return sciPaperAMapper.selectSciPaperAList(sciPaperA);
  }

  @Override
  @DataScope(deptAlias = "d", userAlias = "u")
  public List<SciPaperA> selectSciPaperAExport(List<String> ListRowId, SciPaperA sciPaperA) {
    return sciPaperAMapper.selectSciPaperAExport(ListRowId, sciPaperA);
  }

  @Override
  @DataScope(deptAlias = "d", userAlias = "u")
  public List<SciPaperA> selectSciPaperAListKY(SciPaperA sciPaperA) {
    return sciPaperAMapper.selectSciPaperAListKY(sciPaperA);
  }

  @Override
  @DataScope(deptAlias = "d", userAlias = "u")
  public List<SciPaperA> selectSciPaperAListXY(SciPaperA sciPaperA) {
    return sciPaperAMapper.selectSciPaperAListXY(sciPaperA);
  }

  /**
   * 新增论文
   *
   * @param sciPaperA 论文
   * @return 结果
   */
  @Override
  public int insertSciPaperA(SciPaperA sciPaperA) {

    return sciPaperAMapper.insertSciPaperA(sciPaperA);
  }


  /**
   * 修改论文
   *
   * @param sciPaperA 论文
   * @return 结果
   */
  @Override
  public int updateSciPaperA(SciPaperA sciPaperA) {
    return sciPaperAMapper.updateSciPaperA(sciPaperA);
  }

  /**
   * 批量删除论文
   *
   * @param ids 需要删除的论文主键
   * @return 结果
   */
  @Override
  public int deleteSciPaperAByIds(String ids) {
    return sciPaperAMapper.deleteSciPaperAByIds(Convert.toStrArray(ids));
  }

  /**
   * 删除论文信息
   *
   * @param id 论文主键
   * @return 结果
   */
  @Override
  public int deleteSciPaperAById(Long id) {
    return sciPaperAMapper.deleteSciPaperAById(id);
  }

  @Override
  public int updateSciPaperAState(Integer id) {
    int a = sciPaperAMapper.updateSciPaperAState(id);
    return a;
  }

  @Override
  public List<SciPaperA> selectSciPaperAListCxList(SciPaperA sciPaperA) {
    return sciPaperAMapper.selectSciPaperAListCxList(sciPaperA);
  }

  @Override
  public List<SciPaperA> selectSciPaperArole(Long userId) {
    return sciPaperAMapper.selectSciPaperArole(userId);
  }

  /*通过uderId查*/
  @Override
  //  @DataScope(deptAlias = "d",userAlias = "u")
  public List<SciPaperA> selectSciPaperAListCx(SciPaperA sciPaperA) {
    return sciPaperAMapper.selectSciPaperAListCx(sciPaperA);
  }

  @Override
  // @DataScope(deptAlias = "d",userAlias = "u")
  public List<String> selectSciPaperAByroleId(Long userId) {
    //数据权限
    List<String> a = sciPaperAMapper.selectSciPaperAByroleId(userId);
    return a;
  }

  @Override
  public int pytg(String id, Long uid, String urlFlag, String order, String user_order) {
    String state = "0";
    SciPaperAr sciPaperAr = new SciPaperAr();
    if (urlFlag.equals("pro")) {
      state = "2"; //教研室通过
      sciPaperAr.setConcate("教研室通过");
    } else if (urlFlag.equals("xytg")) {
      state = "4"; //学院通过
      sciPaperAr.setConcate("学院通过");
    } else if (urlFlag.equals("kytg")) {
      state = "8"; //科研处通过
      sciPaperAr.setConcate("科研处通过");
      int points = sciPaperACfgMapper.selectSciPaperACfgPoints(order, user_order);
      System.out.println("points = " + points);
      int b = sciPaperAMapper.updateSciPaperArs(id, points);
      System.out.println("b = " + b);
    }
    int a = sciPaperAMapper.pytg(id, state);

    sciPaperAr.setUid(uid);
    sciPaperAr.setAr_id(Integer.valueOf(id));
    sciPaperAr.setState("通过");
    sciPaperAMapper.insertSciPaperAr(sciPaperAr);
    return a;
  }

  @Override
  public int pybh(String id, Long userId, String remark, String urlFlag) {
    String state = "0";
    SciPaperAr sciPaperAr = new SciPaperAr();
    System.out.println("urlFlag = " + urlFlag);
    if (urlFlag.equals("xytg") || urlFlag.equals("xyth")) {
      state = "5";
      sciPaperAr.setState("学院驳回");
    } else if (urlFlag.equals("pro") || urlFlag.equals("proth")) {
      state = "3";
      sciPaperAr.setState("教研室驳回");
    } else if (urlFlag.equals("kyth")) {
      state = "7";
      sciPaperAr.setState("科研室驳回");
      int points = 0;
      int b = sciPaperAMapper.updateSciPaperArs(id, points);
    }
    int a = sciPaperAMapper.pytg(id, state);

    sciPaperAr.setUid(userId);
    sciPaperAr.setAr_id(Integer.valueOf(id));
    //System.out.println("remark132131 = " + remark);

    sciPaperAr.setConcate(remark);


    sciPaperAMapper.insertSciPaperAr(sciPaperAr);
    return a;
  }

  @Override
  public List<SciPaperAr> selectSciPaperArList(SciPaperAr sciPaperAr) {
    return sciPaperAMapper.selectSciPaperArList(sciPaperAr);
  }

  @Override
  public List<SciPaperA> selectAllPaperName(String query) {
    return sciPaperAMapper.selectAllPaperName(query);
  }

  @Override
  public Integer selectSciPaperA(SciPaperA paper) {
    System.out.println("--------------------------");
    Integer a = sciPaperAMapper.selectSciPaperA(paper);
    System.out.println("a = " + a);
    return a;
  }

  @Override
  public List<SciPaperA> getStatsQuery(Map<String, String> params) {
    return sciPaperAMapper.getStatsQuery(params);
  }
}
