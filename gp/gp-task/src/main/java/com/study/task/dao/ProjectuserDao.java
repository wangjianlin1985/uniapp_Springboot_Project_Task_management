package com.study.task.dao;

import java.util.List;
import java.util.Map;

import com.study.common.core.base.BaseDao;
import com.study.task.entity.Projectuser;

public interface ProjectuserDao extends BaseDao<Projectuser>{

	List<Map<String, Object>> listTodo(Long userId);
	
	int deleteByProjectId(Long projectId);
	
}
