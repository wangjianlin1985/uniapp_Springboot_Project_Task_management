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
import com.study.task.entity.Tasklabel;
import com.study.task.service.TasklabelService;

@Controller
@RequestMapping("/task/label")
public class TasklabelController extends BaseController {
	
	
    private static String prefix = "task/label";

	@Autowired
	private TasklabelService labelService;

	@GetMapping()
	public String label() {
		return prefix + "/label";
	}

	@RequestMapping("list")
	@ResponseBody
	public TableDataInfo list(Tasklabel label) {
		startPage();
		List<Tasklabel> list = labelService.list(label);
		return getDataTable(list);
	}

	/**
	 * 新增信息
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 新增保存
	 * @param label
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Tasklabel label) {
		return toAjax(labelService.add(label));
	}

	/**
	 * 修改
	 * 
	 * @param configId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{labelId}")
	public String edit(@PathVariable("labelId") Long labelId, ModelMap mmap) {
		mmap.addAttribute("label", labelService.getById(labelId));
		return prefix + "/edit";
	}

	/**
	 * 修改保存
	 * @param label
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult updateSave(@Validated Tasklabel label) {
		return toAjax(labelService.update(label));
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional
	public AjaxResult remove(String ids) {
		return toAjax(labelService.deleteByIds(ids));
	}

	/**
	 * 所有数据列表
	 * @return
	 */
	@RequestMapping("data")
	@ResponseBody
	public List<Map<String, Object>> data() {
		List<Tasklabel> list = labelService.list(new Tasklabel());
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		for(Tasklabel g : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("id", g.getId());
			map.put("name", g.getName());
			mapList.add(map);
		}
		return mapList;
	}
	

	
	
	
}
