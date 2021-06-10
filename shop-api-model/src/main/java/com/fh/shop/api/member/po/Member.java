package com.fh.shop.api.member.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class Member implements Serializable {
        @ApiModelProperty(value = "主键id",example = "0")
        private Long id;
        @ApiModelProperty(value = "会员名",required = true)
        private String memberName;
        @ApiModelProperty(value = "密码",required = true)
        private String password;
        @ApiModelProperty(value = "昵称",required = true)
        private String nickName;
        @ApiModelProperty(value = "邮箱",required = true)
        private String email;
        @ApiModelProperty(value = "手机号",required = true)
        private String phone;
        @ApiModelProperty(value = "状态",required = true,example = "0")
        private int status;
        @ApiModelProperty(value = "积分",required = true,example = "0")
        private Long integral;
}
