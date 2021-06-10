package com.fh.shop.api.common;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.common.SecretLogin;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class BaseController {

    public static MemberVo getMemberVo(HttpServletRequest request){
        try {
            String membervo = URLDecoder.decode(request.getHeader(SecretLogin.MEMVO),"utf-8");
            MemberVo memberVo = JSON.parseObject(membervo, MemberVo.class);
            return memberVo;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }

    }

}
