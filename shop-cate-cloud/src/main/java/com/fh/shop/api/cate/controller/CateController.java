package com.fh.shop.api.cate.controller;

import com.fh.shop.api.cate.biz.ICateService;
import com.fh.shop.api.cate.config.UserController;
import com.fh.shop.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api")
@Slf4j
public class CateController {

//    @Value("${user.name}")
//    private String name;
//
//    @Value("${user.age}")
//    private int age;
//    @Autowired
//    private UserController userController;

    @Resource(name = "cateService")
    private ICateService cateService;
//
//    @Value("${server.port}")
//    private String info;

    @RequestMapping(value = "/cates",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse findCate(){
//        log.info("用户名{}",userController.getName());
//        log.info("用户年龄{}",userController.getAge());
//        log.info("喜爱图书{}",userController.getBook().getBookName());
//        log.info("图书颜色{}",userController.getBook().getColor().getColorName());
        System.out.println("-------===");
        return  cateService.findCate();
    }






}
