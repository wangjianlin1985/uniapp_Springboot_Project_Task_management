package com.study.web.controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.task.entity.Tasklabel;
import com.study.task.service.TasklabelService;

import cn.hutool.core.convert.Convert;

/**
 * 标签相关API 
 */
@RestController
@RequestMapping("openapi/task/label")
@CrossOrigin
public class ProjectLabelApiController extends BaseController{

	@Autowired
	private TasklabelService labelService;
	
	// 标签列表
	@RequestMapping("list")
	public TableDataInfo list() {
		return getDataTable(labelService.list(new Tasklabel()));
	}
	
	// 标签新增
	@RequestMapping("add")
	public AjaxResult add(@RequestBody Tasklabel label) {
		return toAjax(labelService.add(label));
	}
	
	// 标签修改	
	@RequestMapping("update")
	public AjaxResult update(@RequestBody Tasklabel label) {
		return toAjax(labelService.update(label));
	}
	
	
	// 标签删除	
	@RequestMapping("delete")
	public AjaxResult delete(@RequestBody Map<String, Object> map) {
		return toAjax(labelService.deleteByIds(Convert.toStr(map.get("ids"))));
	}
		
}
