package com.study.task.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 标签管理
 */
@Data
public class Tasklabel implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
}
