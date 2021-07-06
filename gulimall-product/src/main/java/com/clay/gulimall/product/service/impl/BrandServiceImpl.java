package com.clay.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.common.utils.Query;
import com.clay.gulimall.product.dao.BrandDao;
import com.clay.gulimall.product.entity.BrandEntity;
import com.clay.gulimall.product.service.BrandService;
import com.clay.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = params.getOrDefault("key", StringUtils.EMPTY).toString();
        LambdaQueryWrapper<BrandEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.eq(BrandEntity::getBrandId, key).or().like(BrandEntity::getName, key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params), queryWrapper

        );

        return new PageUtils(page);
    }

    @Override
    public void updateDetail(BrandEntity brand) {
        this.updateById(brand);
        if (StringUtils.isBlank(brand.getName())) {
           return;
        }
        String brandName=brand.getName();
        Long brandId = brand.getBrandId();
        categoryBrandRelationService.updateBrand(brandId,brandName);

    }

}