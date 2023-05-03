package com.study.web.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.task.entity.Projecttemp;
import com.study.task.entity.Tempstep;
import com.study.task.service.ProjecttempService;
import com.study.task.service.TempstepService;

import cn.hutool.core.convert.Convert;

/**
 * 模板和模板步骤相关API
 */
@RestController
@RequestMapping("openapi/task/projecttemp")
@CrossOrigin
public class ProjecttempApiController extends BaseController{
	
	@Autowired
	private ProjecttempService projecttempService;
	@Autowired
	private TempstepService tempstepService;
	
	
	/**********************步骤相关接口*************************************/
	// 步骤列表
	@RequestMapping("step/list/{tempId}")
	public TableDataInfo stepList(@PathVariable Long tempId) {
		List<Tempstep> list = tempstepService.listByTempId(tempId);
		return getDataTable(list);
	}
	
	// 步骤新增
	@RequestMapping("step/add")
	public AjaxResult stepadd(@RequestBody Tempstep step) {
		return toAjax(tempstepService.add(step));
	}
	
	// 步骤修改
	@RequestMapping("step/update")
	public AjaxResult stepupdate(@RequestBody Tempstep step) {
		return toAjax(tempstepService.update(step));
	}
	
	// 步骤查詢單個ID
	@RequestMapping("step/get/{id}")
	public AjaxResult stepget(@PathVariable Long id) {
		return AjaxResult.success(tempstepService.getById(id));
	}

	// 步骤删除
	@RequestMapping("step/delete")
	public AjaxResult stepdelete(@RequestBody Map<String, Object> map) {
		return toAjax(tempstepService.deleteByIds(Convert.toStr(map.get("ids"))));
	}
	
		
		
		
	/**********************模板相关接口*************************************/
	
	// 模板列表
	@RequestMapping("list")
	public TableDataInfo list() {
		List<Projecttemp> list = projecttempService.list(new Projecttemp());
		return getDataTable(list);
	}
	
	// 模板新增
	@RequestMapping("add")
	public AjaxResult add(@RequestBody Projecttemp temp) {
		return toAjax(projecttempService.add(temp));
	}
	
	// 模板修改
	@RequestMapping("update")
	public AjaxResult update(@RequestBody Projecttemp temp) {
		return toAjax(projecttempService.update(temp));
	}
	
	// 模板查詢單個ID
	@RequestMapping("get/{id}")
	public AjaxResult get(@PathVariable Long id) {
		return AjaxResult.success(projecttempService.getById(id));
	}

	// 模板删除
	@RequestMapping("delete")
	public AjaxResult delete(@RequestBody Map<String, Object> map) {
		return toAjax(projecttempService.deleteByIds(Convert.toStr(map.get("ids"))));
	}
	
	
}
