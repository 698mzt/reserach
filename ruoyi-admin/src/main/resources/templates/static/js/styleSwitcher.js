// function addSwitcher() {
//     var dzSwitcher = '<div class="sidebar-right"><a class="sidebar-right-trigger wave-effect wave-effect-x" href="javascript:void(0)"><span><i class="fa fa-cog fa-spin"></i></span></a><div class="sidebar-right-inner"><div class="tab-content tab-content-default tabcontent-border"><div class="tab-pane fade active show" id="home8" role="tabpanel"><div class="admin-settings"><h4>Pick your style</h4><div><p>Background</p><select class="form-control" name="theme_version" id="theme_version"><option value="light">Light</option><option value="dark">Dark</option></select></div><div><p>Background</p><select class="form-control" name="theme_version" id="theme_version"><option value="light">Light</option><option value="dark">Dark</option></select></div><div><p>Layout</p><select class="form-control" name="theme_layout" id="theme_layout"><option value="vertical">Vertical</option><option value="horizontal">Horizontal</option></select></div><div><p>Sidebar</p><select class="form-control" name="sidebar_style" id="sidebar_style"><option value="full">Full</option><option value="mini">Mini</option><option value="compact">Compact</option><option value="modern">Modern</option><option value="overlay">Overlay</option><option value="icon-hover">Icon-hover</option></select></div><div><p>Sidebar position</p><select class="form-control" name="sidebar_position" id="sidebar_position"><option value="static">Static</option><option value="fixed">Fixed</option></select></div><div><p>Header position</p><select class="form-control" name="header_position" id="header_position"><option value="static">Static</option><option value="fixed">Fixed</option></select></div><div><p>Container</p><select class="form-control" name="container_layout" id="container_layout"><option value="wide">Wide</option><option value="boxed">Boxed</option><option value="wide-boxed">Wide Boxed</option></select></div><div><p>Direction</p><select class="form-control" name="theme_direction" id="theme_direction"><option value="ltr">LTR</option><option value="rtl">RTL</option></select></div><div><p>Body Font</p><select class="form-control" name="typography" id="typography"><option value="roboto">Roboto</option><option value="poppins">Poppins</option><option value="opensans">Open Sans</option><option value="HelveticaNeue">HelveticaNeue</option></select></div><div><p>Navigation Header</p><div><span><input type="radio" name="navigation_header" value="color_1" class="filled-in chk-col-primary" id="nav_header_color_1"><label for="nav_header_color_1"></label></span> <span><input type="radio" name="navigation_header" value="color_2" class="filled-in chk-col-primary" id="nav_header_color_2"><label for="nav_header_color_2"></label></span> <span><input type="radio" name="navigation_header" value="color_3" class="filled-in chk-col-primary" id="nav_header_color_3"><label for="nav_header_color_3"></label></span> <span><input type="radio" name="navigation_header" value="color_4" class="filled-in chk-col-primary" id="nav_header_color_4"><label for="nav_header_color_4"></label></span> <span><input type="radio" name="navigation_header" value="color_5" class="filled-in chk-col-primary" id="nav_header_color_5"><label for="nav_header_color_5"></label></span> <span><input type="radio" name="navigation_header" value="color_6" class="filled-in chk-col-primary" id="nav_header_color_6"><label for="nav_header_color_6"></label></span> <span><input type="radio" name="navigation_header" value="color_7" class="filled-in chk-col-primary" id="nav_header_color_7"><label for="nav_header_color_7"></label></span> <span><input type="radio" name="navigation_header" value="color_8" class="filled-in chk-col-primary" id="nav_header_color_8"><label for="nav_header_color_8"></label></span> <span><input type="radio" name="navigation_header" value="color_9" class="filled-in chk-col-primary" id="nav_header_color_9"><label for="nav_header_color_9"></label></span> <span><input type="radio" name="navigation_header" value="color_10" class="filled-in chk-col-primary" id="nav_header_color_10"><label for="nav_header_color_10"></label></span> <span><input type="radio" name="navigation_header" value="color_11" class="filled-in chk-col-primary" id="nav_header_color_11"><label for="nav_header_color_11"></label></span> <span><input type="radio" name="navigation_header" value="color_12" class="filled-in chk-col-primary" id="nav_header_color_12"><label for="nav_header_color_12"></label></span> <span><input type="radio" name="navigation_header" value="color_13" class="filled-in chk-col-primary" id="nav_header_color_13"><label for="nav_header_color_13"></label></span> <span><input type="radio" name="navigation_header" value="color_14" class="filled-in chk-col-primary" id="nav_header_color_14"><label for="nav_header_color_14"></label></span> <span><input type="radio" name="navigation_header" value="color_15" class="filled-in chk-col-primary" id="nav_header_color_15"><label for="nav_header_color_15"></label></span></div></div><div><p>Header</p><div><span><input type="radio" name="header_bg" value="color_1" class="filled-in chk-col-primary" id="header_color_1"><label for="header_color_1"></label></span> <span><input type="radio" name="header_bg" value="color_2" class="filled-in chk-col-primary" id="header_color_2"><label for="header_color_2"></label></span> <span><input type="radio" name="header_bg" value="color_3" class="filled-in chk-col-primary" id="header_color_3"><label for="header_color_3"></label></span> <span><input type="radio" name="header_bg" value="color_4" class="filled-in chk-col-primary" id="header_color_4"><label for="header_color_4"></label></span> <span><input type="radio" name="header_bg" value="color_5" class="filled-in chk-col-primary" id="header_color_5"><label for="header_color_5"></label></span> <span><input type="radio" name="header_bg" value="color_6" class="filled-in chk-col-primary" id="header_color_6"><label for="header_color_6"></label></span> <span><input type="radio" name="header_bg" value="color_7" class="filled-in chk-col-primary" id="header_color_7"><label for="header_color_7"></label></span> <span><input type="radio" name="header_bg" value="color_8" class="filled-in chk-col-primary" id="header_color_8"><label for="header_color_8"></label></span> <span><input type="radio" name="header_bg" value="color_9" class="filled-in chk-col-primary" id="header_color_9"><label for="header_color_9"></label></span> <span><input type="radio" name="header_bg" value="color_10" class="filled-in chk-col-primary" id="header_color_10"><label for="header_color_10"></label></span> <span><input type="radio" name="header_bg" value="color_11" class="filled-in chk-col-primary" id="header_color_11"><label for="header_color_11"></label></span> <span><input type="radio" name="header_bg" value="color_12" class="filled-in chk-col-primary" id="header_color_12"><label for="header_color_12"></label></span> <span><input type="radio" name="header_bg" value="color_13" class="filled-in chk-col-primary" id="header_color_13"><label for="header_color_13"></label></span> <span><input type="radio" name="header_bg" value="color_14" class="filled-in chk-col-primary" id="header_color_14"><label for="header_color_14"></label></span> <span><input type="radio" name="header_bg" value="color_15" class="filled-in chk-col-primary" id="header_color_15"><label for="header_color_15"></label></span></div></div><div><p>Sidebar</p><div><span><input type="radio" name="sidebar_bg" value="color_1" class="filled-in chk-col-primary" id="sidebar_color_1"><label for="sidebar_color_1"></label></span> <span><input type="radio" name="sidebar_bg" value="color_2" class="filled-in chk-col-primary" id="sidebar_color_2"><label for="sidebar_color_2"></label></span> <span><input type="radio" name="sidebar_bg" value="color_3" class="filled-in chk-col-primary" id="sidebar_color_3"><label for="sidebar_color_3"></label></span> <span><input type="radio" name="sidebar_bg" value="color_4" class="filled-in chk-col-primary" id="sidebar_color_4"><label for="sidebar_color_4"></label></span> <span><input type="radio" name="sidebar_bg" value="color_5" class="filled-in chk-col-primary" id="sidebar_color_5"><label for="sidebar_color_5"></label></span> <span><input type="radio" name="sidebar_bg" value="color_6" class="filled-in chk-col-primary" id="sidebar_color_6"><label for="sidebar_color_6"></label></span> <span><input type="radio" name="sidebar_bg" value="color_7" class="filled-in chk-col-primary" id="sidebar_color_7"><label for="sidebar_color_7"></label></span> <span><input type="radio" name="sidebar_bg" value="color_8" class="filled-in chk-col-primary" id="sidebar_color_8"><label for="sidebar_color_8"></label></span> <span><input type="radio" name="sidebar_bg" value="color_9" class="filled-in chk-col-primary" id="sidebar_color_9"><label for="sidebar_color_9"></label></span> <span><input type="radio" name="sidebar_bg" value="color_10" class="filled-in chk-col-primary" id="sidebar_color_10"><label for="sidebar_color_10"></label></span> <span><input type="radio" name="sidebar_bg" value="color_11" class="filled-in chk-col-primary" id="sidebar_color_11"><label for="sidebar_color_11"></label></span> <span><input type="radio" name="sidebar_bg" value="color_12" class="filled-in chk-col-primary" id="sidebar_color_12"><label for="sidebar_color_12"></label></span> <span><input type="radio" name="sidebar_bg" value="color_13" class="filled-in chk-col-primary" id="sidebar_color_13"><label for="sidebar_color_13"></label></span> <span><input type="radio" name="sidebar_bg" value="color_14" class="filled-in chk-col-primary" id="sidebar_color_14"><label for="sidebar_color_14"></label></span> <span><input type="radio" name="sidebar_bg" value="color_15" class="filled-in chk-col-primary" id="sidebar_color_15"><label for="sidebar_color_15"></label></span></div></div></div></div></div></div></div>';
//
//     if ($("#dzSwitcher").length == 0) {
//         jQuery('body').append(dzSwitcher);
//
//         const sr = new PerfectScrollbar('.sidebar-right-inner');
//
//         $('.sidebar-right-trigger').on('click', function() {
//             $('.sidebar-right').toggleClass('show');
//         });
//     }
// }
// jQuery(window).on('load', function() {
//
//
//
// });
// (function($) {
//     "use strict"
//     addSwitcher();
//
//
//     const body = $('body');
//     const html = $('html');
//
//     //get the DOM elements from right sidebar
//     const typographySelect = $('#typography');
//     const versionSelect = $('#theme_version');
//     const layoutSelect = $('#theme_layout');
//     const sidebarStyleSelect = $('#sidebar_style');
//     const sidebarPositionSelect = $('#sidebar_position');
//     const headerPositionSelect = $('#header_position');
//     const containerLayoutSelect = $('#container_layout');
//     const themeDirectionSelect = $('#theme_direction');
//
//     //change the theme typography controller
//     typographySelect.on('change', function() {
//         body.attr('data-typography', this.value);
//     });
//
//     //change the theme version controller
//     versionSelect.on('change', function() {
//         body.attr('data-theme-version', this.value);
//     });
//
//     //change the sidebar position controller
//     sidebarPositionSelect.on('change', function() {
//         this.value === "fixed" && body.attr('data-sidebar-style') === "modern" && body.attr('data-layout') === "vertical" ?
//             alert("Sorry, Modern sidebar layout dosen't support fixed position!") :
//             body.attr('data-sidebar-position', this.value);
//     });
//
//     //change the header position controller
//     headerPositionSelect.on('change', function() {
//         body.attr('data-header-position', this.value);
//     });
//
//     //change the theme direction (rtl, ltr) controller
//     themeDirectionSelect.on('change', function() {
//         html.attr('dir', this.value);
//         html.attr('class', '');
//         html.addClass(this.value);
//         body.attr('direction', this.value);
//     });
//
//     //change the theme layout controller
//     layoutSelect.on('change', function() {
//         if (body.attr('data-sidebar-style') === 'overlay') {
//             body.attr('data-sidebar-style', 'full');
//             body.attr('data-layout', this.value);
//             return;
//         }
//
//         body.attr('data-layout', this.value);
//     });
//
//     //change the container layout controller
//     containerLayoutSelect.on('change', function() {
//         if (this.value === "boxed") {
//
//             if (body.attr('data-layout') === "vertical" && body.attr('data-sidebar-style') === "full") {
//                 body.attr('data-sidebar-style', 'overlay');
//                 body.attr('data-container', this.value);
//                 return;
//             }
//         }
//
//         body.attr('data-container', this.value);
//     });
//
//     //change the sidebar style controller
//     sidebarStyleSelect.on('change', function() {
//         if (body.attr('data-layout') === "horizontal") {
//             if (this.value === "overlay") {
//                 alert("Sorry! Overlay is not possible in Horizontal layout.");
//                 return;
//             }
//         }
//
//         if (body.attr('data-layout') === "vertical") {
//             if (body.attr('data-container') === "boxed" && this.value === "full") {
//                 alert("Sorry! Full menu is not available in Vertical Boxed layout.");
//                 return;
//             }
//
//             if (this.value === "modern" && body.attr('data-sidebar-position') === "fixed") {
//                 alert("Sorry! Modern sidebar layout is not available in the fixed position. Please change the sidebar position into Static.");
//                 return;
//             }
//         }
//
//         body.attr('data-sidebar-style', this.value);
//
//         if (body.attr('data-sidebar-style') === 'icon-hover') {
//             $('.dlabnav').hover(function() {
//                 $('#main-wrapper').addClass('icon-hover-toggle');
//             }, function() {
//                 $('#main-wrapper').removeClass('icon-hover-toggle');
//             });
//         }
//     });
//
//     //change the nav-header background controller
//     $('input[name="navigation_header"]').on('click', function() {
//         body.attr('data-nav-headerbg', this.value);
//     });
//
//     //change the header background controller
//     $('input[name="header_bg"]').on('click', function() {
//         body.attr('data-headerbg', this.value);
//     });
//
//     //change the sidebar background controller
//     $('input[name="sidebar_bg"]').on('click', function() {
//         body.attr('data-sibebarbg', this.value);
//     });
//
//     //change the primary color controller
//     $('input[name="primary_bg"]').on('click', function() {
//         body.attr('data-primary', this.value);
//     });
//
// })(jQuery);


function addSwitcher() {
    var dzSwitcher = '<div class="sidebar-right"><a class="sidebar-right-trigger wave-effect wave-effect-x" href="javascript:void(0)"><span><i class="fa fa-cog fa-spin"></i></span></a><div class="sidebar-right-inner" ><div class="tab-content tab-content-default tabcontent-border" ><div class="tab-pane fade active show" id="home8" role="tabpanel"><div class="admin-settings"><h4>选择您的风格</h4><div><p>环境</p><select class="form-control" name="theme_version" id="theme_version"><option value="light">光亮</option><option value="dark">黑暗</option></select></div><div><p>背景设置</p><select class="form-control" name="background_image" id="background_image"><option value="">无背景</option><option value="background1.jpg">图片1</option><option value="ziran.jpg">图片2</option><option value="xinyin.jpg">图片3</option></select></div><div><p>背景透明度</p><input type="range" id="background-opacity" min="0" max="1" step="0.01" value="1"><div><p>标头位置</p><select class="form-control" name="header_position" id="header_position"><option value="static">静态的</option><option value="fixed">固定的</option></select></div><div><p>容器</p><select class="form-control" name="container_layout" id="container_layout"><option value="wide">宽</option><option value="boxed">盒装</option><option value="wide-boxed">宽箱</option></select></div><div><p>方向</p><select class="form-control" name="theme_direction" id="theme_direction"><option value="ltr">LTR</option><option value="rtl">RTL</option></select></div><div><p>Body 字体</p><select class="form-control" name="typography" id="typography"><option value="roboto">Roboto</option><option value="poppins">Poppins</option><option value="opensans">Open Sans</option><option value="HelveticaNeue">HelveticaNeue</option></select></div>' +
        '<div>\n' +
        '    <p>轮播图按钮文字颜色</p>\n' +
        '    <div>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#FFFFFF" id="carousel_button_text_color_white" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_white" style="border: 1px solid #000; background-color: #FFFFFF;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#FF5733" id="carousel_button_text_color_red" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_red" style="background-color: #FF5733;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#3498DB" id="carousel_button_text_color_blue" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_blue" style="background-color: #3498DB;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#33FF57" id="carousel_button_text_color_green" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_green" style="background-color: #33FF57;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#3357FF" id="carousel_button_text_color_royalblue" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_royalblue" style="background-color: #3357FF;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#FF33A1" id="carousel_button_text_color_hotpink" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_hotpink" style="background-color: #FF33A1;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#33FFD1" id="carousel_button_text_color_apuamarine" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_apuamarine" style="background-color: #33FFD1;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#FFA533" id="carousel_button_text_color_goldenrod" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_goldenrod" style="background-color: #FFA533;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="rgba(224,51,255,0.86)" id="carousel_button_text_color_lime" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_lime" style="background-color: rgba(224,51,255,0.86);"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#808080" id="carousel_button_text_color_gray" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_gray" style="background-color: #808080;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#FF66CC" id="carousel_button_text_color_lavenderblush" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_lavenderblush" style="background-color: #FF66CC;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#33CCFF" id="carousel_button_text_color_azure" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_azure" style="background-color: #33CCFF;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#6633FF" id="carousel_button_text_color_blueviolet" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_blueviolet" style="background-color: #6633FF;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#FFCC33" id="carousel_button_text_color_peachpuff" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_peachpuff" style="background-color: #FFCC33;"></label>\n' +
        '        </span>\n' +
        '        <span>\n' +
        '            <input type="radio" name="carousel_button_text_color" value="#E0FF33FF" id="carousel_button_text_color_yellow" class="filled-in chk-col-radio">\n' +
        '            <label for="carousel_button_text_color_yellow" style="background-color: #E0FF33FF;"></label>\n' +
        '        </span>\n' +
        '        <!-- 添加更多颜色选项 -->\n' +
        '    </div>\n' +
        '</div></div></div></div></div>';


    if ($("#dzSwitcher").length == 0) {
        jQuery('body').append(dzSwitcher);

        const sr = new PerfectScrollbar('.sidebar-right-inner');

        $('.sidebar-right-trigger').on('click', function() {
            $('.sidebar-right').toggleClass('show');
        });
    }
}

jQuery(window).on('load', function() {
});
(function($) {
    "use strict"
    addSwitcher();

    const body = $('body');
    const html = $('html');

    //get the DOM elements from right sidebar
    const typographySelect = $('#typography');
    const versionSelect = $('#theme_version');
    const layoutSelect = $('#theme_layout');
    const sidebarStyleSelect = $('#sidebar_style');
    const sidebarPositionSelect = $('#sidebar_position');
    const headerPositionSelect = $('#header_position');
    const containerLayoutSelect = $('#container_layout');
    const themeDirectionSelect = $('#theme_direction');

    //change the theme typography controller
    typographySelect.on('change', function() {
        body.attr('data-typography', this.value);
    });

    //change the theme version controller
    versionSelect.on('change', function() {
        body.attr('data-theme-version', this.value);
    });

    //change the sidebar position controller
    sidebarPositionSelect.on('change', function() {
        this.value === "fixed" && body.attr('data-sidebar-style') === "modern" && body.attr('data-layout') === "vertical" ?
            alert("Sorry, Modern sidebar layout dosen't support fixed position!") :
            body.attr('data-sidebar-position', this.value);
    });

    //change the header position controller
    headerPositionSelect.on('change', function() {
        body.attr('data-header-position', this.value);
    });

    //change the theme direction (rtl, ltr) controller
    themeDirectionSelect.on('change', function() {
        html.attr('dir', this.value);
        html.attr('class', '');
        html.addClass(this.value);
        body.attr('direction', this.value);
    });

    //change the theme layout controller
    layoutSelect.on('change', function() {
        if (body.attr('data-sidebar-style') === 'overlay') {
            body.attr('data-sidebar-style', 'full');
            body.attr('data-layout', this.value);
            return;
        }

        body.attr('data-layout', this.value);
    });

    //change the container layout controller
    containerLayoutSelect.on('change', function() {
        if (this.value === "boxed") {

            if (body.attr('data-layout') === "vertical" && body.attr('data-sidebar-style') === "full") {
                body.attr('data-sidebar-style', 'overlay');
                body.attr('data-container', this.value);
                return;
            }
        }

        body.attr('data-container', this.value);
    });

    //change the sidebar style controller
    sidebarStyleSelect.on('change', function() {
        if (body.attr('data-layout') === "horizontal") {
            if (this.value === "overlay") {
                alert("Sorry! Overlay is not possible in Horizontal layout.");
                return;
            }
        }

        if (body.attr('data-layout') === "vertical") {
            if (body.attr('data-container') === "boxed" && this.value === "full") {
                alert("Sorry! Full menu is not available in Vertical Boxed layout.");
                return;
            }

            if (this.value === "modern" && body.attr('data-sidebar-position') === "fixed") {
                alert("Sorry! Modern sidebar layout is not available in the fixed position. Please change the sidebar position into Static.");
                return;
            }
        }

        body.attr('data-sidebar-style', this.value);

        if (body.attr('data-sidebar-style') === 'icon-hover') {
            $('.dlabnav').hover(function() {
                $('#main-wrapper').addClass('icon-hover-toggle');
            }, function() {
                $('#main-wrapper').removeClass('icon-hover-toggle');
            });
        }
    });


    // 添加背景图片选择的事件监听器
    $('#background_image').on('change', function() {
        var backgroundImage = this.value;
        var opacity = $('#background-opacity').val(); // 假设您有一个用于调整透明度的滑块或输入框

        if (backgroundImage) {
            // 构建完整路径（如果需要的话）
            var fullImagePath = 'static/picture/' + backgroundImage;
            document.documentElement.style.setProperty('--background-image', 'url(' + fullImagePath + ')');
        } else {
            document.documentElement.style.setProperty('--background-image', 'none');
        }

        // 更新透明度
        document.documentElement.style.setProperty('--background-opacity', opacity);
    });

    // 确保DOM完全加载后执行
    $(document).ready(function() {
        // 添加颜色选择事件监听器
        $('input[name="carousel_button_text_color"]').on('change', function() {
            var selectedColor = this.value; // 获取选中的颜色值
            $('.leftBtn, .rightBtn').css('color', selectedColor); // 更新按钮的文字颜色
        });
    });
})(jQuery);