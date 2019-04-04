package com.wangdinghua.springbootdemo.springbootdemo.logging.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wdh
 * @date 2019/04/04
 */
@RestController
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    public String test(){
        LOGGER.debug("这是一个debug日志。。。");
        return "test";
    }

}
