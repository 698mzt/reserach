/**
 * 页面渲染公共JS
 * 提供统一的状态渲染、按钮渲染、动作分发能力，供八大模块共用
 *
 * 使用方式：
 * 1. 在页面中引入此JS文件
 * 2. 调用 PageRender.renderStatusToString(statusMeta) 渲染状态
 * 3. 调用 PageRender.renderActionsToString(actions) 渲染按钮
 * 4. 调用 PageRender.registerHandler(actionKey, handler) 注册动作处理器
 * 5. 调用 PageRender.dispatchAction(actionKey, data) 分发动作
 */
var PageRender = (function () {

    /** 颜色映射配置 */
    var colorConfig = {
        /** 状态标签颜色映射 */
        status: {
            'primary': 'label-primary',
            'success': 'label-success',
            'warning': 'label-warning',
            'danger': 'label-danger',
            'info': 'label-info',
            'default': 'label-default'
        },
        /** 按钮颜色映射（Bootstrap btn-* 类名） */
        button: {
            'primary': 'btn-primary',
            'success': 'btn-success',
            'warning': 'btn-warning',
            'danger': 'btn-danger',
            'info': 'btn-info',
            'default': 'btn-default'
        }
    };

    /** 动作处理器注册表 */
    var actionHandlers = {};

    /**
     * 获取状态标签CSS类名
     *
     * @param {string} colorType 颜色类型
     * @returns {string} CSS类名
     */
    function getStatusColorClass(colorType) {
        return colorConfig.status[colorType] || colorConfig.status['default'];
    }

    /**
     * 获取按钮CSS颜色类名
     *
     * @param {string} colorType 颜色类型
     * @returns {string} CSS类名
     */
    function getButtonColorClass(colorType) {
        return colorConfig.button[colorType] || colorConfig.button['default'];
    }

    /**
     * 渲染状态标签为HTML字符串
     * 使用内联样式,与横向课题模块保持一致样式规范
     *
     * @param {Object} statusMeta 状态元数据 {statusCode, statusText, colorType}
     * @param {Object} [options] 可选配置 {template: 'badge'|'label'|'text'}
     * @returns {string} HTML字符串
     */
    function renderStatusToString(statusMeta, options) {
        if (!statusMeta || !statusMeta.statusText) {
            return '<span style="color:#999999; background-color:#f5f5f5; padding:4px 8px; border-radius:4px; font-weight:500; font-size:12px; display:inline-block; white-space:nowrap;">未知</span>';
        }
        var opts = $.extend({template: 'label'}, options || {});
        var statusCode = statusMeta.statusCode || '';
        var statusText = statusMeta.statusText;
        
        // 默认颜色配置(根据colorType)
        var colorClass = getStatusColorClass(statusMeta.colorType);
        var textColor = '#999999';
        var bgColor = '#f5f5f5';
        
        // 根据状态关键词匹配颜色(与横向课题保持一致)
        if (statusCode.indexOf('DRAFT') !== -1) {
            textColor = '#999999';
            bgColor = '#f5f5f5';
        } else if (statusCode.indexOf('JYS') !== -1) {
            textColor = '#ff9800';
            bgColor = '#fff3e0';
        } else if (statusCode.indexOf('XY') !== -1 || statusCode.indexOf('KYC') !== -1) {
            textColor = '#2196f3';
            bgColor = '#e3f2fd';
        } else if (statusCode.indexOf('PASSED') !== -1) {
            textColor = '#4caf50';
            bgColor = '#e8f5e9';
        } else if (statusCode.indexOf('REJECTED') !== -1) {
            textColor = '#f44336';
            bgColor = '#ffebee';
        } else {
            // 兜底: 使用colorType映射到Bootstrap颜色类
            var classMap = {
                'primary': {color: '#337ab7', bg: '#d9edf7'},
                'success': {color: '#4caf50', bg: '#e8f5e9'},
                'warning': {color: '#ff9800', bg: '#fff3e0'},
                'danger': {color: '#f44336', bg: '#ffebee'},
                'info': {color: '#2196f3', bg: '#e3f2fd'},
                'default': {color: '#999999', bg: '#f5f5f5'}
            };
            var colors = classMap[statusMeta.colorType] || classMap['default'];
            textColor = colors.color;
            bgColor = colors.bg;
        }

        switch (opts.template) {
            case 'badge':
                return '<span style="color:' + textColor + '; background-color:' + bgColor + '; padding:2px 6px; border-radius:4px; font-weight:500; font-size:11px; display:inline-block; white-space:nowrap;">' + statusText + '</span>';
            case 'text':
                return '<span>' + statusText + '</span>';
            case 'label':
            default:
                return '<span style="color:' + textColor + '; background-color:' + bgColor + '; padding:4px 8px; border-radius:4px; font-weight:500; font-size:12px; display:inline-block; white-space:nowrap;">' + statusText + '</span>';
        }
    }

    /**
     * 渲染状态标签到指定容器
     *
     * @param {string|HTMLElement} container 渲染容器（选择器或DOM元素）
     * @param {Object} statusMeta 状态元数据
     * @param {Object} [options] 可选配置
     */
    function renderStatus(container, statusMeta, options) {
        $(container).html(renderStatusToString(statusMeta, options));
    }

    /**
     * 渲染按钮列表为HTML字符串
     * 统一使用美化按钮样式，根据场景自动选择尺寸
     *
     * @param {Array} actions 动作列表 [{actionKey, actionText, colorType, displayType, visible, sortOrder, confirmMsg}]
     * @param {Object} [options] 可选配置
     *   - style: 'table'(列表页操作列，btn-xs) | 'page'(详情页底部按钮，btn-sm)，默认'table'
     *   - separator: 按钮之间的分隔符，默认' '
     *   - nowrap: 是否强制按钮在一行显示，默认false
     * @returns {string} HTML字符串
     */
    function renderActionsToString(actions, options) {
        if (!actions || !actions.length) {
            return '';
        }
        var opts = $.extend({style: 'table', separator: ' ', nowrap: false}, options || {});

        var sortedActions = actions.slice().sort(function (a, b) {
            return (a.sortOrder || 0) - (b.sortOrder || 0);
        });

        var htmlParts = [];
        for (var i = 0; i < sortedActions.length; i++) {
            var action = sortedActions[i];
            if (action.visible === false) {
                continue;
            }
            var confirmAttr = action.confirmMsg ? ' data-confirm="' + action.confirmMsg + '"' : '';
            var colorClass = getButtonColorClass(action.colorType);
            var html = '';

            if (opts.style === 'table') {
                html = '<a href="javascript:void(0)" class="btn btn-xs ' + colorClass + '" ' +
                    'data-action-key="' + action.actionKey + '"' + confirmAttr + '>' +
                    action.actionText + '</a>';
            } else {
                html = '<button type="button" class="btn btn-sm ' + colorClass + '" ' +
                    'data-action-key="' + action.actionKey + '"' + confirmAttr + '>' +
                    action.actionText + '</button>';
            }
            htmlParts.push(html);
        }

        var result = htmlParts.join(opts.separator);
        
        if (opts.nowrap) {
            result = '<div style="display: -webkit-box; display: -webkit-flex; display: -ms-flexbox; display: flex; -webkit-flex-wrap: nowrap; -ms-flex-wrap: nowrap; flex-wrap: nowrap; -webkit-box-pack: start; -webkit-justify-content: flex-start; -ms-flex-pack: start; justify-content: flex-start; -webkit-box-align: center; -webkit-align-items: center; -ms-flex-align: center; align-items: center; white-space: nowrap; word-break: keep-all; overflow: hidden; gap: 4px;">' + result + '</div>';
        }
        
        return result;
    }

    /**
     * 渲染按钮列表到指定容器，并自动绑定事件
     *
     * @param {string|HTMLElement} container 渲染容器
     * @param {Array} actions 动作列表
     * @param {Object} [options] 可选配置
     * @param {Object} [rowData] 行数据，传递给动作处理器
     */
    function renderActions(container, actions, options, rowData) {
        var $container = $(container);
        $container.html(renderActionsToString(actions, options));

        if (rowData) {
            $container.data('row-data', rowData);
        }

        $container.off('click.pageRender').on('click.pageRender', 'a[data-action-key], button[data-action-key]', function (e) {
            e.preventDefault();
            var $btn = $(this);
            var actionKey = $btn.data('action-key');
            var confirmMsg = $btn.data('confirm');

            if (confirmMsg) {
                $.modal.confirm(confirmMsg, function () {
                    dispatchAction(actionKey, rowData || $container.data('row-data'));
                });
            } else {
                dispatchAction(actionKey, rowData || $container.data('row-data'));
            }
        });
    }

    /**
     * 注册动作处理器
     *
     * @param {string} actionKey 动作标识
     * @param {Function} handler 处理函数 function(actionKey, data)
     */
    function registerHandler(actionKey, handler) {
        actionHandlers[actionKey] = handler;
    }

    /**
     * 批量注册动作处理器
     *
     * @param {Object} handlers 处理器映射 {actionKey: handler}
     */
    function registerHandlers(handlers) {
        $.extend(actionHandlers, handlers);
    }

    /**
     * 分发动作到对应的处理器
     *
     * @param {string} actionKey 动作标识
     * @param {Object} data 业务数据
     */
    function dispatchAction(actionKey, data) {
        var handler = actionHandlers[actionKey];
        if (handler) {
            try {
                return handler(actionKey, data);
            } catch (error) {
                $.modal.alertError('操作失败，请稍后重试');
            }
        } else {
            $.modal.alertWarning('未注册的操作 [' + actionKey + ']');
        }
    }

    /**
     * 更新颜色映射配置
     *
     * @param {Object} newConfig 新配置
     */
    function updateColorConfig(newConfig) {
        if (newConfig.status) {
            $.extend(colorConfig.status, newConfig.status);
        }
        if (newConfig.button) {
            $.extend(colorConfig.button, newConfig.button);
        }
    }

    return {
        renderStatusToString: renderStatusToString,
        renderStatus: renderStatus,
        renderActionsToString: renderActionsToString,
        renderActions: renderActions,
        registerHandler: registerHandler,
        registerHandlers: registerHandlers,
        dispatchAction: dispatchAction,
        updateColorConfig: updateColorConfig
    };
})();
