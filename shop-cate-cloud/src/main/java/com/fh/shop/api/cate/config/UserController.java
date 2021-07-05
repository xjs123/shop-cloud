package com.fh.shop.api.cate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@ConfigurationProperties(prefix = "user")
//@Component
//@Data
public class UserController {

    private String name;

    private int age;

    private Book book;



}
