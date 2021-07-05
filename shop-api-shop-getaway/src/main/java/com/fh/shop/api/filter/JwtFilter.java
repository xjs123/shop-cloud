package com.fh.shop.api.filter;


import com.alibaba.fastjson.JSON;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.SecretLogin;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.KeyUtil;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
@Component
public class JwtFilter implements GlobalFilter,Ordered {

    @Value("${fh.shop.checkUrls}")
    private List<String> checks;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();


        ServerHttpRequest request = exchange.getRequest();
//        if (methodHTTP.equalsIgnoreCase("OPTIONS")) {
//            // 禁止路由 不会继续向微服务发送请求
//            currentContext.setSendZuulResponse(false);
//            return null;
//        }
        //判断是否需要登录拦截
        //获取当前请求的url
        String requestURI = request.getURI().toString();
        boolean isCheck = false;
        for (String checkUrl : checks) {
            if(requestURI.contains(checkUrl)){
                isCheck = true;
                break;
            }
        }
        if(!isCheck){
            //不需要判断用户是否登录 直接访问接口
           return chain.filter(exchange);
        }
        //获取头信息
        String header = request.getHeaders().getFirst("x-auth");
        //判断有没有头信息
        if(StringUtils.isEmpty(header)){
            return buildResponse(response,ResponseEnum.TOKEN_HEADER_IS_NULL);
//            throw new ShopException(ResponseEnum.TOKEN_HEADER_IS_NULL);
        }

        //判断头信息是否规范
        String[] headerArr = header.split("\\.");
        if(headerArr.length!=2){
            return buildResponse(response,ResponseEnum.TOKEN_HEADER_IS_FULL);
        }
        //然后就可以进行验签
        //获取签名
        String memberVo64 = headerArr[0];
        String sout64 = headerArr[1];
        String member = new String(Base64.getDecoder().decode(memberVo64),"utf-8");
        String sout = new String(Base64.getDecoder().decode(sout64),"utf-8");
        if(!Md5Util.sout(member,SecretLogin.SECRE).equals(sout)){
            return buildResponse(response,ResponseEnum.TOKEN_HEADER_ERROR);
        }
        MemberVo membervo = JSON.parseObject(member, MemberVo.class);

//        currentContext.addZuulRequestHeader(SecretLogin.MEMVO,URLEncoder.encode(member,"utf-8"));


        Long id = membervo.getId();
        Boolean ex = RedisUtil.ex(KeyUtil.getksy(id));
        if(!ex){
            return buildResponse(response,ResponseEnum.TOKEN_IS_NULL);
        }
        RedisUtil.er(KeyUtil.getksy(id),SecretLogin.SECODE);

        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().header(SecretLogin.MEMVO, URLEncoder.encode(member, "utf-8")).build();
        return chain.filter(exchange.mutate().request(serverHttpRequest).build());
    }


    public Mono<Void> buildResponse(ServerHttpResponse response, ResponseEnum responseEnum) {
        ServerResponse error = ServerResponse.error(responseEnum);
        String errorJson = JSON.toJSONString(error);
        response.getHeaders().add("content-type", "application/json;charset=utf-8");
        DataBuffer wrap = response.bufferFactory().wrap(errorJson.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(wrap));
    }



    @Override
    public int getOrder() {
        return -100;
    }
}
