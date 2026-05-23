/**
 * 统计模块公共 JS 模块
 * 包含公共表格初始化、总计/小计行渲染、日期工具函数
 */

/**
 * 公共表格初始化参数封装
 * 提供 RuoYi 表格的公共默认配置，各模板传入自定义选项进行覆盖
 * @param {Object} customOptions 模板特定的表格选项
 */
function initTable(customOptions) {
    var defaults = {
        method: 'post',
        sidePagination: 'server',
        pagination: true,
        showSearch: false
    };
    var options = $.extend({}, defaults, customOptions);
    $.table.init(options);
}

/**
 * 总计/小计行渲染
 * 在表格加载完成后，查找 "总计" 和 "小计" 行并应用合并列样式
 * @param {Object} $table - 表格的 jQuery 对象
 */
function applyStatRowStyle($table) {
    if (!$table || !$table.length) return;

    var $tbody = $table.find('tbody');
    if (!$tbody.length) return;

    $tbody.find('tr').each(function () {
        var $row = $(this);
        var $cells = $row.find('td');

        if ($cells.length > 2) {
            var userNameText = $cells.eq(2).text().trim();
            if (userNameText === '总计') {
                $row.addClass('total-row');
                var $deptCell = $cells.eq(1);
                var $teacherCell = $cells.eq(2);
                $deptCell.addClass('total-merged-cell')
                    .attr('colspan', '2')
                    .text('总计');
                $teacherCell.hide();
                $cells.eq(0).addClass('bs-checkbox');
            } else if (userNameText === '小计') {
                $row.addClass('statistics-row');
                var $deptSubCell = $cells.eq(1);
                var $teacherSubCell = $cells.eq(2);
                $deptSubCell.addClass('statistics-merged-cell')
                    .attr('colspan', '2')
                    .text($deptSubCell.text() + ' 小计');
                $teacherSubCell.hide();
                $cells.eq(0).addClass('bs-checkbox');
            }
        }
    });
}

/**
 * 获取今天日期字符串
 * @param {string} [separator='-'] - 日期分隔符
 * @returns {string} 格式化后的日期，如 "2025-07-15"
 */
function getToday(separator) {
    separator = separator || '-';
    var now = new Date();
    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    var day = now.getDate();
    if (month < 10) month = '0' + month;
    if (day < 10) day = '0' + day;
    return year + separator + month + separator + day;
}

/**
 * 显示导出进度模态框
 * @param {string} [text] - 进度提示文字
 */
function showExportProgress(text) {
    text = text || '正在导出数据，请稍候...';
    var $modal = $('#exportProgressModal');
    if (!$modal.length) return;
    $('#exportProgressText').text(text);
    $('#exportProgressBar').css('width', '0%').text('0%').attr('aria-valuenow', '0');
    $modal.modal({ backdrop: 'static', keyboard: false });
    $modal.modal('show');
}

/**
 * 更新导出进度条
 * @param {number} percent - 进度百分比（0-100）
 * @param {string} [text] - 进度提示文字
 */
function updateExportProgress(percent, text) {
    var $bar = $('#exportProgressBar');
    if (!$bar.length) return;
    $bar.css('width', percent + '%').text(percent + '%').attr('aria-valuenow', percent);
    if (text) {
        $('#exportProgressText').text(text);
    }
}

/**
 * 关闭导出进度模态框
 */
function hideExportProgress() {
    var $modal = $('#exportProgressModal');
    if (!$modal.length) return;
    $modal.modal('hide');
}

/**
 * 注入统一勾选 UI 样式
 */
function ensureSelectionUiStyle() {
    if (document.getElementById('statistic-selection-ui-style')) {
        return;
    }
    var css = ''
        + '.stat-selection-summary{display:flex;align-items:center;gap:12px;margin:0 0 8px;padding:8px 12px;background:#f5f7fa;border:1px solid #e4e7ed;border-radius:4px;}'
        + '.stat-selection-summary__text{color:#606266;font-size:13px;line-height:20px;}'
        + '.stat-selection-summary__text strong{color:#409eff;font-size:16px;margin:0 4px;}'
        + '.stat-selection-summary__actions{display:flex;align-items:center;gap:8px;flex-wrap:wrap;}'
        + '.stat-selection-summary__btn{padding:4px 10px;border:1px solid #dcdfe6;border-radius:4px;background:#fff;color:#606266;cursor:pointer;outline:none;}'
        + '.stat-selection-summary__btn:hover{color:#409eff;border-color:#c6e2ff;}'
        + '.stat-compare-panel{margin:0 0 8px;padding:12px;background:#f8fafc;border:1px solid #e4e7ed;border-radius:4px;}'
        + '.stat-compare-panel__head{display:flex;align-items:center;justify-content:space-between;gap:12px;flex-wrap:wrap;margin-bottom:10px;}'
        + '.stat-compare-panel__title{font-size:14px;font-weight:600;color:#303133;line-height:20px;}'
        + '.stat-compare-panel__desc{font-size:12px;color:#909399;line-height:20px;}'
        + '.stat-compare-panel__empty{padding:10px 12px;background:#fff;border:1px dashed #dcdfe6;border-radius:4px;font-size:13px;color:#909399;}'
        + '.stat-compare-panel__grid{display:grid;grid-template-columns:repeat(10,1fr);gap:8px;}'
        + '.stat-compare-card{padding:10px 12px;background:#fff;border:1px solid #ebeef5;border-radius:4px;transition:all .2s ease;}'
        + '.stat-compare-card.is-diff-up{border-color:#f56c6c;background:#fef0f0;}'
        + '.stat-compare-card.is-diff-down{border-color:#e6a23c;background:#fdf6ec;}'
        + '.stat-compare-card.is-same{border-color:#dcdfe6;background:#fff;}'
        + '.stat-compare-card__label{font-size:12px;color:#606266;line-height:18px;min-height:36px;word-break:break-all;}'
        + '.stat-compare-card__main{margin-top:8px;font-size:18px;font-weight:600;color:#303133;line-height:24px;}'
        + '.stat-compare-card__sub{margin-top:6px;display:flex;justify-content:space-between;gap:8px;font-size:12px;color:#909399;line-height:18px;}'
        + '.stat-compare-card__delta{margin-top:6px;font-size:12px;font-weight:600;line-height:18px;}'
        + '.stat-compare-card__delta.is-diff-up{color:#f56c6c;}'
        + '.stat-compare-card__delta.is-diff-down{color:#e6a23c;}'
        + '.stat-scroll-panel{display:flex;align-items:center;justify-content:flex-start;gap:8px;margin:8px 15px;padding:6px 12px;background:#f5f7fa;border:1px solid #e4e7ed;border-radius:4px;flex-wrap:nowrap;white-space:nowrap;}'
        + '.stat-scroll-panel__status{font-size:12px;color:#909399;line-height:20px;}'
        + '.stat-scroll-panel__status.is-running{color:#67c23a;}'
        + '.stat-scroll-panel__status.is-paused{color:#e6a23c;}'
        + '.stat-scroll-panel__status.is-stopped{color:#909399;}'
        + '.stat-scroll-panel__btn{padding:4px 10px;border:1px solid #dcdfe6;border-radius:4px;background:#fff;color:#606266;cursor:pointer;outline:none;}'
        + '.stat-scroll-panel__btn:hover{color:#409eff;border-color:#c6e2ff;}'
        + '.stat-selected-row>td{background-color:#ecf5ff !important;}'
        + '.fixed-table-body .stat-selected-row:hover>td{background-color:#d9ecff !important;}'
        + '.fixed-table-body .stat-diff-row:hover>td{background-color:#f0f0f0 !important;}';
    $('<style id="statistic-selection-ui-style"></style>').text(css).appendTo('head');
}

function _isSummaryRowProxy(row) {
    if (typeof _isStatisticSummaryRow === 'function') {
        return _isStatisticSummaryRow(row);
    }
    if (!row) return true;
    var userName = String(row.userName || '').trim();
    var deptName = String(row.deptName || '').trim();
    var parentName = String(row.parentName || '').trim();
    return userName === '总计' || userName === '小计'
        || deptName === '总计' || deptName === '小计'
        || /小计$/.test(deptName)
        || parentName === '总计' || parentName === '小计';
}

function _filterStatisticRowsProxy(rows) {
    if (typeof _filterStatisticRows === 'function') {
        return _filterStatisticRows(rows || []);
    }
    rows = rows || [];
    var filtered = [];
    for (var i = 0; i < rows.length; i++) {
        if (!_isSummaryRowProxy(rows[i])) {
            filtered.push(rows[i]);
        }
    }
    return filtered;
}

function _getVisibleStatisticRows($table) {
    if (!$table || !$table.length) return [];
    return _filterStatisticRowsProxy($table.bootstrapTable('getData') || []);
}

function _getCrossPageStatisticRows($table) {
    if (!$table || !$table.length) {
        return { rows: [], selectedCount: 0, fromCrossPage: false };
    }

    if (typeof resolveCrossPageRows === 'function') {
        var result = resolveCrossPageRows($table, function (row) {
            if (typeof _selectionKey === 'function') {
                return _selectionKey(row);
            }
            return JSON.stringify(row || {});
        }, true);
        if (result && result.fromCrossPage) {
            return {
                rows: _filterStatisticRowsProxy(result.rows || []),
                selectedCount: result.selectedCount || 0,
                fromCrossPage: true
            };
        }
    }

    if (typeof _getTableSelectionScope === 'function' && typeof _getSelectionBucket === 'function') {
        var scope = _getTableSelectionScope($table);
        var bucket = _getSelectionBucket(scope);
        var keys = Object.keys(bucket || {});
        var rows = [];
        for (var i = 0; i < keys.length; i++) {
            rows.push(bucket[keys[i]]);
        }
        return {
            rows: _filterStatisticRowsProxy(rows),
            selectedCount: keys.length,
            fromCrossPage: keys.length > 0
        };
    }

    return { rows: [], selectedCount: 0, fromCrossPage: false };
}

function _flattenTableColumns(columns, list) {
    list = list || [];
    columns = columns || [];
    for (var i = 0; i < columns.length; i++) {
        if ($.isArray(columns[i])) {
            _flattenTableColumns(columns[i], list);
        } else {
            list.push(columns[i]);
        }
    }
    return list;
}

function _decodeTitleText(text) {
    if (text === undefined || text === null) return '';
    return $('<div></div>').html(String(text)).text().replace(/\s+/g, ' ').trim();
}

function _isIdentityField(field) {
    var skipMap = {
        state: true,
        userId: true,
        deptId: true,
        parentId: true,
        userName: true,
        deptName: true,
        parentName: true,
        majorName: true,
        zydm: true,
        xy: true,
        学院: true,
        专业: true,
        教研室: true,
        教师: true
    };
    return !!skipMap[field];
}

function _isNumericLikeValue(value) {
    if (value === null || value === undefined || value === '') return true;
    if (typeof value === 'number') return true;
    if (typeof value === 'string') {
        var trimmed = $.trim(value).replace(/,/g, '');
        if (trimmed === '') return true;
        return !isNaN(trimmed);
    }
    return false;
}

function _resolveCompareMetricColumns($table, rows) {
    var options = $table.bootstrapTable('getOptions') || {};
    var flatColumns = _flattenTableColumns(options.columns || [], []);
    var metrics = [];
    var seen = {};
    rows = rows || [];

    for (var i = 0; i < flatColumns.length; i++) {
        var column = flatColumns[i] || {};
        var field = column.field;
        if (!field || column.checkbox || column.radio || _isIdentityField(field) || seen[field]) {
            continue;
        }

        var numeric = false;
        for (var j = 0; j < rows.length; j++) {
            if (!_isSummaryRowProxy(rows[j]) && rows[j][field] !== undefined) {
                numeric = _isNumericLikeValue(rows[j][field]);
                if (!numeric) {
                    break;
                }
            }
        }

        if (!numeric && rows.length > 0) {
            continue;
        }

        seen[field] = true;
        metrics.push({
            field: field,
            title: _decodeTitleText(column.title || field)
        });
    }

    return metrics;
}

function _aggregateMetricValues(rows, metrics) {
    rows = rows || [];
    metrics = metrics || [];
    var totals = {};
    for (var i = 0; i < metrics.length; i++) {
        totals[metrics[i].field] = 0;
    }

    for (var rowIndex = 0; rowIndex < rows.length; rowIndex++) {
        var row = rows[rowIndex] || {};
        if (_isSummaryRowProxy(row)) {
            continue;
        }
        for (var metricIndex = 0; metricIndex < metrics.length; metricIndex++) {
            var field = metrics[metricIndex].field;
            var raw = row[field];
            var value = parseFloat(raw);
            if (!isNaN(value)) {
                totals[field] += value;
            }
        }
    }

    return totals;
}

function _formatMetricNumber(value) {
    value = parseFloat(value || 0);
    if (isNaN(value)) {
        return '0';
    }
    if (Math.abs(value - Math.round(value)) < 0.000001) {
        return String(Math.round(value));
    }
    return value.toFixed(2).replace(/\.00$/, '').replace(/(\.\d)0$/, '$1');
}

function _ensureComparePanel($table) {
    if (!$table || !$table.length) return null;
    var $panel = $table.data('selection-compare-panel');
    if ($panel && $panel.length) {
        return $panel;
    }

    var html = ''
        + '<div class="stat-compare-panel">'
        + '  <div class="stat-compare-panel__head">'
        + '    <div class="stat-compare-panel__title">跨页汇总对比</div>'
        + '    <div class="stat-compare-panel__desc" data-role="compare-desc"></div>'
        + '  </div>'
        + '  <div data-role="compare-body"></div>'
        + '</div>';

    $panel = $(html);
    var $summary = $table.data('selection-summary');
    var $scrollPanel = $table.data('scroll-panel');
    if ($scrollPanel && $scrollPanel.length) {
        $scrollPanel.before($panel);
    } else if ($summary && $summary.length) {
        $summary.after($panel);
    } else {
        var $container = $table.closest('.select-table');
        if (!$container.length) {
            $container = $table.parent();
        }
        $container.before($panel);
    }
    $table.data('selection-compare-panel', $panel);
    return $panel;
}

function _buildCompareCards(metrics, selectedTotals, visibleTotals) {
    var html = '<div class="stat-compare-panel__grid">';
    for (var i = 0; i < metrics.length; i++) {
        var metric = metrics[i];
        var selectedValue = selectedTotals[metric.field] || 0;
        var visibleValue = visibleTotals[metric.field] || 0;
        var delta = selectedValue - visibleValue;
        var stateClass = 'is-same';
        var deltaClass = '';
        var deltaText = '差值 0';

        if (delta > 0.000001) {
            stateClass = 'is-diff-up';
            deltaClass = 'is-diff-up';
            deltaText = '差值 +' + _formatMetricNumber(delta);
        } else if (delta < -0.000001) {
            stateClass = 'is-diff-down';
            deltaClass = 'is-diff-down';
            deltaText = '差值 ' + _formatMetricNumber(delta);
        }

        html += ''
            + '<div class="stat-compare-card ' + stateClass + '">'
            + '  <div class="stat-compare-card__label">' + metric.title + '</div>'
            + '  <div class="stat-compare-card__main">' + _formatMetricNumber(selectedValue) + '</div>'
            + '  <div class="stat-compare-card__sub">'
            + '    <span>当前页：' + _formatMetricNumber(visibleValue) + '</span>'
            + '  </div>'
            + '  <div class="stat-compare-card__delta ' + deltaClass + '">' + deltaText + '</div>'
            + '</div>';
    }
    html += '</div>';
    return html;
}

function updateStatisticComparePanel($table) {
}

function updateDifferenceRowHighlight($table) {
    if (!$table || !$table.length) return;
    var $tbody = $table.find('tbody');
    if (!$tbody.length) return;

    $tbody.find('tr').removeClass('stat-diff-row');

    var crossPage = _getCrossPageStatisticRows($table);
    if (!crossPage.selectedCount) {
        return;
    }

    var selectedMap = {};
    for (var i = 0; i < crossPage.rows.length; i++) {
        if (typeof _selectionKey === 'function') {
            selectedMap[_selectionKey(crossPage.rows[i])] = true;
        }
    }

    var currentRows = $table.bootstrapTable('getData') || [];
    $tbody.find('tr[data-index]').each(function () {
        var $row = $(this);
        var index = parseInt($row.attr('data-index'), 10);
        if (isNaN(index)) {
            return;
        }
        var rowData = currentRows[index];
        if (!rowData || _isSummaryRowProxy(rowData)) {
            return;
        }
        if ($row.hasClass('stat-selected-row')) {
            return;
        }
        if (typeof _selectionKey === 'function' && !selectedMap[_selectionKey(rowData)]) {
            $row.addClass('stat-diff-row');
        }
    });
}

function refreshStatisticSelectionState($table) {
    if (!$table || !$table.length) return;
    updateSelectionSummary($table);
    updateSelectedRowHighlight($table);
    updateStatisticComparePanel($table);
    updateDifferenceRowHighlight($table);
}

/**
 * 更新已勾选行高亮
 * @param {Object} $table - 表格的 jQuery 对象
 */
function updateSelectedRowHighlight($table) {
    if (!$table || !$table.length) return;
    var $tbody = $table.find('tbody');
    if (!$tbody.length) return;
    $tbody.find('tr').removeClass('stat-selected-row');
    $tbody.find('input[name="btSelectItem"]:checked').each(function () {
        $(this).closest('tr').addClass('stat-selected-row');
    });
}

/**
 * 更新勾选汇总信息
 * @param {Object} $table - 表格的 jQuery 对象
 */
function updateSelectionSummary($table) {
    if (!$table || !$table.length) return;
    var $summary = $table.data('selection-summary');
    if (!$summary || !$summary.length) return;

    var selectedCount = 0;
    if (typeof getSelectionCount === 'function') {
        selectedCount = getSelectionCount($table) || 0;
    } else {
        var selectedRows = $table.bootstrapTable('getSelections') || [];
        selectedCount = selectedRows.length;
    }

    var currentRows = _getVisibleStatisticRows($table);
    var visibleCount = currentRows.length;
    $summary.find('[data-role="selection-count"]').text(selectedCount);
    $summary.find('[data-role="selection-visible-count"]').text(visibleCount);
}

/**
 * 初始化统一勾选 UI
 * @param {Object} $table - 表格的 jQuery 对象
 */
function initSelectionUi($table) {
    if (!$table || !$table.length) return;
    if ($table.data('selection-ui-initialized')) {
        refreshStatisticSelectionState($table);
        return;
    }

    ensureSelectionUiStyle();

    var summaryHtml = ''
        + '<div class="stat-selection-summary">'
        + '  <div class="stat-selection-summary__text">'
        + '    已跨页勾选<strong data-role="selection-count">0</strong>项，当前页共<span data-role="selection-visible-count">0</span>条数据'
        + '  </div>'
        + '</div>';

    var $summary = $(summaryHtml);
    var $container = $table.closest('.select-table');
    if (!$container.length) {
        $container = $table.parent();
    }
    $container.before($summary);
    $table.data('selection-summary', $summary);

    $table.on('load-success.bs.table.selectionUi check.bs.table.selectionUi uncheck.bs.table.selectionUi check-all.bs.table.selectionUi uncheck-all.bs.table.selectionUi', function () {
        setTimeout(function () {
            refreshStatisticSelectionState($table);
        }, 0);
    });

    $table.data('selection-ui-initialized', true);
    refreshStatisticSelectionState($table);
}

/**
 * 启用表格自动滚动播放
 * 当表格数据行过多时，自动缓慢向下滚动，无需手动翻页或滚轮
 * @param {string} tableId - 表格的id（如 '#bootstrap-table'）
 * @param {number|Object} [speed=30] - 滚动速度（像素/秒）或配置对象
 * @param {Object} [options] - 自动滚动配置
 */
function enableAutoScroll(tableId, speed, options) {
    if (typeof speed === 'object') {
        options = speed;
        speed = null;
    }
    speed = speed || 30;
    options = $.extend({
        loop: true,
        autoStart: false,
        showControls: true,
        startDelay: 300,
        step: 2
    }, options || {});

    var $table = $(tableId);
    if (!$table.length) return;

    ensureSelectionUiStyle();

    var timer = null;
    var manualPaused = true;
    var hoverPaused = false;
    var hasReachedBottom = false;
    var scrollDirection = 1;
    var $panel = null;
    var $status = null;
    var $toggle = null;

    function getScrollMetrics(forceRefresh) {
        var cached = forceRefresh ? null : $table.data('auto-scroll-container');
        var candidates = [];
        var seen = [];

        function pushCandidates($items) {
            if (!$items || !$items.length) return;
            $items.each(function () {
                if (!this || !this.isConnected) return;
                if (seen.indexOf(this) >= 0) return;
                seen.push(this);
                candidates.push($(this));
            });
        }

        function buildMetrics($container) {
            if (!$container || !$container.length || !$container[0]) {
                return null;
            }
            var element = $container[0];
            var clientHeight = element.clientHeight || $container.innerHeight() || $container.height() || 0;
            var scrollHeight = element.scrollHeight || 0;
            return {
                $container: $container,
                element: element,
                clientHeight: clientHeight,
                scrollHeight: scrollHeight,
                maxH: Math.max(0, scrollHeight - clientHeight)
            };
        }

        if (cached && cached.length && cached[0] && cached[0].isConnected) {
            var cachedMetrics = buildMetrics(cached);
            if (cachedMetrics) {
                return cachedMetrics;
            }
        }

        pushCandidates($table.closest('.bootstrap-table').find('.fixed-table-body'));
        pushCandidates($table.closest('.fixed-table-container').find('.fixed-table-body'));
        pushCandidates($table.parent().find('.fixed-table-body'));
        pushCandidates($table.siblings('.fixed-table-body'));

        /* virtualScroll:true 时 .fixed-table-body overflow=hidden，改查外层容器 */
        var $bsTable = $table.closest('.bootstrap-table');
        if ($bsTable.length) {
            var $fc = $bsTable.find('.fixed-table-container').first();
            if ($fc.length) candidates.push($fc);
            var $selTable = $bsTable.closest('.select-table');
            if ($selTable.length) candidates.push($selTable);
            candidates.push($bsTable);
        }

        var bestMetrics = null;
        var bestScore = -1;
        for (var i = 0; i < candidates.length; i++) {
            var metrics = buildMetrics(candidates[i]);
            if (!metrics) {
                continue;
            }
            var isVisible = candidates[i].is(':visible');
            var score = (isVisible ? 100000 : 0) + (metrics.maxH * 10) + metrics.clientHeight;
            if (score > bestScore) {
                bestScore = score;
                bestMetrics = metrics;
            }
        }

        if (bestMetrics) {
            $table.data('auto-scroll-container', bestMetrics.$container);
        }
        return bestMetrics;
    }

    function shouldPause() {
        return manualPaused || hoverPaused;
    }

    function buildPanel() {
        if (!options.showControls) return;
        $panel = $table.data('scroll-panel');
        if ($panel && $panel.length) {
            $status = $panel.find('[data-role="scroll-status"]');
            $toggle = $panel.find('[data-role="scroll-toggle"]');
            return;
        }
        var html = ''
            + '<div class="stat-scroll-panel">'
            + '  <span class="stat-scroll-panel__status" data-role="scroll-status">点击「开始滚动」浏览数据</span>'
            + '  <button type="button" class="stat-scroll-panel__btn" data-role="scroll-toggle">开始滚动</button>'
            + '  <button type="button" class="stat-scroll-panel__btn" data-role="panel-clear-selection">清空勾选</button>'
            + '</div>';
        $panel = $(html);
        var $container = $table.closest('.select-table');
        if (!$container.length) {
            $container = $table.parent();
        }
        $container.before($panel);
        $status = $panel.find('[data-role="scroll-status"]');
        $toggle = $panel.find('[data-role="scroll-toggle"]');
        $toggle.on('click', function () {
            if (manualPaused) {
                /* 点击开始滚动：绕过所有检测，直接操作 DOM */
                manualPaused = false;
                hasReachedBottom = false;
                scrollDirection = 1;
                var $ftb = $table.closest('.bootstrap-table').find('.fixed-table-body').first();
                if (!$ftb.length) {
                    setStatus('未找到可滚动的表格区域', 'is-stopped');
                    manualPaused = true;
                    return;
                }
                $ftb.css('overflow', 'auto').css('overflow-y', 'auto');
                $table.data('auto-scroll-scrollTarget', $ftb);
                /* 同时也锁定 .select-table 作为备选滚动容器 */
                var $selTable = $table.closest('.select-table');
                if ($selTable.length) {
                    $selTable.css('overflow', 'auto').css('overflow-y', 'auto');
                    $table.data('auto-scroll-selectTable', $selTable);
                }
                setStatus('滚动中' + (options.loop ? '（循环）' : ''), 'is-running');
                if ($toggle) $toggle.text('停止滚动');
                startScrollDirect($ftb);
            } else {
                /* 点击停止滚动 */
                manualPaused = true;
                stopScroll();
                setStatus('已暂停', 'is-paused');
                if ($toggle) $toggle.text('开始滚动');
            }
        });
        $panel.find('[data-role="panel-clear-selection"]').on('click', function() {
            if (typeof clearChartSelections === 'function') {
                clearChartSelections($table);
            } else {
                $table.bootstrapTable('uncheckAll');
            }
        });
        $table.data('scroll-panel', $panel);
    }

    var _directTimer = null;

    function startScrollDirect(target) {
        if (_directTimer) clearInterval(_directTimer);
        _directTimer = setInterval(function () {
            if (manualPaused) {
                clearInterval(_directTimer);
                _directTimer = null;
                return;
            }
            if (hoverPaused) return;
            /* 先尝试 scrollTarget（.fixed-table-body），如果断开则重新查找 */
            var $target = $table.data('auto-scroll-scrollTarget');
            if (!$target || !$target.length || !$target[0].isConnected) {
                $target = $table.closest('.bootstrap-table').find('.fixed-table-body').first();
                if ($target.length && $target[0].isConnected) {
                    $target.css('overflow', 'auto').css('overflow-y', 'auto');
                    $table.data('auto-scroll-scrollTarget', $target);
                } else {
                    clearInterval(_directTimer);
                    _directTimer = null;
                    return;
                }
            }
            var el = $target[0];
            var maxH = Math.max(0, (el.scrollHeight || 0) - (el.clientHeight || 0));
            if (maxH > 0) {
                doScroll(el, maxH);
            } else {
                /* fixed-table-body 不溢出，回退到 .select-table */
                var $selTable = $table.data('auto-scroll-selectTable');
                if ($selTable && $selTable.length && $selTable[0].isConnected) {
                    var selEl = $selTable[0];
                    var selMaxH = Math.max(0, (selEl.scrollHeight || 0) - (selEl.clientHeight || 0));
                    if (selMaxH > 0) {
                        doScroll(selEl, selMaxH);
                        return;
                    }
                }
                /* 两边都不溢出 → 循环模式回卷，非循环模式停止 */
                if (options.loop) {
                    el.scrollTop = 0;
                } else {
                    manualPaused = true;
                    hasReachedBottom = true;
                    setStatus('已滚动到底部', 'is-stopped');
                    clearInterval(_directTimer);
                    _directTimer = null;
                    if ($toggle) $toggle.text('开始滚动');
                }
            }
        }, Math.max(16, Math.round(1000 / speed)));
    }

    function doScroll(el, maxH) {
        var cur = el.scrollTop || 0;
        if (scrollDirection > 0) {
            if (cur >= maxH - 2) {
                if (options.loop) {
                    scrollDirection = -1;
                    setStatus('滚动中（循环）', 'is-running');
                    return;
                } else {
                    manualPaused = true;
                    hasReachedBottom = true;
                    setStatus('已滚动到底部', 'is-stopped');
                    clearInterval(_directTimer);
                    _directTimer = null;
                    if ($toggle) $toggle.text('开始滚动');
                    return;
                }
            }
            el.scrollTop = Math.min(cur + options.step, maxH);
        } else {
            if (cur <= 2) {
                scrollDirection = 1;
                setStatus('滚动中（循环）', 'is-running');
                return;
            }
            el.scrollTop = Math.max(cur - options.step, 0);
        }
    }

    function setStatus(text, state) {
        if (!$status || !$status.length) return;
        $status.text(text).removeClass('is-running is-paused is-stopped');
        if (state) {
            $status.addClass(state);
        }
        if ($toggle && $toggle.length) {
            $toggle.text(manualPaused ? '开始滚动' : '停止滚动');
            $toggle.prop('disabled', hasReachedBottom && !options.loop);
        }
    }

    function startScroll() {
        if (timer) return;
        timer = setInterval(function () {
            var metrics = getScrollMetrics();
            if (!metrics) return;
            if (shouldPause()) return;
            var cur = metrics.$container.scrollTop();
            if (metrics.maxH <= 0) {
                syncStatus(true);
                return;
            }
            if (scrollDirection > 0) {
                if (cur >= metrics.maxH - 2) {
                    if (options.loop) {
                        scrollDirection = -1;
                        return;
                    } else {
                        hasReachedBottom = true;
                        syncStatus(true);
                        return;
                    }
                }
                metrics.$container.scrollTop(Math.min(cur + options.step, metrics.maxH));
            } else {
                if (cur <= 2) {
                    scrollDirection = 1;
                    return;
                }
                metrics.$container.scrollTop(Math.max(cur - options.step, 0));
            }
        }, Math.max(16, Math.round(1000 / speed)));
    }

    function stopScroll() {
        if (timer) {
            clearInterval(timer);
            timer = null;
        }
    }

    function syncStatus(forceRefresh) {
        var metrics = getScrollMetrics(forceRefresh);
        if (!metrics) {
            setStatus('滚动容器未就绪', 'is-stopped');
            return;
        }
        if (metrics.maxH <= 0) {
            stopScroll();
            hasReachedBottom = false;
            setStatus('当前数据无需滚动', 'is-stopped');
            return;
        }
        if (hasReachedBottom && !options.loop) {
            stopScroll();
            setStatus('已滚动到底部', 'is-stopped');
            return;
        }
        if (manualPaused) {
            stopScroll();
            setStatus('已暂停', 'is-paused');
            return;
        }
        if (hoverPaused) {
            stopScroll();
            setStatus('鼠标悬停，已暂停', 'is-paused');
            return;
        }
        startScroll();
        setStatus(options.loop ? '滚动中（循环）' : '滚动中（到底后停止）', 'is-running');
    }

    function scheduleSync(delay, forceRefresh) {
        setTimeout(function () {
            if (forceRefresh) {
                $table.removeData('auto-scroll-container');
            }
            syncStatus(forceRefresh);
        }, delay);
    }

    function scheduleAutoStart() {
        manualPaused = true;
        if (_directTimer) {
            clearInterval(_directTimer);
            _directTimer = null;
        }
        setStatus('自动滚动启动中...', 'is-stopped');
        if ($toggle) $toggle.text('开始滚动');
        var retryCount = 0;
        var maxRetries = 20;
        function tryAutoStart() {
            if (!manualPaused) return;
            if ($toggle && $toggle.length && $toggle.is(':visible')) {
                $toggle.click();
                return;
            }
            retryCount++;
            if (retryCount < maxRetries) {
                setTimeout(tryAutoStart, 500);
            }
        }
        setTimeout(tryAutoStart, 800);
    }

    buildPanel();

    $table.off('.autoScroll');
    $table.closest('.fixed-table-container')
        .add($table.closest('.bootstrap-table'))
        .off('.autoScroll')
        .on('mouseenter.autoScroll', '.fixed-table-body', function () {
            hoverPaused = true;
            if (manualPaused) return;
            setStatus('鼠标悬停，已暂停', 'is-paused');
        })
        .on('mouseleave.autoScroll', '.fixed-table-body', function () {
            hoverPaused = false;
            if (manualPaused) return;
            var $ftb = $table.data('auto-scroll-scrollTarget');
            if ($ftb && $ftb.length) {
                setStatus('滚动中' + (options.loop ? '（循环）' : ''), 'is-running');
            }
        });

    $table.on('load-success.bs.table.autoScroll page-change.bs.table.autoScroll', function () {
        hasReachedBottom = false;
        if (!manualPaused) {
            manualPaused = true;
            if (_directTimer) {
                clearInterval(_directTimer);
                _directTimer = null;
            }
            if (options.autoStart) {
                scheduleAutoStart();
            } else {
                setStatus('表格已刷新，请重新开始滚动', 'is-stopped');
                if ($toggle) $toggle.text('开始滚动');
            }
        }
    });

    if (options.autoStart) {
        scheduleAutoStart();
    } else {
        setStatus('自动滚动待启动', 'is-stopped');
        scheduleSync(options.startDelay, true);
    }
}

