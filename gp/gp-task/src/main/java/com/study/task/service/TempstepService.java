package com.study.task.service;

import java.util.List;

import com.study.common.core.base.BaseService;
import com.study.task.entity.Tempstep;

public interface TempstepService extends BaseService<Tempstep>{
	
	List<Tempstep> listByTempId(Long tempId);

}
