package com.study.task.entity;

import java.io.Serializable;

import lombok.Data;
	
/**
 * 模板
 */
@Data
public class Projecttemp implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**id*/
	private Long id;
	
	/**名称*/
	private String name;
	
	/**排序*/
	private Integer sort;
	
	/**说明*/
	private String remark;
	
}
