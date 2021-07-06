package com.clay.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.coupon.entity.CouponHistoryEntity;

import java.util.Map;

/**
 * 优惠券领取历史记录
 *
 * @author wangkai
 * @email 
 * @date 2021-03-08 22:34:23
 */
public interface CouponHistoryService extends IService<CouponHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

