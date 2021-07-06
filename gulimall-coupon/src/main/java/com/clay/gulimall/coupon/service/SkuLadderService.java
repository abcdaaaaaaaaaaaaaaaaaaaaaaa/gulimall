package com.clay.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.coupon.entity.SkuLadderEntity;

import java.util.Map;

/**
 * 商品阶梯价格
 *
 * @author wangkai
 * @email 
 * @date 2021-03-08 22:34:23
 */
public interface SkuLadderService extends IService<SkuLadderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

