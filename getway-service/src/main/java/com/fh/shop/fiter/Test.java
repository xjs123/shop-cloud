package com.fh.shop.fiter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
//        String str ="test ok";
//        String str2 ="test";
//        String str3 ="ok";
//        System.out.println(str==(str2+str3).intern());

        List a =new ArrayList();
        a.add(1);
        a.add(109);
        a.add(3);
        List a2 =new ArrayList();
        a2.add(4);
        a2.add(5);
        a2.add(6);
        a.addAll(a2);

        System.out.println(a.stream().sorted().collect(Collectors.toList()).toString());
    }
}
