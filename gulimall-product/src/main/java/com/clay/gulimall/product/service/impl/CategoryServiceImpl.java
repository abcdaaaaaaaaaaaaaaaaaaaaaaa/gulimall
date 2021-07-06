package com.clay.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.common.utils.Query;
import com.clay.gulimall.product.dao.CategoryDao;
import com.clay.gulimall.product.entity.CategoryEntity;
import com.clay.gulimall.product.service.CategoryBrandRelationService;
import com.clay.gulimall.product.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);

        List<CategoryEntity> oneLevle = entities.parallelStream().filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .peek(menu -> menu.setChildren( getChilderns(menu, entities))).sorted(Comparator.comparingInt(CategoryEntity::getSort)).
                        collect(Collectors.toList());


        return entities;
    }

    @Override
    public void removeMenusByIds(List<Long> asList) {
        //TODO 检查当前剩余菜单 是否被引用

        baseMapper.deleteBatchIds(asList);
    }


    /**
     * @param menu
     * @param entities
     * @return
     */
    private List<CategoryEntity> getChilderns(CategoryEntity menu, List<CategoryEntity> entities) {
        return entities.parallelStream().filter(categoryEntity -> categoryEntity.getParentCid().equals(menu.getCatId()))
                .peek(categoryEntity -> categoryEntity.setChildren(getChilderns(categoryEntity, entities)))
                .sorted(Comparator.comparing(CategoryEntity::getSort)).collect(Collectors.toList());
    }


    @Override
    public ArrayList<Long> getParentIdList(Long attrGroupId) {
        ArrayList<Long> parentIdList = new ArrayList<>();
        getParentById(attrGroupId,parentIdList);
        Collections.reverse(parentIdList);
        return parentIdList;
    }

    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        if (StringUtils.isBlank(category.getName())){
            return;
        }
        String name = category.getName();
        Long catId = category.getCatId();
        categoryBrandRelationService.updateCategory(catId, name);
    }

    private void getParentById(Long attrGroupId,ArrayList<Long> parentIdList) {
        parentIdList.add(attrGroupId);
        CategoryEntity entity = this.getById(attrGroupId);
        Long parentCid = entity.getParentCid();
        if (parentCid==null||parentCid==0){
            return;
        }
        this.getParentById(parentCid, parentIdList);
    }


}