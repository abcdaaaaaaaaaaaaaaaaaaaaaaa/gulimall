package com.clay.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.coupon.entity.HomeSubjectSpuEntity;

import java.util.Map;

/**
 * 专题商品
 *
 * @author wangkai
 * @email 
 * @date 2021-03-08 22:34:23
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

