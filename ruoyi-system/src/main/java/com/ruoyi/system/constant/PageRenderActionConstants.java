package com.ruoyi.system.constant;

/**
 * 页面渲染通用动作标识常量
 * 定义八大模块共用的动作标识，确保前后端按钮标识统一
 *
 * @author ruoyi
 */
public class PageRenderActionConstants {

    /** 查看详情 */
    public static final String ACTION_VIEW = "view";

    /** 查看流程 */
    public static final String ACTION_VIEW_PROCESS = "viewProcess";

    /** 编辑 */
    public static final String ACTION_EDIT = "edit";

    /** 删除 */
    public static final String ACTION_REMOVE = "remove";

    /** 提交 */
    public static final String ACTION_SUBMIT = "submit";

    /** 申请结项（横向课题特有） */
    public static final String ACTION_OVER_APPLY = "overApply";

    /** 追加金额（横向课题特有） */
    public static final String ACTION_REAMOUNT = "reamount";

    /** 审批通过 */
    public static final String ACTION_APPROVE = "approve";

    /** 审批驳回 */
    public static final String ACTION_REJECT = "reject";

    /** 撤回 */
    public static final String ACTION_RECALL = "recall";

    /** 批阅（教研室审批入口） */
    public static final String ACTION_REVIEW = "review";

    /** 科研核查（科研处审批入口） */
    public static final String ACTION_KY_REVIEW = "kyReview";

    /** 学院批阅（学院审批入口） */
    public static final String ACTION_DEPT_REVIEW = "deptReview";

    private PageRenderActionConstants() {
    }
}
