package com.clay.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.common.utils.Query;
import com.clay.gulimall.product.dao.AttrGroupDao;
import com.clay.gulimall.product.entity.AttrGroupEntity;
import com.clay.gulimall.product.service.AttrGroupService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                Query.page(params, AttrGroupEntity.class),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long logId) {
        String key = params.getOrDefault("key", StringUtils.EMPTY).toString();
        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();
        if (logId != null && logId != 0) {
            wrapper.eq(AttrGroupEntity::getCatelogId, logId);
        }
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(obj -> obj.eq(AttrGroupEntity::getAttrGroupId, key).or().like(AttrGroupEntity::getAttrGroupName, key));
        }
        IPage<AttrGroupEntity> page = this.page(Query.page(params, AttrGroupEntity.class), wrapper);
        return new PageUtils(page);
    }

}