package com.study.task.entity;

import java.io.Serializable;

import com.study.system.entity.SysUser;

import lombok.Data;

@Data
public class Projectuser implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Project project;
	
	private SysUser user;
	
	private Integer status; // 0未同意，1同意,2不同意
	
	private String searchKey; // 邮箱或者用户名。按这个搜索
	
}
