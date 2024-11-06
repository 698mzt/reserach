package com.ruoyi.web.controller.system;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.ConfigService;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Controller
public class SysLoginController extends BaseController
{
    /**
     * 是否开启记住我功能
     */
    @Value("${shiro.rememberMe.enabled: false}")
    private boolean rememberMe;

    @Autowired
    private ConfigService configService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, ModelMap mmap, Model model)
    {
        // 获取请求中的所有 Cookie
        Cookie[] cookies = request.getCookies();
// 如果没有 Cookie 或者 Cookie 数组为空
        if (cookies == null || cookies.length == 0) {
            // 设置默认用户名为 "admin"
            model.addAttribute("username", "admin");
            // 设置默认密码为 "admin123"
            model.addAttribute("password", "admin123");
        } else {
            // 标记是否找到了用户名
            boolean foundUsername = false;
            // 标记是否找到了密码
            boolean foundPassword = false;
            // 遍历每个 Cookie
            for (Cookie cookie : cookies) {
                try {
                    // 如果 Cookie 的名称是 "username"
                    if ("username".equals(cookie.getName())) {
                        // 获取并设置用户名
                        String username = cookie.getValue();
                        model.addAttribute("username", username);
                        // 标记已找到用户名
                        foundUsername = true;
                    } else if ("password".equals(cookie.getName())) {
                        // 如果 Cookie 的名称是 "password"
                        // 获取并设置密码
                        String password = cookie.getValue();
                        model.addAttribute("password", password);
                        // 标记已找到密码
                        foundPassword = true;
                    }
                    // 一旦找到 username 和 password 就退出循环
                    if (foundUsername && foundPassword) {
                        break;
                    }
                } catch (Exception e) {
                    // 处理异常
                    System.err.println("Error processing cookie: " + e.getMessage());
                }
            }
            // 如果未找到用户名，设置默认用户名为 "admin"
            if (!foundUsername) {
                model.addAttribute("username", "admin");
            }
            // 如果未找到密码，设置默认密码为 "admin123"
            if (!foundPassword) {
                model.addAttribute("password", "admin123");
            }
        }

        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request))
        {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }
        // 是否开启记住我
        mmap.put("isRemembered", rememberMe);
        // 是否开启用户注册
        mmap.put("isAllowRegister", Convert.toBool(configService.getKey("sys.account.registerUser"), false));
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe,HttpServletResponse response)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            // 设置或删除cookie
            setRememberMeCookie(username,password,rememberMe,response);
            return success();
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @GetMapping("/unauth")
    public String unauth()
    {
        return "error/unauth";
    }

    private void setRememberMeCookie(String username,String password, Boolean rememberMe,HttpServletResponse response) {
        if (rememberMe) {
            // 设置usernameCookie
            Cookie usernameCookie = new Cookie("username", username);
            usernameCookie.setPath("/");
            usernameCookie.setMaxAge(259200); // 3天
            usernameCookie.setSecure(true); // 只能在HTTPS下传输
            usernameCookie.setHttpOnly(true); // 防止JavaScript读取
            response.addCookie(usernameCookie);

            // 设置password Cookie
            Cookie passwordCookie = new Cookie("password", password);
            passwordCookie.setPath("/");
            passwordCookie.setMaxAge(259200); // 3天
            passwordCookie.setSecure(true); // 只能在HTTPS下传输
            passwordCookie.setHttpOnly(true); // 防止JavaScript读取
            response.addCookie(passwordCookie);
        } else {
            // 删除usernameCookie
            Cookie usernameCookie = new Cookie("username", null);
            usernameCookie.setPath("/");
            usernameCookie.setMaxAge(0);
            response.addCookie(usernameCookie);

            // 删除passwordCookie
            Cookie passwordCookie = new Cookie("password", null);
            passwordCookie.setPath("/");
            passwordCookie.setMaxAge(0);
            response.addCookie(passwordCookie);
        }
    }
}
