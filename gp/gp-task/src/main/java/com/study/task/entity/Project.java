package com.study.task.entity;

import java.io.Serializable;

import com.study.system.entity.SysUser;

import lombok.Data;

@Data
public class Project implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private SysUser duty;
	
	private Integer status; 
	
	private Projecttemp temp;
	
	private String remark;
	
}
