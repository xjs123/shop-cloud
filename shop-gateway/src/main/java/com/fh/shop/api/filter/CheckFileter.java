package com.fh.shop.api.filter;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.SecretLogin;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.KeyUtil;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;

@Component
@Slf4j
public class CheckFileter extends ZuulFilter {
    @Value("${shop.fh.checks}")
    private List<String> checks;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException {
        log.info("====={}",checks);
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        String methodHTTP = request.getMethod();
        if (methodHTTP.equalsIgnoreCase("OPTIONS")) {
            // 禁止路由 不会继续向微服务发送请求
            currentContext.setSendZuulResponse(false);
            return null;
        }
        //判断是否需要登录拦截
        //获取当前请求的url
        String requestURI = request.getRequestURI();
        boolean isCheck = false;
        for (String checkUrl : checks) {
            if(requestURI.contains(checkUrl)){
                isCheck = true;
                break;
            }
        }
        if(!isCheck){
            //不需要判断用户是否登录 直接访问接口
            return null;
        }
        //获取头信息
        String header = request.getHeader("x-auth");
        //判断有没有头信息
        if(header==null){
            return getObject(ResponseEnum.TOKEN_HEADER_IS_NULL);
//            throw new ShopException(ResponseEnum.TOKEN_HEADER_IS_NULL);
        }

        //判断头信息是否规范
        String[] headerArr = header.split("\\.");
        if(headerArr.length!=2){
            return getObject(ResponseEnum.TOKEN_HEADER_IS_FULL);
        }
        //然后就可以进行验签
        //获取签名
        String memberVo64 = headerArr[0];
        String sout64 = headerArr[1];
        String member = new String(Base64.getDecoder().decode(memberVo64),"utf-8");
        String sout = new String(Base64.getDecoder().decode(sout64),"utf-8");
        if(!Md5Util.sout(member,SecretLogin.SECRE).equals(sout)){
            return getObject(ResponseEnum.TOKEN_HEADER_ERROR);
        }
        MemberVo membervo = JSON.parseObject(member, MemberVo.class);

        currentContext.addZuulRequestHeader(SecretLogin.MEMVO,URLEncoder.encode(member,"utf-8"));


        Long id = membervo.getId();
        Boolean ex = RedisUtil.ex(KeyUtil.getksy(id));
        if(!ex){
            return getObject(ResponseEnum.TOKEN_IS_NULL);
        }
        RedisUtil.er(KeyUtil.getksy(id),SecretLogin.SECODE);

        return null;


    }

    private Object getObject(ResponseEnum responseEnum) {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        currentContext.setSendZuulResponse(false);
        ServerResponse error = ServerResponse.error(responseEnum);
        currentContext.setResponseBody(JSON.toJSONString(error));
        return null;
    }
}
