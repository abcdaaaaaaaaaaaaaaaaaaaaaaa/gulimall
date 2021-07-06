package com.clay.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.product.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author wangkai
 * @email
 * @date 2021-02-27 23:36:54
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenusByIds(List<Long> asList);

    ArrayList<Long> getParentIdList(Long attrGroupId);

    void updateDetail(CategoryEntity category);
}

