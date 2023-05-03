package com.study.task.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Tempstep implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**步骤ID*/
	private Long id;
	
	/**步骤名称*/
	private String name;
	
	/**步骤说明*/
	private String remark;
	
	/**步骤排序，大到小*/
	private Integer sort;
	
	/**所属模板*/
	private Projecttemp temp;
	
}
