/**
 * 科研工作量图表模块
 * 负责学院/教研室/科研处/教师维度的 ECharts 图表渲染
 */

var MAX_CHART_ITEMS = 10;

var STATISTIC_CHART_THEME = {
    color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc', '#6e7074'],
    textColor: '#303133',
    subTextColor: '#909399',
    axisColor: '#606266',
    splitLineColor: '#ebeef5'
};

var _chartSelections = {};

function _selectionStorageKey(scope) {
    return 'statistic.chart.selection.' + scope;
}

function _resolveSelectionScope(scope) {
    return scope ? String(scope) : 'default';
}

function _getTableSelectionScope($table) {
    if (!$table || !$table.length) {
        return 'default';
    }
    return _resolveSelectionScope($table.data('selection-scope'));
}

function _getSelectionBucket(scope) {
    scope = _resolveSelectionScope(scope);
    if (!_chartSelections[scope]) {
        _chartSelections[scope] = {};
    }
    return _chartSelections[scope];
}

function _persistSelectionBucket(scope) {
    scope = _resolveSelectionScope(scope);
    try {
        localStorage.setItem(_selectionStorageKey(scope), JSON.stringify(_getSelectionBucket(scope)));
    } catch (e) {
    }
}

function _restoreSelectionBucket(scope) {
    scope = _resolveSelectionScope(scope);
    try {
        var raw = localStorage.getItem(_selectionStorageKey(scope));
        if (raw) {
            var parsed = JSON.parse(raw);
            _chartSelections[scope] = parsed && typeof parsed === 'object' ? parsed : {};
        } else {
            _chartSelections[scope] = _chartSelections[scope] || {};
        }
    } catch (e) {
        _chartSelections[scope] = _chartSelections[scope] || {};
    }
    return _chartSelections[scope];
}

function _getSelectionCountByScope(scope) {
    return Object.keys(_getSelectionBucket(scope)).length;
}

function getSelectionCount($table) {
    return _getSelectionCountByScope(_getTableSelectionScope($table));
}

function _selectionKey(row) {
    row = row || {};
    if (row.userId !== undefined && row.userId !== null && row.userId !== '') {
        return 'u_' + row.userId;
    }
    if (row.deptId !== undefined && row.deptId !== null && row.deptId !== '') {
        var subKey = row.userId || row.userName || '';
        return 'd_' + row.deptId + '_' + subKey;
    }
    if (row.parentId !== undefined && row.parentId !== null && row.parentId !== '') {
        return 'p_' + row.parentId;
    }

    var parentName = String(row.parentName || '').trim();
    var deptName = String(row.deptName || '').trim();
    var userName = String(row.userName || '').trim();

    if (parentName || deptName || userName) {
        return ['n', parentName, deptName, userName].join('|');
    }
    return 'k_' + JSON.stringify(row);
}

function _resolveCheckField(rows) {
    if (!rows || rows.length === 0) return null;
    var candidates = ['userId', 'deptId', 'parentId', 'userName', 'deptName', 'parentName'];
    for (var i = 0; i < candidates.length; i++) {
        var field = candidates[i];
        var matched = true;
        for (var j = 0; j < rows.length; j++) {
            if (rows[j][field] === undefined || rows[j][field] === null || rows[j][field] === '') {
                matched = false;
                break;
            }
        }
        if (matched) {
            return field;
        }
    }
    return null;
}

function _isStatisticSummaryRow(row) {
    if (!row) return true;
    var userName = String(row.userName || '').trim();
    var deptName = String(row.deptName || '').trim();
    var parentName = String(row.parentName || '').trim();
    return userName === '总计' || userName === '小计'
        || deptName === '总计' || deptName === '小计'
        || /小计$/.test(deptName)
        || parentName === '总计' || parentName === '小计';
}

function _filterStatisticRows(rows) {
    var filtered = [];
    rows = rows || [];
    for (var i = 0; i < rows.length; i++) {
        if (!_isStatisticSummaryRow(rows[i])) {
            filtered.push(rows[i]);
        }
    }
    return filtered;
}

function initCrossPageSelection($table, options) {
    if (typeof options === 'string') {
        options = { scope: options };
    }
    options = options || {};
    var scope = _resolveSelectionScope(options.scope || $table.attr('id') || 'default');
    $table.data('selection-scope', scope);
    var bucket = _restoreSelectionBucket(scope);

    $table.on('check.bs.table', function(e, row) {
        bucket[_selectionKey(row)] = row;
        _persistSelectionBucket(scope);
        _renderCharts($table);
    });
    $table.on('uncheck.bs.table', function(e, row) {
        delete bucket[_selectionKey(row)];
        _persistSelectionBucket(scope);
        _renderCharts($table);
    });
    $table.on('check-all.bs.table', function() {
        var rows = $table.bootstrapTable('getData') || [];
        for (var i = 0; i < rows.length; i++) {
            bucket[_selectionKey(rows[i])] = rows[i];
        }
        _persistSelectionBucket(scope);
        _renderCharts($table);
    });
    $table.on('uncheck-all.bs.table', function() {
        var rows = $table.bootstrapTable('getData') || [];
        for (var i = 0; i < rows.length; i++) {
            delete bucket[_selectionKey(rows[i])];
        }
        _persistSelectionBucket(scope);
        _renderCharts($table);
    });
    $table.on('load-success.bs.table', function() {
        var $this = $(this);
        var allRows = $this.bootstrapTable('getData') || [];
        if (!allRows.length) {
            _renderCharts($this);
            return;
        }
        var batchIndexes = [];
        for (var i = 0; i < allRows.length; i++) {
            if (bucket[_selectionKey(allRows[i])]) {
                batchIndexes.push(i);
            }
        }
        if (batchIndexes.length > 0) {
            setTimeout(function() {
                $this.bootstrapTable('uncheckAll');
                for (var idx = 0; idx < batchIndexes.length; idx++) {
                    $this.bootstrapTable('check', batchIndexes[idx]);
                }
                _renderCharts($this);
                if (typeof refreshStatisticSelectionState === 'function') {
                    refreshStatisticSelectionState($this);
                }
            }, 100);
            return;
        }
        _renderCharts($this);
        if (typeof refreshStatisticSelectionState === 'function') {
            refreshStatisticSelectionState($this);
        }
    });
}

function clearChartSelections($table, scope) {
    scope = _resolveSelectionScope(scope || _getTableSelectionScope($table));
    _chartSelections[scope] = {};
    try {
        localStorage.removeItem(_selectionStorageKey(scope));
    } catch (e) {
    }
    if ($table && $table.length) {
        $table.bootstrapTable('uncheckAll');
    }
}

function _getOrCreateChart(chartDom) {
    var existing = echarts.getInstanceByDom(chartDom);
    return existing || echarts.init(chartDom);
}

function _clearChart(chart) {
    if (chart) {
        chart.clear();
    }
}

function _bindChartResize(chartDom) {
    if (!chartDom) {
        return;
    }
    if (!chartDom.__chartResizeBound) {
        chartDom.__chartResizeBound = true;
        $(window).on('resize', function() {
            var instance = echarts.getInstanceByDom(chartDom);
            if (instance) {
                instance.resize();
            }
        });
    }
}

function _renderCharts($table) {
    if (!$table || !$table.length) {
        return;
    }
    if (document.getElementById('collegeChart')) {
        renderCollegeChart($table);
    }
    if (document.getElementById('collegeRose')) {
        renderCollegeRose($table);
    }
    if (document.getElementById('teacherRadar')) {
        renderTeacherRadar($table);
    }
    if (document.getElementById('teacherBar')) {
        renderTeacherBar($table);
    }
    if (document.getElementById('planTeacherRadar')) {
        renderPlanTeacherRadar($table);
    } else if (document.getElementById('planRadar')) {
        renderPlanCollegeRadar($table);
    }
    if (document.getElementById('planTeacherBar')) {
        renderPlanTeacherBar($table);
    } else if (document.getElementById('planBar')) {
        renderPlanCollegeBar($table);
    }
    if (document.getElementById('planChart')) {
        renderPlanSunburst($table);
    }
    if (document.getElementById('achievementChart')) {
        renderAchievementRose($table);
    }
    if (document.getElementById('achievementBar')) {
        renderAchievementBar($table);
    }
}

function resolveCrossPageRows($table, keyFn, forceAll) {
    var scope = _getTableSelectionScope($table);
    var bucket = _getSelectionBucket(scope);
    var keys = Object.keys(bucket);
    if (keys.length > 0) {
        var distinct = [];
        var seen = {};
        for (var i = 0; i < keys.length; i++) {
            var row = bucket[keys[i]];
            var k = keyFn ? keyFn(row) : keys[i];
            if (!seen[k]) {
                seen[k] = true;
                distinct.push(row);
            }
        }
        var truncated = distinct.length > MAX_CHART_ITEMS && !forceAll;
        if (truncated) {
            distinct.sort(function(a, b) {
                return parseFloat(b.__score || 0) - parseFloat(a.__score || 0);
            });
            distinct = distinct.slice(0, MAX_CHART_ITEMS);
        }
        return { rows: distinct, truncated: truncated, fromCrossPage: true, selectedCount: keys.length };
    }
    return { rows: [], truncated: false, fromCrossPage: false, selectedCount: 0 };
}

function resolveDisplayRows($table, keyFn, forceAll) {
    var selectedRows = $table.bootstrapTable('getSelections');
    var allRows = $table.bootstrapTable('getData');
    if (!allRows || allRows.length === 0) return { rows: [], truncated: false, allRows: [] };
    if (forceAll && selectedRows && selectedRows.length > 0) {
        var seen = {};
        var distinct = [];
        for (var i = 0; i < selectedRows.length; i++) {
            var key = keyFn(selectedRows[i]);
            if (!seen[key]) {
                seen[key] = true;
                distinct.push(selectedRows[i]);
            }
        }
        var truncated = distinct.length > MAX_CHART_ITEMS;
        if (truncated) {
            distinct.sort(function(a, b) {
                return parseFloat(b.__score || 0) - parseFloat(a.__score || 0);
            });
            distinct = distinct.slice(0, MAX_CHART_ITEMS);
        }
        return { rows: distinct, truncated: truncated, allRows: allRows };
    }
    var all = [];
    for (var i = 0; i < allRows.length; i++) {
        if (typeof keyFn === 'function') {
            all.push(allRows[i]);
        }
    }
    return { rows: all.slice(0, MAX_CHART_ITEMS), truncated: false, allRows: allRows };
}

var SCORE_LABELS = ['横向课题', '成果转化', '纵向校级以上', '纵向校级', '学术论文', '教材著作', '专利', '软著', '奖励', '讲座报告'];

/** 解析行的积分值 */
function extractScoreValues(row) {
    return [
        parseFloat(row.hxktScore) || 0,
        parseFloat(row.cgzhScore) || 0,
        parseFloat(row.zxktxjysScore) || 0,
        parseFloat(row.zxktxjScore) || 0,
        parseFloat(row.xslwScore) || 0,
        parseFloat(row.jczzScore) || 0,
        parseFloat(row.zlScore) || 0,
        parseFloat(row.rzScore) || 0,
        parseFloat(row.jlScore) || 0,
        parseFloat(row.jzbgScore) || 0
    ];
}

/** 解析行的个数值（教师/教研室维度，英文字段） */
function extractCntValues(row) {
    return [
        parseInt(row.hxktCnt, 10) || 0,
        parseInt(row.cgzhCnt, 10) || 0,
        parseInt(row.zxktxjysCnt, 10) || 0,
        parseInt(row.zxktxjCnt, 10) || 0,
        parseInt(row.xslwCnt, 10) || 0,
        parseInt(row.jczzCnt, 10) || 0,
        parseInt(row.zlCnt, 10) || 0,
        parseInt(row.rzCnt, 10) || 0,
        parseInt(row.jlCnt, 10) || 0,
        parseInt(row.jzbgCnt, 10) || 0
    ];
}

/** 解析行的个数值（学院/学校维度，中文字段） */
function extractCntValuesByChineseFields(row) {
    return [
        parseFloat(row['横向课题科研项目']) || 0,
        parseFloat(row['成果转化']) || 0,
        parseFloat(row['纵向科研项目-校级以上']) || 0,
        parseFloat(row['纵向科研项目-校级']) || 0,
        parseFloat(row['学术论文']) || 0,
        parseFloat(row['教材著作']) || 0,
        parseFloat(row['专利']) || 0,
        parseFloat(row['软著']) || 0,
        parseFloat(row['奖励']) || 0,
        parseFloat(row['学术报告(讲座类)']) || 0
    ];
}

function resolveAchievementCntValues(row) {
    if (!row) {
        return [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    }
    if (row['横向课题科研项目'] !== undefined || row['成果转化'] !== undefined || row['纵向科研项目-校级以上'] !== undefined) {
        return extractCntValuesByChineseFields(row);
    }
    return extractCntValues(row);
}

/**
 * ============================================================
 * 页面1：科研工作量 → 堆叠柱状图（学院/科研处）
 * 显示各教研室的10大类积分堆叠对比
 * ============================================================
 */
function renderCollegeChart($table) {
    var chartDom = document.getElementById('collegeChart');
    if (!chartDom) return;
    var chart = echarts.init(chartDom);
    var result = resolveCrossPageRows($table, function(r) { return r.deptName; }, true);
    if (!result.fromCrossPage) {
        result = resolveDisplayRows($table, function(r) { return r.deptName; }, true);
    }
    var selectedRows = result.rows;
    var truncated = result.truncated;

    if (!selectedRows || selectedRows.length === 0) {
        chart.setOption({
            title: { text: '请跨页勾选教研室进行对比', left: 10, top: 5, textStyle: { fontSize: 14, color: '#909399' } },
            grid: { left: '2%', right: '2%', bottom: '10%', top: '22%', containLabel: true }
        });
        return;
    }

    var cntLabel = result.selectedCount > 0 ? result.selectedCount : selectedRows.length;
    var titleText;
    if (cntLabel === 1) {
        titleText = '已勾选1个教研室（可跨页勾选多个进行对比）';
    } else if (truncated) {
        titleText = '已勾选' + cntLabel + '个教研室，显示总分前 ' + MAX_CHART_ITEMS + ' 个';
    } else {
        titleText = cntLabel + '个教研室科研积分对比';
    }
    var deptNames = [];
    var hxktS = [], cgzhS = [], zxktxjysS = [], zxktxjS = [], xslwS = [], jczzS = [];
    var zlS = [], rzS = [], jlS = [], jzbgS = [];

    for (var i = 0; i < selectedRows.length; i++) {
        var r = selectedRows[i];
        deptNames.push(r.deptName || ('教研室' + (i + 1)));
        var vals = extractScoreValues(r);
        hxktS.push(vals[0]); cgzhS.push(vals[1]); zxktxjysS.push(vals[2]); zxktxjS.push(vals[3]);
        xslwS.push(vals[4]); jczzS.push(vals[5]); zlS.push(vals[6]); rzS.push(vals[7]);
        jlS.push(vals[8]); jzbgS.push(vals[9]);
    }

    chart.setOption({
        title: { text: titleText, left: 10, top: 5, textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, confine: true,
            formatter: function(params) {
                var total = 0;
                for (var i = 0; i < params.length; i++) total += params[i].value || 0;
                var html = params[0].axisValue + '<br/><strong>总分：' + total.toFixed(2) + '</strong><br/>';
                for (var i = 0; i < params.length; i++) {
                    if (params[i].value > 0) {
                        html += params[i].marker + ' ' + params[i].seriesName
                            + '：' + params[i].value.toFixed(2) + '<br/>';
                    }
                }
                return html;
            }
        },
        legend: { type: 'scroll', bottom: 0, textStyle: { fontSize: 9 }, pageIconSize: 10, pageTextStyle: { fontSize: 9 } },
        grid: { left: '10%', right: '4%', bottom: '20%', top: '15%', containLabel: true },
        xAxis: { type: 'category', data: deptNames, axisLabel: { rotate: 30, fontSize: 10, interval: 0 } },
        yAxis: { type: 'value', name: '积分', nameTextStyle: { padding: [0, 0, 0, 10] } },
        series: [
            { name: '横向课题', type: 'bar', stack: 'total', data: hxktS },
            { name: '成果转化', type: 'bar', stack: 'total', data: cgzhS },
            { name: '纵向校级以上', type: 'bar', stack: 'total', data: zxktxjysS },
            { name: '纵向校级', type: 'bar', stack: 'total', data: zxktxjS },
            { name: '学术论文', type: 'bar', stack: 'total', data: xslwS },
            { name: '教材著作', type: 'bar', stack: 'total', data: jczzS },
            { name: '专利', type: 'bar', stack: 'total', data: zlS },
            { name: '软著', type: 'bar', stack: 'total', data: rzS },
            { name: '奖励', type: 'bar', stack: 'total', data: jlS },
            { name: '讲座报告', type: 'bar', stack: 'total', data: jzbgS }
        ]
    });
}

/**
 * 绘制教师雷达图（教研室/我的科研工作量页面）
 */
function renderTeacherRadar($table) {
    var chartDom = document.getElementById('teacherRadar');
    if (!chartDom) return;
    var chart = _getOrCreateChart(chartDom);
    _clearChart(chart);
    _bindChartResize(chartDom, chart);
    var result = resolveCrossPageRows($table, function(r) { return r.userName; }, true);
    if (!result.fromCrossPage) {
        result = resolveDisplayRows($table, function(r) { return r.userName; }, true);
    }
    var selectedRows = result.rows;
    var truncated = result.truncated;

    if (!selectedRows || selectedRows.length === 0) {
        chart.setOption({
            title: { text: '请勾选教师进行对比', left: 10, top: 5, textStyle: { fontSize: 12, color: '#909399' } }
        });
        return;
    }

    var cntLabel = result.selectedCount > 0 ? result.selectedCount : selectedRows.length;
    var titleText;
    if (truncated) {
        titleText = '已勾选多名教师，显示总分前 ' + MAX_CHART_ITEMS + ' 名（共勾选' + cntLabel + '人）';
    } else {
        titleText = cntLabel + '名教师科研能力对比';
    }

    // 计算全局最大值，所有维度使用统一的max，确保数据差异真实反映
    var globalMax = 0;
    for (var j = 0; j < selectedRows.length; j++) {
        var vals = extractScoreValues(selectedRows[j]);
        for (var k = 0; k < vals.length; k++) {
            if (vals[k] > globalMax) globalMax = vals[k];
        }
    }
    var unifiedMax = Math.max(globalMax * 1.2, 1);

    var indicator = [];
    for (var i = 0; i < SCORE_LABELS.length; i++) {
        indicator.push({ name: SCORE_LABELS[i], max: unifiedMax });
    }

    var seriesData = [];
    for (var j = 0; j < selectedRows.length; j++) {
        seriesData.push({
            value: extractScoreValues(selectedRows[j]),
            name: selectedRows[j].userName || ('教师' + (j + 1))
        });
    }

    chart.setOption({
        title: { text: titleText, left: 10, top: 4, textStyle: { fontSize: 13 } },
        tooltip: { confine: true },
        legend: { type: 'scroll', bottom: 2, left: 'center', right: 'center', textStyle: { fontSize: 10 }, pageIconSize: 10, pageTextStyle: { fontSize: 10 } },
        radar: {
            indicator: indicator,
            radius: '52%',
            center: ['50%', '52%'],
            nameGap: 10,
            splitNumber: 5,
            axisName: { color: '#606266', fontSize: 11, width: 68, overflow: 'break' }
        },
        series: [{
            type: 'radar',
            areaStyle: { opacity: 0.08 },
            lineStyle: { width: 2 },
            data: seriesData
        }]
    });
}

/**
 * 绘制教师横向条形图（教研室/我的科研工作量页面）
 */
function renderTeacherBar($table) {
    var chartDom = document.getElementById('teacherBar');
    if (!chartDom) return;
    var chart = _getOrCreateChart(chartDom);
    _clearChart(chart);
    _bindChartResize(chartDom, chart);
    var result = resolveCrossPageRows($table, function(r) { return r.userName; }, true);
    if (!result.fromCrossPage) {
        result = resolveDisplayRows($table, function(r) { return r.userName; }, true);
    }
    var selectedRows = result.rows;
    var truncated = result.truncated;

    if (!selectedRows || selectedRows.length === 0) {
        chart.setOption({
            title: { text: '请勾选教师进行对比', left: 10, top: 5, textStyle: { fontSize: 12, color: '#909399' } }
        });
        return;
    }

    var cntLabel = result.selectedCount > 0 ? result.selectedCount : selectedRows.length;
    var titleText;
    if (truncated) {
        titleText = '已勾选多名教师，显示总分前 ' + MAX_CHART_ITEMS + ' 名（共勾选' + cntLabel + '人）';
    } else {
        titleText = cntLabel + '名教师科研明细对比';
    }

    var names = [];
    var hxktS = [], cgzhS = [], zxktxjysS = [], zxktxjS = [], xslwS = [], jczzS = [];
    var zlS = [], rzS = [], jlS = [], jzbgS = [];

    for (var i = 0; i < selectedRows.length; i++) {
        var r = selectedRows[i];
        names.push(r.userName || r.deptName || ('教研室' + (i + 1)));
        var vals = extractScoreValues(r);
        hxktS.push(vals[0]); cgzhS.push(vals[1]); zxktxjysS.push(vals[2]); zxktxjS.push(vals[3]);
        xslwS.push(vals[4]); jczzS.push(vals[5]); zlS.push(vals[6]); rzS.push(vals[7]);
        jlS.push(vals[8]); jzbgS.push(vals[9]);
    }

    chart.setOption({
        title: { text: titleText, left: 10, top: 4, textStyle: { fontSize: 13 } },
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, confine: true },
        legend: {
            type: 'scroll',
            bottom: 2,
            left: 'center',
            right: 'center',
            textStyle: { fontSize: 10 },
            pageIconSize: 10,
            pageTextStyle: { fontSize: 10 }
        },
        grid: { left: 80, right: 24, bottom: 72, top: 38, containLabel: false },
        xAxis: {
            type: 'value',
            name: '积分',
            nameTextStyle: { fontSize: 11, padding: [0, 0, 0, 8] },
            axisLabel: { fontSize: 10 }
        },
        yAxis: {
            type: 'category',
            data: names.reverse(),
            axisLabel: { fontSize: 11, width: 68, overflow: 'truncate' }
        },
        series: [
            { name: '横向课题', type: 'bar', data: hxktS.reverse(), barMaxWidth: 16 },
            { name: '成果转化', type: 'bar', data: cgzhS.reverse(), barMaxWidth: 16 },
            { name: '纵向校级以上', type: 'bar', data: zxktxjysS.reverse(), barMaxWidth: 16 },
            { name: '纵向校级', type: 'bar', data: zxktxjS.reverse(), barMaxWidth: 16 },
            { name: '学术论文', type: 'bar', data: xslwS.reverse(), barMaxWidth: 16 },
            { name: '教材著作', type: 'bar', data: jczzS.reverse(), barMaxWidth: 16 },
            { name: '专利', type: 'bar', data: zlS.reverse(), barMaxWidth: 16 },
            { name: '软著', type: 'bar', data: rzS.reverse(), barMaxWidth: 16 },
            { name: '奖励', type: 'bar', data: jlS.reverse(), barMaxWidth: 16 },
            { name: '讲座报告', type: 'bar', data: jzbgS.reverse(), barMaxWidth: 16 }
        ]
    });
}

/**
 * ============================================================
 * 页面2：科研任务计划 → 旭日图
 * 内圈=10个大类，外圈=各小类，环的大小代表个数
 * ============================================================
 */
var PLAN_CATEGORY_MAP = {
    'zxktxjys': { name: '纵向-校级以上', fields: ['zcgjjjkyxm','zcsbjjjkyxm','zcsbjjgxm','zcsbjzxkyxm','zctjjxhjkyxm'],
                  labels: ['国家基金','省部基金','省部教改','省部纵向','厅局学会'] },
    'zxktxj':   { name: '纵向-校级', fields: ['zcxjjxglxmywyys','zcxjjxglxmywyyx'],
                  labels: ['校级1万以上','校级1万以下'] },
    'hxkt':     { name: '横向课题', fields: ['ewyyx','edwwy','wdswy','sdeswy','esdsswwy','wswwydwswy','wsdqswwy','qswdybwy'],
                  labels: ['2万以下','2-5万','5-10万','10-20万','20-35万','35-50万','50-75万','75-100万'] },
    'cgzh':     { name: '成果转化', fields: ['eyxxx','edwxx','wdsxx','sdesxx','esdsswxx','sswdwsxx','dywsxx'],
                  labels: ['2万以下','2-5万','5-10万','10-20万','20-35万','35-50万','50万以上'] },
    'xslw':     { name: '学术论文', fields: ['SCI','EI','hx','sw','pt','xb'],
                  labels: ['SCI','EI','核心','三网','普通','校办'] },
    'jczz':     { name: '教材著作', fields: ['cbzz1','cbzz2','cbyz1','cbyz2','cbjc1','cbjc2','zbjc'],
                  labels: ['专著一类','专著二类','译著一类','译著二类','教材国规','教材','自编'] },
    'zl':       { name: '专利', fields: ['sqfmzl','syxxzl','wxsjzl'],
                  labels: ['发明专利','实用新型','外观设计'] },
    'rz':       { name: '软著', fields: ['jsjrjzzq'],
                  labels: ['软件著作权'] },
    'jl':       { name: '奖励', fields: ['sjjxcgj','sjkxjsj','stjjzrkxlpj','stjjsklpj','xjjxcgj','yyxkyxjcgj','xhjjxcgj','xhjkycgj'],
                  labels: ['省教学成果','省科技奖','省精品课','市自然奖','校教学成果','校科研奖','学会教学成果','学会科研成果'] },
    'jzbg':     { name: '讲座报告', fields: ['jbgj','jbgn','cjgj','cjgn','xjxs'],
                  labels: ['举办国际','举办国内','参加国际','参加国内','校级学术'] }
};

/** 获取行数据中指定字段的纯数字值 */
function getIntField(row, field) {
    if (row[field] === undefined || row[field] === null) return 0;
    if (typeof row[field] === 'number') return row[field];
    var cleaned = String(row[field]).replace(/个/g, '').trim();
    return parseInt(cleaned, 10) || 0;
}

/** 构建旭日图数据 */
function buildSunburstData(rows) {
    var rootName = '科研项目';
    var rootValue = 0;
    var children = [];

    for (var catKey in PLAN_CATEGORY_MAP) {
        var cat = PLAN_CATEGORY_MAP[catKey];
        var catTotal = 0;
        var subChildren = [];

        for (var f = 0; f < cat.fields.length; f++) {
            var fieldVal = 0;
            for (var r = 0; r < rows.length; r++) {
                fieldVal += getIntField(rows[r], cat.fields[f]);
            }
            if (fieldVal > 0) {
                subChildren.push({ name: cat.labels[f] + ':' + fieldVal, value: fieldVal });
                catTotal += fieldVal;
            }
        }

        if (catTotal > 0) {
            children.push({
                name: cat.name + ' (' + catTotal + ')',
                value: catTotal,
                children: subChildren.length > 0 ? subChildren : undefined
            });
            rootValue += catTotal;
        }
    }

    return { name: rootName + ' (' + rootValue + ')', children: children };
}

function buildPlanCategoryTotals(row) {
    var totals = [];
    for (var catKey in PLAN_CATEGORY_MAP) {
        if (Object.prototype.hasOwnProperty.call(PLAN_CATEGORY_MAP, catKey)) {
            var cat = PLAN_CATEGORY_MAP[catKey];
            var total = 0;
            for (var i = 0; i < cat.fields.length; i++) {
                total += getIntField(row, cat.fields[i]);
            }
            totals.push(total);
        }
    }
    return totals;
}

function resolvePlanChartRows($table) {
    var allData = _filterStatisticRows($table.bootstrapTable('getData'));
    if (!allData || allData.length === 0) {
        return { rows: [], fromCrossPage: false, selectedCount: 0, truncated: false };
    }

    var result = resolveCrossPageRows($table, function(r) { return r.deptId || r.deptName || r.parentName; }, true);
    var selectedRows = _filterStatisticRows($table.bootstrapTable('getSelections'));
    var rows = result.fromCrossPage ? _filterStatisticRows(result.rows) : selectedRows;

    if (!rows || rows.length === 0) {
        var displayRows = resolveDisplayRows($table, function(r) { return r.deptId || r.deptName || r.parentName; }, true);
        rows = _filterStatisticRows(displayRows.rows);
        return {
            rows: rows,
            fromCrossPage: false,
            selectedCount: rows.length,
            truncated: displayRows.truncated
        };
    }

    return {
        rows: rows,
        fromCrossPage: result.fromCrossPage,
        selectedCount: result.selectedCount || rows.length,
        truncated: result.truncated
    };
}

function renderPlanCollegeRadar($table) {
    var chartDom = document.getElementById('planRadar');
    if (!chartDom) return;
    var chart = _getOrCreateChart(chartDom);
    _clearChart(chart);
    _bindChartResize(chartDom);

    var result = resolvePlanChartRows($table);
    var rows = result.rows;
    if (!rows || rows.length === 0) {
        chart.setOption({
            title: { text: '暂无科研任务计划数据', left: 10, top: 5, textStyle: { fontSize: 12, color: '#909399' } }
        });
        return;
    }

    // 计算全局最大值，所有维度使用统一的max，确保数据差异真实反映
    var globalMax = 0;
    for (var j = 0; j < rows.length; j++) {
        var totals = buildPlanCategoryTotals(rows[j]);
        for (var k = 0; k < totals.length; k++) {
            if (totals[k] > globalMax) globalMax = totals[k];
        }
    }
    var unifiedMax = Math.max(globalMax + 1, 1);

    var indicator = [];
    for (var i = 0; i < SCORE_LABELS.length; i++) {
        indicator.push({ name: SCORE_LABELS[i], max: unifiedMax });
    }

    var seriesData = [];
    for (var k = 0; k < rows.length; k++) {
        seriesData.push({
            value: buildPlanCategoryTotals(rows[k]),
            name: rows[k].deptName || rows[k].parentName || ('教研室' + (k + 1))
        });
    }

    var titleText = '学院科研任务计划雷达图';
    if (result.fromCrossPage && result.selectedCount > 0) {
        titleText += '（已跨页勾选' + result.selectedCount + '项）';
    } else if (result.truncated) {
        titleText += '（显示前' + MAX_CHART_ITEMS + '项）';
    }

    chart.setOption({
        title: { text: titleText, left: 10, top: 4, textStyle: { fontSize: 12 } },
        tooltip: { confine: true },
        legend: { type: 'scroll', bottom: 2, left: 'center', right: 'center', textStyle: { fontSize: 10 }, pageIconSize: 10, pageTextStyle: { fontSize: 10 } },
        radar: {
            indicator: indicator,
            radius: '58%',
            center: ['50%', '55%'],
            nameGap: 28,
            splitNumber: 5,
            axisName: { color: '#606266', fontSize: 10, lineHeight: 16, padding: [0, 0, 0, 8] }
        },
        series: [{
            type: 'radar',
            areaStyle: { opacity: 0.08 },
            lineStyle: { width: 2 },
            data: seriesData
        }]
    });
}

/**
 * 绘制教师科研任务计划雷达图（教研室页面）
 * 按教师维度对比各计划类别的数量分布
 */
function renderPlanTeacherRadar($table) {
    var chartDom = document.getElementById('planTeacherRadar');
    if (!chartDom) return;
    var chart = _getOrCreateChart(chartDom);
    _clearChart(chart);
    _bindChartResize(chartDom);

    var allData = _filterStatisticRows($table.bootstrapTable('getData'));
    if (!allData || allData.length === 0) {
        chart.setOption({
            title: { text: '暂无数据', left: 10, top: 5, textStyle: { fontSize: 12, color: '#909399' } }
        });
        return;
    }

    var result = resolveCrossPageRows($table, function(r) { return r.userName; }, true);
    if (!result.fromCrossPage) {
        result = resolveDisplayRows($table, function(r) { return r.userName; }, true);
    }
    var selectedRows = result.rows;
    var truncated = result.truncated;

    var rows;
    var cntLabel;
    var titleText;
    if (selectedRows && selectedRows.length > 0) {
        rows = selectedRows;
        cntLabel = result.selectedCount > 0 ? result.selectedCount : selectedRows.length;
        titleText = truncated ? '已勾选多名教师，显示前 ' + MAX_CHART_ITEMS + ' 名（共勾选' + cntLabel + '人）' : cntLabel + '名教师科研任务计划对比';
    } else {
        rows = allData;
        titleText = '全部教师科研任务计划';
        if (rows.length > MAX_CHART_ITEMS) {
            rows = rows.slice(0, MAX_CHART_ITEMS);
            titleText = '全体教师科研任务计划（显示前' + MAX_CHART_ITEMS + '项）';
        }
    }

    var globalMax = 0;
    for (var j = 0; j < rows.length; j++) {
        var totals = buildPlanCategoryTotals(rows[j]);
        for (var k = 0; k < totals.length; k++) {
            if (totals[k] > globalMax) globalMax = totals[k];
        }
    }
    var unifiedMax = Math.max(globalMax + 1, 1);

    var indicator = [];
    for (var i = 0; i < SCORE_LABELS.length; i++) {
        indicator.push({ name: SCORE_LABELS[i], max: unifiedMax });
    }

    var seriesData = [];
    for (var k = 0; k < rows.length; k++) {
        seriesData.push({
            value: buildPlanCategoryTotals(rows[k]),
            name: rows[k].userName || ('教师' + (k + 1))
        });
    }

    chart.setOption({
        title: { text: titleText, left: 10, top: 4, textStyle: { fontSize: 12 } },
        tooltip: { confine: true },
        legend: { type: 'scroll', bottom: 2, left: 'center', right: 'center', textStyle: { fontSize: 10 }, pageIconSize: 10, pageTextStyle: { fontSize: 10 } },
        radar: {
            indicator: indicator,
            radius: '58%',
            center: ['50%', '55%'],
            nameGap: 28,
            splitNumber: 5,
            axisName: { color: '#606266', fontSize: 10, lineHeight: 16, padding: [0, 0, 0, 8] }
        },
        series: [{
            type: 'radar',
            areaStyle: { opacity: 0.08 },
            lineStyle: { width: 2 },
            data: seriesData
        }]
    });
}

function renderPlanCollegeBar($table) {
    var chartDom = document.getElementById('planBar');
    if (!chartDom) return;
    var chart = _getOrCreateChart(chartDom);
    _clearChart(chart);
    _bindChartResize(chartDom);

    var result = resolvePlanChartRows($table);
    var rows = result.rows;
    if (!rows || rows.length === 0) {
        chart.setOption({
            title: { text: '暂无科研任务计划数据', left: 10, top: 5, textStyle: { fontSize: 12, color: '#909399' } }
        });
        return;
    }

    var names = [];
    var seriesData = [[], [], [], [], [], [], [], [], [], []];
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        names.push(row.deptName || row.parentName || ('教研室' + (i + 1)));
        var totals = buildPlanCategoryTotals(row);
        for (var j = 0; j < totals.length; j++) {
            seriesData[j].push(totals[j]);
        }
    }

    var titleText = '学院科研任务计划条形图';
    if (result.fromCrossPage && result.selectedCount > 0) {
        titleText += '（已跨页勾选' + result.selectedCount + '项）';
    } else if (result.truncated) {
        titleText += '（显示前' + MAX_CHART_ITEMS + '项）';
    }

    var series = [];
    for (var k = 0; k < SCORE_LABELS.length; k++) {
        series.push({
            name: SCORE_LABELS[k],
            type: 'bar',
            data: seriesData[k],
            barMaxWidth: 16
        });
    }

    chart.setOption({
        title: { text: titleText, left: 10, top: 4, textStyle: { fontSize: 12 } },
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, confine: true },
        legend: { type: 'scroll', bottom: 2, left: 'center', right: 'center', textStyle: { fontSize: 9 }, pageIconSize: 10, pageTextStyle: { fontSize: 9 } },
        grid: { left: '8%', right: '3%', bottom: '28%', top: '18%', containLabel: true },
        xAxis: { type: 'category', data: names, axisLabel: { rotate: 30, fontSize: 9, interval: 0, margin: 4 } },
        yAxis: { type: 'value', name: '数量', nameTextStyle: { fontSize: 10, padding: [0, 0, 0, 8] }, axisLabel: { fontSize: 9 } },
        series: series
    }, { notMerge: true });
}

/**
 * 绘制教师科研任务计划条形图（教研室页面）
 * 按教师维度展示各计划类别的数量堆叠
 */
function renderPlanTeacherBar($table) {
    var chartDom = document.getElementById('planTeacherBar');
    if (!chartDom) return;
    var chart = _getOrCreateChart(chartDom);
    _clearChart(chart);
    _bindChartResize(chartDom);

    var allData = _filterStatisticRows($table.bootstrapTable('getData'));
    if (!allData || allData.length === 0) {
        chart.setOption({
            title: { text: '暂无数据', left: 10, top: 5, textStyle: { fontSize: 12, color: '#909399' } }
        });
        return;
    }

    var result = resolveCrossPageRows($table, function(r) { return r.userName; }, true);
    if (!result.fromCrossPage) {
        result = resolveDisplayRows($table, function(r) { return r.userName; }, true);
    }
    var selectedRows = result.rows;
    var truncated = result.truncated;

    var rows;
    var cntLabel;
    var titleText;
    if (selectedRows && selectedRows.length > 0) {
        rows = selectedRows;
        cntLabel = result.selectedCount > 0 ? result.selectedCount : selectedRows.length;
        titleText = truncated ? '已勾选多名教师，显示前 ' + MAX_CHART_ITEMS + ' 名（共勾选' + cntLabel + '人）' : cntLabel + '名教师科研任务计划明细对比';
    } else {
        rows = allData;
        titleText = '全体教师科研任务计划明细';
        if (rows.length > MAX_CHART_ITEMS) {
            rows = rows.slice(0, MAX_CHART_ITEMS);
            titleText = '全体教师科研任务计划明细（显示前' + MAX_CHART_ITEMS + '项）';
        }
    }

    var names = [];
    var seriesData = [[], [], [], [], [], [], [], [], [], []];
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        names.push(row.userName || ('教师' + (i + 1)));
        var totals = buildPlanCategoryTotals(row);
        for (var j = 0; j < totals.length; j++) {
            seriesData[j].push(totals[j]);
        }
    }

    var series = [];
    for (var k = 0; k < SCORE_LABELS.length; k++) {
        series.push({
            name: SCORE_LABELS[k],
            type: 'bar',
            data: seriesData[k],
            barMaxWidth: 16
        });
    }

    chart.setOption({
        title: { text: titleText, left: 10, top: 4, textStyle: { fontSize: 12 } },
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, confine: true },
        legend: { type: 'scroll', bottom: 2, left: 'center', right: 'center', textStyle: { fontSize: 9 }, pageIconSize: 10, pageTextStyle: { fontSize: 9 } },
        grid: { left: '8%', right: '3%', bottom: '28%', top: '18%', containLabel: true },
        xAxis: { type: 'category', data: names, axisLabel: { rotate: 30, fontSize: 9, interval: 0, margin: 4 } },
        yAxis: { type: 'value', name: '数量', nameTextStyle: { fontSize: 10, padding: [0, 0, 0, 8] }, axisLabel: { fontSize: 9 } },
        series: series
    }, { notMerge: true });
}

function renderPlanSunburst($table) {
    var chartDom = document.getElementById('planChart');
    if (!chartDom) return;
    var chart = _getOrCreateChart(chartDom);
    _clearChart(chart);
    _bindChartResize(chartDom, chart);

    var allData = $table.bootstrapTable('getData');
    if (!allData || allData.length === 0) {
        chart.setOption({
            title: { text: '暂无数据', left: 10, top: 5, textStyle: { fontSize: 14, color: '#909399' } }
        });
        return;
    }

    var result = resolveCrossPageRows($table, function(r) { return r.userId || r.userName || r.deptId || r.deptName || r.parentName; }, false);
    var selectedRows = $table.bootstrapTable('getSelections');
    var rows = result.fromCrossPage ? result.rows : ((selectedRows && selectedRows.length > 0) ? selectedRows : allData);

    var sunburstData = buildSunburstData(rows);
    if (!sunburstData.children || sunburstData.children.length === 0) {
        chart.setOption({
            title: { text: '暂无分类数据', left: 10, top: 5, textStyle: { fontSize: 14, color: '#909399' } }
        });
        return;
    }

    chart.setOption({
        title: { text: '科研任务计划分布', left: 10, top: 2, textStyle: { fontSize: 11 } },
        tooltip: {
            trigger: 'item',
            formatter: function(p) {
                if (p.treePathInfo) {
                    var path = '';
                    for (var i = 0; i < p.treePathInfo.length; i++) {
                        path += p.treePathInfo[i].name + ' / ';
                    }
                    return path + '<br/><strong>' + p.value + ' 个</strong>';
                }
                return p.name + '<br/>' + p.value + ' 个';
            }
        },
        series: [{
            type: 'sunburst',
            data: [sunburstData],
            radius: ['0%', '88%'],
            center: ['50%', '54%'],
            sort: 'desc',
            emphasis: { focus: 'ancestor' },
            levels: [
                {},
                { r0: '8%', r: '35%', label: { rotate: 'radial', fontSize: 8 } },
                { r0: '35%', r: '65%', label: { rotate: 'tangential', fontSize: 7 } }
            ],
            label: { fontSize: 8, rotate: 'radial' }
        }]
    });
}

/**
 * 渲染学院版旭日图（按教研室聚合）
 */
function renderPlanCollegeSunburst($table) {
    var chartDom = document.getElementById('planChart');
    if (!chartDom) return;
    var chart = _getOrCreateChart(chartDom);
    _clearChart(chart);
    _bindChartResize(chartDom);

    var allData = _filterStatisticRows($table.bootstrapTable('getData'));
    if (!allData || allData.length === 0) {
        chart.setOption({
            title: { text: '暂无数据', left: 10, top: 5, textStyle: { fontSize: 14, color: '#909399' } }
        });
        return;
    }

    var result = resolveCrossPageRows($table, function(r) { return r.deptId || r.deptName || r.parentName; }, false);
    var selectedRows = _filterStatisticRows($table.bootstrapTable('getSelections'));
    var rows = result.fromCrossPage ? result.rows : ((selectedRows && selectedRows.length > 0) ? selectedRows : allData);
    var sunburstData = buildSunburstData(rows);

    if (!sunburstData.children || sunburstData.children.length === 0) {
        chart.setOption({
            title: { text: '暂无分类数据', left: 10, top: 5, textStyle: { fontSize: 14, color: '#909399' } }
        });
        return;
    }

    var titleText;
    if (result.fromCrossPage && result.selectedCount > 0) {
        titleText = '学院科研任务计划分布（已跨页勾选' + result.selectedCount + '项）';
    } else if (selectedRows && selectedRows.length > 0) {
        titleText = '学院科研任务计划分布（当前页已勾选' + selectedRows.length + '项）';
    } else {
        titleText = '学院科研任务计划分布';
    }

    chart.setOption({
        title: { text: titleText, left: 10, top: 5, textStyle: { fontSize: 14 } },
        tooltip: {
            trigger: 'item',
            formatter: function(p) {
                if (p.treePathInfo) {
                    var path = '';
                    for (var i = 0; i < p.treePathInfo.length; i++) {
                        path += p.treePathInfo[i].name + ' / ';
                    }
                    return path + '<br/><strong>' + p.value + ' 个</strong>';
                }
                return p.name + '<br/>' + p.value + ' 个';
            }
        },
        series: [{
            type: 'sunburst',
            data: [sunburstData],
            radius: ['0%', '90%'],
            center: ['50%', '55%'],
            sort: 'desc',
            emphasis: { focus: 'ancestor' },
            levels: [
                {},
                { r0: '10%', r: '40%', label: { rotate: 'radial', fontSize: 9 } },
                { r0: '40%', r: '70%', label: { rotate: 'tangential', fontSize: 8 } }
            ],
            label: { fontSize: 9, rotate: 'radial' }
        }]
    });
}

/**
 * ============================================================
 * 页面3：业绩成果数 → 南丁格尔玫瑰图
 * 展示10大类成果的个数分布
 * ============================================================
 */
function renderAchievementRose($table) {
    var chartDom = document.getElementById('achievementChart');
    if (!chartDom) return;
    var chart = _getOrCreateChart(chartDom);
    _clearChart(chart);
    _bindChartResize(chartDom);

    var allData = _filterStatisticRows($table.bootstrapTable('getData'));
    if (!allData || allData.length === 0) {
        chart.setOption({
            title: { text: '暂无数据', left: 10, top: 5, textStyle: { fontSize: 14, color: '#909399' } }
        }, { notMerge: true });
        return;
    }

    var totals = [0,0,0,0,0,0,0,0,0,0];
    for (var i = 0; i < allData.length; i++) {
        var vals = resolveAchievementCntValues(allData[i]);
        for (var j = 0; j < vals.length; j++) totals[j] += vals[j];
    }

    var data = [];
    for (var j = 0; j < SCORE_LABELS.length; j++) {
        if (totals[j] > 0) {
            data.push({ name: SCORE_LABELS[j], value: totals[j] });
        }
    }

    if (data.length === 0) {
        chart.setOption({
            title: { text: '无成果数据', left: 10, top: 5, textStyle: { fontSize: 14, color: '#909399' } }
        });
        return;
    }

    chart.setOption({
        title: { text: '成果总体分布', left: 10, top: 2, textStyle: { fontSize: 11 } },
        tooltip: {
            trigger: 'item',
            formatter: function(p) {
                return p.name + '<br/>个数：<strong>' + p.value + '</strong><br/>占比：' + p.percent + '%';
            }
        },
        legend: { type: 'scroll', bottom: 0, textStyle: { fontSize: 8 }, itemHeight: 8, itemWidth: 12 },
        series: [{
            type: 'pie',
            radius: ['12%', '62%'],
            center: ['50%', '50%'],
            roseType: 'area',
            minAngle: 10,
            label: { formatter: '{b}\n{d}%', fontSize: 8 },
            data: data
        }]
    }, { notMerge: true });
}

/**
 * 业绩成果数分组柱状图（勾选对比）
 */
function renderAchievementBar($table) {
    var chartDom = document.getElementById('achievementBar');
    if (!chartDom) return;
    var chart = _getOrCreateChart(chartDom);
    _clearChart(chart);
    _bindChartResize(chartDom);

    var allData = _filterStatisticRows($table.bootstrapTable('getData'));
    if (!allData || allData.length === 0) {
        chart.setOption({
            title: { text: '暂无成果数据', left: 10, top: 5, textStyle: { fontSize: 12, color: '#909399' } }
        }, { notMerge: true });
        return;
    }

    var result = resolveCrossPageRows($table, function(r) { return r.userName || r.deptName || r.parentName; }, true);
    var selectedRows = _filterStatisticRows($table.bootstrapTable('getSelections'));
    var rows = result.fromCrossPage ? _filterStatisticRows(result.rows) : ((selectedRows && selectedRows.length > 0) ? selectedRows : allData);
    var truncated = result.fromCrossPage ? result.truncated : (rows.length > MAX_CHART_ITEMS);

    if (!result.fromCrossPage && rows.length > MAX_CHART_ITEMS) {
        rows = rows.slice(0, MAX_CHART_ITEMS);
    }

    var names, hxkt, cgzh, zxktxjys, zxktxj, xslw, jczz, zl, rz, jl, jzbg;
    names = [];
    hxkt = []; cgzh = []; zxktxjys = []; zxktxj = []; xslw = []; jczz = [];
    zl = []; rz = []; jl = []; jzbg = [];

    for (var i = 0; i < rows.length; i++) {
        var r = rows[i];
        names.push(r.userName || r.deptName || r.parentName || ('项' + (i + 1)));
        var v = resolveAchievementCntValues(r);
        hxkt.push(v[0]); cgzh.push(v[1]); zxktxjys.push(v[2]); zxktxj.push(v[3]);
        xslw.push(v[4]); jczz.push(v[5]); zl.push(v[6]); rz.push(v[7]);
        jl.push(v[8]); jzbg.push(v[9]);
    }

    var titleText;
    if (result.fromCrossPage && result.selectedCount > 0) {
        titleText = '成果对比（已跨页勾选' + result.selectedCount + '项）';
    } else if (selectedRows && selectedRows.length > 0) {
        titleText = '成果对比（当前页已勾选' + selectedRows.length + '项）';
    } else if (truncated) {
        titleText = '成果对比（默认显示前' + MAX_CHART_ITEMS + '项）';
    } else {
        titleText = '成果对比';
    }

    chart.setOption({
        title: { text: titleText, left: 10, top: 2, textStyle: { fontSize: 11 } },
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, confine: true },
        legend: { type: 'scroll', bottom: 0, textStyle: { fontSize: 8 }, itemHeight: 8, itemWidth: 12 },
        grid: { left: '6%', right: '2%', bottom: '16%', top: '18%', containLabel: true },
        xAxis: { type: 'category', data: names, axisLabel: { rotate: 30, fontSize: 8, interval: 0 } },
        yAxis: { type: 'value', name: '个数', nameTextStyle: { fontSize: 10 }, axisLabel: { fontSize: 9 } },
        series: [
            { name: '横向课题', type: 'bar', data: hxkt, barMaxWidth: 16 },
            { name: '成果转化', type: 'bar', data: cgzh, barMaxWidth: 16 },
            { name: '纵向校级以上', type: 'bar', data: zxktxjys, barMaxWidth: 16 },
            { name: '纵向校级', type: 'bar', data: zxktxj, barMaxWidth: 16 },
            { name: '学术论文', type: 'bar', data: xslw, barMaxWidth: 16 },
            { name: '教材著作', type: 'bar', data: jczz, barMaxWidth: 16 },
            { name: '专利', type: 'bar', data: zl, barMaxWidth: 16 },
            { name: '软著', type: 'bar', data: rz, barMaxWidth: 16 },
            { name: '奖励', type: 'bar', data: jl, barMaxWidth: 16 },
            { name: '讲座报告', type: 'bar', data: jzbg, barMaxWidth: 16 }
        ]
    }, { notMerge: true });
}

/**
 * ============================================================
 * 科研处玫瑰图（已有，保留）
 * ============================================================
 */
var _collegeAllData = null;

function renderCollegeRose($table) {
    var chartDom = document.getElementById('collegeRose');
    if (!chartDom) return;
    var chart = echarts.init(chartDom);
    var result = resolveCrossPageRows($table, function(r) { return r.deptName || r.parentName; }, false);
    var selectedRows = _filterStatisticRows($table.bootstrapTable('getSelections'));

    var rows;
    var titlePrefix;
    if (result.fromCrossPage) {
        rows = result.rows;
        titlePrefix = '学院科研工作量分布（已跨页勾选' + result.selectedCount + '人）';
    } else if (selectedRows && selectedRows.length > 0) {
        rows = selectedRows;
        titlePrefix = '学院科研工作量分布（当前页已勾选）';
    } else if (_collegeAllData) {
        rows = _collegeAllData;
        titlePrefix = '学院科研工作量分布';
    } else {
        var summaryUrl = ($table.bootstrapTable('getOptions').url || '').replace(/\/list$/, '/collegeSummary');
        if (summaryUrl) {
            $.post(summaryUrl, function(res) {
                if (res && res.rows && res.rows.length > 0) {
                    _collegeAllData = res.rows;
                    renderCollegeRose($table);
                }
            });
        }
        return;
    }

    if (!rows || rows.length === 0) {
        chart.setOption({
            title: { text: titlePrefix, left: 10, top: 5, textStyle: { fontSize: 12 } }
        });
        return;
    }

    var titleText = titlePrefix;
    var collegeMap = {};
    for (var i = 0; i < rows.length; i++) {
        var r = rows[i];
        var name = r.parentName || '未知';
        if (!collegeMap[name]) collegeMap[name] = { count: 0, total: 0 };
        collegeMap[name].count++;
        collegeMap[name].total += parseFloat(r.__score || 0);
    }

    var data = [];
    for (var k in collegeMap) {
        data.push({ name: k, value: collegeMap[k].total });
    }

    chart.setOption({
        title: { text: titleText, left: 10, top: 5, textStyle: { fontSize: 12 } },
        tooltip: {
            trigger: 'item',
            formatter: function(p) {
                var info = collegeMap[p.name];
                return p.name + '<br/>教师人数：' + info.count + ' 人<br/>工作量合计：' + info.total.toFixed(1);
            }
        },
        legend: { type: 'scroll', bottom: 0, textStyle: { fontSize: 9 } },
        series: [{
            type: 'pie',
            radius: ['30%', '70%'],
            center: ['50%', '50%'],
            minAngle: 15,
            label: { formatter: '{b}\n{d}%' },
            data: data
        }]
    });
}
