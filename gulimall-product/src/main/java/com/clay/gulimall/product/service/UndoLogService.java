package com.clay.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.product.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author wangkai
 * @email 
 * @date 2021-02-27 23:36:54
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

