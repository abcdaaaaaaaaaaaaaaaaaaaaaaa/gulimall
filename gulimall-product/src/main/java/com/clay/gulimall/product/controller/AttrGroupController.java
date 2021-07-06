package com.clay.gulimall.product.controller;

import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.common.utils.R;
import com.clay.gulimall.product.dto.AttrGroupRealtionDTO;
import com.clay.gulimall.product.entity.AttrEntity;
import com.clay.gulimall.product.entity.AttrGroupEntity;
import com.clay.gulimall.product.service.AttrAttrgroupRelationService;
import com.clay.gulimall.product.service.AttrGroupService;
import com.clay.gulimall.product.service.AttrService;
import com.clay.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 属性分组
 *
 * @author wangkai
 * @email 
 * @date 2021-02-27 23:36:54
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @GetMapping("/{catlogId}/withattr")
    public R getAttrGoupWithAttrs(@PathVariable Long catlogId){

        //todo
        return R.ok().data(null);
    }

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable Long attrgroupId){

        List<AttrEntity> list=attrService.getRelationAttr(attrgroupId);
        return R.ok().data(list);
    }

    @PostMapping("/attr/relation")
    public R addRealtion(@RequestBody List<AttrGroupRealtionDTO> realtionDTOList){

        attrAttrgroupRelationService.saveRealtion(realtionDTOList);
        return R.ok();
    }


    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable Long attrgroupId,@RequestParam Map<String, Object> params){

        PageUtils page=attrService.getNoRelationAttr(attrgroupId,params);
        return R.ok().put("page",page);
    }

    @PostMapping("/releation/delete")
    public R deleteRelation(@RequestBody AttrGroupRealtionDTO[] realtionDTS){

      attrService.deleteRelation(realtionDTS);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{categlogId}")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("categlogId") Long logId){
        PageUtils page = attrGroupService.queryPage(params,logId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        ArrayList<Long> parentIdList=categoryService.getParentIdList(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(parentIdList);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
