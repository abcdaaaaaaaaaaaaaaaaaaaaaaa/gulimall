package com.clay.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.common.utils.Query;
import com.clay.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.clay.gulimall.product.dto.AttrGroupRealtionDTO;
import com.clay.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.clay.gulimall.product.service.AttrAttrgroupRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service("attrAttrgroupRelationService")
public class AttrAttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveRealtion(List<AttrGroupRealtionDTO> realtionDTOList) {
        Set<AttrAttrgroupRelationEntity> relationEntitySet = realtionDTOList.stream().map(groupRealtionDTO -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(groupRealtionDTO, relationEntity);
            return relationEntity;
        }).collect(Collectors.toSet());
        this.saveBatch(relationEntitySet);
    }
}