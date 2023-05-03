package com.study.task.service;

import java.util.List;
import java.util.Map;

import com.study.common.core.base.BaseService;
import com.study.task.entity.Projectuser;

public interface ProjectuserService extends BaseService<Projectuser> {
	
	List<Map<String, Object>> listTodo(Long userId);
	
	int deleteByProjectId(Long projectId);

}
