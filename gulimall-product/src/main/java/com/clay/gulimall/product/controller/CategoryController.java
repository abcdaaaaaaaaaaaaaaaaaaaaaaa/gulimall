package com.clay.gulimall.product.controller;

import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.common.utils.R;
import com.clay.gulimall.product.entity.CategoryEntity;
import com.clay.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 商品三级分类
 *
 * @author wangkai
 * @email 
 * @date 2021-02-27 23:36:54
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    /**
     * 树形列表
     */
    @RequestMapping("/list/tree")
    public R list(){
    List<CategoryEntity> entityties=categoryService.listWithTree();

        return R.ok().put("data", entityties);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateDetail(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds){

        categoryService.removeMenusByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
