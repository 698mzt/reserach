package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
public class fileDoc extends BaseController {
    private String prefix = "/system/file";
    @GetMapping()
    public String apply()
    {
        return prefix + "/apply";
    }
}
