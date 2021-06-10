package com.fh.shop.api.cart.controller;

import com.fh.shop.api.cart.biz.ICartService;
import com.fh.shop.api.common.BaseController;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/carts")
@Api(tags = "购物车接口")
public class CartController extends BaseController {

    @Resource(name = "cartService")
    private ICartService cartService;

    @Autowired
    private HttpServletRequest request;


    @PostMapping("/addCart")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "请求头",paramType = "header",required = true,dataType = "java.lang.String"),
            @ApiImplicitParam(name = "skuId",value = "商品id",required = true,dataType = "java.lang.Long"),
            @ApiImplicitParam(name = "count",value = "数量",required = true,dataType = "java.lang.Long")
    })
    @ApiOperation("添加商品到购物车")
    public ServerResponse addCart(Long skuId, Long count) {
        MemberVo memberVo = getMemberVo(request);
        Long id = memberVo.getId();
        return cartService.addCart(id,skuId,count);
    }



    @GetMapping("/findCart")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "请求头",paramType = "header",required = true,dataType = "java.lang.String"),
    })
    @ApiOperation("查询购物车")
    public ServerResponse findCart() {
        MemberVo memberVo = getMemberVo(request);
        Long id = memberVo.getId();
        return cartService.findCart(id);
    }



    @DeleteMapping("/deleteCartSku")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "请求头",paramType = "header",required = true,dataType = "java.lang.String"),
            @ApiImplicitParam(name = "skuId",value = "商品id",required = true,dataType = "java.lang.Long")
    })
    @ApiOperation("删除购物车商品")
    public ServerResponse deleteCartSku(Long skuId) {
        MemberVo memberVo = getMemberVo(request);
        Long id = memberVo.getId();
        return cartService.deleteCartSku(id,skuId);
    }



    @DeleteMapping("/deleteBatchCartSku")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth",value = "请求头",paramType = "header",required = true,dataType = "java.lang.String"),
    })
    @ApiOperation("删除购物车商品")
    public ServerResponse deleteBatchCartSku(String ids){
        MemberVo memberVo = getMemberVo(request);
        Long id = memberVo.getId();
        return cartService.deleteBatchCartSku(id,ids);
    }


}
