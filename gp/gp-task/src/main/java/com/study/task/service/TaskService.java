package com.study.task.service;

import com.study.common.core.base.BaseService;
import com.study.task.entity.Task;

public interface TaskService extends BaseService<Task>{

	int updateStatus(Task task);
	
}
