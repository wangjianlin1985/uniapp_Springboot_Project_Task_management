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
import com.study.task.entity.Projecttemp;
import com.study.task.service.ProjecttempService;

@Controller
@RequestMapping("/task/projecttemp")
public class ProjecttempController extends BaseController{

    private static String prefix = "task/projecttemp";

	@Autowired
	private ProjecttempService tempService;

	@GetMapping()
	public String temp() {
		return prefix + "/temp";
	}

	@RequestMapping("list")
	@ResponseBody
	public TableDataInfo list(Projecttemp temp) {
		startPage();
		List<Projecttemp> list = tempService.list(temp);
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
	 * @param temp
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Projecttemp temp) {
		return toAjax(tempService.add(temp));
	}

	/**
	 * 修改
	 * 
	 * @param configId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{tempId}")
	public String edit(@PathVariable("tempId") Long tempId, ModelMap mmap) {
		mmap.addAttribute("temp", tempService.getById(tempId));
		return prefix + "/edit";
	}

	/**
	 * 修改保存
	 * @param temp
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult updateSave(@Validated Projecttemp temp) {
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
	@RequestMapping("data")
	@ResponseBody
	public List<Map<String, Object>> data() {
		List<Projecttemp> list = tempService.list(new Projecttemp());
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		for(Projecttemp g : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("id", g.getId());
			map.put("name", g.getName());
			mapList.add(map);
		}
		return mapList;
	}
	

}
