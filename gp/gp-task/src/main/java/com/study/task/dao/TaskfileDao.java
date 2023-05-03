package com.study.task.dao;

import java.util.List;

import com.study.common.core.base.BaseDao;
import com.study.task.entity.Taskfile;

public interface TaskfileDao extends BaseDao<Taskfile>{
	
	List<Taskfile> listByTaskId(Long taskId);

}
