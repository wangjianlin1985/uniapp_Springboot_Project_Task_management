package com.study.task.dao;

import java.util.List;

import com.study.common.core.base.BaseDao;
import com.study.task.entity.Tempstep;

public interface TempstepDao extends BaseDao<Tempstep>{
	
	List<Tempstep> listByTempId(Long tempId);
	

}
