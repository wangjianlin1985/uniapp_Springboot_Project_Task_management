package com.study.task.service;

import java.util.List;

import com.study.common.core.base.BaseService;
import com.study.task.entity.Taskfile;

public interface TaskfileService extends BaseService<Taskfile>{
	
	List<Taskfile> listByTaskId(Long taskId);

}
