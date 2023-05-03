package com.study.task.entity;

import java.io.Serializable;
import java.util.List;

import com.study.system.entity.SysUser;

import lombok.Data;

@Data
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private String remark;
	
	private Tasklabel label;
	
	private SysUser user;
	
	private String yxj;
	
	private String endTime; // 预计结束时间
	
	private String finishTime; // 实际完成时间
	
	private Integer status; // 0进行中  1正常完成  2超时完成 3 不能完成
	
	private Tempstep step;
	
	private Project project;
	
	/**用来接收文件路径和附件*/
	private String fileList;
	
	private List<Taskfile> flist;
}
