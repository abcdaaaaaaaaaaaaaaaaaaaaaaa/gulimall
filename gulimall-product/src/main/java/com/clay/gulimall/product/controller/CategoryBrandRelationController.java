package com.clay.gulimall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.common.utils.R;
import com.clay.gulimall.product.entity.BrandEntity;
import com.clay.gulimall.product.entity.CategoryBrandRelationEntity;
import com.clay.gulimall.product.service.CategoryBrandRelationService;
import com.clay.gulimall.product.vo.BrandVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 品牌分类关联
 *
 * @author wangkai
 * @email 
 * @date 2021-02-27 23:36:54
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().page(page);
    }

    /**
     * 列表
     */
    @RequestMapping("/brands/list")
    public R relationBrandsList(@RequestParam(required = true) Long  catId){
      List<BrandEntity> brandVOList=  categoryBrandRelationService.getBrandByCatId(catId);
        List<BrandVO> resultList = brandVOList.parallelStream().map(brandEntity -> {
            BrandVO brandVO = new BrandVO();
            brandVO.setBrandId(brandEntity.getBrandId());
            brandVO.setBrandName(brandEntity.getName());
            return brandVO;
        }).collect(Collectors.toList());

        return R.ok().data(resultList);
    }
    /**
     * 列表
     */
    @GetMapping("/catelog/list")
    public R catelogList(@RequestParam Long brandId){

        LambdaQueryWrapper<CategoryBrandRelationEntity> wrapper = new LambdaQueryWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getBrandId,brandId);
        List<CategoryBrandRelationEntity> list = categoryBrandRelationService.list(wrapper);
        return R.ok().data(list);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){

		categoryBrandRelationService.saveDetial(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
