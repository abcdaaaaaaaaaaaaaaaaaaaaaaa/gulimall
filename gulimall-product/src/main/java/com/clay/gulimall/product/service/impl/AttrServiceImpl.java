package com.clay.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clay.gulimall.common.constant.ProductConstant;
import com.clay.gulimall.common.utils.PageUtils;
import com.clay.gulimall.common.utils.Query;
import com.clay.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.clay.gulimall.product.dao.AttrDao;
import com.clay.gulimall.product.dao.AttrGroupDao;
import com.clay.gulimall.product.dao.CategoryDao;
import com.clay.gulimall.product.dto.AttrDTO;
import com.clay.gulimall.product.dto.AttrGroupRealtionDTO;
import com.clay.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.clay.gulimall.product.entity.AttrEntity;
import com.clay.gulimall.product.entity.AttrGroupEntity;
import com.clay.gulimall.product.entity.CategoryEntity;
import com.clay.gulimall.product.service.AttrService;
import com.clay.gulimall.product.service.CategoryService;
import com.clay.gulimall.product.vo.AttrRespVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;


    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAttr(AttrDTO attrDTO) {
        AttrEntity entity = new AttrEntity();
        BeanUtils.copyProperties(attrDTO, entity);
        this.save(entity);
        if (attrDTO.getAttrType() != ProductConstant.ATTR_TYPE_BASE.getCode()||attrDTO.getAttrGroupId()==null) {
            return;
        }
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(entity.getAttrId());
        relationEntity.setAttrGroupId(attrDTO.getAttrGroupId());
        attrAttrgroupRelationDao.insert(relationEntity);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType) {
        LambdaQueryWrapper<AttrEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (catelogId != 0) {
            queryWrapper.eq(AttrEntity::getCatelogId, catelogId).eq(AttrEntity::getAttrType, StringUtils.equalsIgnoreCase(attrType, "BASE") ? ProductConstant.ATTR_TYPE_BASE.getCode() : ProductConstant.ATTR_TYPE_SALE.getCode());
        }
        String key = params.getOrDefault("key", StringUtils.EMPTY).toString();
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageUtils;
        }
        List<AttrRespVO> collect = records.stream().map(attrEntity -> {
            AttrRespVO respVO = new AttrRespVO();
            BeanUtils.copyProperties(attrEntity, respVO);
            Long attrId = attrEntity.getAttrId();
            if (StringUtils.equalsIgnoreCase("base", attrType)) {
                AttrAttrgroupRelationEntity attrGruop = attrAttrgroupRelationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
                if (attrGruop != null&&attrGruop.getAttrGroupId()!=null) {
                    Long attrGroupId = attrGruop.getAttrGroupId();
                    String attrGroupName = attrGroupDao.selectById(attrGroupId).getAttrGroupName();
                    respVO.setGroupName(attrGroupName);
                }
            }

            CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
            respVO.setCatlogName(categoryEntity.getName());
            return respVO;
        }).collect(Collectors.toList());
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Override
    public AttrRespVO getAttrInfo(Long attrId) {
        AttrRespVO respVO = new AttrRespVO();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, respVO);
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> relationEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        relationEntityLambdaQueryWrapper.eq(AttrAttrgroupRelationEntity::getAttrId, attrId);
        if (attrEntity.getAttrType() == ProductConstant.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(relationEntityLambdaQueryWrapper);
            if (null != relationEntity) {
                Long attrGroupId = relationEntity.getAttrGroupId();
                respVO.setAttrGroupId(attrGroupId);
                AttrGroupEntity groupEntity = attrGroupDao.selectById(attrGroupId);
                respVO.setGroupName(groupEntity.getAttrGroupName());
            }
        }

        Long catelogId = attrEntity.getCatelogId();
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity == null) {
            return respVO;

        }
        respVO.setCatlogName(categoryEntity.getName());
        ArrayList<Long> parentIdList = categoryService.getParentIdList(catelogId);
        respVO.setCatlogPath(parentIdList);
        return respVO;
    }

    @Transactional
    @Override
    public void updateAttr(AttrDTO attrDTO) {
        AttrEntity entity = new AttrEntity();
        BeanUtils.copyProperties(attrDTO, entity);
        this.updateById(entity);
        if (attrDTO.getAttrType() != ProductConstant.ATTR_TYPE_BASE.getCode()) {
            return;
        }
        Long attrId = entity.getAttrId();
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrId);
        relationEntity.setAttrGroupId(attrDTO.getAttrGroupId());
        LambdaUpdateWrapper<AttrAttrgroupRelationEntity> updateWrapper = new LambdaUpdateWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrId);
        int update = attrAttrgroupRelationDao.update(relationEntity, updateWrapper);
        if (retBool(update)) {
            return;
        }
        attrAttrgroupRelationDao.insert(relationEntity);
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entityList = attrAttrgroupRelationDao.selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrgroupId));
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        Set<Long> collect = entityList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toSet());
        List<AttrEntity> attrEntities = this.listByIds(collect);
        return attrEntities;
    }

    @Override
    public void deleteRelation(AttrGroupRealtionDTO[] realtionDTS) {
        Stream.of(realtionDTS).forEach(attrGroupRealtionDTO -> {
            attrAttrgroupRelationDao.delete(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupRealtionDTO.getAttrGroupId()).eq(AttrAttrgroupRelationEntity::getAttrId, attrGroupRealtionDTO.getAttrId()));
        });

    }

    /**
     * 获取当前分组未关联的属性
     *
     * @param attrgroupId
     * @param params
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params) {
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        List<AttrGroupEntity> attrGroupEntityList = attrGroupDao.selectList(new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getCatelogId, catelogId));
        Set<Long> attrGroupIdList = attrGroupEntityList.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toSet());

        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = attrAttrgroupRelationDao.selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().in(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupIdList));
        Set<Long> attIds = attrAttrgroupRelationEntityList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toSet());
        LambdaQueryWrapper<AttrEntity> queryWrapper = new LambdaQueryWrapper<AttrEntity>().eq(AttrEntity::getCatelogId, catelogId).eq(AttrEntity::getAttrType, ProductConstant.ATTR_TYPE_BASE.getCode());
        if (!CollectionUtils.isEmpty(attIds)){
            queryWrapper.notIn(AttrEntity::getAttrId, attIds);
        }

        String key = params.getOrDefault("key", StringUtils.EMPTY).toString();

        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);


        return new PageUtils(page);
    }



}