package com.study.task.dao;

import com.study.common.core.base.BaseDao;
import com.study.task.entity.Task;

public interface TaskDao extends BaseDao<Task>{
	
	int updateStatus(Task task);

}
