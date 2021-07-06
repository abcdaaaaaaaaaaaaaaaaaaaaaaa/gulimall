package com.clay.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.product.dto.AttrGroupRealtionDTO;
import com.clay.gulimall.product.entity.AttrAttrgroupRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author wangkai
 * @email 
 * @date 2021-02-27 23:36:55
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveRealtion(List<AttrGroupRealtionDTO> realtionDTOList);
}

