package com.fh.shop.api.goods.controller;

import com.fh.shop.api.goods.biz.ISkuService;
import com.fh.shop.common.ServerResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class SkuController {

        @Resource(name = "skuService")
        private ISkuService skuService;

        @GetMapping("/skus/status")
        public ServerResponse findStatusList(){
            return skuService.findStatusList();
        }

        @GetMapping("/skus/selectById")
        public ServerResponse selectById(@RequestParam("id") Long id){
                return  skuService.selectById(id);
        }

}
