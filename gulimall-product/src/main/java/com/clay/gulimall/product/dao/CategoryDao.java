package com.clay.gulimall.product.dao;

import com.clay.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author wangkai
 * @email 
 * @date 2021-02-27 23:36:54
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
