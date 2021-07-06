package com.clay.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.coupon.entity.MemberPriceEntity;

import java.util.Map;

/**
 * 商品会员价格
 *
 * @author wangkai
 * @email 
 * @date 2021-03-08 22:34:23
 */
public interface MemberPriceService extends IService<MemberPriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

