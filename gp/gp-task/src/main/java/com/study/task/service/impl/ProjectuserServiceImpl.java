package com.study.task.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.common.core.base.BaseServiceImpl;
import com.study.task.dao.ProjectuserDao;
import com.study.task.entity.Projectuser;
import com.study.task.service.ProjectuserService;

@Service
public class ProjectuserServiceImpl extends BaseServiceImpl<Projectuser> implements ProjectuserService {

	@Autowired
	private ProjectuserDao dao;
	
	@Override
	public List<Map<String, Object>> listTodo(Long userId) {
		return dao.listTodo(userId);
	}

	@Override
	public int deleteByProjectId(Long projectId) {
		return dao.deleteByProjectId(projectId);
	}

}
