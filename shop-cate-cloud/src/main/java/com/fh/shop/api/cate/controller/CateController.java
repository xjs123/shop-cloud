package com.fh.shop.api.cate.controller;

import com.fh.shop.api.cate.biz.ICateService;
import com.fh.shop.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api")
@Slf4j
public class CateController {

    @Resource(name = "cateService")
    private ICateService cateService;
    @Value("${server.port}")
    private String info;

    @RequestMapping(value = "/cates",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findCate(){
        log.info("======{}",info);
        return  cateService.findCate();
    }


}
