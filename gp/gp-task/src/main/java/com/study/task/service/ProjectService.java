package com.study.task.service;

import com.study.common.core.base.BaseService;
import com.study.task.entity.Project;

public interface ProjectService extends BaseService<Project>{
	
	int updateStatus(Project project);

}
