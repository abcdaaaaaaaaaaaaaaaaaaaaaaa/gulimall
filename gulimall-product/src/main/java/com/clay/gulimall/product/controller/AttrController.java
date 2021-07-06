package com.clay.gulimall.product.controller;

import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.common.utils.R;
import com.clay.gulimall.product.dto.AttrDTO;
import com.clay.gulimall.product.service.AttrService;
import com.clay.gulimall.product.vo.AttrRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 商品属性
 *
 * @author wangkai
 * @email 
 * @date 2021-02-27 23:36:55
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

     /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/{attrType}/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params,@PathVariable Long catelogId,@PathVariable String attrType){
        PageUtils page = attrService.queryBaseAttrPage(params, catelogId,attrType);
        return R.ok().put("page", page);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
        AttrRespVO attr = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrDTO attrDTO){
		attrService.saveAttr(attrDTO);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrDTO attrDTO){
		attrService.updateAttr(attrDTO);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
