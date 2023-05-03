package com.study.task.controller;

import java.util.List;

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
import com.study.system.entity.SysUser;
import com.study.task.entity.Project;
import com.study.task.entity.Projecttemp;
import com.study.task.entity.Projectuser;
import com.study.task.service.ProjectService;
import com.study.task.service.ProjecttempService;
import com.study.task.service.ProjectuserService;

import cn.hutool.core.convert.Convert;

@Controller
@RequestMapping("/task/project")
public class ProjectController extends BaseController{

    private static String prefix = "task/project";

	@Autowired
	private ProjectService projectService;
	@Autowired
	private ProjecttempService tempService;
	@Autowired
	private ProjectuserService projectuserService;
	
	
	@GetMapping()
	public String project() {
		return prefix + "/project";
	}

	@RequestMapping("list")
	@ResponseBody
	public TableDataInfo list(Project project) {
		project.setStatus(0);
		Long userId = getUserId();
		// 如果不是超管的话就各自显示自己的
		if (!userId.equals(1L) ) {
			SysUser user = new SysUser();
			user.setUserId(userId);
			project.setDuty(user);
		}
		startPage();
		List<Project> list = projectService.list(project);
		return getDataTable(list);
	}
	
	
	

	@GetMapping("huishouIndex")
	public String huishouIndex() {
		return prefix + "/projectHuishou";
	}

	
	@RequestMapping("huishouList")
	@ResponseBody
	public TableDataInfo huishouList(Project project) {
		project.setStatus(1);
		Long userId = getUserId();
		// 如果不是超管的话就各自显示自己的
		if (!userId.equals(1L) ) {
			SysUser user = new SysUser();
			user.setUserId(userId);
			project.setDuty(user);
		}
		startPage();
		List<Project> list = projectService.list(project);
		return getDataTable(list);
	}
	

	/**
	 * 改变状态
	 */
	@PostMapping("/updateStatus")
	@ResponseBody
	public AjaxResult updateStatus(Project project) {
		return toAjax(projectService.updateStatus(project));
	}
	
	
	

	/**
	 * 新增信息
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		List<Projecttemp> list = tempService.list(new Projecttemp());
		mmap.addAttribute("tempList", list);
		return prefix + "/add";
	}

	/**
	 * 新增保存
	 * @param project
	 * @return
	 */
	@PostMapping("/add")
	@Transactional
	@ResponseBody
	public AjaxResult addSave(@Validated Project project) {
		SysUser user = new SysUser();
		user.setUserId(getUserId());
		project.setDuty(user);
		int r = projectService.add(project);
		if (r > 0) {
			Projectuser pu = new Projectuser();
			pu.setProject(project);
			pu.setUser(user);
			pu.setStatus(1);
			projectuserService.add(pu);
		}
		return toAjax(r);
	}

	/**
	 * 修改
	 * 
	 * @param configId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{projectId}")
	public String edit(@PathVariable("projectId") Long projectId, ModelMap mmap) {
		mmap.addAttribute("project", projectService.getById(projectId));
		return prefix + "/edit";
	}

	/**
	 * 修改保存
	 * @param project
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult updateSave(@Validated Project project) {
		return toAjax(projectService.update(project));
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional
	public AjaxResult remove(String ids) {
		Long[] idsLong = Convert.toLongArray(ids);
		for(Long projectId : idsLong) {
			projectuserService.deleteByProjectId(projectId);
		}
		return toAjax(projectService.deleteByIds(ids));
	}


}
