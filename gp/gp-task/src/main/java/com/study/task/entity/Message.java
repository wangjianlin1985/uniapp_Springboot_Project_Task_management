package com.study.task.entity;

import java.io.Serializable;

import com.study.system.entity.SysUser;

import lombok.Data;

@Data
public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String content;
	
	private SysUser user;
	
	private Task task;
	
	private String time;
	
}
