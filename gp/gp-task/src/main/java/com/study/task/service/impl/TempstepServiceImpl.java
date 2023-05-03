package com.study.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.common.core.base.BaseServiceImpl;
import com.study.task.dao.TempstepDao;
import com.study.task.entity.Tempstep;
import com.study.task.service.TempstepService;

@Service
public class TempstepServiceImpl extends BaseServiceImpl<Tempstep> implements TempstepService{

	@Autowired
	private TempstepDao dao;
	
	@Override
	public List<Tempstep> listByTempId(Long tempId) {
		return dao.listByTempId(tempId);
	}

}
