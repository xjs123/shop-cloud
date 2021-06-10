package com.fh.shop.api.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class OrigFilter extends ZuulFilter {

    @Value("${shop.fh.checks}")
    private List<String> checks;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();
        //处理跨域问题
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        //处理客户端传过来的自定义头信息
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "x-auth,content-type,x-token");
        //处理客户端发过来put,delete
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "PUT,POST,DELETE,GET");
        //解决 options 请求
        
       return null;

    }
}
