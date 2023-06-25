package com.neo.springboot.aoplog.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.neo.springboot.aoplog.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: neo
 * @description 测试Controller
 * @date: 2023/6/24 20:18
 */

@RestController
@Slf4j
public class TestController {

    /**
     * 加 @Log 显示日志，不加不显示
     * @param name
     * @return
     */

    @GetMapping("test")
    @Log
    public Dict test(String name){
        return Dict.create().set("name",StrUtil.isBlank(name)?"无参数":name);
    }

    @PostMapping("testJson")
    @Log
    public Dict testJson(@RequestBody Map<String,Object> params){
        final String jsonStr = JSONUtil.toJsonStr(params);
        log.info(jsonStr);
        return Dict.create().set("json", params);
    }
}
