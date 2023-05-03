package com.study.task.controller;

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
import com.study.system.entity.SysUser;
import com.study.system.service.SysUserService;
import com.study.task.entity.Project;
import com.study.task.entity.Projectuser;
import com.study.task.service.ProjectuserService;

@Controller
@RequestMapping("/task/projectuser")
public class ProjectuserController extends BaseController {

	private static String prefix = "task/projectuser";

	@Autowired
	private ProjectuserService projectuserService;
	@Autowired
	private SysUserService userService;
	
	@GetMapping("index/{projectId}")
	public String temp(@PathVariable Long projectId,ModelMap mmap) {
		mmap.addAttribute("projectId", projectId);
		return prefix + "/projectuser";
	}

	@RequestMapping("list/{projectId}")
	@ResponseBody
	public TableDataInfo list(@PathVariable Long projectId) {
		Projectuser pu = new Projectuser();
		Project project = new Project();
		project.setId(projectId);
		pu.setProject(project);
		List<Projectuser> list = projectuserService.list(pu);
		return getDataTable(list);
	}
	
	
	/**
	 * 新增信息
	 */
	@GetMapping("/add/{projectId}")
	public String add(@PathVariable Long projectId,ModelMap mmap) {
		mmap.addAttribute("projectId", projectId);
		return prefix + "/add";
	}
	

	/**
	 * 新增保存
	 * @param temp
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated Projectuser pu) {
		SysUser searchUser = new SysUser();
		searchUser.setLoginName(pu.getSearchKey());
		SysUser user = userService.selectUserByLoginName(searchUser);
		if (user == null) {
			return error("用户不存在");
		}
		if (user.getUserId().equals(getUserId())) {
			return error("不能邀请自己");
		}
		pu.setStatus(0);
		pu.setUser(user);
		return toAjax(projectuserService.add(pu));
	}
	
	
	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@Transactional
	public AjaxResult remove(String ids) {
		return toAjax(projectuserService.deleteByIds(ids));
	}

	
	
	
	/**
	 * 改变状态(被邀请人是否同意)
	 */
	@PostMapping("/updateStatus")
	@ResponseBody
	@Transactional
	public AjaxResult updateStatus(Projectuser pu) {
		return toAjax(projectuserService.update(pu));
	}

	
	
	@GetMapping("todoIndex")
	public String todoIndex() {
		return prefix + "/projectuserTodo";
	}
	
	@RequestMapping("todoList")
	@ResponseBody
	public TableDataInfo todoList() {
		List<Map<String, Object>> list = projectuserService.listTodo(getUserId());
		return getDataTable(list);
	}
	
	
	
}
