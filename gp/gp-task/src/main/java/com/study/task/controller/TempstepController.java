package com.study.task.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.common.core.base.BaseController;
import com.study.common.core.domain.AjaxResult;
import com.study.common.core.page.TableDataInfo;
import com.study.task.entity.Tempstep;
import com.study.task.service.TempstepService;

@Controller
@RequestMapping("/task/tempstep")
public class TempstepController extends BaseController{
	

    private static String prefix = "task/tempstep";

	@Autowired
	private TempstepService tempService;

	@GetMapping("index/{tempId}")
	public String temp(@PathVariable Long tempId,ModelMap mmap) {
		mmap.addAttribute("tempId", tempId);
		return prefix + "/step";
	}

	@RequestMapping("list/{tempId}")
	@ResponseBody
	public TableDataInfo list(@PathVariable Long tempId) {
		List<Tempstep> list = tempService.listByTempId(tempId);
		return getDataTable(list);
	}
	

	/**
	 * 新增信息
	 */
	@GetMapping("/add/{tempId}")
	public String add(@PathVariable Long tempId,ModelMap mmap) {
		mmap.addAttribute("tempId", tempId);
		return prefix + "/add";
	}
	

	/**
	 * 新增保存
	 * @param temp
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Tempstep temp) {
		return toAjax(tempService.add(temp));
	}
	

	/**
	 * 修改
	 * 
	 * @param configId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Tempstep sTempstep = tempService.getById(id);
		mmap.addAttribute("temp", sTempstep);
		return prefix + "/edit";
	}
	

	/**
	 * 修改保存
	 * @param temp
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult updateSave(@Validated Tempstep temp) {
		return toAjax(tempService.update(temp));
	}
	

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional
	public AjaxResult remove(String ids) {
		return toAjax(tempService.deleteByIds(ids));
	}

	
	/**
	 * 所有数据列表
	 * @return
	 */
	@RequestMapping("data/{tempId}")
	@ResponseBody
	public List<Map<String, Object>> data(@PathVariable Long tempId) {
		List<Tempstep> list = tempService.listByTempId(tempId);
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		for(Tempstep g : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("id", g.getId());
			map.put("name", g.getName());
			map.put("sort", g.getSort());
			mapList.add(map);
		}
		return mapList;
	}
	


}
