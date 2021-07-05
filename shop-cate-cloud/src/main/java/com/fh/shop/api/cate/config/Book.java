package com.fh.shop.api.cate.config;

import lombok.Data;

import java.io.Serializable;
//@Data
public class Book implements Serializable {

    private String bookName;

    private int price;

    private Color color;
}
