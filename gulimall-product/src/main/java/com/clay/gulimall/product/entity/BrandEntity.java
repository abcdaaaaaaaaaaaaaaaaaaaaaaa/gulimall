package com.clay.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.clay.gulimall.product.validate.ListValue;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 * 
 * @author wangkai
 * @email 
 * @date 2021-02-27 23:36:54
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "品牌id不能为空",groups = UpdateGroup.class)
	@Null(message = "新增不能指定id",groups = AddGroup.class)
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名称不能为空",groups = {UpdateGroup.class,AddGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(groups = {AddGroup.class})
	@URL(message = "logo必须是一个合法的url地址",groups = {UpdateGroup.class,AddGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(groups ={UpdateGroup.class,AddGroup.class} )
	@ListValue(vals = {1,0},groups = {UpdateStateGroup.class,UpdateGroup.class,AddGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@Pattern(regexp = "^[a-zA-z]$")
	private String firstLetter;
	/**
	 * 排序
	 */
	@Min(value = 0,message = "排序字段必须大于0")
	private Integer sort;

	public  interface AddGroup {
	}

	public interface UpdateGroup {
	}

	public interface UpdateStateGroup {
	}

}
