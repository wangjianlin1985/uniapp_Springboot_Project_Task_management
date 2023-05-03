package com.study.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.common.core.base.BaseServiceImpl;
import com.study.task.dao.TaskfileDao;
import com.study.task.entity.Taskfile;
import com.study.task.service.TaskfileService;

@Service
public class TaskfileServiceImpl extends BaseServiceImpl<Taskfile> implements TaskfileService{

	@Autowired
	private TaskfileDao dao;
	
	@Override
	public List<Taskfile> listByTaskId(Long taskId) {
		return dao.listByTaskId(taskId);
	}

}
