/**
 * 通用方法封装处理
 * Copyright (c) 2019 ruoyi 
 */

var startLayDate;
var endLayDate;
var isScrollToTop = parent.isScrollToTop;

$(function() {
	
    // 回到顶部绑定
    if ($.fn.toTop !== undefined) {
        $('#scroll-up').toTop();
    }
	
    // select2复选框事件绑定
    if ($.fn.select2 !== undefined) {
        $.fn.select2.defaults.set( "theme", "bootstrap" );
        $("select.form-control:not(.noselect2)").each(function () {
            $(this).select2().on("change", function () {
                $(this).valid();
            })
        })
    }
	
    // iCheck单选框及复选框事件绑定
    if ($.fn.iCheck !== undefined) {
        $(".check-box:not(.noicheck),.radio-box:not(.noicheck)").each(function() {
            $(this).iCheck({
                checkboxClass: 'icheckbox-blue',
                radioClass: 'iradio-blue',
            })
        })
    }
	
    // 取消回车自动提交表单
    $(document).on("keypress", ":input:not(textarea):not([type=submit])", function(event) {
        if (event.keyCode == 13) {
            event.preventDefault();
        }
    });
	 
    // laydate 时间控件绑定
    if ($(".select-time").length > 0 && $('#startTime').length > 0 && $('#endTime').length > 0) {
       layui.use('laydate', function() {
            var laydate = layui.laydate;
            if (!laydate) {
                console.warn('laydate module not loaded properly');
                return;
            }
            startLayDate = laydate.render({
                elem: '#startTime',
                max: $('#endTime').val(),
                theme: 'molv',
                type: $('#startTime').attr("data-type") || 'date',
                trigger: 'click',
                done: function(value, date) {
                    // 结束时间大于开始时间
                    if (value !== '') {
                        endLayDate.config.min.year = date.year;
                        endLayDate.config.min.month = date.month - 1;
                        endLayDate.config.min.date = date.date;
                    } else {
                        endLayDate.config.min.year = '';
                        endLayDate.config.min.month = '';
                        endLayDate.config.min.date = '';
                    }
                    $('#endTime').trigger('click');
                }
            });
            endLayDate = laydate.render({
                elem: '#endTime',
                min: $('#startTime').val(),
                theme: 'molv',
                type: $('#endTime').attr("data-type") || 'date',
                trigger: 'click',
                done: function(value, date) {
                    // 开始时间小于结束时间
                    if (value !== '') {
                        startLayDate.config.max.year = date.year;
                        startLayDate.config.max.month = date.month - 1;
                        startLayDate.config.max.date = date.date;
                    } else {
                        startLayDate.config.max.year = '2099';
                        startLayDate.config.max.month = '12';
                        startLayDate.config.max.date = '31';
                    }
                }
            });
        });
    }
	
    // laydate time-input 时间控件绑定
    if ($(".time-input").length > 0) {
        layui.use('laydate', function () {
            var com = layui.laydate;
            if (!com) {
                console.warn('laydate module not loaded properly');
                return;
            }
            $(".time-input").each(function (index, item) {
                var time = $(item);
                // 控制控件外观
                var type = time.attr("data-type") || 'date';
                // 控制回显格式
                var format = time.attr("data-format") || 'yyyy-MM-dd';
                // 控制日期控件按钮
                var buttons = time.attr("data-btn") || 'clear|now|confirm', newBtnArr = [];
                // 日期控件选择完成后回调处理
                var callback = time.attr("data-callback") || {};
                if (buttons) {
                    if (buttons.indexOf("|") > 0) {
                        var btnArr = buttons.split("|"), btnLen = btnArr.length;
                        for (var j = 0; j < btnLen; j++) {
                            if ("clear" === btnArr[j] || "now" === btnArr[j] || "confirm" === btnArr[j]) {
                                newBtnArr.push(btnArr[j]);
                            }
                        }
                    } else {
                        if ("clear" === buttons || "now" === buttons || "confirm" === buttons) {
                            newBtnArr.push(buttons);
                        }
                    }
                } else {
                    newBtnArr = ['clear', 'now', 'confirm'];
                }
                com.render({
                    elem: item,
                    theme: 'molv',
                    trigger: 'click',
                    type: type,
                    format: format,
                    btns: newBtnArr,
                    done: function (value, data) {
                        if (typeof window[callback] != 'undefined'
                            && window[callback] instanceof Function) {
                            window[callback](value, data);
                        }
                    }
                });
            });
        });
    }
	
    // tree 关键字搜索绑定
    if ($("#keyword").length > 0) {
        $("#keyword").bind("focus", function focusKey(e) {
            if ($("#keyword").hasClass("empty")) {
                $("#keyword").removeClass("empty");
            }
        }).bind("blur", function blurKey(e) {
            if ($("#keyword").val() === "") {
                $("#keyword").addClass("empty");
            }
            $.tree.searchNode(e);
        }).bind("input propertychange", $.tree.searchNode);
    }
	
    // tree表格树 展开/折叠
    var expandFlag;
    $("#expandAllBtn").click(function() {
        var dataExpand = $.common.isEmpty(table.options.expandAll) ? true : table.options.expandAll;
        expandFlag = $.common.isEmpty(expandFlag) ? dataExpand : expandFlag;
        if (!expandFlag) {
            $.bttTable.bootstrapTreeTable('expandAll');
        } else {
            $.bttTable.bootstrapTreeTable('collapseAll');
        }
        expandFlag = expandFlag ? false: true;
    })
	
    // 按下ESC按钮关闭弹层
    $('body', document).on('keyup', function(e) {
        if (e.which === 27) {
            $.modal.closeAll();
        }
    });
    
    // 修复 modal aria-hidden 无障碍访问问题
    // 当 modal 打开时，确保正确处理 aria-hidden 属性
    $(document).on('shown.bs.modal', '.modal', function() {
        $(this).removeAttr('aria-hidden');
    }).on('hidden.bs.modal', '.modal', function() {
        $(this).attr('aria-hidden', 'true');
    });
});

(function ($) {
    'use strict';
    $.fn.toTop = function(opt) {
        var elem = this;
        var win = (opt && opt.hasOwnProperty('win')) ? opt.win : $(window);
        var doc = (opt && opt.hasOwnProperty('doc')) ? opt.doc : $('html, body');
        var options = $.extend({
            autohide: true,
            offset: 50,
            speed: 500,
            position: true,
            right: 15,
            bottom: 5
        }, opt);
        elem.css({
            'cursor': 'pointer'
        });
        if (options.autohide) {
            elem.css('display', 'none');
        }
        if (options.position) {
            elem.css({
                'position': 'fixed',
                'right': options.right,
                'bottom': options.bottom,
            });
        }
        elem.click(function() {
            doc.animate({
                scrollTop: 0
            }, options.speed);
        });
        win.scroll(function() {
            var scrolling = win.scrollTop();
            if (options.autohide) {
                if (scrolling > options.offset) {
                    elem.fadeIn(options.speed);
                } else elem.fadeOut(options.speed);
            }
        });
    };
})(jQuery);

/** 刷新选项卡 */
var refreshItem = function(){
    var topWindow = $(window.parent.document);
    var currentId = $('.page-tabs-content', topWindow).find('.active').attr('data-id');
    var target = $('.RuoYi_iframe[data-id="' + currentId + '"]', topWindow);
    var url = target.attr('src');
    target.attr('src', url).ready();
}

/** 关闭选项卡 */
var closeItem = function(dataId){
	var topWindow = $(window.parent.document);
	if ($.common.isNotEmpty(dataId)) {
	    window.parent.$.modal.closeLoading();
	    // 根据dataId关闭指定选项卡
	    $('.menuTab[data-id="' + dataId + '"]', topWindow).remove();
	    // 移除相应tab对应的内容区
	    $('.mainContent .RuoYi_iframe[data-id="' + dataId + '"]', topWindow).remove();
	    return;
	}
	var panelUrl = window.frameElement.getAttribute('data-panel');
	$('.page-tabs-content .active i', topWindow).click();
	if ($.common.isNotEmpty(panelUrl)) {
	    $('.menuTab[data-id="' + panelUrl + '"]', topWindow).addClass('active').siblings('.menuTab').removeClass('active');
	    $('.mainContent .RuoYi_iframe', topWindow).each(function() {
	        if ($(this).data('id') == panelUrl) {
	            openToCurrentTab(this);
	            return false;
            }
        });
    }
}

/** 创建选项卡 */
function createMenuItem(dataUrl, menuName, isRefresh) {
    var panelUrl = window.frameElement.getAttribute('data-id'),
    dataIndex = $.common.random(1, 100),
    flag = true;
    if (dataUrl == undefined || $.trim(dataUrl).length == 0) return false;
    var topWindow = $(window.parent.document);
    // 选项卡菜单已存在
    $('.menuTab', topWindow).each(function() {
        if ($(this).data('id') == dataUrl) {
            if (!$(this).hasClass('active')) {
                $(this).addClass('active').siblings('.menuTab').removeClass('active');
                scrollToTab(this);
                $('.page-tabs-content').animate({ marginLeft: ""}, "fast");
                // 显示tab对应的内容区
                $('.mainContent .RuoYi_iframe', topWindow).each(function() {
                    if ($(this).data('id') == dataUrl) {
                        openToCurrentTab(this);
                        return false;
                    }
                });
            }
            if (isRefresh) {
                refreshTab();
            }
            flag = false;
            return false;
        }
    });
    // 选项卡菜单不存在
    if (flag) {
        var str = '<a href="javascript:;" class="active menuTab noactive" data-id="' + dataUrl + '" data-panel="' + panelUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
        $('.menuTab', topWindow).removeClass('active');

        // 添加选项卡对应的iframe
        var str1 = '<iframe class="RuoYi_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" data-panel="' + panelUrl + '" seamless></iframe>';
        if (isScrollToTop) {
            $('.mainContent', topWindow).find('iframe.RuoYi_iframe').hide().parents('.mainContent').append(str1);
        } else {
            $('.mainContent', topWindow).find('iframe.RuoYi_iframe').css({"visibility": "hidden", "position": "absolute"}).parents('.mainContent').append(str1);
        }
        
        window.parent.$.modal.loading("数据加载中，请稍候...");
        $('.mainContent iframe:visible', topWindow).on('load', function() {
            window.parent.$.modal.closeLoading();
        });

        // 添加选项卡
        $('.menuTabs .page-tabs-content', topWindow).append(str);
        scrollToTab($('.menuTab.active', topWindow));
    }
    return false;
}

// 刷新iframe
    function refreshTab() {
        var topWindow = $(window.parent.document);
        var currentId = $('.page-tabs-content', topWindow).find('.active').attr('data-id');
        var target = $('.RuoYi_iframe[data-id="' + currentId + '"]', topWindow);
        var url = target.attr('src');
        target.attr('src', url).ready();
    }

// 滚动到指定选项卡
function scrollToTab(element) {
    var topWindow = $(window.parent.document);
    var marginLeftVal = calSumWidth($(element).prevAll()),
    marginRightVal = calSumWidth($(element).nextAll());
    // 可视区域非tab宽度
    var tabOuterWidth = calSumWidth($(".content-tabs", topWindow).children().not(".menuTabs"));
    //可视区域tab宽度
    var visibleWidth = $(".content-tabs", topWindow).outerWidth(true) - tabOuterWidth;
    //实际滚动宽度
    var scrollVal = 0;
    if ($(".page-tabs-content", topWindow).outerWidth() < visibleWidth) {
        scrollVal = 0;
    } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
        if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
            scrollVal = marginLeftVal;
            var tabElement = element;
            while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content", topWindow).outerWidth() - visibleWidth)) {
                scrollVal -= $(tabElement).prev().outerWidth();
                tabElement = $(tabElement).prev();
            }
        }
    } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
        scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
    }
    $('.page-tabs-content', topWindow).animate({ marginLeft: 0 - scrollVal + 'px' }, "fast");
}

// 计算元素集合的总宽度
function calSumWidth(elements) {
    var width = 0;
    $(elements).each(function() {
        width += $(this).outerWidth(true);
    });
    return width;
}

// 返回当前激活的Tab页面关联的iframe的Windows对象
function activeWindow() {
	var topWindow = $(window.parent.document);
	var currentId = $('.page-tabs-content', topWindow).find('.active').attr('data-id');
	if (!currentId) {
		return window.parent;
	}
    return $('.RuoYi_iframe[data-id="' + currentId + '"]', topWindow)[0].contentWindow;
}

function openToCurrentTab(obj) {

    if (isScrollToTop) {
        $(obj).show().siblings('.RuoYi_iframe').hide();
    } else {
        $(obj).css({"visibility": "visible", "position": "static"}).siblings('.RuoYi_iframe').css({"visibility": "hidden", "position": "absolute"});
    }

}

/** 密码规则范围验证 */
function checkpwd(chrtype, password) {
    if (chrtype == 1) {
        if (!$.common.numValid(password)) {
            $.modal.alertWarning("密码只能为0-9数字");
            return false;
        }
    } else if (chrtype == 2) {
        if (!$.common.enValid(password)) {
            $.modal.alertWarning("密码只能为a-z和A-Z字母");
            return false;
        }
    } else if (chrtype == 3) {
        if (!$.common.enNumValid(password)) {
            $.modal.alertWarning("密码必须包含字母以及数字");
            return false;
        }
    } else if (chrtype == 4) {
        if (!$.common.charValid(password)) {
            $.modal.alertWarning("密码必须包含字母、数字、以及特殊符号<font color='red'>~!@#$%^&*()-=_+</font>");
            return false;
        }
    }
    return true;
}

/** 开始时间/时分秒 */
function beginOfTime(date) {
    if ($.common.isNotEmpty(date)) {
        return $.common.sprintf("%s 00:00:00", date);
    }
}

/** 结束时间/时分秒 */
function endOfTime(date) {
    if ($.common.isNotEmpty(date)) {
        return $.common.sprintf("%s 23:59:59", date);
    }
}

/** 重置日期/年月日 */
function resetDate() {
	if ($.common.isNotEmpty(startLayDate) && $.common.isNotEmpty(endLayDate)) {
	    endLayDate.config.min.year = '';
	    endLayDate.config.min.month = '';
	    endLayDate.config.min.date = '';
	    startLayDate.config.max.year = '2099';
	    startLayDate.config.max.month = '12';
	    startLayDate.config.max.date = '31';
	}
}

// 日志打印封装处理
var log = {
    log: function(msg) {
        console.log(msg);
    },
    info: function(msg) {
        console.info(msg);
    },
    warn: function(msg) {
        console.warn(msg);
    },
    error: function(msg) {
        console.error(msg);
    }
};

// 本地缓存处理
var storage = {
    set: function(key, value) {
        window.localStorage.setItem(key, value);
    },
    get: function(key) {
        return window.localStorage.getItem(key);
    },
    remove: function(key) {
        window.localStorage.removeItem(key);
    },
    clear: function() {
        window.localStorage.clear();
    }
};

// 主子表操作封装处理
var sub = {
    editRow: function() {
    	var dataColumns = [];
		for (var columnIndex = 0; columnIndex < table.options.columns.length; columnIndex++) {
    		if (table.options.columns[columnIndex].visible != false) {
    			dataColumns.push(table.options.columns[columnIndex]);
    		}
    	}
		var params = new Array();
		var data = $("#" + table.options.id).bootstrapTable('getData');
    	var count = data.length;
    	for (var dataIndex = 0; dataIndex < count; dataIndex++) {
    	    var columns = $('#' + table.options.id + ' tr[data-index="' + dataIndex + '"] td:visible');
    	    var obj = new Object();
    	    for (var i = 0; i < columns.length; i++) {
    	        var inputValue = $(columns[i]).find('input');
    	        var selectValue = $(columns[i]).find('select');
    	        var textareaValue = $(columns[i]).find('textarea');
    	        var key = dataColumns[i].field;
    	        if ($.common.isNotEmpty(inputValue.val())) {
    	            obj[key] = inputValue.val();
    	        } else if ($.common.isNotEmpty(selectValue.val())) {
    	            obj[key] = selectValue.val();
    	        } else if ($.common.isNotEmpty(textareaValue.val())) {
    	            obj[key] = textareaValue.val();
    	        } else {
    	            if (key == "index" && $.common.isNotEmpty(data[dataIndex].index)) {
    	                obj[key] = data[dataIndex].index;
    	            } else {
    	                obj[key] = "";
    	            }
    	        }
    	    }
    	    var item = data[dataIndex];
    	    var extendObj = $.extend({}, item, obj);
    	    params.push({ index: dataIndex, row: extendObj });
    	}
    	$("#" + table.options.id).bootstrapTable("updateRow", params);
    },
    delRow: function(column) {
    	sub.editRow();
    	var subColumn = $.common.isEmpty(column) ? "index" : column;
    	var ids = $.table.selectColumns(subColumn);
        if (ids.length == 0) {
            $.modal.alertWarning("请至少选择一条记录");
            return;
        }
        $("#" + table.options.id).bootstrapTable('remove', { field: subColumn, values: ids });
    },
    delRowByIndex: function(value, tableId) {
    	var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
    	sub.editRow();
        $("#" + currentId).bootstrapTable('remove', { field: "index", values: [value] });
        sub.editRow();
    },
    addRow: function(row, tableId) {
    	var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
    	table.set(currentId);
    	var count = $("#" + currentId).bootstrapTable('getData').length;
    	sub.editRow();
    	$("#" + currentId).bootstrapTable('insertRow', { index: count + 1, row: row });
    }
};

// 动态加载css文件
function loadCss(file, headElem) {
    var link = document.createElement('link');
    link.href = file;
    link.rel = 'stylesheet';
    link.type = 'text/css';
    if (headElem) headElem.appendChild(link);
    else document.getElementsByTagName('head')[0].appendChild(link);
}

// 动态加载js文件
function loadJs(file, headElem) {
    var script = document.createElement('script');
    script.src = file;
    script.type = 'text/javascript';
    if (headElem) headElem.appendChild(script);
    else document.getElementsByTagName('head')[0].appendChild(script);
}

// 禁止后退键（Backspace）
window.onload = function() {
	document.getElementsByTagName("body")[0].onkeydown = function() {
		// 获取事件对象  
		var elem = event.relatedTarget || event.srcElement || event.target || event.currentTarget;
		// 判断按键为backSpace键  
		if (event.keyCode == 8) {
			// 判断是否需要阻止按下键盘的事件默认传递  
			var name = elem.nodeName;
			var className = elem.className;
			// 屏蔽特定的样式名称
			if (className.indexOf('note-editable') != -1)
			{
				return true;
			}
			if (name != 'INPUT' && name != 'TEXTAREA') {
				return _stopIt(event);
			}
			var type_e = elem.type.toUpperCase();
			if (name == 'INPUT' && (type_e != 'TEXT' && type_e != 'TEXTAREA' && type_e != 'PASSWORD' && type_e != 'FILE' && type_e != 'SEARCH' && type_e != 'NUMBER' && type_e != 'EMAIL' && type_e != 'URL')) {
				return _stopIt(event);
			}
			if (name == 'INPUT' && (elem.readOnly == true || elem.disabled == true)) {
				return _stopIt(event);
			}
		}
	};
};
function _stopIt(e) {
	if (e.returnValue) {
		e.returnValue = false;
	}
	if (e.preventDefault) {
		e.preventDefault();
	}
	return false;
}

/** 设置全局ajax处理 */
$.ajaxSetup({
    complete: function(XMLHttpRequest, textStatus) {
        if (textStatus == 'timeout') {
            $.modal.alertWarning("服务器超时，请稍后再试！");
            $.modal.enable();
            $.modal.closeLoading();
        } else if (textStatus == "parsererror" || textStatus == "error") {
            $.modal.alertWarning("服务器错误，请联系管理员！");
            $.modal.enable();
            $.modal.closeLoading();
        }
    }
});

/**
 * 科研分计算工具
 * 用于横向课题科研分实时计算
 */
var researchScore = {
    configData: null,
    tableName: "",
    initialized: false,

    init: function() {
        if (this.initialized) {
            return;
        }
        var contextPath = typeof ctx !== 'undefined' ? ctx : (typeof window.ctx !== 'undefined' ? window.ctx : '');
        var apiUrl = contextPath + "system/projectScoreCfg/getCfgCard";

        $.ajax({
            url: apiUrl,
            type: "POST",
            async: false,
            success: function(data) {
                if (data.code === "0" && data.data) {
                    researchScore.configData = data.data;
                    researchScore.tableName = "sci_project_score_cfg (横向课题得分配置表)";
                    researchScore.initialized = true;
                }
            },
            error: function(xhr, status, error) {
                console.error("获取科研分配置数据失败", error);
            }
        });
    },

    calculate: function(amount) {
        if (!amount || isNaN(amount) || amount < 0) {
            return {
                firstScore: 0,
                secondScore: 0,
                thirdScore: 0,
                fourthScore: 0,
                valid: false
            };
        }

        if (this.configData && this.configData.fundsList && this.configData.fundsList.length > 0) {
            var fundsList = this.configData.fundsList;

            for (var i = 0; i < fundsList.length; i++) {
                var fundsConfig = fundsList[i];
                var min = parseFloat(fundsConfig.funds_min);
                var max = fundsConfig.funds_max ? parseFloat(fundsConfig.funds_max) : null;

                if (amount >= min && (max === null || amount <= max)) {
                    var userScoreList = fundsConfig.userScoreList;
                    var rangeText = max === null ? (min + "万元及以上") : (min + "万元 - " + max + "万元");

                    var firstConfig = this.getUserScoreByOrder(userScoreList, 1);
                    var secondConfig = this.getUserScoreByOrder(userScoreList, 2);
                    var thirdConfig = this.getUserScoreByOrder(userScoreList, 3);
                    var fourthConfig = this.getUserScoreByOrder(userScoreList, 4);

                    return {
                        firstScore: firstConfig ? this.calculateScore(firstConfig, min, max, amount) : 0,
                        secondScore: secondConfig ? this.calculateScore(secondConfig, min, max, amount) : 0,
                        thirdScore: thirdConfig ? this.calculateScore(thirdConfig, min, max, amount) : 0,
                        fourthScore: fourthConfig ? this.calculateScore(fourthConfig, min, max, amount) : 0,
                        valid: true,
                        rangeText: rangeText,
                        tableName: this.tableName
                    };
                }
            }

            return this.calculateByDefault(amount);
        } else {
            return this.calculateByDefault(amount);
        }
    },

    calculateByDefault: function(amount) {
        var firstScore, secondScore, thirdScore, fourthScore;

        if (amount >= 100) {
            firstScore = 9880; secondScore = 4460; thirdScore = 1780; fourthScore = 880;
        } else if (amount >= 75) {
            firstScore = 7400; secondScore = 3360; thirdScore = 1340; fourthScore = 650;
        } else if (amount >= 50) {
            firstScore = 4360; secondScore = 1980; thirdScore = 780; fourthScore = 380;
        } else if (amount >= 35) {
            firstScore = 2860; secondScore = 1280; thirdScore = 520; fourthScore = 240;
        } else if (amount >= 20) {
            firstScore = 1520; secondScore = 680; thirdScore = 280; fourthScore = 120;
        } else if (amount >= 10) {
            firstScore = 440; secondScore = 200; thirdScore = 80; fourthScore = 40;
        } else if (amount >= 5) {
            firstScore = 220; secondScore = 100; thirdScore = 40; fourthScore = 20;
        } else if (amount >= 2) {
            firstScore = 80; secondScore = 30; thirdScore = 20; fourthScore = 10;
        } else if (amount > 0) {
            firstScore = Math.round(80 * (amount / 2));
            secondScore = Math.round(30 * (amount / 2));
            thirdScore = Math.round(20 * (amount / 2));
            fourthScore = Math.round(10 * (amount / 2));
        } else {
            firstScore = 0; secondScore = 0; thirdScore = 0; fourthScore = 0;
        }

        return {
            firstScore: firstScore,
            secondScore: secondScore,
            thirdScore: thirdScore,
            fourthScore: fourthScore,
            valid: true
        };
    },

    getUserScoreByOrder: function(userScoreList, order) {
        if (!userScoreList || userScoreList.length === 0) {
            return null;
        }
        for (var i = 0; i < userScoreList.length; i++) {
            var item = userScoreList[i];
            var userOrder = item.userOrder || item.user_order;
            if (parseInt(userOrder) === order) {
                return item;
            }
        }
        return null;
    },

    calculateScore: function(config, min, max, amount) {
        var totalScore = parseFloat(config.totalScore || config.total_score);
        var startScore = config.startScore || config.start_score;
        var endScore = config.endScore || config.end_score;
        startScore = startScore ? parseFloat(startScore) : null;
        endScore = endScore ? parseFloat(endScore) : null;

        if (totalScore !== null && !isNaN(totalScore)) {
            return Math.round(totalScore);
        } else if (startScore !== null && endScore !== null && !isNaN(startScore) && !isNaN(endScore)) {
            return Math.round((startScore + endScore) / 2);
        } else {
            return 0;
        }
    },

    updateDisplay: function(amount, scoreElementId) {
        var scoreElement = document.getElementById(scoreElementId || 'score-info');
        if (!scoreElement) return;

        if (!amount) {
            scoreElement.innerHTML = "项目金额未填写，科研分待计算";
            return;
        }

        var amountNum = parseFloat(amount);
        if (isNaN(amountNum) || amountNum < 0) {
            scoreElement.innerHTML = "项目金额格式错误";
            return;
        }

        var scores = this.calculate(amountNum);
        scoreElement.innerHTML =
            "预计科研分分配：<br>" +
            "主持人：" + scores.firstScore + " 分，" +
            "成员1：" + scores.secondScore + " 分，" +
            "成员2：" + scores.thirdScore + " 分，" +
            "成员3：" + scores.fourthScore + " 分<br>" +
            "<strong>注意：</strong>后续添加成员不参与科研分分配。";
    },

    getScoreByRanking: function(amount, ranking) {
        var scores = this.calculate(amount);
        switch(ranking) {
            case 1: return scores.firstScore;
            case 2: return scores.secondScore;
            case 3: return scores.thirdScore;
            case 4: return scores.fourthScore;
            default: return 0;
        }
    }
};

$(function() {
    researchScore.init();
});

/**
 * 批量下载功能
 * @param {string} module - 模块类型，如：'horizontal', 'vertical' 等
 * @param {string} tableId - 表格ID，用于获取选中的数据
 */
var batchDownloadInProgress = false;

function batchDownload(module, tableId) {
    var ids = $.table.selectColumns("id", false, tableId);
    
    if (!ids || ids.length === 0) {
        $.modal.alertWarning("未选择数据");
        return;
    }
    
    if (batchDownloadInProgress) {
        $.modal.alertWarning("批量下载正在处理中，请稍候...");
        return;
    }
    
    batchDownloadInProgress = true;
    $.modal.loading("正在打包下载，请稍候...");
    
    var xhr = new XMLHttpRequest();
    xhr.open("GET", ctx + "common/batchDownload?ids=" 
        + encodeURIComponent(ids.join(",")) 
        + "&module=" + encodeURIComponent(module), true);
    
    xhr.responseType = "blob";
    xhr.timeout = 10 * 60 * 1000;
    
    xhr.onload = function () {
        var blob = xhr.response;
        var contentDisposition = xhr.getResponseHeader("Content-Disposition") || "";
        var contentType = xhr.getResponseHeader("Content-Type") || "";
        
        var isDownloadResponse = contentDisposition.indexOf("attachment") !== -1
            || contentType.indexOf("application/zip") !== -1;
        
        if (xhr.status >= 200 && xhr.status < 300 && isDownloadResponse) {
            triggerBlobDownload(blob, parseDownloadFileName(contentDisposition));
            finishBatchDownload();
        } else {
            readBlobAsText(blob, function (text) {
                $.modal.alertError(extractDownloadErrorMessage(text));
                finishBatchDownload();
            });
        }
    };
    
    xhr.onerror = function () {
        $.modal.alertError("批量下载失败，请检查网络或稍后重试");
        finishBatchDownload();
    };
    
    xhr.ontimeout = function () {
        $.modal.alertError("批量下载超时，请稍后重试");
        finishBatchDownload();
    };
    
    xhr.send();
}

function triggerBlobDownload(blob, fileName) {
    var resolvedFileName = fileName || "批量下载.zip";
    
    if (window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(blob, resolvedFileName);
        return;
    }
    
    var downloadUrl = window.URL.createObjectURL(blob);
    var link = document.createElement("a");
    link.style.display = "none";
    link.href = downloadUrl;
    link.download = resolvedFileName;
    document.body.appendChild(link);
    link.click();
    
    setTimeout(function () {
        document.body.removeChild(link);
        window.URL.revokeObjectURL(downloadUrl);
    }, 100);
}

function parseDownloadFileName(contentDisposition) {
    if (!contentDisposition) return "";
    
    var utf8Match = contentDisposition.match(/filename\*=UTF-8''([^;]+)/i);
    if (utf8Match && utf8Match[1]) {
        return decodeURIComponent(utf8Match[1]).replace(/["]/g, "");
    }
    
    var fileNameMatch = contentDisposition.match(/filename="?([^";]+)"?/i);
    return fileNameMatch && fileNameMatch[1] 
        ? decodeURIComponent(fileNameMatch[1]) 
        : "";
}

function finishBatchDownload() {
    batchDownloadInProgress = false;
    $.modal.closeLoading();
}

function readBlobAsText(blob, callback) {
    var reader = new FileReader();
    reader.onload = function(e) {
        callback(e.target.result);
    };
    reader.readAsText(blob);
}

function extractDownloadErrorMessage(text) {
    try {
        var json = JSON.parse(text);
        return json.msg || "批量下载失败";
    } catch (e) {
        return text || "批量下载失败";
    }
}
