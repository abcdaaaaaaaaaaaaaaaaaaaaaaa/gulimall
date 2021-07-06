package com.clay.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.product.dto.AttrDTO;
import com.clay.gulimall.product.dto.AttrGroupRealtionDTO;
import com.clay.gulimall.product.entity.AttrEntity;
import com.clay.gulimall.product.vo.AttrRespVO;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author wangkai
 * @email 
 * @date 2021-02-27 23:36:55
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrDTO attrDTO);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType);

    AttrRespVO getAttrInfo(Long attrId);

    void updateAttr(AttrDTO attrDTO);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRealtionDTO[] realtionDTS);

    PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params);


}

