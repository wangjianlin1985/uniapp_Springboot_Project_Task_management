package com.study.task.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.common.core.base.BaseServiceImpl;
import com.study.task.dao.TaskDao;
import com.study.task.entity.Task;
import com.study.task.service.TaskService;

@Service
public class TaskServiceImpl extends BaseServiceImpl<Task> implements TaskService {

	@Autowired
	private TaskDao dao;
	
	@Override
	public int updateStatus(Task task) {
		return dao.updateStatus(task);
	}

}
