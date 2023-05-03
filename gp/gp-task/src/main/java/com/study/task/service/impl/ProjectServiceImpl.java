package com.study.task.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.common.core.base.BaseServiceImpl;
import com.study.task.dao.ProjectDao;
import com.study.task.entity.Project;
import com.study.task.service.ProjectService;

@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project> implements ProjectService{

	@Autowired
	private ProjectDao dao;
	
	@Override
	public int updateStatus(Project project) {
		return dao.updateStatus(project);
	}

}
