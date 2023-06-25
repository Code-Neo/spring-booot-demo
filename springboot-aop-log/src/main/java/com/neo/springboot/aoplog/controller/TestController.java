package com.neo.springboot.aoplog.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: neo
 * @description 测试AOP日志
 * @date: 2023/6/24 18:28
 */

@RestController
@Slf4j
public class TestController {

    @GetMapping("test")
    public Dict test(String name){
        return Dict.create().set("name",StrUtil.isBlank(name)?"无参数":name);
    }

    @PostMapping("testJson")
    public Dict testJson(@RequestBody Map<String,Object> params){
        final String jsonStr = JSONUtil.toJsonStr(params);
        log.info(jsonStr);
        return Dict.create().set("json", params);
    }
}
