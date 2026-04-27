package com.ruoyi.system.domain;

import lombok.Data;

/**
 * 审批结果返回对象
 * <p>
 * 替代原来 Map&lt;String, Object&gt; 的返回方式，提供编译期类型安全。
 * 通过静态工厂方法构建成功/失败结果，私有构造器防止外部直接实例化。
 * </p>
 */
@Data
public class ApprovalResult {

    /** 操作是否成功 */
    private boolean success;

    /** 结果描述信息，成功时为操作提示，失败时为错误原因 */
    private String message;

    /** 审批流转后的新状态值，由流程节点配置（passState/rejectState）计算得出 */
    private String newState;

    /** 当前所处的审批节点 */
    private SysApprovalNode currentNode;

    /** 下一个审批节点，无后续节点时为null（表示流程即将结束） */
    private SysApprovalNode nextNode;

    /** 当前流程的全部节点列表，按 nodeOrder 排序 */
    private java.util.List<SysApprovalNode> nodeList;

    /** 当前流程配置信息 */
    private SysApprovalProcess process;

    /** 是否为最后一个节点（nextNode == null 时为true） */
    private boolean last;

    /** 私有构造器，强制通过静态工厂方法创建实例 */
    private ApprovalResult() {}

    /**
     * 构建成功结果（基础版），仅包含消息和新状态
     *
     * @param message  结果描述
     * @param newState 新状态值
     * @return 成功的审批结果
     */
    public static ApprovalResult ok(String message, String newState) {
        ApprovalResult r = new ApprovalResult();
        r.success = true;
        r.message = message;
        r.newState = newState;
        return r;
    }

    /**
     * 构建成功结果（审批通过专用），包含下一节点和是否最后节点标识
     *
     * @param message  结果描述
     * @param newState 新状态值
     * @param nextNode 下一审批节点，无后续时传null
     * @param isLast   是否最后一个节点
     * @return 成功的审批结果
     */
    public static ApprovalResult ok(String message, String newState, SysApprovalNode nextNode, boolean isLast) {
        ApprovalResult r = ok(message, newState);
        r.nextNode = nextNode;
        r.last = isLast;
        return r;
    }

    /**
     * 构建成功结果（节点查询专用），包含完整节点信息和流程配置
     *
     * @param message     结果描述
     * @param newState    新状态值（查询场景可为null）
     * @param currentNode 当前节点
     * @param nextNode    下一节点
     * @param nodeList    全部节点列表
     * @param process     流程配置
     * @return 成功的审批结果
     */
    public static ApprovalResult okWithNode(String message, String newState,
            SysApprovalNode currentNode, SysApprovalNode nextNode,
            java.util.List<SysApprovalNode> nodeList, SysApprovalProcess process) {
        ApprovalResult r = ok(message, newState);
        r.currentNode = currentNode;
        r.nextNode = nextNode;
        r.nodeList = nodeList;
        r.process = process;
        return r;
    }

    /**
     * 构建失败结果
     *
     * @param message 失败原因描述
     * @return 失败的审批结果
     */
    public static ApprovalResult fail(String message) {
        ApprovalResult r = new ApprovalResult();
        r.success = false;
        r.message = message;
        return r;
    }
}
