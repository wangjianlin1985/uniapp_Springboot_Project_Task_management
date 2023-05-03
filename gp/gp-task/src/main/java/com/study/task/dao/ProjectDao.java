package com.study.task.dao;

import com.study.common.core.base.BaseDao;
import com.study.task.entity.Project;

public interface ProjectDao extends BaseDao<Project>{

	int updateStatus(Project project);
	
}
