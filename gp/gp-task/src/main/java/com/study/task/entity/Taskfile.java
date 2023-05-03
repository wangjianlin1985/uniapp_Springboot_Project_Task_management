package com.study.task.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Taskfile implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Task task;
	
	private String path;
	
	private String name;
	
}
