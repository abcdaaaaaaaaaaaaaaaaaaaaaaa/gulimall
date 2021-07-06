package com.clay.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.common.utils.Query;
import com.clay.gulimall.product.dao.BrandDao;
import com.clay.gulimall.product.dao.CategoryBrandRelationDao;
import com.clay.gulimall.product.dao.CategoryDao;
import com.clay.gulimall.product.entity.BrandEntity;
import com.clay.gulimall.product.entity.CategoryBrandRelationEntity;
import com.clay.gulimall.product.entity.CategoryEntity;
import com.clay.gulimall.product.service.BrandService;
import com.clay.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    BrandDao brandDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetial(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();

        CategoryEntity categoryEntity = categoryDao.selectById(brandId);
        BrandEntity brandEntity = brandDao.selectById(brandId);

        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        this.save(categoryBrandRelation);

    }

    @Override
    public void updateCategory(Long catId, String name) {
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setCatelogName(name);
        relationEntity.setCatelogId(catId);
        LambdaUpdateWrapper<CategoryBrandRelationEntity> updateWrapper = new LambdaUpdateWrapper<>(relationEntity).eq(CategoryBrandRelationEntity::getCatelogId,catId);
        this.update(updateWrapper);
    }

    @Override
    public void updateBrand(Long brandId, String brandName) {
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setBrandId(brandId);
        relationEntity.setBrandName(brandName);
        LambdaUpdateWrapper<CategoryBrandRelationEntity> updateWrapper = new LambdaUpdateWrapper<CategoryBrandRelationEntity>(relationEntity).eq(CategoryBrandRelationEntity::getBrandId, brandId);
        this.update(updateWrapper);
    }

    @Override
    public List<BrandEntity> getBrandByCatId(Long catId) {
        List<CategoryBrandRelationEntity> list = super.list(new LambdaQueryWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getCatelogId, catId));
        Set<Long> brandIdSet = list.stream().map(CategoryBrandRelationEntity::getBrandId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(brandIdSet)){
            return null;
        }
        return brandService.listByIds(brandIdSet);
    }

}